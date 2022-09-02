package com.sakura.common.domian;

import lombok.Data;

import java.util.List;

/**
 * created by xiaokun on 2022/9/1.
 */
@Data
public class PremissonInfo {
    private List menuList;//菜单
    private List<String> resourceList;//资源
    private List<String> filterList;//资源过滤
}
