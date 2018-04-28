package com.jundger.work.web;

import com.jundger.common.util.ToMap;
import com.jundger.work.pojo.Comment;
import com.jundger.work.pojo.Site;
import com.jundger.work.service.SiteService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/4/18 21:33
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/site")
public class SiteController {

	@Resource
	private SiteService siteService;

	private static Logger logger = Logger.getLogger(SiteController.class);

	@ResponseBody
	@RequestMapping(value = "/getlist", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public Map<String, Object> getSiteList() {

		logger.info("getSiteList request...");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			List<Map<String, Object>> list = siteService.getShowList();
			if (null != list && !list.isEmpty()) {
				returnMsg.put("code", "1");
				returnMsg.put("msg", "SUCCESS");
				returnMsg.put("data", list);
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "EMPTY");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "FAIL");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "/getcomment")
	public Map<String, Object> getCommentBySite(@RequestParam(value = "site_id") String site_id) {

		logger.info("getCommentBySite request...");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			List<Comment> comments = siteService.getCommentBySite(site_id);
			if (null != comments && !comments.isEmpty()) {
				returnMsg.put("code", "1");
				returnMsg.put("msg", "SUCCESS");
				returnMsg.put("data", comments);
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "EMPTY");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "FAIL");
		}

		return returnMsg;
	}
}
