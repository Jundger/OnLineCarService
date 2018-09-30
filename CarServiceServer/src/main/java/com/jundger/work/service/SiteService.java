package com.jundger.work.service;

import com.jundger.work.pojo.Comment;
import com.jundger.work.pojo.FaultCode;
import com.jundger.work.pojo.Site;

import java.util.List;
import java.util.Map;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/4/18 21:35
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public interface SiteService {

	List<Map<String, Object>> getShowList(Float longitude, Float latitude, Double radius);

	List<String> getRepairmanId(Float longitude, Float latitude, Double radius);

	List<Map<String, Object>> getLocationList();

	Map<String, Object> getSiteByName(String name);

	// 根据维修店id拉取用户评论
	List<Map<String, Object>> getCommentBySite(Integer id);

	// 根据维修人员id拉取用户评论
	List<Map<String, Object>> getCommentByRepairman(Integer id);

}
