package com.justdoit712.springboot.demo.task.job;

import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * 定时任务的具体实现类，演示了三种常用的定时方式
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 11:38
 */
@Component
public class TaskJob {

    private static final Logger log = LoggerFactory.getLogger(TaskJob.class);

    /**
     * 按照 cron 表达式定时执行
     * 这里的表达式表示：每隔 10 秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void job1() {
        log.info("【job1】开始执行：测试 cron 表达式，当前时间={}", DateUtil.formatDateTime(new Date()));
    }

    /**
     * 按照固定速率（fixedRate）定时执行
     * 从上一次任务的【开始】时间算起，每 5 秒执行一次
     * 注意：如果该任务执行时间超过 5 秒，下一次任务会立刻开始执行。
     */
    @Scheduled(fixedRate = 5000)
    public void job2() {
        log.info("【job2】开始执行：测试 fixedRate 固定速率，当前时间={}", DateUtil.formatDateTime(new Date()));
    }

    /**
     * 按照固定延迟（fixedDelay）定时执行
     * 从上一次任务的【结束】时间算起，延迟 8 秒后执行下一次任务
     * 注意：它保证了每次任务之间必定间隔 8 秒，无论上一次任务执行了多久。
     */
    @Scheduled(fixedDelay = 8000)
    public void job3() {
        log.info("【job3】开始执行：测试 fixedDelay 固定延迟，当前时间={}", DateUtil.formatDateTime(new Date()));
    }
}
