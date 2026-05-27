package cn.hutool.extra.mq.business;

import java.io.Serializable;
import java.util.Date;

public class NotificationMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String notificationId;
    private String userId;
    private String title;
    private String content;
    private String type;
    private Date createTime;
    private boolean read;

    public NotificationMessage() {
        this.createTime = new Date();
        this.read = false;
    }

    public NotificationMessage(String notificationId, String userId, String title, String content, String type) {
        this();
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "notificationId='" + notificationId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", read=" + read +
                '}';
    }
}
