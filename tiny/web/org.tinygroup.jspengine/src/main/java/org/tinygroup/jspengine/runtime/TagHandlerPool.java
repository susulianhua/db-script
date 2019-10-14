/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.jspengine.runtime;

import org.tinygroup.jspengine.Constants;

import javax.servlet.ServletConfig;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * Pool of tag handlers that can be reused.
 *
 * @author Jan Luehe
 */
public class TagHandlerPool {

    public static final String OPTION_TAGPOOL = "tagpoolClassName";
    public static final String OPTION_MAXSIZE = "tagpoolMaxSize";

    private Tag[] handlers;
    private ResourceInjector resourceInjector;

    // index of next available tag handler
    private int current;

    /**
     * Constructs a tag handler pool with the default capacity.
     */
    public TagHandlerPool() {
        // Nothing - jasper generated servlets call the other constructor,
        // this should be used in future + init .
    }

    /**
     * Constructs a tag handler pool with the given capacity.
     *
     * @param capacity Tag handler pool capacity
     * @deprecated Use static getTagHandlerPool
     */
    public TagHandlerPool(int capacity) {
        this.handlers = new Tag[capacity];
        this.current = -1;
    }

    public static TagHandlerPool getTagHandlerPool(ServletConfig config) {
        TagHandlerPool result = null;

        String tpClassName = getOption(config, OPTION_TAGPOOL, null);
        if (tpClassName != null) {
            try {
                Class c = Class.forName(tpClassName);
                result = (TagHandlerPool) c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
        }
        if (result == null) result = new TagHandlerPool();
        result.init(config);

        return result;
    }

    protected static String getOption(ServletConfig config, String name, String defaultV) {
        if (config == null) return defaultV;

        String value = config.getInitParameter(name);
        if (value != null) return value;
        if (config.getServletContext() == null)
            return defaultV;
        value = config.getServletContext().getInitParameter(name);
        if (value != null) return value;
        return defaultV;
    }

    protected void init(ServletConfig config) {
        int maxSize = -1;
        String maxSizeS = getOption(config, OPTION_MAXSIZE, null);
        if (maxSizeS != null) {
            try {
                maxSize = Integer.parseInt(maxSizeS);
            } catch (Exception ex) {
                maxSize = -1;
            }
        }
        if (maxSize < 0) {
            maxSize = Constants.MAX_POOL_SIZE;
        }
        this.handlers = new Tag[maxSize];
        this.current = -1;

        this.resourceInjector = (ResourceInjector)
                config.getServletContext().getAttribute(
                        Constants.JSP_RESOURCE_INJECTOR_CONTEXT_ATTRIBUTE);
    }

    /**
     * Gets the next available tag handler from this tag handler pool,
     * instantiating one if this tag handler pool is empty.
     *
     * @param handlerClass Tag handler class
     * @return Reused or newly instantiated tag handler
     * @throws JspException if a tag handler cannot be instantiated
     */
    public Tag get(Class handlerClass) throws JspException {
        Tag handler = null;
        synchronized (this) {
            if (current >= 0) {
                handler = handlers[current--];
                return handler;
            }
        }

        // Out of sync block - there is no need for other threads to
        // wait for us to construct a tag for this thread.
        Tag tagHandler = null;
        try {
            tagHandler = (Tag) handlerClass.newInstance();
            if (resourceInjector != null) {
                resourceInjector.inject(tagHandler);
            }
        } catch (Exception e) {
            throw new JspException(e.getMessage(), e);
        }

        return tagHandler;
    }

    /**
     * Adds the given tag handler to this tag handler pool, unless this tag
     * handler pool has already reached its capacity, in which case the tag
     * handler's release() method is called.
     *
     * @param handler Tag handler to add to this tag handler pool
     */
    public void reuse(Tag handler) {
        synchronized (this) {
            if (current < (handlers.length - 1)) {
                handlers[++current] = handler;
                return;
            }
        }

        // There is no need for other threads to wait for us to release
        handler.release();
        if (resourceInjector != null) {
            resourceInjector.preDestroy(handler);
        }

    }

    /**
     * Calls the release() method of all available tag handlers in this tag
     * handler pool.
     */
    public synchronized void release() {
        for (int i = current; i >= 0; i--) {
            handlers[i].release();
            if (resourceInjector != null) {
                resourceInjector.preDestroy(handlers[i]);
            }
        }
    }

}

