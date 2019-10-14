/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
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

package org.tinygroup.flowcomponent.test.testcase.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * <!-- begin-user-doc --> 系统用户表 * <!-- end-user-doc -->
 */
public class TsysUser implements Serializable {

    /**
     * <!-- begin-user-doc --> 用户代码 * <!-- end-user-doc -->
     */
    private String userId;

    /**
     * <!-- begin-user-doc --> 机构编号 * <!-- end-user-doc -->
     */
    private String branchCode;

    /**
     * <!-- begin-user-doc --> 部门编号 * <!-- end-user-doc -->
     */
    private String depCode;

    /**
     * <!-- begin-user-doc --> 用户名称 * <!-- end-user-doc -->
     */
    private String userName;

    /**
     * <!-- begin-user-doc --> 用户密码 * <!-- end-user-doc -->
     */
    private String userPwd;

    /**
     * <!-- begin-user-doc --> 用户分类 * <!-- end-user-doc -->
     */
    private String userType;

    /**
     * <!-- begin-user-doc --> 柜员组别 * <!-- end-user-doc -->
     */
    private String userLevel;

    /**
     * <!-- begin-user-doc --> 银行编号 * <!-- end-user-doc -->
     */
    private String bankNo;

    /**
     * <!-- begin-user-doc --> 用户状态 * <!-- end-user-doc -->
     */
    private String userStatus;

    /**
     * <!-- begin-user-doc --> 锁定状态 * <!-- end-user-doc -->
     */
    private String lockStatus;

    /**
     * <!-- begin-user-doc --> 创建者 * <!-- end-user-doc -->
     */
    private String creator;

    /**
     * <!-- begin-user-doc --> 创建时间 * <!-- end-user-doc -->
     */
    private Date createDate;

    /**
     * <!-- begin-user-doc --> 最后修改时间 * <!-- end-user-doc -->
     */
    private Date modifyDate;

    /**
     * <!-- begin-user-doc --> 密码修改时间 * <!-- end-user-doc -->
     */
    private Date passModifyDate;

    /**
     * <!-- begin-user-doc --> 授权级别 * <!-- end-user-doc -->
     */
    private String authLevel;

    /**
     * <!-- begin-user-doc --> 个性化主页 * <!-- end-user-doc -->
     */
    private String homePage;

    /**
     * <!-- begin-user-doc --> 备注 * <!-- end-user-doc -->
     */
    private String remark;

    /**
     * <!-- begin-user-doc --> 扩展字段1 * <!-- end-user-doc -->
     */
    private String extField1;

    /**
     * <!-- begin-user-doc --> 扩展字段2 * <!-- end-user-doc -->
     */
    private String extField2;

    /**
     * <!-- begin-user-doc --> 扩展字段4 * <!-- end-user-doc -->
     */
    private String extField4;

    /**
     * <!-- begin-user-doc --> 扩展字段5 * <!-- end-user-doc -->
     */
    private String extField5;

    /**
     * <!-- begin-user-doc --> 扩展字段3 * <!-- end-user-doc -->
     */
    private String extField3;

    public String getUserId() {
        return userId;
    }

    /**
     * <!-- begin-user-doc --> 用户代码 * <!-- end-user-doc -->
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * <!-- begin-user-doc --> 机构编号 * <!-- end-user-doc -->
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDepCode() {
        return depCode;
    }

    /**
     * <!-- begin-user-doc --> 部门编号 * <!-- end-user-doc -->
     */
    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * <!-- begin-user-doc --> 用户名称 * <!-- end-user-doc -->
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    /**
     * <!-- begin-user-doc --> 用户密码 * <!-- end-user-doc -->
     */
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserType() {
        return userType;
    }

    /**
     * <!-- begin-user-doc --> 用户分类 * <!-- end-user-doc -->
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserLevel() {
        return userLevel;
    }

    /**
     * <!-- begin-user-doc --> 柜员组别 * <!-- end-user-doc -->
     */
    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getBankNo() {
        return bankNo;
    }

    /**
     * <!-- begin-user-doc --> 银行编号 * <!-- end-user-doc -->
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getUserStatus() {
        return userStatus;
    }

    /**
     * <!-- begin-user-doc --> 用户状态 * <!-- end-user-doc -->
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    /**
     * <!-- begin-user-doc --> 锁定状态 * <!-- end-user-doc -->
     */
    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getCreator() {
        return creator;
    }

    /**
     * <!-- begin-user-doc --> 创建者 * <!-- end-user-doc -->
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    /**
     * <!-- begin-user-doc --> 创建时间 * <!-- end-user-doc -->
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * <!-- begin-user-doc --> 最后修改时间 * <!-- end-user-doc -->
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getPassModifyDate() {
        return passModifyDate;
    }

    /**
     * <!-- begin-user-doc --> 密码修改时间 * <!-- end-user-doc -->
     */
    public void setPassModifyDate(Date passModifyDate) {
        this.passModifyDate = passModifyDate;
    }

    public String getAuthLevel() {
        return authLevel;
    }

    /**
     * <!-- begin-user-doc --> 授权级别 * <!-- end-user-doc -->
     */
    public void setAuthLevel(String authLevel) {
        this.authLevel = authLevel;
    }

    public String getHomePage() {
        return homePage;
    }

    /**
     * <!-- begin-user-doc --> 个性化主页 * <!-- end-user-doc -->
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getRemark() {
        return remark;
    }

    /**
     * <!-- begin-user-doc --> 备注 * <!-- end-user-doc -->
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtField1() {
        return extField1;
    }

    /**
     * <!-- begin-user-doc --> 扩展字段1 * <!-- end-user-doc -->
     */
    public void setExtField1(String extField1) {
        this.extField1 = extField1;
    }

    public String getExtField2() {
        return extField2;
    }

    /**
     * <!-- begin-user-doc --> 扩展字段2 * <!-- end-user-doc -->
     */
    public void setExtField2(String extField2) {
        this.extField2 = extField2;
    }

    public String getExtField4() {
        return extField4;
    }

    /**
     * <!-- begin-user-doc --> 扩展字段4 * <!-- end-user-doc -->
     */
    public void setExtField4(String extField4) {
        this.extField4 = extField4;
    }

    public String getExtField5() {
        return extField5;
    }

    /**
     * <!-- begin-user-doc --> 扩展字段5 * <!-- end-user-doc -->
     */
    public void setExtField5(String extField5) {
        this.extField5 = extField5;
    }

    public String getExtField3() {
        return extField3;
    }

    /**
     * <!-- begin-user-doc --> 扩展字段3 * <!-- end-user-doc -->
     */
    public void setExtField3(String extField3) {
        this.extField3 = extField3;
    }

}
