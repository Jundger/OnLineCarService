package com.jundger.work.pojo.JPush;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 17:01
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class Notification<T> {

    private Platform<T> android;

    public Notification(Platform<T> android) {
        this.android = android;
    }

    public Platform<T> getAndroid() {
        return android;
    }
}
