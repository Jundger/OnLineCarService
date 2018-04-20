package com.jundger.work.web;

import com.jundger.common.util.ToMap;
import com.jundger.work.pojo.Site;
import com.jundger.work.service.SiteService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping(value = "site")
public class SiteController {

	@Resource
	private SiteService siteService;

	private static Logger logger = Logger.getLogger(SiteController.class);

	@ResponseBody
	@RequestMapping(value = "getlist", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public Map<String, Object> getSiteList(HttpServletRequest request, Model model) {

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		List<Map<String, Object>> list = siteService.getShowList();
		returnMsg.put("code", "1");
		returnMsg.put("msg", "SUCCESS");
		returnMsg.put("data", list);

		return returnMsg;
	}
}
