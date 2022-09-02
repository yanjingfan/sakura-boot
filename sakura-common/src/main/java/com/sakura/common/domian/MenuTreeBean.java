package com.sakura.common.domian;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xiaokun on 2022/9/2.
 */
@Data
public class MenuTreeBean {
    private String id;
    private String text;
    private String url;
    private String parentId;
    private List<MenuTreeBean> children = new ArrayList();
    private String icon;
    private int isShow;
}
