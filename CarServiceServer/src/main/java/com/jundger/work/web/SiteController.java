package com.jundger.work.web;

import com.jundger.work.constant.Consts;
import com.jundger.work.service.SiteService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
	@RequestMapping(value = "/getList", produces = "application/json; charset=utf-8")
	public Object getSiteList(@RequestParam(value = "longitude", required = false, defaultValue = Consts.DEFAULT_LONGITUDE) Float longitude,
							  @RequestParam(value = "latitude", required = false, defaultValue = Consts.DEFAULT_LATITUDE) Float latitude,
							  @RequestParam(value = "radius", required = false, defaultValue = Consts.DEFAULT_RADIUS) Float radius) {

		logger.info("=================获取附近维修点接口调用==================");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			List<Map<String, Object>> list = siteService.getShowList(longitude, latitude, radius.doubleValue());
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
	@RequestMapping(value = "/getLocation", produces = "application/json; charset=utf-8")
	public Object getLocationList() {

		logger.info("=================获取维修点坐标接口调用==================");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			List<Map<String, Object>> list = siteService.getLocationList();
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
	@RequestMapping(value = "/query", produces = "application/json; charset=utf-8")
	public Object querySite(@RequestParam(value = "site_name") String name) {

		logger.info("=================查询维修点信息接口调用==================");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			Map<String, Object> site = siteService.getSiteByName(name);
			if (null != site && !site.isEmpty()) {
				returnMsg.put("code", "1");
				returnMsg.put("msg", "SUCCESS");
				returnMsg.put("data", site);
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
	@RequestMapping(value = "/getComment")
	public Map<String, Object> getCommentBySite(@RequestParam(value = "site_id", required = false) Integer site_id,
												@RequestParam(value = "repairman_id", required = false) Integer repairman_id) {

		logger.info("getCommentBySite request...");

		Map<String, Object> returnMsg = new HashMap<>();

		try {

			if (null == site_id && null == repairman_id) {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "NULL_PARAMETER");
				return returnMsg;
			}

			List<Map<String, Object>> comments;
			if (null != site_id ) {
				comments = siteService.getCommentBySite(site_id);
			} else {
				comments = siteService.getCommentByRepairman(repairman_id);
			}

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
