package com.github.gaoqisen.webcenter.pojo;


import java.util.Date;

public class SysRest {
    /**
     * url和应用名称的摘要
     */
    private String digest;

    /**
     * url链接
     */
    private String url;

    /**
     * api名称
     */
    private String apiName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 权限（anon不用登录就可以访问，authc登录后即可访问，perms[权限]拥有此权限方可访问）
     */
    private String permissions;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}