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
import org.tinygroup.jspengine.JspCompilationContext;

import java.io.File;
import java.io.Writer;
import java.util.List;

interface JavaCompiler {

    /**
     * Start Java compilation
     *
     * @param className Name of the class under compilation
     * @param pageNode  Internal form for the page, used for error line mapping
     */
    JavacErrorDetail[] compile(String className, Node.Nodes pageNodes)
            throws JasperException;

    /**
     * Get a Writer for the Java file.
     * The writer is used by JSP compiler.  This method allows the Java
     * compiler control where the Java file should be generated so it knows how
     * to handle the input for java compilation accordingly.
     */
    Writer getJavaWriter(String javaFileName, String javaEncoding)
            throws JasperException;

    /**
     * Remove/save the generated Java File from/to disk
     */
    void doJavaFile(boolean keep) throws JasperException;

    /**
     * Return the time the class file was generated.
     */
    long getClassLastModified();

    /**
     * Save the generated class file to disk, if not already done.
     */
    void saveClassFile(String className, String classFileName);

    /**
     * Java Compiler options.
     */
    void setClassPath(List<File> cp);

    void setDebug(boolean debug);

    void setExtdirs(String exts);

    void setTargetVM(String targetVM);

    void setSourceVM(String sourceVM);

    /**
     * Initializations
     */
    void init(JspCompilationContext ctxt,
              ErrorDispatcher err,
              boolean suppressLogging);
}
    
