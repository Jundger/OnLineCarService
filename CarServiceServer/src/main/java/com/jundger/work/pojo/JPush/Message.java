package com.jundger.work.pojo.JPush;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 17:45
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class Message<T> {

    private String msg_content;
    private String title;
    private String content_type;
    private T extras;

    public Message(String msg_content, String title, String content_type, T extras) {
        this.msg_content = msg_content;
        this.title = title;
        this.content_type = content_type;
        this.extras = extras;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent_type() {
        return content_type;
    }

    public T getExtras() {
        return extras;
    }
}
