# 项目规范

- 本目录是单模块 Spring Boot 课程。相邻的若依前后端和 `ruoyi-learn` 用于源码对照。
- 本地练习使用 Java 17 与 Maven，应用启动使用 MySQL，自动化测试使用独立 H2 数据库。MySQL 练习只使用单独的本地练习库。
- MyBatis Mapper 接口声明方法和参数，SQL 放在同名的 XML Mapper 中。
- 每次修改后执行相关 Maven 验证，并同步 `ROADMAP.md`。
