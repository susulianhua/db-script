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
// ========================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at 
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.tinygroup.jspengine.org.apache.commons.logging;

/**
 * LogConfigurationException
 * <p>
 * Bridging com.sun.org.apache.logging.
 */
public class LogConfigurationException extends RuntimeException {

    /**
     *
     */
    public LogConfigurationException() {
        super();
    }

    /**
     * @param message
     */
    public LogConfigurationException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public LogConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public LogConfigurationException(Throwable cause) {
        super(cause);
    }
}
