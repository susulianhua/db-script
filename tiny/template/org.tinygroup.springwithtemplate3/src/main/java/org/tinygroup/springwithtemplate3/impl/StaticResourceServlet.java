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
package org.tinygroup.springwithtemplate3.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class StaticResourceServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -288208057906965434L;


    private static Logger logger = LoggerFactory
            .getLogger(StaticResourceServlet.class);


    private FullContextFileRepository fullContextFileRepository;

    public FullContextFileRepository getFullContextFileRepository() {
        return fullContextFileRepository;
    }

    public void setFullContextFileRepository(
            FullContextFileRepository fullContextFileRepository) {
        this.fullContextFileRepository = fullContextFileRepository;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(request, response);
    }

    public void init() throws ServletException {
        super.init();
        fullContextFileRepository = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean("fullContextFileRepository");
    }

    protected void doRequest(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        logger.logMessage(LogLevel.INFO, "{}开始处理...", servletPath);
        FileObject fileObject = fullContextFileRepository
                .getFileObject(servletPath);
        if (fileObject != null && fileObject.isExist()) {
            String ims = request.getHeader("If-Modified-Since");
            if (ims != null && ims.length() > 0) {
                if (ims.equals(new Date(fileObject.getLastModifiedTime())
                        .toGMTString())) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            String dateString = new Date(fileObject.getLastModifiedTime())
                    .toGMTString();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Last-modified", dateString);
            response.setHeader("Connection", "keep-alive");
            response.setHeader("Cache-Control", "max-age=315360000");
            response.setHeader("Date", dateString);
            response.setContentType(fullContextFileRepository
                    .getFileContentType(fileObject.getExtName()));
            StreamUtil.io(fileObject.getInputStream(), response.getOutputStream(), true, false);
            logger.logMessage(LogLevel.INFO, "{}处理完成。", servletPath);
        } else {
            logger.logMessage(LogLevel.INFO, "{}未找到。", servletPath);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
