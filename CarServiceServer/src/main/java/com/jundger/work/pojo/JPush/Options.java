package com.jundger.work.pojo.JPush;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 17:51
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class Options {
    private Integer time_to_live;

    public Options(Integer time_to_live) {
        this.time_to_live = time_to_live;
    }

    public Integer getTime_to_live() {
        return time_to_live;
    }
}
