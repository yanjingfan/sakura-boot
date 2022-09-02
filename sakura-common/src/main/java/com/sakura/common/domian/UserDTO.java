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

    private String username;
    /**
     * 密码
     */
    private String passwd;

    private String phone;
    /**
     * 0-普通用户 1-管理员
     */
    private String userType;

    private String managementId;

    private String managementName;

    private List<String> permissionList;

    /**
     * 菜单
     */
    private List menuList;

    /**
     * 资源
     */
    private List<String> resourceList;

    /**
     * 资源过滤
     */
    private List<String> filterList;
}
