# 六周讲义

每周两课，每课约 90 分钟。每次先读最小例子，运行并观察结果，再改变一个条件验证猜想。练习项目提供完整参考源码，第一次做时只看当前课涉及的文件。

## 第 1 周｜让 Spring Boot 听见你的请求

**第 1 课 · 写 `/hello`。** 在 IDEA 打开本目录的 `pom.xml`，按 `LearningApplication → HelloController → HelloService` 的顺序读代码。启动之后访问 `/hello?name=Java`。想象你是框架：先匹配 `GET /hello`，再把查询参数 `name` 交给方法，最后把返回的 `Greeting` 写成 JSON。

要亲手从零写时，在临时练习目录复写这三个类。先让 Service 返回固定问候词，随后再加 `@Value` 读取配置。最小 POM 只需保留本项目的父 POM、Java 版本、Web MVC Starter 和 Spring Boot Maven 插件。跑通后回到本项目继续下一课。

**第 2 课 · 谁创建了 Service？** 修改练习项目的 `application.yml`，把端口从 `8081` 临时改到 `8090`，重启并访问新端口。随后临时移除 `HelloService` 上的 `@Service`，观察日志会指出 Controller 的构造器缺少哪个依赖，再恢复。对照若依的 [RuoYiApplication](../../RuoYi-Vue/ruoyi-admin/src/main/java/com/ruoyi/RuoYiApplication.java) 和 [岗位 Controller](../../RuoYi-Vue/ruoyi-admin/src/main/java/com/ruoyi/web/controller/system/SysPostController.java)，找出启动入口和 Service 注入的位置。

**验收**：能从 POM 指出 Web Starter，能用 `mvn.cmd -version` 确认 Maven 用的 Java，能解释没有 Spring 管理的 Service 时为什么会在启动阶段报错。

## 第 2 周｜读岗位接口，再打包

**第 3 课 · 一个 URL 的三种参数。** 读 `PostController` 的岗位列表、详情和新增。实际发请求前先按第 3 周准备本地 MySQL。分别找出 URL 上的查询参数、路径中的 ID、请求体中的 JSON 是从哪里进方法的。比较成功响应、校验失败和查不到数据时的 HTTP 状态。

```powershell
curl.exe -i "http://127.0.0.1:8081/posts?name=Java&pageSize=1"
curl.exe -i "http://127.0.0.1:8081/posts/2"
Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:8081/posts" -ContentType "application/json" -Body (@{name="Support Engineer"} | ConvertTo-Json)
curl.exe -i -H "Content-Type: application/json" -d "{}" "http://127.0.0.1:8081/posts"
```

预期分别是 `200`、`200`、`201`、`400`。再调用 `/posts/999`，确认 `404`。找出 `@Valid`、`@NotBlank` 和 `ApiExceptionHandler` 各在什么时机起作用。对照若依的 [GlobalExceptionHandler](../../RuoYi-Vue/ruoyi-framework/src/main/java/com/ruoyi/framework/web/exception/GlobalExceptionHandler.java)，分别观察 HTTP 状态和响应体里的业务 `code`。

**第 4 课 · 同一个 JAR 怎么换端口？** 在课程目录执行 `mvn.cmd package`。找出 `target` 里的 JAR，再依次执行这三条命令。每次访问日志里显示的端口，确认响应后按 Ctrl+C 停止当前进程。

```powershell
java -jar target/spring-boot-course-1.0.0-SNAPSHOT.jar
java -jar target/spring-boot-course-1.0.0-SNAPSHOT.jar --spring.profiles.active=mysql,local
java -jar target/spring-boot-course-1.0.0-SNAPSHOT.jar --spring.profiles.active=mysql,local --server.port=8090
```

预期端口依次为 `8081`、`8082`、`8090`，问候语依次取基础配置、`Hi`、`Hi`。比较 `@Value` 读取单项配置和若依的 [RuoYiConfig](../../RuoYi-Vue/ruoyi-common/src/main/java/com/ruoyi/common/config/RuoYiConfig.java) 对一组配置做绑定。

配置练习可以再做一步。在临时练习中增加 `learn.punctuation: "!"`，建一个带两个字段的 `@ConfigurationProperties("learn")` 配置类，用 `@EnableConfigurationProperties` 注册，再让 `HelloService` 通过构造器取得配置对象。把 `local` 的标点改成 `?`，重启后检查 JSON 是否变成 `{"message":"Hi, Java?"}`。

**验收**：能解释为什么第三次只有端口改变，以及 `package` 之前会执行哪些阶段。

