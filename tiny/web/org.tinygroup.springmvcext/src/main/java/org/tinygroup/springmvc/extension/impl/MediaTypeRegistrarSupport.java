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

import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.tinygroup.springmvc.extension.MalformedMediaTypeException;
import org.tinygroup.springmvc.extension.MediaTypeRegistrar;

/**
 * mediaType注册工厂
 *
 * @author renhui
 */
public class MediaTypeRegistrarSupport implements MediaTypeRegistrar {
    private MediaTypeRespository mediaTypeRespository;

    public void setMediaTypeRespository(
            MediaTypeRespository mediaTypeRespository) {
        this.mediaTypeRespository = mediaTypeRespository;
    }

    public void setMediaTypes(String fileExtension, String mediaTypes) {
        if (StringUtils.isBlank(mediaTypes)) {
            return;
        }
        int pos = mediaTypes.indexOf(MediaTypeRegistrar.MEDIA_TYPE_SPLIT);
        try {
            if (pos == -1) {
                MediaType mediaType = MediaType.valueOf(mediaTypes);
                mediaTypeRespository.register(fileExtension, mediaType);
            } else {
                String[] mediaTypez = mediaTypes
                        .split(MediaTypeRegistrar.MEDIA_TYPE_SPLIT);
                for (String mtStr : mediaTypez) {
                    if (StringUtils.isNotBlank(mtStr)) {
                        MediaType mediaType = MediaType.valueOf(mtStr);
                        mediaTypeRespository.register(fileExtension, mediaType);
                    }
                }
            }
        } catch (Exception e) {
            throw new MalformedMediaTypeException("FileExtension ["
                    + fileExtension + "]--mediaTypes [" + mediaTypes
                    + "] are invalid!", e);
        }

    }
}
