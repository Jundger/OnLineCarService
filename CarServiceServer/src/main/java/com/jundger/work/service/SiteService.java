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

	List<Map<String, Object>> getShowList();

	List<Comment> getCommentBySite(String site_id);
}
