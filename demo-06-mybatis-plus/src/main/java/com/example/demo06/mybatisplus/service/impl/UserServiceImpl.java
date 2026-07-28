package com.example.demo06.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo06.mybatisplus.entity.User;
import com.example.demo06.mybatisplus.mapper.UserMapper;
import com.example.demo06.mybatisplus.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户服务实现类
 * 继承 ServiceImpl 可以大幅简化 Service 层的编写
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
