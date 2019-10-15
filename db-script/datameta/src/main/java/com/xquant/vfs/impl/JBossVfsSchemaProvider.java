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
package com.xquant.vfs.impl;


import com.xquant.vfs.FileObject;
import com.xquant.vfs.SchemaProvider;
import com.xquant.vfs.VFS;

public class JBossVfsSchemaProvider implements SchemaProvider {

    private static final String JBOSS_VFS = "vfs:";

    public FileObject resolver(String resourceResolve) {
        String resource = resourceResolve.substring(JBOSS_VFS.length());
        if (resource.indexOf('!') < 0) {
            return VFS.getSchemaProvider(FileSchemaProvider.FILE_PROTOCOL).resolver(resource);
        } else {
            return VFS.getSchemaProvider(JarSchemaProvider.JAR_PROTOCOL).resolver(resource);
        }
    }

    public boolean isMatch(String resource) {
        return resource.toLowerCase().startsWith(JBOSS_VFS);
    }

    public String getSchema() {
        return JBOSS_VFS;
    }
}
