package com.drewsec.user_central.service;

import com.drewsec.user_central.dto.NotificationDto;

public interface NotificationService {

    void sendNotification(String userId, NotificationDto notificationDto);

}
