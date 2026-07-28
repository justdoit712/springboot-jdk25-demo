package com.example.demo05.mybatis.mapper;

import com.example.demo05.mybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 原生 MyBatis Mapper 接口
 * 演示注解方式与 XML 方式
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:18
 */
@Mapper
public interface UserMapper {

    /**
     * 方式一：使用注解直接在接口里写 SQL，适合非常简单的查询
     */
    @Select("SELECT * FROM user")
    List<User> findAllAnnotations();

    /**
     * 方式二：使用 XML 映射文件写复杂的动态 SQL (见 UserMapper.xml)
     */
    User findByIdXml(@Param("id") Long id);
}
