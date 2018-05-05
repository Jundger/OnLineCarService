package com.jundger.work.serviceimpl;

import com.jundger.work.constant.Consts;
import com.jundger.work.dao.CommentMapper;
import com.jundger.work.dao.SiteMapper;
import com.jundger.work.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
@Service("siteService")
public class SiteServiceImpl implements SiteService {

	@Resource
	private SiteMapper siteMapper;

	@Autowired
	private CommentMapper commentMapper;

	@Override
	public List<Map<String, Object>> getShowList(Float longitude, Float latitude, Double radius, Integer count) {
		return siteMapper.selectNearList(longitude, latitude, radius, count);
	}

	@Override
	public List<Map<String, Object>> getCommentBySite(String site_id) {
		return commentMapper.selectShowCommentBySite(Integer.parseInt(site_id));
	}

	@Override
	public List<Map<String, Object>> getLocationList() {
		return siteMapper.selectLocation();
	}

	@Override
	public Map<String, Object> getSiteByName(String name) {
		return siteMapper.selectSiteByName(name);
	}
}
