package com.gaoqisen.webcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "sys_code_menu")
public class SysCodeMenu {
    public static final String COL_CREATE_TIME = "create_time";
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统ID
     */
    @TableField(value = "sys_id")
    private String sysId;

    /**
     * 菜单ID
     */
    @TableField(value = "menu_id")
    private String menuId;

    public static final String COL_ID = "id";

    public static final String COL_SYS_ID = "sys_id";

    public static final String COL_MENU_ID = "menu_id";

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
     * 获取系统ID
     *
     * @return sys_id - 系统ID
     */
    public String getSysId() {
        return sysId;
    }

    /**
     * 设置系统ID
     *
     * @param sysId 系统ID
     */
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    /**
     * 获取菜单ID
     *
     * @return menu_id - 菜单ID
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * 设置菜单ID
     *
     * @param menuId 菜单ID
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}