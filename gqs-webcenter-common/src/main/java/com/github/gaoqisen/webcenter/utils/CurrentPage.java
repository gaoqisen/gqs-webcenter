package com.github.gaoqisen.webcenter.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

public class CurrentPage<T> {

    // 每页显示条数
    private Integer size;
    // 当前页数
    private Integer current;
    // 排序字段
    private  String orderField;
    // 排序方式
    private String order;
    // 查询参数
    private String params;

    public static final String ASC = "asc";


    public IPage<T> getPage() {
        Integer curPage = 1;
        Integer curSise = 10;
        if(size != null && size > 0){
            curSise = size;
        }
        if(current != null && current > 0){
            curPage = current;
        }

        Page<T> page = new Page<T>(curPage, curSise);
        page.setOptimizeCountSql(true);
        String filterOrderField = SQLFilter.sqlInject(orderField);

        if(StringUtils.isNotBlank(filterOrderField) && StringUtils.isNotBlank(order)) {
            if(ASC.equalsIgnoreCase(order)) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }


    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
