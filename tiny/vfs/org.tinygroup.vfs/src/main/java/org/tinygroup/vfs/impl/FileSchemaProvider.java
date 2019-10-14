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

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;

public class FileSchemaProvider implements SchemaProvider {

    public static final String FILE_PROTOCOL = "file:";

    public FileObject resolver(String resourceResolve) {
        String resource = resourceResolve;
        if (isMatch(resource)) {
            resource = resource.substring(FILE_PROTOCOL.length());
        }
        return new FileObjectImpl(this, resource);
    }

    public boolean isMatch(String resource) {
        return resource.toLowerCase().startsWith(FILE_PROTOCOL);
    }

    public String getSchema() {
        return FILE_PROTOCOL;
    }
}
