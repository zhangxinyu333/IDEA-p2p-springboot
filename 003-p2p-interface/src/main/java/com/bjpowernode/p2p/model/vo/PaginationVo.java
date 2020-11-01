package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 张新宇
 * 2020/7/26
 */
public class PaginationVo<T> implements Serializable {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 每页展示的数据
     */
    private List<T> dataList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
