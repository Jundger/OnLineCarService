package com.jundger.work.serviceimpl;

import com.jundger.work.dao.SiteMapper;
import com.jundger.work.pojo.Site;
import com.jundger.work.service.SiteService;
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

	@Override
	public List<Map<String, Object>> getShowList() {
		return siteMapper.selectShowList();
	}
}
