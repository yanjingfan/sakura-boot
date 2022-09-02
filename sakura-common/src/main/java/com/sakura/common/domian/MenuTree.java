package com.sakura.common.domian;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单实体类
 * created by xiaokun on 2022/9/2.
 */
@Data
public class MenuTree {

    private String id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单图片url
     */
    private String iconUrl;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 子菜单集合
     */
    private List<MenuTree> children = new ArrayList();

    /**
     * 是否显示，1：显示，0：不显示
     */
    private int show;
}
