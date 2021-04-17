package com.sakura.common.web.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocalUser {
    private String id;
    private String userId;
    private String userName;
    private String orgId;
    private String depId;
    private String depName;
    private String userType;
    private String sessionId;
}
