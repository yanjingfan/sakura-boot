package com.sakura.common.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @auther YangFan
 * @Date 2021/8/7 18:20
 */
@ConfigurationProperties(prefix = "security", ignoreUnknownFields = true)
public class WebSecurityProperties {

    private final Csrf csrf = new Csrf();

    private final Sql sql = new Sql();

    public Csrf getCsrf() {
        return csrf;
    }

    public Sql getSql() {
        return sql;
    }

    public static class Csrf {
        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 忽略的URL
         */
        private List<String> excludes;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getExcludes() {
            return excludes;
        }

        public void setExcludes(List<String> excludes) {
            this.excludes = excludes;
        }
    }

    public static class Sql {
        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 忽略的URL
         */
        private List<String> excludes;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getExcludes() {
            return excludes;
        }

        public void setExcludes(List<String> excludes) {
            this.excludes = excludes;
        }
    }

}
