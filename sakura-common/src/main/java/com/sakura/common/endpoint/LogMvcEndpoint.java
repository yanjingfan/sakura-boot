package com.sakura.common.endpoint;

import com.sakura.common.result.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 利用actuator动态设置SpringBoot的logback框架日志级别
 *
 */
@RequestMapping("/log")
@RestController
public class LogMvcEndpoint {

	private static Logger logger = LoggerFactory.getLogger(LogMvcEndpoint.class);

	@Autowired
    private LogLevelProperties properties;

    // 注意该 path 不是 http://localhost:8080/{level}/{pkn}
    // 而是构造方法中 MyLogEndpoint 的 id 对应 path 下的请求节点。
    // http://localhost:8080/mgrLogging/{level}/{pkn}

    @RequestMapping(value = "/{level}/{pkn}", method=RequestMethod.POST)
    public CommonResult<String> changeLog(@PathVariable LogLevel level, @PathVariable("pkn") String packageName) {
        boolean enabled = properties.isEnabled();
        // 关闭此功能
        if (!enabled) {
            return CommonResult.success("默认不开启此功能，如需开启，请添加相应的配置！");
        }

	    String format = "[%s]包下的日志级别成功改为[%s]";
    	logger.warn(String.format(format, packageName, level));

        try {
            // 处理日志 level 改变逻辑，根据个人需求改变
            LogbackLoggingSystem logbackLoggingSystem = new LogbackLoggingSystem(this.getClass().getClassLoader());
            logbackLoggingSystem.setLogLevel(packageName, level);

            // 处理成功（未抛出异常）返回 success
            return CommonResult.success(String.format(format, packageName, level));
        } catch (Exception e) {
            logger.error("改动日志级别失败！", e);
            return CommonResult.failed("改动日志级别失败！！");
        }
    }
}
