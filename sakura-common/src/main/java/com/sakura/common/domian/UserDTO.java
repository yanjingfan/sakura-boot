package com.sakura.common.domian;

import lombok.*;

import javax.validation.constraints.Email;
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
    @NotEmpty
    private String username;

    /**
     * 密码
     */
    @NotEmpty
    private String passwd;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 0-普通用户 1-管理员
     */
    private String userType;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

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
     * 帐号启用状态：1->启用；0->禁用
     */
    private Integer userStatus;

    /**
     * 用户来源：0->自填；1->管理员添加；2->微信；3：第三方
     */
    private Integer source;

    /**
     * 是否是管理员：1->是；0->否
     */
    private Integer admin;

    /**
     * 排序字段
     */
    private Long sort;

    /**
     * 平台id
     */
    private Integer platformId;

    /**
     * 菜单
     */
    private List<MenuTree> menuList;

    /**
     * 资源，控制菜单按钮的显示
     */
    private List<String> resourceList;

    /**
     * 资源过滤，控制查询是否添加权限控制
     */
    private List<String> filterList;
}
