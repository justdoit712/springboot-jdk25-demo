package com.justdoit712.springboot.demo.smartdoc.controller;

import com.justdoit712.springboot.demo.smartdoc.model.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 用户管理接口
 *
 * @author justdoit712
 * @date Created in 2026-07-31 11:47
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 获取用户列表
     * 
     * 这里写的一段文字就是这个接口的详细描述。
     * 它可以是非常详细的业务说明，只要是合法的 Javadoc 就会被 smart-doc 解析出来。
     *
     * @return 用户列表数据
     */
    @GetMapping("/list")
    public List<UserDTO> getUserList() {
        UserDTO user = new UserDTO();
        user.setId(10001L);
        user.setName("张三");
        user.setAge(25);
        user.setEmail("zhangsan@example.com");
        return Collections.singletonList(user);
    }

    /**
     * 根据 ID 获取用户详情
     *
     * @param id 用户唯一标识 ID
     * @return 用户信息详情
     */
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setName("李四");
        return user;
    }

    /**
     * 新增一个用户
     *
     * @param userDTO 用户实体对象
     * @return 保存成功后的用户信息
     */
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        // 模拟保存逻辑
        if (userDTO.getId() == null) {
            userDTO.setId(System.currentTimeMillis());
        }
        return userDTO;
    }

    /**
     * 删除一个用户
     *
     * @param id 用户唯一标识 ID
     * @return 返回成功信息
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        return "用户 " + id + " 删除成功";
    }
}
