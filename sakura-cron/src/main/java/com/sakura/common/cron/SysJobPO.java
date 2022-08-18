package com.sakura.common.cron;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @auther yangfan
 * @date 2021/10/18
 * @describle 定时任务实体类
 */
@Entity
@Data
@Table(name = "sys_job")
public class SysJobPO {
    /**
     * 任务ID
     */
    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;
    /**
     * bean名称
     */
    @Column(name = "bean_name")
    private String beanName;
    /**
     * 方法名称
     */
    @Column(name = "method_name")
    private String methodName;
    /**
     * 方法参数
     */
    @Column(name = "method_params")
    private String methodParams;
    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;
    /**
     * 状态（1正常 0暂停）
     */
    @Column(name = "job_status")
    private Integer jobStatus;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    //传到前台的时间格式
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //传到后台的时间格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    //传到前台的时间格式
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //传到后台的时间格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
