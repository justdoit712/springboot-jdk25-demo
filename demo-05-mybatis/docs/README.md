# 原生 MyBatis 模块使用说明

本模块 (`demo-05-mybatis`) 演示了如何在 Spring Boot 3 中整合官方原生的 MyBatis。

## 🚀 启动前必须修改的配置

如果您要在自己的本地环境中运行本模块，请务必检查并修改以下几处配置：

### 1. 数据库连接信息

请打开文件 [`src/main/resources/application.yml`](../src/main/resources/application.yml)，找到 `spring.datasource` 节点：

```yaml
spring:
  datasource:
    # 1. 检查数据库名称 (这里是 demo-jdk25)，以及连接端口 (默认是 3306)
    url: jdbc:mysql://localhost:3306/demo-jdk25?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    # 2. 检查并填入您的 MySQL 登录用户名
    username: root
    # 3. 检查并填入您的 MySQL 登录密码
    password: 123456
```

### 2. 数据库表与初始数据

由于本项目使用的是您本地真实的 MySQL 数据库，为了防止后续每次重启时不小心清空了您的数据，代码中默认**关闭**了自动建表和插数据的功能。

因此，如果您是**第一次**运行本项目，或者您的 `demo-jdk25` 数据库里还没有 `user` 表，请手动执行以下操作：

1. 打开您的 MySQL 客户端（如 Navicat、DBeaver 或 IDEA 自带的 Database 工具）。
2. 在您的 `demo-jdk25` 数据库中，运行 [`src/main/resources/db/schema.sql`](../src/main/resources/db/schema.sql) 文件中的建表语句，创建 `user` 表。
3. 运行 [`src/main/resources/db/data.sql`](../src/main/resources/db/data.sql) 文件，插入 5 条基础测试数据。

---

以上两步准备好之后，您就可以在 IDEA 中启动 `SpringBootDemoMybatisApplication`，愉快地进行接口测试了！
