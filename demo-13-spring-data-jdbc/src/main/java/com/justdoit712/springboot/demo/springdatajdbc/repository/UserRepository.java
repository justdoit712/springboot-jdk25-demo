package com.justdoit712.springboot.demo.springdatajdbc.repository;

import com.justdoit712.springboot.demo.springdatajdbc.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 用户持久层接口
 * 继承 CrudRepository 即可直接获得基础的增删改查方法
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 14:09
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * 演示 1：通过方法名自动生成 SQL
     * Spring Data JDBC 会自动解析方法名生成类似: 
     * SELECT * FROM users WHERE name = ?
     *
     * @param name 用户姓名
     * @return 匹配的用户列表
     */
    List<User> findByName(String name);

    /**
     * 演示 2：对于极其复杂的场景，直接使用 @Query 手写原生 SQL
     * Spring Data JDBC 不会有任何修改，你写什么它就老老实实执行什么。
     *
     * @param status 状态
     * @param minAge 最小年龄
     * @return 匹配的用户列表
     */
    @Query("SELECT * FROM users WHERE status = :status AND age > :minAge")
    List<User> findByCustomComplexCondition(@Param("status") Integer status, @Param("minAge") Integer minAge);
}
