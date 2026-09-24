package com.eldercare.models;

import java.time.LocalDateTime;

public class Communication {

    private int communicationId;
    private int elderlyId;
    private int caregiverId;

    private String messageType;
    private String message;

    private LocalDateTime sentTime;
    private LocalDateTime readTime;

    private boolean isRead;


    // Constructor for creating a new communication
    public Communication(int elderlyId,
                         int caregiverId,
                         String messageType,
                         String message) {

        this.elderlyId = elderlyId;
        this.caregiverId = caregiverId;
        this.messageType = messageType;
        this.message = message;
        this.isRead = false;
        this.sentTime = LocalDateTime.now();
    }


    // Constructor for loading from database
    public Communication(int communicationId,
                         int elderlyId,
                         int caregiverId,
                         String messageType,
                         String message,
                         LocalDateTime sentTime,
                         LocalDateTime readTime,
                         boolean isRead) {

        this.communicationId = communicationId;
        this.elderlyId = elderlyId;
        this.caregiverId = caregiverId;
        this.messageType = messageType;
        this.message = message;
        this.sentTime = sentTime;
        this.readTime = readTime;
        this.isRead = isRead;
    }


    public int getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(int communicationId) {
        this.communicationId = communicationId;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public int getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(int caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }


    public boolean isValid() {
        return elderlyId > 0
                && caregiverId > 0
                && message != null
                && !message.isEmpty();
    }


    @Override
    public String toString() {
        return "Communication{" +
                "communicationId=" + communicationId +
                ", elderlyId=" + elderlyId +
                ", caregiverId=" + caregiverId +
                ", messageType='" + messageType + '\'' +
                ", message='" + message + '\'' +
                ", sentTime=" + sentTime +
                ", readTime=" + readTime +
                ", isRead=" + isRead +
                '}';
    }
}