package com.jundger.work.service;

import com.jundger.work.pojo.Article;

import java.util.List;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/4/18 21:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public interface ArticleService {

	// 获取数据库中所有文章
	List<Article> getAll();
}
