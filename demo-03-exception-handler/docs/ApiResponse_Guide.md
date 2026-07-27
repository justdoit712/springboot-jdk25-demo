# 📖 Spring Boot 统一响应与全局异常处理 (初学者完整指南)

## 1. 痛点引入：为什么 API 需要统一返回格式？

在没有做统一封装之前，控制器（Controller）的返回结果五花八门：
- 查询用户列表返回：`List<User>`
- 删除用户成功返回：`String "success"`
- 系统出现异常返回：Spring 默认的黑白错误页，或者报 500 堆栈错误。

**前端开发者的噩梦**：
前端拿到的数据一会儿是数组，一会儿是字符串，报错时又变成一个 HTML 网页。前端逻辑里充斥着各种 `if-else` 判断，极易崩溃。

**解决方案**：
无论后端发生什么（成功、业务失败、未知报错），**都用统一的 JSON 容器装起来**返回给前端！

```json
{
  "code": 200,             // 状态码（告诉前端是成功还是失败）
  "message": "操作成功",    // 提示信息（可以直接展示给用户）
  "data": { ... }          // 真正的业务数据（成功时有，失败时为 null）
}
```

---

## 2. 演进过程：为什么把构造函数私有化？

### 阶段 1：普通构造函数（容易传错参数）
```java
// 初学者写法：频繁 new 对象
return new ApiResponse(200, "操作成功", userData);
return new ApiResponse(400, "业务逻辑异常", null); 
```
- **问题**：如果字段较多（如状态码、提示语、时间戳、追踪 ID），每次 `new` 都容易把参数位置传错。

### 阶段 2：静态工厂方法（语义化、清晰、直观）
```java
// 进阶写法：私有化构造函数，仅通过有名字的静态方法创建
return ApiResponse.ofSuccess(userData);             // 语义：成功并返回数据
return ApiResponse.ofStatus(Status.BUSINESS_ERROR); // 语义：根据状态码枚举返回错误
```
- **好处**：方法名字本身就说明了用途（`ofSuccess` / `ofMessage`），代码可读性成倍提升！

---

## 3. 全流程式图解：一次请求处理全过程

```text
========================================================================================
场景一：正常查询请求处理流程
========================================================================================

[前端 / 客户端]
      │
      │ 1. 发起请求 GET /demo/test/success
      ▼
[TestController]
      │
      │ 2. 构造对象: ApiResponse.ofSuccess("请求成功...")
      ▼
[ApiResponse 对象]
      │
      │ 3. 交给 Spring Boot 框架（调用 public getXxx() 方法）
      ▼
[Jackson 序列化器]
      │
      │ 4. 转为统一 JSON 字符串发回前端
      ▼
[前端 / 客户端]  ==> 收到: {"code": 200, "message": "操作成功", "data": "请求成功..."}


========================================================================================
场景二：抛出业务异常处理流程
========================================================================================

[前端 / 客户端]
      │
      │ 1. 发起请求 GET /demo/test/business-error
      ▼
[TestController]
      │
      │ 2. 抛出异常: throw new BaseException(BUSINESS_ERROR)
      ▼
[GlobalExceptionHandler]  (@RestControllerAdvice 捕获)
      │
      │ 3. 统一拦截并包装: ApiResponse.of(code, message, null)
      ▼
[ApiResponse 对象]
      │
      │ 4. 交给 Spring Boot 框架（调用 public getXxx() 方法）
      ▼
[Jackson 序列化器]
      │
      │ 5. 转为统一 JSON 字符串发回前端
      ▼
[前端 / 客户端]  ==> 收到: {"code": 400, "message": "业务逻辑异常", "data": null}
```

---

## 4. `ApiResponse.java` 5 个静态工厂方法的区别与适用场景

| 静态方法 | 适用场景 | 前端收到的 JSON 效果 |
| :--- | :--- | :--- |
| `ApiResponse.ofSuccess(data)` | 正常的**查询/获取**数据接口 | `{"code": 200, "message": "操作成功", "data": {...}}` |
| `ApiResponse.ofMessage(msg)` | 正常的**修改/删除/提交**成功提示 | `{"code": 200, "message": "用户删除成功", "data": null}` |
| `ApiResponse.ofStatus(status)` | 业务拦截或按标准枚举提示 | `{"code": 400, "message": "业务逻辑异常", "data": null}` |
| `ApiResponse.ofStatus(status, data)` | 按标准枚举提示，同时附带部分数据 | `{"code": 400, "message": "业务逻辑异常", "data": {...}}` |
| `ApiResponse.of(code, msg, data)` | 最底层的自由组合（常用于异常拦截时动态拼接错误消息） | `{"code": 500, "message": "系统发生错误：除零错误", "data": null}` |

---

## 5. 初学者常见踩坑 FAQ

### Q1：为什么 getters (`getCode()`, `getMessage()`, `getData()`) 必须写成 `public`？
- **答**：Spring Boot 默认使用 **Jackson** 把 Java 对象转成 JSON 字符串。Jackson 依靠反射调用对象的 `public getXxx()` 方法获取字段值。如果 getters 设为 `private`，转出来的 JSON 就会是空的 `{}`，或者报错！

### Q2：业务代码里直接 `throw new BaseException(...)` 会导致网页报 500 吗？
- **答**：不会！因为我们写了 [GlobalExceptionHandler.java](file:///d:/Work/study/springboot/demo-03-exception-handler/src/main/java/com/example/demo03/exception/handler/GlobalExceptionHandler.java)。`@RestControllerAdvice` 就像网子一样把所有抛出的异常罩住，转换成优雅的 JSON 格式返回给前端。
