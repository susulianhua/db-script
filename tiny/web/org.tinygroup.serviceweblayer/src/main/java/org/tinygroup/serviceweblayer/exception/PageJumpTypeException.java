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
package org.tinygroup.serviceweblayer.exception;

import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.serviceweblayer.errorcode.ServiceweblayerExceptionCode;

@SuppressWarnings("serial")
public class PageJumpTypeException extends BaseRuntimeException {

    public PageJumpTypeException(String type) {
        super(ServiceweblayerExceptionCode.PAGE_JUMP_TYPE_EXCEPTION_CODE, type);
    }

}
