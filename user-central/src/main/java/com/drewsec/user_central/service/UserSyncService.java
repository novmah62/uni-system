package com.drewsec.user_central.service;

import org.springframework.security.oauth2.jwt.Jwt;

public interface UserSyncService {

    void synchronizeWithIdp(Jwt token);
    void synchronizeWithCache();

}
