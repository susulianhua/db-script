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

import org.tinygroup.jspengine.JasperException;
import org.tinygroup.jspengine.Options;

/**
 */
public class TextOptimizer {

    public static void concatenate(Compiler compiler, Node.Nodes page)
            throws JasperException {

        Options options = compiler.getCompilationContext().getOptions();
        PageInfo pageInfo = compiler.getPageInfo();
        boolean trim =
                options.getTrimSpaces() || pageInfo.isTrimDirectiveWhitespaces();

        if (trim) {
            TextCatVisitor v = new TextCatVisitor(true, trim);
            page.visit(v);
            v.collectText();
        }
        TextCatVisitor v = new TextCatVisitor(false, trim);
        page.visit(v);

        // Cleanup, in case the page ends with a template text
        v.collectText();
    }

    /**
     * A visitor to concatenate contiguous template texts.
     */
    static class TextCatVisitor extends Node.Visitor {

        private final String emptyText = new String("");
        private int textNodeCount = 0;
        private Node.TemplateText firstTextNode = null;
        private StringBuffer textBuffer;
        private boolean prePass;
        private boolean trim;

        public TextCatVisitor(boolean prePass, boolean trim) {
            this.prePass = prePass;
            this.trim = trim;
        }

        public void doVisit(Node n) throws JasperException {
            collectText();
        }

        /*
         * The following directives are ignored in text concatenation
         * except in the pre pass phase.
         */

        public void visit(Node.PageDirective n) throws JasperException {
            if (prePass) {
                collectText();
            }
        }

        public void visit(Node.TagDirective n) throws JasperException {
            if (prePass) {
                collectText();
            }
        }

        public void visit(Node.TaglibDirective n) throws JasperException {
            if (prePass) {
                collectText();
            }
        }

        public void visit(Node.AttributeDirective n) throws JasperException {
            if (prePass) {
                collectText();
            }
        }

        public void visit(Node.VariableDirective n) throws JasperException {
            if (prePass) {
                collectText();
            }
        }

        /*
         * Don't concatenate text across body boundaries
         */
        public void visitBody(Node n) throws JasperException {
            super.visitBody(n);
            collectText();
        }

        public void visit(Node.TemplateText n) throws JasperException {

            if ((trim) && !prePass && n.isAllSpace()) {
                n.setText(emptyText);
                return;
            }

            if (textNodeCount++ == 0) {
                firstTextNode = n;
                textBuffer = new StringBuffer(n.getText());
            } else {
                // Append text to text buffer
                textBuffer.append(n.getText());
                n.setText(emptyText);
            }
        }

        /**
         * This method breaks concatenation mode.  As a side effect it copies
         * the concatenated string to the first text node
         */
        private void collectText() {

            if (textNodeCount > 1) {
                // Copy the text in buffer into the first template text node.
                firstTextNode.setText(textBuffer.toString());
            }
            textNodeCount = 0;
        }

    }
}