## 第 3 周｜让岗位数据进入 MySQL

**第 5 课 · 从 Java 方法找到 SQL。** 按 `PostController → PostService → PostMapper → PostMapper.xml` 阅读。Mapper 接口声明想做什么，XML 提供 SQL，MyBatis 负责绑定参数和映射结果。用 `mvn.cmd dependency:tree` 找到 MyBatis Starter、连接池和 MySQL 驱动，看看哪些是直接声明的。

准备专用空库 `spring_boot_course`，用本地有授权的练习账号一次性执行 [schema.sql](../sql/schema.sql)。从课程目录启动 MySQL 客户端，再执行下列命令。课程库和若依库分别使用各自的 SQL。

```sql
USE spring_boot_course;
SOURCE sql/schema.sql;
```

在当前 PowerShell 会话提供 `COURSE_DB_USER`、`COURSE_DB_PASSWORD`，再启动：

```powershell
$dbCredential = Get-Credential -Message "输入本地课程数据库账号"
$env:COURSE_DB_USER = $dbCredential.UserName
$env:COURSE_DB_PASSWORD = $dbCredential.GetNetworkCredential().Password
java -jar target/spring-boot-course-1.0.0-SNAPSHOT.jar --spring.profiles.active=mysql
```

**第 6 课 · 加筛选，再翻页。** 调用列表接口时先不传 `name`，再传 `name=Java`。把 `pageSize` 设为 1，分别请求第 1、2、3 页。按种子数据，三页应分别返回 `Java Developer`、`Java Lead`、空列表，且三个响应的 `total` 都是 2。再新增一个岗位并重启，验证 MySQL 中的数据仍在。对照若依的 [岗位 XML](../../RuoYi-Vue/ruoyi-system/src/main/resources/mapper/system/SysPostMapper.xml) 和 [DruidConfig](../../RuoYi-Vue/ruoyi-framework/src/main/java/com/ruoyi/framework/config/DruidConfig.java)，看若依如何读取主库配置、拼筛选条件，以及它把分页放在何处。

**验收**：能说明列表 SQL 和计数 SQL 为什么要用同一个筛选条件。暂时没有 MySQL 时可跑 `mvn.cmd "-Dtest=DatabaseHttpTests" test` 验证同一套 Mapper。

## 第 4 周｜构建若依，在页面上发出真实请求

**第 7 课 · 用 Maven 读懂模块关系。** 在相邻的 `RuoYi-Vue` 根目录执行：

```powershell
mvn.cmd -pl ruoyi-admin help:effective-pom
mvn.cmd -pl ruoyi-admin -am package dependency:tree
```

在 [若依父 POM](../../RuoYi-Vue/pom.xml) 找 `<modules>` 和 Spring Boot BOM，再在 `ruoyi-admin` 的依赖树中找 `ruoyi-framework → spring-boot-starter-webmvc`。父 POM 给依赖管理版本，依赖树告诉你库从哪条路径进来。`-pl` 选模块，`-am` 把它依赖的仓库内模块一起构建。若遇到离线缓存里找不到构建插件依赖，先用 Maven 输出里的坐标确认缺项，再在可访问镜像的环境重新解析。构建成功后的入口是 `ruoyi-admin/target/ruoyi-admin.jar`。

**第 8 课 · 给岗位列表打一组断点。** 准备一套独立的若依本地 MySQL 库和 Redis 实例。若依初始化脚本使用本地空库，按 `sql/ry_20261117.sql`、`sql/quartz.sql` 的顺序执行。启动后端前，核对激活的 `druid` Profile、本地地址和连接配置。可以用 IDEA 运行 `RuoYiApplication`，通过运行配置的环境变量提供实际值。下列是待核对的变量名：

- `SPRING_DATASOURCE_DRUID_MASTER_URL`、`SPRING_DATASOURCE_DRUID_MASTER_USERNAME`、`SPRING_DATASOURCE_DRUID_MASTER_PASSWORD`
- `SPRING_DATA_REDIS_HOST`、`SPRING_DATA_REDIS_PORT`、`SPRING_DATA_REDIS_PASSWORD`

先访问 `http://localhost:8080/captchaImage` 确认后端响应。随后在相邻的 `RuoYi-Vue2` 目录运行：

```powershell
$env:port = '8083'
npm.cmd run dev
```

