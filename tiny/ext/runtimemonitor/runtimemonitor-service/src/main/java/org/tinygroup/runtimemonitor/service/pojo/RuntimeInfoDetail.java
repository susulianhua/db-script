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
package org.tinygroup.runtimemonitor.service.pojo;

import java.io.Serializable;
import java.util.Map;

/**
 * 运行时间详细信息基类
 * @author zhangliang08072
 * @version $Id: BaseRuntimeInfo.java, v 0.1 2016年12月28日 下午2:19:42 zhangliang08072 Exp $
 */
public class RuntimeInfoDetail implements Serializable {

    private static final long serialVersionUID = 5941948745200852568L;
    private String runtimeId;//代表本次运行的唯一标识
    private String businessId;//代表操作业务的唯一标识
    private String subType;//代表业务的子类型（譬如tiny服务分为local和remoate）
    private Long beginTime;//起始时间
    private Long endTime;//结束时间
    private Long timeInterval;//运行时长
    private String runResult;//运行结果
    private String exceptionClassName;//异常的类名
    private String exceptionMsg;//异常信息(不含堆栈信息)

    private Map<String, String> extendsItemMap;//特色扩展字段

    public String getRuntimeId() {
        return runtimeId;
    }

    public void setRuntimeId(String runtimeId) {
        this.runtimeId = runtimeId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Map<String, String> getExtendsItemMap() {
        return extendsItemMap;
    }

    public void setExtendsItemMap(Map<String, String> extendsItemMap) {
        this.extendsItemMap = extendsItemMap;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getRunResult() {
        return runResult;
    }

    public void setRunResult(String runResult) {
        this.runResult = runResult;
    }

    public String getExceptionClassName() {
        return exceptionClassName;
    }

    public void setExceptionClassName(String exceptionClassName) {
        this.exceptionClassName = exceptionClassName;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }
}
