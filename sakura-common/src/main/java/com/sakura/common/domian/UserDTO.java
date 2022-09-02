package com.sakura.common.domian;

import lombok.*;

import java.util.List;

/**
 * 用户信息类，在sa-token示例模块需要用到此类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    /**
     * 姓名
     */
    private String username;

    /**
     * 密码
     */
    private String passwd;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 0-普通用户 1-管理员
     */
    private String userType;

    /**
     * 部门ID
     */
    private String managementId;

    /**
     * 部门名称
     */
    private String managementName;

    private List<String> permissionList;

    /**
     * 菜单
     */
    private List<MenuTreeBean> menuList;

    /**
     * 资源，控制菜单按钮的显示
     */
    private List<String> resourceList;

    /**
     * 资源过滤，控制查询是否添加权限控制
     */
    private List<String> filterList;
}
