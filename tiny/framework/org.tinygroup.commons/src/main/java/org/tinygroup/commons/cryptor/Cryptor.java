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
package org.tinygroup.commons.cryptor;

/**
 * 功能说明: 加解密接口
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-30 <br>
 * <br>
 */
public interface Cryptor {
    /**
     * Encrypt the plain text password.
     *
     * @param plainKey The password.
     * @return The encrypted password String.
     * @throws Exception If an error occurs.
     */
    String encrypt(String plainKey) throws Exception;

    /**
     * Encrypt the plain text password.
     *
     * @param plainKey The password.
     * @param seed     The seed.
     * @return The encrypted password String.
     * @throws Exception If an error occurs.
     */
    String encrypt(String plainKey, String seed) throws Exception;

    /**
     * Decrypts the password.
     *
     * @param encryptedKey the encrypted password.
     * @return The plain text password.
     * @throws Exception If an error occurs.
     */
    String decrypt(String encryptedKey) throws Exception;

    /**
     * Decrypts the password.
     *
     * @param encryptedKey the encrypted password.
     * @param seed         The seed.
     * @return The plain text password.
     * @throws Exception If an error occurs.
     */
    String decrypt(String encryptedKey, String seed) throws Exception;
}
