package com.sakura.common.cron;

import com.sakura.common.cron.exception.CronException;
import com.sakura.common.cron.utils.CronTaskRegistrar;
import com.sakura.common.cron.utils.SchedulingRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @auther yangfan
 * @date 2021/10/18
 * @describle
 */
@Service
@Slf4j
public class SysJobService {

    @Autowired
    private SysJobRepository sysJobRepository;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Transactional(rollbackFor = {Exception.class})
    public void addSysJob(SysJob sysJob) {
        SysJobPO sysJobPO = new SysJobPO();
        BeanUtils.copyProperties(sysJob, sysJobPO);
        sysJobPO.setCreateTime(LocalDateTime.now());
        sysJobPO.setUpdateTime(LocalDateTime.now());
        try {
            sysJobRepository.save(sysJobPO);
        } catch (Exception e) {
            log.info("新增定时任务失败!", e);
            throw new CronException("新增定时任务失败!");
        }
        SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
    }

    @Transactional(rollbackFor = {Exception.class})
    public void editSysJob(SysJob sysJob) {

        SysJobPO sysJobPO = new SysJobPO();
        BeanUtils.copyProperties(sysJob, sysJobPO);
        sysJobPO.setUpdateTime(LocalDateTime.now());

        SysJobPO existedSysJob = sysJobRepository.getOne(sysJob.getJobId());
        //先移除
        SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(task);

        sysJobPO.setCreateTime(existedSysJob.getCreateTime());
        try {
            sysJobRepository.save(sysJobPO);
            //然后判断是否开启
            if (sysJob.getJobStatus().equals(SysJobStatus.NORMAL.ordinal())) {
                SchedulingRunnable task2 = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task2, sysJob.getCronExpression());
            }
        } catch (Exception e) {
            log.info("编辑定时任务失败!", e);
            throw new CronException("编辑定时任务失败!");
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteSysJobById(Integer jobId) {
        SysJobPO existedSysJob = sysJobRepository.getOne(jobId);
        try {
            sysJobRepository.deleteById(jobId);
        } catch (Exception e) {
            log.info("删除定时任务失败!", e);
            throw new CronException("删除定时任务失败!");
        }
        SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(task);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void startOrStopSysJob(Integer jobId, Integer status) {
        SysJobPO existedSysJob = sysJobRepository.getOne(jobId);
        SchedulingRunnable task2 = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(task2);

        existedSysJob.setJobStatus(status);
        sysJobRepository.save(existedSysJob);

        if (existedSysJob.getJobStatus().equals(SysJobStatus.NORMAL.ordinal())) {
            SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
            cronTaskRegistrar.addCronTask(task, existedSysJob.getCronExpression());
        }
    }

    public List<SysJobPO> list() {
        return sysJobRepository.findAll();
    }

}
