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
package org.tinygroup.springmvc.extension.impl;

import org.springframework.http.MediaType;
import org.tinygroup.springmvc.util.LinkedMultiValueMap;
import org.tinygroup.springmvc.util.MultiValueMap;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mediatype注册器
 *
 * @author renhui
 */
public class MediaTypeRespository {

    private final MultiValueMap<String, MediaType> mediaTypes = new LinkedMultiValueMap<String, MediaType>(
            64);
    private final ConcurrentHashMap<MediaType, String> fileExtensions = new ConcurrentHashMap<MediaType, String>();

    public MediaTypeRespository() {
        fileExtensions.put(MediaType.TEXT_HTML, "shtm");
    }


    void register(String fileExtension, MediaType mediaType) {
        List<MediaType> mts = this.mediaTypes.get(fileExtension);
        if (mts == null || !mts.contains(mediaType)) {
            this.mediaTypes.add(fileExtension, mediaType);
        }
        if (MediaType.TEXT_HTML == mediaType) {
            return;
        }
        fileExtensions.put(mediaType, fileExtension);
    }

    public String getExtension(MediaType mediaType) {
        return fileExtensions.get(mediaType);
    }

    public List<MediaType> getContentTypes(String fileExtension) {
        return this.mediaTypes.get(fileExtension);
    }
}
