package com.github.gaoqisen.webcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "sys_role_menu")
public class SysRoleMenu {

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 角色ID
	 */
	@TableField(value = "role_id")
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@TableField(value = "menu_id")
	private Long menuId;

	public static final String COL_ID = "id";

	public static final String COL_ROLE_ID = "role_id";

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
	 * 获取角色ID
	 * @return role_id - 角色ID
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * 设置角色ID
	 * @param roleId 角色ID
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取菜单ID
	 * @return menu_id - 菜单ID
	 */
	public Long getMenuId() {
		return menuId;
	}

	/**
	 * 设置菜单ID
	 * @param menuId 菜单ID
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

}