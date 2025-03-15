package kma.ktlt.post.app.api;

import kma.ktlt.post.domain.common.PageResponse;
import kma.ktlt.post.domain.common.ResponseData;
import kma.ktlt.post.domain.notification.NotificationService;
import kma.ktlt.post.domain.notification.dto.CountNotificationResponse;
import kma.ktlt.post.domain.notification.dto.NotificationResponse;
import kma.ktlt.post.domain.notification.dto.SetTokenDeviceRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    NotificationService notificationService;

    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @GetMapping
    public ResponseData<PageResponse<List<NotificationResponse>>> getNotification(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResponse<List<NotificationResponse>> response = notificationService.getNotification(pageNo, pageSize);

        return new ResponseData<>(
                HttpStatus.OK.value(),
                "successfully",
                LocalDateTime.now(),
                response
        );
    }

    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")


    @PutMapping("/token-device")
    public ResponseData<Void> setTokenDeviceNotification(@RequestBody SetTokenDeviceRequest request) {
        notificationService.setTokenDeviceFirebase(request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Token device updated successfully",
                LocalDateTime.now()
        );
    }

    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @GetMapping("/count-notification-not-read")
    public ResponseData<CountNotificationResponse> countNotificationNotRead() {
        CountNotificationResponse response = notificationService.countNotificationNotRead();
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "successfully",
                LocalDateTime.now(),
                response
        );
    }

}
