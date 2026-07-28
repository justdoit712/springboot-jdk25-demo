package com.example.demo06.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo06.mybatisplus.entity.User;

/**
 * <p>
 * 用户 Mapper 接口
 * 继承 BaseMapper 就自动拥有了 CRUD 能力，不用写任何 XML 语句
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:12
 */
public interface UserMapper extends BaseMapper<User> {
}
