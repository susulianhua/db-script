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
package org.tinygroup.template;

import org.antlr.v4.runtime.ParserRuleContext;
import org.tinygroup.template.interpret.AbstractTemplateExceptionInfo;
import org.tinygroup.template.interpret.DefaultExceptionInfo;
import org.tinygroup.template.interpret.DefaultExceptionInfoFormater;
import org.tinygroup.template.interpret.MacroExceptionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoguo on 2014/6/4.
 */
public class TemplateException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7478199180490496722L;


    /**
     * 异常信息列表
     */
    protected List<TemplateExceptionInfo> exceptionInfos = new ArrayList<TemplateExceptionInfo>();

    protected String detailMessage = null;

    private TemplateExceptionInfoFormater templateExceptionInfoFormater;

    public TemplateException() {
        super();
    }

    public TemplateException(String msg) {
        super(msg);
        detailMessage = msg;
    }

    public TemplateException(Throwable throwable) {
        super(throwable);
        copyExceptionInfos(throwable);
    }

    public TemplateException(Throwable throwable, ParserRuleContext context, String fileInfo) {
        this(throwable);
        updateExceptionInfo(context, fileInfo);
    }

    public TemplateException(String msg, ParserRuleContext context, String fileInfo) {
        super(msg);
        detailMessage = msg;
        updateExceptionInfo(context, fileInfo);
    }

    public TemplateExceptionInfoFormater getTemplateExceptionInfoFormater() {
        if (templateExceptionInfoFormater == null) {
            templateExceptionInfoFormater = new DefaultExceptionInfoFormater();
        }
        return templateExceptionInfoFormater;
    }

    public void setTemplateExceptionInfoFormater(
            TemplateExceptionInfoFormater templateExceptionInfoFormater) {
        this.templateExceptionInfoFormater = templateExceptionInfoFormater;
    }

    protected void copyExceptionInfos(Throwable throwable) {
        if (throwable instanceof TemplateException) {
            //异常本身就是链式的，因此父子异常之间的exceptionInfos可以复用
            TemplateException e = (TemplateException) throwable;
            exceptionInfos = e.exceptionInfos;
        }

    }

    /**
     * 更新异常信息块
     *
     * @param context
     * @param fileInfo
     */
    public void updateExceptionInfo(ParserRuleContext context, String fileInfo) {
        if (fileInfo != null) {
            if (exceptionInfos.isEmpty()) {
                exceptionInfos.add(new DefaultExceptionInfo(context, detailMessage, fileInfo));
            } else {
                //得到最近插入的异常信息块
                AbstractTemplateExceptionInfo info = (AbstractTemplateExceptionInfo) exceptionInfos.get(exceptionInfos.size() - 1);
                if (info.getFileInfo().equals(fileInfo)) {
                    //更新ParserRuleContext
                    //info.updateParserRuleContext(context);
                } else {
                    //插入新记录
                    exceptionInfos.add(new DefaultExceptionInfo(context, detailMessage, fileInfo));
                }
            }
        }
    }

    /**
     * 获取本异常包含的全部异常信息块
     * @return
     */
    public List<TemplateExceptionInfo> getExceptionInfos() {
        return exceptionInfos;
    }

    /**
     * 获取本异常包含的第一个异常信息块
     * @return
     */
    public TemplateExceptionInfo getExceptionInfo() {
        if (exceptionInfos != null && !exceptionInfos.isEmpty()) {
            return exceptionInfos.get(0);
        }
        return null;
    }

    /**
     * 重组异常信息块
     */
    public void recombine() {
        Map<String, Macro> macroMaps = new HashMap<String, Macro>();
        for (TemplateExceptionInfo info : exceptionInfos) {
            if (info.isMacroException()) {
                //得到宏相关信息
                macroMaps.put(info.getMacroInfo().getMacroPath(), info.getMacroInfo());
            }
        }
        List<TemplateExceptionInfo> list = new ArrayList<TemplateExceptionInfo>();
        for (TemplateExceptionInfo info : exceptionInfos) {
            if (info instanceof AbstractTemplateExceptionInfo) {
                AbstractTemplateExceptionInfo ainfo = (AbstractTemplateExceptionInfo) info;
                if (macroMaps.containsKey(ainfo.getFileInfo())) {
                    //宏异常
                    list.add(0, new MacroExceptionInfo(ainfo.getParserRuleContext(), ainfo.getReason(), ainfo.getFileInfo(), macroMaps.get(ainfo.getFileInfo())));
                } else {
                    //页面异常
                    list.add(0, new DefaultExceptionInfo(ainfo.getParserRuleContext(), ainfo.getReason(), ainfo.getFileInfo()));
                }
            } else {
                list.add(info);
            }

        }
        exceptionInfos = list;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (TemplateExceptionInfo info : exceptionInfos) {
            sb.append(getTemplateExceptionInfoFormater().getMessage(info));
        }
        return sb.toString();
    }

}
