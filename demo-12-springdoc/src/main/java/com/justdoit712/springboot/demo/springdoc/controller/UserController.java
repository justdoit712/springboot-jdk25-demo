package com.justdoit712.springboot.demo.springdoc.controller;

import com.justdoit712.springboot.demo.springdoc.model.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 用户管理接口
 * 注意：OpenAPI 3 规范中，使用 @Tag 替代了原来的 @Api，使用 @Operation 替代了 @ApiOperation
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 11:52
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理模块", description = "提供用户的增删改查 RESTful 接口")
public class UserController {

    @GetMapping("/list")
    @Operation(summary = "获取用户列表", description = "查询系统中所有的用户数据，默认返回一条 mock 数据。")
    public List<UserDTO> getUserList() {
        UserDTO user = new UserDTO();
        user.setId(10001L);
        user.setName("张三");
        user.setAge(25);
        user.setEmail("zhangsan@example.com");
        return Collections.singletonList(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 获取用户详情")
    public UserDTO getUserById(
            @Parameter(description = "用户唯一标识 ID", required = true, example = "10001")
            @PathVariable("id") Long id) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setName("李四");
        return user;
    }

    @PostMapping
    @Operation(summary = "新增一个用户", description = "保存新用户，如果不传 ID 则系统会自动生成一个")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getId() == null) {
            userDTO.setId(System.currentTimeMillis());
        }
        return userDTO;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除一个用户")
    public String deleteUser(
            @Parameter(description = "待删除的用户 ID", required = true)
            @PathVariable("id") Long id) {
        return "用户 " + id + " 删除成功";
    }
}
