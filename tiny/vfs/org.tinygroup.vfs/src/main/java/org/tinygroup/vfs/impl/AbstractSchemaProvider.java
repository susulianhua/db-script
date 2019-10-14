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
package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.SchemaProvider;

/**
 * Created by luoguo on 2014/11/9.
 */
public abstract class AbstractSchemaProvider implements SchemaProvider {

    public String getResourceResolve(String resourceResolve, String protocal) {
        String resource = resourceResolve;
        if (resource.startsWith(protocal)) {
            resource = resource.substring(protocal.length());
        }
        if (resource.startsWith(FileSchemaProvider.FILE_PROTOCOL)) {
            resource = resource.substring(FileSchemaProvider.FILE_PROTOCOL.length());
        }
        return resource;
    }
}
