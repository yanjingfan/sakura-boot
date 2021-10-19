package com.sakura.common.cron.utils;

import java.util.concurrent.ScheduledFuture;

/**
 * @auther yangfan
 * @date 2021/10/18
 * @describle ScheduledFuture的包装类。ScheduledFuture是ScheduledExecutorService定时任务线程池的执行结果。
 */
public final class ScheduledTask {
    volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
