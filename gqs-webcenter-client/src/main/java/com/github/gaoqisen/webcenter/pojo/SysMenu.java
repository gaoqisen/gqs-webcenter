package com.github.gaoqisen.webcenter.pojo;


import java.util.List;

public class SysMenu {
    private Long menuId;

    private Long parentId;

    private String name;

    private String url;

    private Integer type;

    private String icon;

    private Integer orderNum;

    private List<?> list;

    private  String sysCodeId;

    public String getSysCodeId() {
        return sysCodeId;
    }

    public void setSysCodeId(String sysCodeId) {
        this.sysCodeId = sysCodeId;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}