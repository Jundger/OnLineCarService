package com.jundger.work.pojo.JPush;

import com.jundger.work.pojo.json.OrderJson;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 16:50
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class JPushJson<T> {

    private String cid;
    private String platform;
    private Audience audience;
    private Notification<T> notification;
    private Message<T> message;
    private Options options;

    public JPushJson(String cid, String platform, Audience audience, Notification<T> notification, Message<T> message, Options options) {
        this.cid = cid;
        this.platform = platform;
        this.audience = audience;
        this.notification = notification;
        this.message = message;
        this.options = options;
    }

    public String getCid() {
        return cid;
    }

    public String getPlatform() {
        return platform;
    }

    public Audience getAudience() {
        return audience;
    }

    public Notification<T> getNotification() {
        return notification;
    }

    public Message<T> getMessage() {
        return message;
    }

    public Options getOptions() {
        return options;
    }
}
