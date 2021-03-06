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
package org.tinygroup.metadata.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.metadata.languagetype.LanguageTypeProcessor;
import org.tinygroup.vfs.FileObject;

/**
 * Created by wangwy11342 on 2016/7/16.
 */
public class LanguageTypeFileResolver extends AbstractFileProcessor {
    private LanguageTypeProcessor languageTypeProcessor;

    public LanguageTypeProcessor getLanguageTypeProcessor() {
        return languageTypeProcessor;
    }

    public void setLanguageTypeProcessor(LanguageTypeProcessor languageTypeProcessor) {
        this.languageTypeProcessor = languageTypeProcessor;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return false;
    }

    public void process() {

    }
}
