package com.sakura.common.endpoint;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @auther YangFan
 * @Date 2021/8/9 20:47
 */
@ConfigurationProperties(prefix = "log.update-level", ignoreUnknownFields = true)
public class LogLevelProperties {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
