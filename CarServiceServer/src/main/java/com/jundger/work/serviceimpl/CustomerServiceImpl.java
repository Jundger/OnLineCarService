package com.jundger.work.serviceimpl;

import com.jundger.work.dao.CollectMapper;
import com.jundger.work.dao.CommentMapper;
import com.jundger.work.dao.CustomerMapper;
import com.jundger.work.pojo.Collect;
import com.jundger.work.pojo.Customer;
import com.jundger.work.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static com.jundger.common.util.JwtUtil.createJWT;
import static com.jundger.common.util.JwtUtil.generalSubject;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerMapper customerDao;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private CollectMapper collectMapper;

    // 通过ID查询用户信息
    public Customer getById(int userId) {
        return this.customerDao.selectByPrimaryKey(userId);
    }

    // 通过ID更新用户信息
    public int updateByPrimaryKeySelective(Customer user) {
        return this.customerDao.updateByPrimaryKeySelective(user);
    }

    // 通过用户名查询用户信息
    public Customer getByphoneNumber(String phoneNumber) {
        return this.customerDao.selectByPhoneNumber(phoneNumber);
    }

    @Override
    public Customer getByEmail(String email) {
        return customerDao.selectByEmail(email);
    }

    // 通过用户名和密码验证登录信息
    public Customer validateLogin(String phoneNumber, String password) {
        return this.customerDao.selectByPhonePsw(phoneNumber, password);
    }

    // 生成Token验证字符串
    public String generalToken(Customer customer, long ttlMillis) {

        String id = String.valueOf(customer.getCustId());
        String subject = generalSubject(customer);

        String token = null;
        try {
            token = createJWT(id, subject, ttlMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public int addCustomer(Customer customer) {
        return this.customerDao.insertSelective(customer);
    }

    @Override
    public List<Map<String, Object>> getCommentByCust(String id) {
        return commentMapper.selectShowCommentByCust(Integer.parseInt(id));
    }

    @Override
    public int addCollect(Collect record) {
        return collectMapper.insertSelective(record);
    }

    @Override
    public List<Collect> getAllCollect(Integer cust_id) {
        return collectMapper.selectByCustId(cust_id);
    }
}
