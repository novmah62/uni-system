package kma.ktlt.post.domain.notification.entity;

import kma.ktlt.post.domain.common.enumType.TypeReference;
import kma.ktlt.post.domain.notification.dto.NotificationResponse;
import kma.ktlt.post.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
    SELECT n FROM Notification n 
    WHERE n.referenceId = :referenceId 
      AND n.type = :typeReference
""")
    Optional<Notification> findNotification(@Param("referenceId") Long referenceId,
                              @Param("typeReference") TypeReference typeReference);

    @Modifying
    @Query("""
    UPDATE Notification n 
    SET n.isRemoved = true 
    WHERE n.type = :typeReference AND n.isRemoved = false AND n.referenceId = :referenceId
""")
    void onPostRemove(@Param("referenceId") Long referenceId,
                                       @Param("typeReference") TypeReference typeReference);


    @Query("SELECT COUNT(n) FROM Notification n WHERE n.receiverId = :userId AND n.isRead = false")
    int countNotificationNotRead(@Param("userId") String userId);


    @Query("""
    SELECT new kma.ktlt.post.domain.notification.dto.NotificationResponse(
        n.id, n.receiverId, n.type, n.referenceId, n.isRead, n.content, n.createdAt, n.updatedAt
    )
    FROM Notification n 
    WHERE n.receiverId = :userId 
    ORDER BY n.createdAt DESC 
""")
    Page<NotificationResponse> getNotification(Pageable pageable, @Param("userId") String userId);


    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiverId = :userId AND n.isRead = false")
    void markReadAll(@Param("userId") String userId);

}
