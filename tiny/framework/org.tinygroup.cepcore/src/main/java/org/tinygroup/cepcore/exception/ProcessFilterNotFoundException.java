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
package org.tinygroup.cepcore.exception;

import org.tinygroup.cepcoreexceptioncode.CEPCoreExceptionCode;
import org.tinygroup.exception.BaseRuntimeException;

public class ProcessFilterNotFoundException extends BaseRuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 9094597551672712176L;

    public ProcessFilterNotFoundException(String eventId, String serviceId) {
        super(CEPCoreExceptionCode.PROCESS_FILTER_NOT_FOUND_EXCEPTION_CODE, eventId, serviceId);
    }

}
