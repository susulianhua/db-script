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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies injectable constructors, methods, and fields. May apply to static
 * as well as instance members. An injectable member may have any access
 * modifier (private, package-private, protected, public). Constructors are
 * injected first, followed by fields, and then methods. Fields and methods
 * in superclasses are injected before those in subclasses. Ordering of
 * injection among fields and among methods in the same class is not specified.
 * <p>
 * <p>Injectable constructors are annotated with {@code @Inject} and accept
 * zero or more dependencies as arguments. {@code @Inject} can apply to at most
 * one constructor per class.
 * <p>
 * <p><tt><blockquote style="padding-left: 2em; text-indent: -2em;">@Inject
 * <i>ConstructorModifiers<sub>opt</sub></i>
 * <i>SimpleTypeName</i>(<i>FormalParameterList<sub>opt</sub></i>)
 * <i>Throws<sub>opt</sub></i>
 * <i>ConstructorBody</i></blockquote></tt>
 * <p>
 * <p>{@code @Inject} is optional for public, no-argument constructors when no
 * other constructors are present. This enables injectors to invoke default
 * constructors.
 * <p>
 * <p><tt><blockquote style="padding-left: 2em; text-indent: -2em;">
 * {@literal @}Inject<sub><i>opt</i></sub>
 * <i>Annotations<sub>opt</sub></i>
 * public
 * <i>SimpleTypeName</i>()
 * <i>Throws<sub>opt</sub></i>
 * <i>ConstructorBody</i></blockquote></tt>
 * <p>
 * <p>Injectable fields:
 * <ul>
 * <li>are annotated with {@code @Inject}.
 * <li>are not final.
 * <li>may have any otherwise valid name.</li></ul>
 * <p>
 * <p><tt><blockquote style="padding-left: 2em; text-indent: -2em;">@Inject
 * <i>FieldModifiers<sub>opt</sub></i>
 * <i>Type</i>
 * <i>VariableDeclarators</i>;</blockquote></tt>
 * <p>
 * <p>Injectable methods:
 * <ul>
 * <li>are annotated with {@code @Inject}.</li>
 * <li>are not abstract.</li>
 * <li>do not declare type parameters of their own.</li>
 * <li>may return a result</li>
 * <li>may have any otherwise valid name.</li>
 * <li>accept zero or more dependencies as arguments.</li></ul>
 * <p>
 * <p><tt><blockquote style="padding-left: 2em; text-indent: -2em;">@Inject
 * <i>MethodModifiers<sub>opt</sub></i>
 * <i>ResultType</i>
 * <i>Identifier</i>(<i>FormalParameterList<sub>opt</sub></i>)
 * <i>Throws<sub>opt</sub></i>
 * <i>MethodBody</i></blockquote></tt>
 * <p>
 * <p>The injector ignores the result of an injected method, but
 * non-{@code void} return types are allowed to support use of the method in
 * other contexts (builder-style method chaining, for example).
 * <p>
 * <p>Examples:
 * <p>
 * <pre>
 *   public class Car {
 *     // Injectable constructor
 *     &#064;Inject public Car(Engine engine) { ... }
 *
 *     // Injectable field
 *     &#064;Inject private Provider&lt;Seat> seatProvider;
 *
 *     // Injectable package-private method
 *     &#064;Inject void install(Windshield windshield, Trunk trunk) { ... }
 *   }</pre>
 * <p>
 * <p>A method annotated with {@code @Inject} that overrides another method
 * annotated with {@code @Inject} will only be injected once per injection
 * request per instance. A method with <i>no</i> {@code @Inject} annotation
 * that overrides a method annotated with {@code @Inject} will not be
 * injected.
 * <p>
 * <p>Injection of members annotated with {@code @Inject} is required. While an
 * injectable member may use any accessibility modifier (including
 * <tt>private</tt>), platform or injector limitations (like security
 * restrictions or lack of reflection support) might preclude injection
 * of non-public members.
 * <p>
 * <h3>Qualifiers</h3>
 * <p>
 * <p>A {@linkplain Qualifier qualifier} may annotate an injectable field
 * or parameter and, combined with the type, identify the implementation to
 * inject. Qualifiers are optional, and when used with {@code @Inject} in
 * injector-independent classes, no more than one qualifier should annotate a
 * single field or parameter. The qualifiers are bold in the following example:
 * <p>
 * <pre>
 *   public class Car {
 *     &#064;Inject private <b>@Leather</b> Provider&lt;Seat> seatProvider;
 *
 *     &#064;Inject void install(<b>@Tinted</b> Windshield windshield,
 *         <b>@Big</b> Trunk trunk) { ... }
 *   }</pre>
 * <p>
 * <p>If one injectable method overrides another, the overriding method's
 * parameters do not automatically inherit qualifiers from the overridden
 * method's parameters.
 * <p>
 * <h3>Injectable Values</h3>
 * <p>
 * <p>For a given type T and optional qualifier, an injector must be able to
 * inject a user-specified class that:
 * <p>
 * <ol type="a">
 * <li>is assignment compatible with T and</li>
 * <li>has an injectable constructor.</li>
 * </ol>
 * <p>
 * <p>For example, the user might use external configuration to pick an
 * implementation of T. Beyond that, which values are injected depend upon the
 * injector implementation and its configuration.
 * <p>
 * <h3>Circular Dependencies</h3>
 * <p>
 * <p>Detecting and resolving circular dependencies is left as an exercise for
 * the injector implementation. Circular dependencies between two constructors
 * is an obvious problem, but you can also have a circular dependency between
 * injectable fields or methods:
 * <p>
 * <pre>
 *   class A {
 *     &#064;Inject B b;
 *   }
 *   class B {
 *     &#064;Inject A a;
 *   }</pre>
 * <p>
 * <p>When constructing an instance of {@code A}, a naive injector
 * implementation might go into an infinite loop constructing an instance of
 * {@code B} to set on {@code A}, a second instance of {@code A} to set on
 * {@code B}, a second instance of {@code B} to set on the second instance of
 * {@code A}, and so on.
 * <p>
 * <p>A conservative injector might detect the circular dependency at build
 * time and generate an error, at which point the programmer could break the
 * circular dependency by injecting {@link Provider Provider&lt;A>} or {@code
 * Provider<B>} instead of {@code A} or {@code B} respectively. Calling {@link
 * Provider#get() get()} on the provider directly from the constructor or
 * method it was injected into defeats the provider's ability to break up
 * circular dependencies. In the case of method or field injection, scoping
 * one of the dependencies (using {@linkplain Singleton singleton scope}, for
 * example) may also enable a valid circular relationship.
 *
 * @see Qualifier @Qualifier
 * @see Provider
 */
@Target({METHOD, CONSTRUCTOR, FIELD})
@Retention(RUNTIME)
@Documented
public @interface Inject {
}
