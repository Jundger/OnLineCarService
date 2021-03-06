package com.jundger.work.dao;

import com.jundger.work.pojo.Site;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SiteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table site
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table site
     *
     * @mbggenerated
     */
    int insert(Site record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table site
     *
     * @mbggenerated
     */
    int insertSelective(Site record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table site
     *
     * @mbggenerated
     */
    Site selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table site
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Site record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table site
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Site record);

    List<Map<String, Object>> selectLocation();

    List<Map<String, Object>> selectNearList(@Param("longitude") Float longitude, @Param("latitude") Float latitude,
                                             @Param("radius") Double radius);

    Map<String, Object> selectSiteByName(@Param("name") String name);

    Map<String, Object> selectSiteByOwnerId(@Param("id") Integer id);

    List<String> selectNearSiteOwnerId(@Param("longitude") Float longitude, @Param("latitude") Float latitude,
                                             @Param("radius") Double radius);

    Site selectByOwnerId(Integer id);
}