package com.gaoqisen.webcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "sys_config")
public class SysConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * key
     */
    @TableField(value = "param_key")
    private String paramKey;

    /**
     * value
     */
    @TableField(value = "param_value")
    private String paramValue;

    /**
     * 状态   0：隐藏   1：显示
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    public static final String COL_ID = "id";

    public static final String COL_PARAM_KEY = "param_key";

    public static final String COL_PARAM_VALUE = "param_value";

    public static final String COL_STATUS = "status";

    public static final String COL_REMARK = "remark";

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取key
     *
     * @return param_key - key
     */
    public String getParamKey() {
        return paramKey;
    }

    /**
     * 设置key
     *
     * @param paramKey key
     */
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * 获取value
     *
     * @return param_value - value
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * 设置value
     *
     * @param paramValue value
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * 获取状态   0：隐藏   1：显示
     *
     * @return status - 状态   0：隐藏   1：显示
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态   0：隐藏   1：显示
     *
     * @param status 状态   0：隐藏   1：显示
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}