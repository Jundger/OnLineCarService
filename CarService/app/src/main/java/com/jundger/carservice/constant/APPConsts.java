package com.jundger.carservice.constant;

/**
 * Created by 14246 on 2018/1/3.
 */

public class APPConsts {

    /**
     * 本地SP存储文件名
     */
    public static final String SHARED_SAVE_NAME = "OnLineCarService";

    /**
     * 本地SQLite数据库名称
     */
    public static final String DATABASE_NAME = "OnLineCarService.db";

    /**
     * 本地SQLite数据库表名
     */
    public static final String DB_TABLE_OBD = "OBD_Info";

    /**
     * 本地SQLite数据库存储版本号
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * 初始定位地点纬度
     * 重庆市城区
     */
    public static final double DEFAULT_LATITUDE = 29.35;

    /**
     * 初始定位地点经度
     * 重庆市城区
     */
    public static final double DEFAULT_LONGITUDE = 106.33;

}
