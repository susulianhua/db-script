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
package org.tinygroup.tinyioc.annotation;

/**
 * Provides instances of {@code T}. Typically implemented by an injector. For
 * any type {@code T} that can be injected, you can also inject
 * {@code Provider<T>}. Compared to injecting {@code T} directly, injecting
 * {@code Provider<T>} enables:
 * <p>
 * <ul>
 * <li>retrieving multiple instances.</li>
 * <li>lazy or optional retrieval of an instance.</li>
 * <li>breaking circular dependencies.</li>
 * <li>abstracting scope so you can look up an instance in a smaller scope
 * from an instance in a containing scope.</li>
 * </ul>
 * <p>
 * <p>For example:
 * <p>
 * <pre>
 *   class Car {
 *     &#064;Inject Car(Provider&lt;Seat> seatProvider) {
 *       Seat driver = seatProvider.get();
 *       Seat passenger = seatProvider.get();
 *       ...
 *     }
 *   }</pre>
 */
public interface Provider<T> {

    /**
     * Provides a fully-constructed and injected instance of {@code T}.
     *
     * @throws RuntimeException if the injector encounters an error while
     *                          providing an instance. For example, if an injectable member on
     *                          {@code T} throws an exception, the injector may wrap the exception
     *                          and throw it to the caller of {@code get()}. Callers should not try
     *                          to handle such exceptions as the behavior may vary across injector
     *                          implementations and even different configurations of the same injector.
     */
    T get();
}
