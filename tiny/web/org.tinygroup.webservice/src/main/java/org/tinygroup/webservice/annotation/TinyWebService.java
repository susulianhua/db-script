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
package org.tinygroup.webservice.annotation;

import com.sun.xml.ws.api.server.InstanceResolverAnnotation;
import org.tinygroup.webservice.instanceresolver.TinyInstanceResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 用于标记执行tiny服务的InstanceResolver
 *
 * @author renhui
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@InstanceResolverAnnotation(TinyInstanceResolver.class)
public @interface TinyWebService {

}
