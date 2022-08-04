package com.sakura.common.domian;

import lombok.*;

import java.util.List;

/**
 * 用户信息类
 * Created by macro on 2020/6/19.
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
    private List<String> permissionList;
}
