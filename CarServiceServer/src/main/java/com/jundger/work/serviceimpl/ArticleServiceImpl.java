package com.jundger.work.serviceimpl;

import com.jundger.work.dao.ArticleMapper;
import com.jundger.work.pojo.Article;
import com.jundger.work.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/4/18 21:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Resource
	private ArticleMapper articleMapper;

	@Override
	public List<Article> getAll() {
		return articleMapper.getALl();
	}
}
