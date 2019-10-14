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

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.tinygroup.jspengine.JasperException;
import org.tinygroup.jspengine.JspCompilationContext;

import java.io.*;
import java.util.*;

/**
 * JDT class compiler. This compiler will load source dependencies from the
 * context classloader, reducing dramatically disk access during
 * the compilation process.
 *
 * @author Cocoon2
 * @author Remy Maucherat
 * @author Kin-man Chung   Modified to implement JavaCompiler
 */

public class JDTJavaCompiler implements JavaCompiler {

    private static boolean USE_INTROSPECTION_TO_INVOKE_GET_PROBLEM = false;
    private static java.lang.reflect.Method GET_PROBLEM_METH = null;
    private final Map settings = new HashMap();
    private JspCompilationContext ctxt;
    private ErrorDispatcher errDispatcher;
    private org.tinygroup.jspengine.org.apache.commons.logging.Log log;
    private String javaFileName;

    /**
     * Invoke CompilationResult#getProblems safely so that it works with
     * 3.1.1 and more recent versions of the eclipse java compiler.
     * See https://jsp.dev.java.net/issues/show_bug.cgi?id=13
     *
     * @param result The compilation result.
     * @return The same object than CompilationResult#getProblems
     */
    private static final IProblem[] safeGetProblems(CompilationResult result) {
        if (!USE_INTROSPECTION_TO_INVOKE_GET_PROBLEM) {
            try {
                return result.getProblems();
            } catch (NoSuchMethodError re) {
                USE_INTROSPECTION_TO_INVOKE_GET_PROBLEM = true;
            }
        }
        try {
            if (GET_PROBLEM_METH == null) {
                GET_PROBLEM_METH = result.getClass()
                        .getDeclaredMethod("getProblems");
            }
            //an array of a particular type can be casted into an array of a super type.
            return (IProblem[]) GET_PROBLEM_METH.invoke(result, null);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void init(JspCompilationContext ctxt,
                     ErrorDispatcher errDispatcher,
                     boolean suppressLogging) {
        this.errDispatcher = errDispatcher;
        this.ctxt = ctxt;
        log = suppressLogging ?
                new org.tinygroup.jspengine.org.apache.commons.logging.impl.NoOpLog() :
                org.tinygroup.jspengine.org.apache.commons.logging.LogFactory.getLog(
                        JDTJavaCompiler.class);
    }

    public void setExtdirs(String exts) {
        // no op here
    }

    public void setClassPath(List<File> cpath) {
        // No op here, because the current classloader is used.  However,
        // This may not include the system classpath specified in options
    }

    public long getClassLastModified() {
        File classFile = new File(ctxt.getClassFileName());
        return classFile.lastModified();
    }

    public Writer getJavaWriter(String javaFileName,
                                String javaEncoding)
            throws JasperException {

        this.javaFileName = javaFileName;

        Writer writer = null;
        try {
            writer = new OutputStreamWriter(
                    new FileOutputStream(javaFileName), javaEncoding);
        } catch (UnsupportedEncodingException ex) {
            errDispatcher.jspError("jsp.error.needAlternateJavaEncoding",
                    javaEncoding);
        } catch (IOException ex) {
            errDispatcher.jspError("jsp.error.unableToCreateOutputWriter",
                    javaFileName, ex);
        }
        return writer;
    }

    public void setDebug(boolean debug) {
        settings.put(CompilerOptions.OPTION_LineNumberAttribute,
                CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_SourceFileAttribute,
                CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_ReportDeprecation,
                CompilerOptions.IGNORE);
        if (debug) {
            settings.put(CompilerOptions.OPTION_LocalVariableAttribute,
                    CompilerOptions.GENERATE);
        }
    }

    public void setSourceVM(String sourceVM) {
        if (sourceVM.equals("1.1")) {
            settings.put(CompilerOptions.OPTION_Source,
                    CompilerOptions.VERSION_1_1);
        } else if (sourceVM.equals("1.2")) {
            settings.put(CompilerOptions.OPTION_Source,
                    CompilerOptions.VERSION_1_2);
        } else if (sourceVM.equals("1.3")) {
            settings.put(CompilerOptions.OPTION_Source,
                    CompilerOptions.VERSION_1_3);
        } else if (sourceVM.equals("1.4")) {
            settings.put(CompilerOptions.OPTION_Source,
                    CompilerOptions.VERSION_1_4);
        } else if (sourceVM.equals("1.5")) {
            settings.put(CompilerOptions.OPTION_Source,
                    CompilerOptions.VERSION_1_5);
        } else {
            log.warn("Unknown source VM " + sourceVM + " ignored.");
            settings.put(CompilerOptions.OPTION_Source,
                    CompilerOptions.VERSION_1_5);
        }
    }

    public void setTargetVM(String targetVM) {
        if (targetVM.equals("1.1")) {
            settings.put(CompilerOptions.OPTION_TargetPlatform,
                    CompilerOptions.VERSION_1_1);
        } else if (targetVM.equals("1.2")) {
            settings.put(CompilerOptions.OPTION_TargetPlatform,
                    CompilerOptions.VERSION_1_2);
        } else if (targetVM.equals("1.3")) {
            settings.put(CompilerOptions.OPTION_TargetPlatform,
                    CompilerOptions.VERSION_1_3);
        } else if (targetVM.equals("1.4")) {
            settings.put(CompilerOptions.OPTION_TargetPlatform,
                    CompilerOptions.VERSION_1_4);
        } else if (targetVM.equals("1.5")) {
            settings.put(CompilerOptions.OPTION_TargetPlatform,
                    CompilerOptions.VERSION_1_5);
        } else {
            log.warn("Unknown target VM " + targetVM + " ignored.");
            settings.put(CompilerOptions.OPTION_TargetPlatform,
                    CompilerOptions.VERSION_1_5);
        }
    }

    public void saveClassFile(String className, String classFileName) {
        // class file are alwyas saved.
    }

    public void doJavaFile(boolean keep) {
        if (!keep) {
            File javaFile = new File(javaFileName);
            javaFile.delete();
        }
    }

    public JavacErrorDetail[] compile(final String targetClassName,
                                      final Node.Nodes pageNodes)
            throws JasperException {

        final String sourceFile = ctxt.getServletJavaFileName();
        final String outputDir =
                ctxt.getOptions().getScratchDir().getAbsolutePath();
        String packageName = ctxt.getServletPackageName();

        final ClassLoader classLoader = ctxt.getJspLoader();
        String[] fileNames = new String[]{sourceFile};
        String[] classNames = new String[]{targetClassName};
        final ArrayList<JavacErrorDetail> problemList =
                new ArrayList<JavacErrorDetail>();

        class CompilationUnit implements ICompilationUnit {

            String className;
            String sourceFile;

            CompilationUnit(String sourceFile, String className) {
                this.className = className;
                this.sourceFile = sourceFile;
            }

            public char[] getFileName() {
                return className.toCharArray();
            }

            public char[] getContents() {
                char[] result = null;
                Reader reader = null;
                try {
                    InputStreamReader isReader =
                            new InputStreamReader(new FileInputStream(sourceFile),
                                    ctxt.getOptions().getJavaEncoding());
                    reader = new BufferedReader(isReader);
                    if (reader != null) {
                        char[] chars = new char[8192];
                        StringBuffer buf = new StringBuffer();
                        int count;
                        while ((count = reader.read(chars, 0,
                                chars.length)) > 0) {
                            buf.append(chars, 0, count);
                        }
                        result = new char[buf.length()];
                        buf.getChars(0, result.length, result, 0);
                    }
                } catch (IOException e) {
                    log.error("Compilation error", e);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ioe) {
                            // Ignore
                        }
                    }
                }
                return result;
            }

            public char[] getMainTypeName() {
                int dot = className.lastIndexOf('.');
                if (dot > 0) {
                    return className.substring(dot + 1).toCharArray();
                }
                return className.toCharArray();
            }

            public char[][] getPackageName() {
                StringTokenizer izer =
                        new StringTokenizer(className, ".");
                char[][] result = new char[izer.countTokens() - 1][];
                for (int i = 0; i < result.length; i++) {
                    String tok = izer.nextToken();
                    result[i] = tok.toCharArray();
                }
                return result;
            }
        }

        final INameEnvironment env = new INameEnvironment() {

            public NameEnvironmentAnswer
            findType(char[][] compoundTypeName) {
                String result = "";
                String sep = "";
                for (int i = 0; i < compoundTypeName.length; i++) {
                    result += sep;
                    result += new String(compoundTypeName[i]);
                    sep = ".";
                }
                return findType(result);
            }

            public NameEnvironmentAnswer
            findType(char[] typeName,
                     char[][] packageName) {
                String result = "";
                String sep = "";
                for (int i = 0; i < packageName.length; i++) {
                    result += sep;
                    result += new String(packageName[i]);
                    sep = ".";
                }
                result += sep;
                result += new String(typeName);
                return findType(result);
            }

            private NameEnvironmentAnswer findType(String className) {

                InputStream is = null;
                try {
                    if (className.equals(targetClassName)) {
                        ICompilationUnit compilationUnit =
                                new CompilationUnit(sourceFile, className);
                        return
                                new NameEnvironmentAnswer(compilationUnit, null);
                    }
                    String resourceName =
                            className.replace('.', '/') + ".class";
                    is = classLoader.getResourceAsStream(resourceName);
                    if (is != null) {
                        byte[] classBytes;
                        byte[] buf = new byte[8192];
                        ByteArrayOutputStream baos =
                                new ByteArrayOutputStream(buf.length);
                        int count;
                        while ((count = is.read(buf, 0, buf.length)) > 0) {
                            baos.write(buf, 0, count);
                        }
                        baos.flush();
                        classBytes = baos.toByteArray();
                        char[] fileName = className.toCharArray();
                        ClassFileReader classFileReader =
                                new ClassFileReader(classBytes, fileName,
                                        true);
                        return
                                new NameEnvironmentAnswer(classFileReader, null);
                    }
                } catch (IOException exc) {
                    log.error("Compilation error", exc);
                } catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException exc) {
                    log.error("Compilation error", exc);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException exc) {
                            // Ignore
                        }
                    }
                }
                return null;
            }

            private boolean isPackage(String result) {
                if (result.equals(targetClassName)) {
                    return false;
                }
                String resourceName = result.replace('.', '/') + ".class";
                InputStream is =
                        classLoader.getResourceAsStream(resourceName);
                return is == null;
            }

            public boolean isPackage(char[][] parentPackageName,
                                     char[] packageName) {
                String result = "";
                String sep = "";
                if (parentPackageName != null) {
                    for (int i = 0; i < parentPackageName.length; i++) {
                        result += sep;
                        String str = new String(parentPackageName[i]);
                        result += str;
                        sep = ".";
                    }
                }
                String str = new String(packageName);
                if (Character.isUpperCase(str.charAt(0))) {
                    if (!isPackage(result)) {
                        return false;
                    }
                }
                result += sep;
                result += str;
                return isPackage(result);
            }

            public void cleanup() {
            }

        };

        final IErrorHandlingPolicy policy =
                DefaultErrorHandlingPolicies.proceedWithAllProblems();

        if (ctxt.getOptions().getJavaEncoding() != null) {
            settings.put(CompilerOptions.OPTION_Encoding,
                    ctxt.getOptions().getJavaEncoding());
        }

        final IProblemFactory problemFactory =
                new DefaultProblemFactory(Locale.getDefault());

        final ICompilerRequestor requestor = new ICompilerRequestor() {
            public void acceptResult(CompilationResult result) {
                try {
                    if (result.hasProblems()) {
                        IProblem[] problems = safeGetProblems(result);
                        for (int i = 0; i < problems.length; i++) {
                            IProblem problem = problems[i];
                            if (problem.isError()) {
                                String name =
                                        new String(problems[i].getOriginatingFileName());
                                try {
                                    problemList.add(
                                            ErrorDispatcher.createJavacError(
                                                    name,
                                                    pageNodes,
                                                    new StringBuffer(problem.getMessage()),
                                                    problem.getSourceLineNumber()));
                                } catch (JasperException e) {
                                    log.error("Error visiting node", e);
                                }
                            }
                        }
                    }
                    if (problemList.isEmpty()) {
                        ClassFile[] classFiles = result.getClassFiles();
                        for (int i = 0; i < classFiles.length; i++) {
                            ClassFile classFile = classFiles[i];
                            char[][] compoundName =
                                    classFile.getCompoundName();
                            String className = "";
                            String sep = "";
                            for (int j = 0;
                                 j < compoundName.length; j++) {
                                className += sep;
                                className += new String(compoundName[j]);
                                sep = ".";
                            }
                            byte[] bytes = classFile.getBytes();
                            String outFile = outputDir + "/" +
                                    className.replace('.', '/') + ".class";
                            FileOutputStream fout =
                                    new FileOutputStream(outFile);
                            BufferedOutputStream bos =
                                    new BufferedOutputStream(fout);
                            bos.write(bytes);
                            bos.close();
                        }
                    }
                } catch (IOException exc) {
                    log.error("Compilation error", exc);
                }
            }
        };

        ICompilationUnit[] compilationUnits =
                new ICompilationUnit[classNames.length];
        for (int i = 0; i < compilationUnits.length; i++) {
            compilationUnits[i] = new CompilationUnit(fileNames[i], classNames[i]);
        }

        Compiler compiler = new Compiler(env,
                policy,
                settings,
                requestor,
                problemFactory);
        compiler.compile(compilationUnits);

        if (problemList.isEmpty()) {
            return null;
        }
        return problemList.toArray(new JavacErrorDetail[]{});
    }

}
