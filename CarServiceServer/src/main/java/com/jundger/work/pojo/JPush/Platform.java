package com.jundger.work.pojo.JPush;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 17:01
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class Platform<T> {

    private String alert;
    private String title;
    private Integer priority;
    private T extras;

    public Platform(String alert, String title, Integer priority, T extras) {
        this.alert = alert;
        this.title = title;
        this.priority = priority;
        this.extras = extras;
    }

    public String getAlert() {
        return alert;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPriority() {
        return priority;
    }

    public T getExtras() {
        return extras;
    }
}
