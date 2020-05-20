package com.gaoqisen.webcenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName(value = "sys_rest")
public class SysRest {
    /**
     * url和应用名称的摘要
     */
    @TableField(value = "digest")
    @TableId
    private String digest;

    /**
     * url链接
     */
    @TableField(value = "url")
    private String url;

    /**
     * api名称
     */
    @TableField(value = "api_name")
    private String apiName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 应用名称
     */
    @TableField(value = "application_name")
    private String applicationName;

    /**
     * 权限（anon不用登录就可以访问，authc登录后即可访问，perms[权限]拥有此权限方可访问）
     */
    @TableField(value = "permissions")
    private String permissions;

    public static final String COL_DIGEST = "digest";

    public static final String COL_URL = "url";

    public static final String COL_API_NAME = "api_name";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_APPLICATION_NAME = "application_name";

    public static final String COL_PERMISSIONS = "permissions";

    /**
     * 获取url和应用名称的摘要
     *
     * @return digest - url和应用名称的摘要
     */
    public String getDigest() {
        return digest;
    }

    /**
     * 设置url和应用名称的摘要
     *
     * @param digest url和应用名称的摘要
     */
    public void setDigest(String digest) {
        this.digest = digest;
    }

    /**
     * 获取url链接
     *
     * @return url - url链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置url链接
     *
     * @param url url链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取api名称
     *
     * @return api_name - api名称
     */
    public String getApiName() {
        return apiName;
    }

    /**
     * 设置api名称
     *
     * @param apiName api名称
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取应用名称
     *
     * @return application_name - 应用名称
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * 设置应用名称
     *
     * @param applicationName 应用名称
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * 获取权限（anon不用登录就可以访问，authc登录后即可访问，perms[权限]拥有此权限方可访问）
     *
     * @return permissions - 权限（anon不用登录就可以访问，authc登录后即可访问，perms[权限]拥有此权限方可访问）
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * 设置权限（anon不用登录就可以访问，authc登录后即可访问，perms[权限]拥有此权限方可访问）
     *
     * @param permissions 权限（anon不用登录就可以访问，authc登录后即可访问，perms[权限]拥有此权限方可访问）
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}