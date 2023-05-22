package com.sakura.common.web.domian;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 用户信息类，在sa-token示例模块需要用到此类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "用户名")
    private String lqbUsername;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String lqbPasswd;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String lqbIcon;

    /**
     * 邮箱
     */
//    @Valid
//    @Email
    @ApiModelProperty(value = "邮箱")
    private String lqbEmail;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String lqbNickName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String lqbMobile;

    /**
     * 第二个手机号
     */
    @ApiModelProperty(value = "第二个手机号")
    private String lqbMobileTwo;

    /**
     * 座机号
     */
    @ApiModelProperty(value = "座机号")
    private String lqbTelephone;

    /**
     * 用户来源：0->自填；1->管理员添加；2->微信；3：第三方
     */
    @ApiModelProperty(value = "用户来源：0->自填；1->管理员添加；2->微信；3：第三方")
    private Integer lqbSource;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private Long lqbOrderNum;

    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private Integer lqbPlatformId;

}