用自己的本地若依账号登录。在浏览器打开开发者工具的 Network，进入「系统管理 → 岗位管理」，筛选岗位名称并查看 `/dev-api/system/post/list`。在 `SysPostController.list` 和 `SysPostServiceImpl.selectPostList` 下断点，再看 [前端页面](../../RuoYi-Vue2/src/views/system/post/index.vue) 怎样读取 `rows` 与 `total`。开发代理会去掉 `/dev-api`，把请求转发到本机后端 `8080`。

**验收**：记下浏览器请求 URL、Controller 收到的对象和列表响应，三处的筛选条件与结果可以互相对照。

## 第 5 周｜分清登录与岗位权限

**第 9 课 · 跟一次登录。** 从若依 `SysLoginController.login` 到 `SysLoginService` 和 [TokenService](../../RuoYi-Vue/ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/TokenService.java)。只记录调用顺序，观察响应中的 Token 与 Redis 中的登录用户缓存分别在何时创建。后续请求先经过 Token 过滤器，过滤器读取用户并恢复当前请求的登录状态。

**第 10 课 · 比较三种请求。** 用匿名会话和两种本地学习账号访问岗位列表。一个角色包含 `system:post:list`，另一个角色没有此权限。可以用普通本地账号和无痕窗口对照，也可以把浏览器请求复制到调试工具里，在本机测试并保护 Token。

| 测试 | 在哪里观察 |
|---|---|
| 未登录 | `SecurityConfig` 和认证入口，观察请求是否在到达 Controller 前结束。 |
| 已登录且缺岗位权限 | `JwtAuthenticationTokenFilter` 和岗位方法上的 `@PreAuthorize`，分别确认认证通过与授权结果。 |
| 已登录且有权限 | Controller 断点和列表响应，确认拿到期望的岗位数据。 |

**验收**：在本地记录三次实际 HTTP 状态和响应体里的业务状态，能说清是登录状态还是权限决定了结果。

## 第 6 周｜课程管理与事务回滚

**第 11 课 · 写一项涉及两张表的操作。** 先读若依 [SysUserServiceImpl.insertUser](../../RuoYi-Vue/ruoyi-system/src/main/java/com/ruoyi/system/service/impl/SysUserServiceImpl.java)，圈出保存用户、岗位关联和角色关联。再写课程新增：先保存课程，再逐条保存标签，两步放进同一个 `@Transactional` 方法。然后补详情、列表和编辑。练习项目的 UUID 由 Service 生成，分页仍然从 1 开始。

| 请求 | 行为 |
|---|---|
| `GET /courses?name=Boot&pageNum=1&pageSize=10` | 按名称筛选，返回 `rows`、`total`。 |
| `GET /courses/{id}` | 返回课程及标签，未知 ID 返回 `404`。 |
| `POST /courses` | 接收 `{"name":"Spring Boot","labels":["java"]}`，成功返回 `201`。 |
| `PUT /courses/{id}` | 保存新课程名并替换标签，未知 ID 返回 `404`。 |

**第 12 课 · 故意让第二次写入失败。** 新增时提交 `{"name":"Rollback","labels":["dup","dup"]}`。课程和第一个标签先写入，第二次插入标签触发数据库唯一约束，接口返回 `409`。接着按课程名查库，确认课程不存在。编辑已有课程时做同样的实验，确认旧名称和旧标签都保住了。最后执行 `mvn.cmd test`、`mvn.cmd package`，用 MySQL Profile 启动 JAR 并重做一次成功请求。

MySQL 模式启动后，可以在另一个 PowerShell 终端发送成功请求，保存返回的 ID。随后用同一个 ID 发一次有重复标签的编辑，再查询详情。

```powershell
$coursesUrl = 'http://127.0.0.1:8081/courses'
$created = Invoke-RestMethod -Method Post -Uri $coursesUrl -ContentType 'application/json' -Body '{"name":"Spring Boot","labels":["java"]}'
$detailUrl = "$coursesUrl/$($created.id)"
Invoke-WebRequest -Method Put -Uri $detailUrl -ContentType 'application/json' -Body '{"name":"Broken edit","labels":["dup","dup"]}' -SkipHttpErrorCheck | Select-Object StatusCode
Invoke-RestMethod -Uri $detailUrl
```

预期 `PUT` 返回 `409`，详情仍返回 `Spring Boot` 和 `java`。再在 MySQL 客户端验证新增失败的那条记录：

```sql
SELECT COUNT(*) FROM learn_course WHERE name = 'Rollback';
```

预期结果是 `0`。

**验收**：能讲清数据库异常为什么会触发回滚，能用日志、SQL 或断点区分一次失败发生在参数校验、数据库写入还是事务提交阶段。
