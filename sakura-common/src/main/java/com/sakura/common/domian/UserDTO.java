package com.sakura.common.domian;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    @NotBlank(message = "姓名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String passwd;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 邮箱
     */
//    @Valid
//    @Email
    private String email;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 第二个手机号
     */
    private String mobileTwo;

    /**
     * 座机号
     */
    private String telephone;

    /**
     * 用户来源：0->自填；1->管理员添加；2->微信；3：第三方
     */
    private Integer source;

    /**
     * 排序字段
     */
    private Long sort;

    /**
     * 平台id
     */
    private Integer platformId;

}
