package kma.ktlt.post.domain.notification.dto;

import jakarta.persistence.*;
import kma.ktlt.post.domain.common.enumType.TypeReference;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


public class NotificationResponse {
    private Long id;

    private String receiverId;


    private TypeReference type;

    private Long referenceId;

    private boolean isRead = false;


    private String content;


    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public TypeReference getType() {
        return type;
    }

    public void setType(TypeReference type) {
        this.type = type;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }


    public NotificationResponse(Long id, String receiverId, TypeReference type, Long referenceId, boolean isRead, String content, LocalDateTime createdAt, LocalDateTime readAt) {
        this.id = id;
        this.receiverId = receiverId;
        this.type = type;
        this.referenceId = referenceId;
        this.isRead = isRead;
        this.content = content;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }
}
