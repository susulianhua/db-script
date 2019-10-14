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
package org.tinygroup.pdfindexsource;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import junit.framework.TestCase;

import java.io.IOException;

/**
 * IText原始接口测试用例
 *
 * @author yancheng11334
 */
public class PdfTest extends TestCase {

    public void testPdfContent() throws IOException {
        String fileName = "src/test/resources/TF-120416-0832-3.pdf";
        String content = readPdfText(fileName, null);
        System.out.println(content);
        assertTrue(content.indexOf("Tiny框架提出了一个在资产库的概念，它不是一个新概念") > 0);
    }

    public void testPdfWithPassword() throws IOException {
        String fileName = "src/test/resources/password.pdf";
        String content = readPdfText(fileName, "tinyabc");
        System.out.println(content);
        assertTrue(content.indexOf("测试") > 0);
    }

    public String readPdfText(String fileName, String password)
            throws IOException {
        PdfReader reader = null;

        if (password != null) {
            reader = new PdfReader(fileName, password.getBytes("UTF-8"));
        } else {
            reader = new PdfReader(fileName);
        }

        try {
            StringBuffer buff = new StringBuffer();
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                buff.append(PdfTextExtractor.getTextFromPage(reader, i));
            }
            return buff.toString();
        } finally {
            reader.close();
        }

    }
}
