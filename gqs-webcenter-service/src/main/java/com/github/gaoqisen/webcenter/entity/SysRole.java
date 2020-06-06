package com.github.gaoqisen.webcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.HashMap;

@TableName(value = "sys_role")
public class SysRole {

	@TableId(value = "role_id", type = IdType.AUTO)
	private Long roleId;

	/**
	 * 角色名称
	 */
	@TableField(value = "role_name")
	private String roleName;

	/**
	 * 备注
	 */
	@TableField(value = "remark")
	private String remark;

	/**
	 * 创建者ID
	 */
	@TableField(value = "create_user_id")
	private Long createUserId;

	@TableField(exist = false)
	private HashMap roleLimitInfo;

	public HashMap getRoleLimitInfo() {
		return roleLimitInfo;
	}

	public void setRoleLimitInfo(HashMap roleLimitInfo) {
		this.roleLimitInfo = roleLimitInfo;
	}

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;

	public static final String COL_ROLE_ID = "role_id";

	public static final String COL_ROLE_NAME = "role_name";

	public static final String COL_REMARK = "remark";

	public static final String COL_CREATE_USER_ID = "create_user_id";

	public static final String COL_CREATE_TIME = "create_time";

	/**
	 * @return role_id
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取角色名称
	 * @return role_name - 角色名称
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * 设置角色名称
	 * @param roleName 角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 获取备注
	 * @return remark - 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取创建者ID
	 * @return create_user_id - 创建者ID
	 */
	public Long getCreateUserId() {
		return createUserId;
	}

	/**
	 * 设置创建者ID
	 * @param createUserId 创建者ID
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * 获取创建时间
	 * @return create_time - 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}