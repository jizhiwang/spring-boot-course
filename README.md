# 用若依学 Spring Boot

这里是六周课程的可运行练习项目。你会先写一个返回 JSON 的接口，再通过 MyBatis 查询 MySQL 岗位数据，最后用课程管理练一次事务回滚。每学到一个机制，就到旁边的若依前后端找它的实际用法。

[学习计划与当前进度](docs/study-plan.md) · [六周讲义](docs/lessons.md) · [项目验证进度](ROADMAP.md)

## 先把第一课跑起来

在本目录打开 PowerShell。启动前，按[数据库准备步骤](docs/lessons.md#第-3-周让岗位数据进入-mysql)初始化课程库，并设置 COURSE_DB_USER 和 COURSE_DB_PASSWORD。项目使用 Java 17、Spring Boot 4.1.2 和 MyBatis Starter 4.1.0，与当前若依后端的版本声明一致。

```powershell
java -version
mvn.cmd -version
mvn.cmd spring-boot:run
```

在另一个终端访问接口：

```powershell
curl.exe -i "http://127.0.0.1:8081/hello?name=Java"
```

预期状态为 HTTP 200，问候语和标点以 YAML 中生效的配置为准。岗位数据默认从 MySQL 读取。需要切换端口、使用 MySQL 或查看若依的源码时，按讲义的周次往下做。

如果 `mvn.cmd -version` 显示的 Java 版本不是 17，在当前终端设置 `$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'`，并把路径替换为自己的 JDK 目录，再检查一次。下文的 `java -jar` 也可以用 `& "$env:JAVA_HOME\bin\java.exe" -jar` 明确指定同一个 JDK。

## 自测

```powershell
mvn.cmd test
mvn.cmd package
java -jar target/spring-boot-course-1.0.0-SNAPSHOT.jar --spring.profiles.active=mysql,local
```

`local` Profile 使用端口 `8082` 和问候语 `Hi`。测试通过 Spring MVC 模拟请求，使用一次性 H2 内存库验证 MyBatis 和事务，无需启动 MySQL 或 Redis。应用默认使用 `mysql` Profile，准备步骤见第 3 周。
