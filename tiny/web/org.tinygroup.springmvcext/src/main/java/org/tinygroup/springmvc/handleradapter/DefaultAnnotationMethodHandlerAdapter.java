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
package org.tinygroup.springmvc.handleradapter;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;

/**
 * 基于注解的方法处理适配器
 */
public class DefaultAnnotationMethodHandlerAdapter extends
        AbstractMethodHandlerAdapter {


    @Override
    public boolean supports(Object handler) {
        Annotation controller = AnnotationUtils.findAnnotation(
                handler.getClass(), Controller.class);
        if (controller == null) {
            controller = AnnotationUtils.findAnnotation(handler.getClass(),
                    RequestMapping.class);
        }
        if (controller != null
                && getMethodResolver(handler).hasHandlerMethods()) {
            return true;
        }
        //
        return isConventionHandler(handler);
    }

}
