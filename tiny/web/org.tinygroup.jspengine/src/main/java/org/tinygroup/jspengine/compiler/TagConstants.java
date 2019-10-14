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
package org.tinygroup.jspengine.compiler;

public interface TagConstants {

    String JSP_URI = "http://java.sun.com/JSP/Page";

    String DIRECTIVE_ACTION = "directive.";

    String ROOT_ACTION = "root";
    String JSP_ROOT_ACTION = "jsp:root";

    String PAGE_DIRECTIVE_ACTION = "directive.page";
    String JSP_PAGE_DIRECTIVE_ACTION = "jsp:directive.page";

    String INCLUDE_DIRECTIVE_ACTION = "directive.include";
    String JSP_INCLUDE_DIRECTIVE_ACTION = "jsp:directive.include";

    String DECLARATION_ACTION = "declaration";
    String JSP_DECLARATION_ACTION = "jsp:declaration";

    String SCRIPTLET_ACTION = "scriptlet";
    String JSP_SCRIPTLET_ACTION = "jsp:scriptlet";

    String EXPRESSION_ACTION = "expression";
    String JSP_EXPRESSION_ACTION = "jsp:expression";

    String USE_BEAN_ACTION = "useBean";
    String JSP_USE_BEAN_ACTION = "jsp:useBean";

    String SET_PROPERTY_ACTION = "setProperty";
    String JSP_SET_PROPERTY_ACTION = "jsp:setProperty";

    String GET_PROPERTY_ACTION = "getProperty";
    String JSP_GET_PROPERTY_ACTION = "jsp:getProperty";

    String INCLUDE_ACTION = "include";
    String JSP_INCLUDE_ACTION = "jsp:include";

    String FORWARD_ACTION = "forward";
    String JSP_FORWARD_ACTION = "jsp:forward";

    String PARAM_ACTION = "param";
    String JSP_PARAM_ACTION = "jsp:param";

    String PARAMS_ACTION = "params";
    String JSP_PARAMS_ACTION = "jsp:params";

    String PLUGIN_ACTION = "plugin";
    String JSP_PLUGIN_ACTION = "jsp:plugin";

    String FALLBACK_ACTION = "fallback";
    String JSP_FALLBACK_ACTION = "jsp:fallback";

    String TEXT_ACTION = "text";
    String JSP_TEXT_ACTION = "jsp:text";
    String JSP_TEXT_ACTION_END = "</jsp:text>";

    String ATTRIBUTE_ACTION = "attribute";
    String JSP_ATTRIBUTE_ACTION = "jsp:attribute";

    String BODY_ACTION = "body";
    String JSP_BODY_ACTION = "jsp:body";

    String ELEMENT_ACTION = "element";
    String JSP_ELEMENT_ACTION = "jsp:element";

    String OUTPUT_ACTION = "output";
    String JSP_OUTPUT_ACTION = "jsp:output";

    String TAGLIB_DIRECTIVE_ACTION = "taglib";
    String JSP_TAGLIB_DIRECTIVE_ACTION = "jsp:taglib";

    /*
     * Tag Files
     */
    String INVOKE_ACTION = "invoke";
    String JSP_INVOKE_ACTION = "jsp:invoke";

    String DOBODY_ACTION = "doBody";
    String JSP_DOBODY_ACTION = "jsp:doBody";

    /*
     * Tag File Directives
     */
    String TAG_DIRECTIVE_ACTION = "directive.tag";
    String JSP_TAG_DIRECTIVE_ACTION = "jsp:directive.tag";

    String ATTRIBUTE_DIRECTIVE_ACTION = "directive.attribute";
    String JSP_ATTRIBUTE_DIRECTIVE_ACTION = "jsp:directive.attribute";

    String VARIABLE_DIRECTIVE_ACTION = "directive.variable";
    String JSP_VARIABLE_DIRECTIVE_ACTION = "jsp:directive.variable";

    /*
     * Directive attributes
     */
    String URN_JSPTAGDIR = "urn:jsptagdir:";
    String URN_JSPTLD = "urn:jsptld:";
}
