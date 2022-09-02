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
    private String password;
    private String phone;
    private String userType;//0-普通用户 1-管理员
    private String managementId;
    private String managementName;
    private List<String> permissionList;
    private PremissonInfo premissonInfo;
}
