# 图书馆管理系统

基于微服务架构的综合性图书资源管理平台，Spring Boot 核心后端 + .NET 10 扩展服务 + RabbitMQ 消息中间件 + PostgreSQL 数据库 + Java Swing / Vue 3 双前端 + Docker 容器化部署。

## 技术栈

| 层次 | 技术 | 说明 |
|------|------|------|
| Java 后端 | Spring Boot 3.2.5 | 自动配置、内嵌 Tomcat、RESTful API |
| .NET 后端 | .NET 10 (ASP.NET Core) | 高性能 Web API、跨平台、原生 AOT |
| 消息中间件 | RabbitMQ 3.x | AMQP 协议、异步解耦、可靠消息投递 |
| 数据库 | PostgreSQL 16 | 企业级 RDBMS、JSONB 支持、全文检索 |
| 持久层 | Spring Data JPA | ORM 映射、Repository 抽象、JPQL 查询 |
| 桌面客户端 | Java Swing | 原生 GUI、跨平台兼容 |
| Web 前端 | Vue 3 + Vite | 组合式 API、响应式、快速构建 |
| 容器化 | Docker + Compose | 标准化镜像、一键编排部署 |
| Java 版本 | JDK 17+ | switch 表达式、pattern matching |

## 环境要求

- **JDK 17+**（必须）
- **Maven 3.8+**（必须）
- **Docker 24+**（容器化部署）
- **PostgreSQL 16**（Docker 内置或独立部署）

```bash
java -version
mvn -version
docker -v
```

## 项目结构

```
library-management-system/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── nginx.conf
├── README.md
├── DESIGN.md
└── src/
    ├── main/
    │   ├── java/com/library/
    │   │   ├── LibraryApplication.java
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── repository/
    │   │   ├── model/
    │   │   │   ├── entity/
    │   │   │   ├── dto/
    │   │   │   ├── factory/
    │   │   │   └── policy/
    │   │   ├── client/
    │   │   ├── config/
    │   │   ├── util/
    │   │   └── manager/
    │   └── resources/
    │       ├── application.yml
    │       └── data.sql
    └── test/java/com/library/
```

## 编译与运行

### 方式一：Docker Compose（推荐）

```bash
docker-compose up -d
```

启动完成后：
- Spring Boot: http://localhost:8080
- RabbitMQ 管理界面: http://localhost:15672
- PostgreSQL: localhost:5432
- Web 前端: http://localhost:80

### 方式二：本地开发

```bash
mvn clean compile -DskipTests
mvn spring-boot:run
```

启动 GUI 客户端：
```bash
mvn exec:java -Dexec.mainClass="com.library.client.MainFrame"
```

## REST API 接口

### Spring Boot 核心 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/resources | 获取所有资源列表 |
| GET | /api/resources/{id} | 按 ID 查询资源 |
| GET | /api/resources/type/{type} | 按类型查询资源 |
| GET | /api/resources/search?keyword= | 关键词搜索资源 |
| POST | /api/resources | 添加新资源 |
| DELETE | /api/resources/{id} | 删除资源 |
| POST | /api/borrow/borrow | 借阅资源 |
| POST | /api/borrow/return | 归还资源 |
| GET | /api/borrow/overdue | 查询逾期记录 |

### .NET 10 扩展 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/statistics/trends | 借阅趋势统计 |
| GET | /api/statistics/hot-resources | 热门资源排名 |
| POST | /api/reports/generate | 生成借阅报表 |
| GET | /api/notifications/{userId} | 查询用户通知 |

### API 使用示例

```bash
curl http://localhost:8080/api/resources
curl http://localhost:8080/api/resources/type/BOOK
curl "http://localhost:8080/api/resources/search?keyword=Java"
curl -X POST "http://localhost:8080/api/borrow/borrow?borrowerId=B001&resourceId=R001"
curl -X POST "http://localhost:8080/api/borrow/return?resourceId=R001"
```

## 初始化数据

**资源数据：**

| 编号 | 标题 | 类型 | 借阅期限 |
|------|------|------|---------|
| R001 | Java编程思想 | BOOK | 30天 |
| R002 | 设计模式 | BOOK | 30天 |
| R003 | Spring实战 | BOOK | 30天 |
| R004 | 自然杂志 | MAGAZINE | 14天 |
| R005 | 读者 | MAGAZINE | 14天 |
| R006 | 星际穿越 | DVD | 7天 |
| R007 | 地球脉动 | DVD | 7天 |
| R008 | 算法导论 | EBOOK | 21天 |

**借阅者数据：**

| 编号 | 姓名 | 类型 |
|------|------|------|
| B001 | 张三 | STUDENT |
| B002 | 李四 | TEACHER |
| B003 | 王五 | PUBLIC |

## 运行测试

```bash
mvn test
mvn test -Dtest=IntegrationTest
mvn test -Dtest=PolicyAndFactoryTest
```

## 设计原则

详见 [DESIGN.md](DESIGN.md)

- **开闭原则(OCP)**：抽象基类 + 工厂模式 + 策略模式
- **单一职责原则(SRP)**：每个类只负责一个职责
- **里氏替换原则(LSP)**：子类可完全替代基类
- **接口隔离原则(ISP)**：BorrowPolicy 细粒度设计
- **依赖倒置原则(DIP)**：Service 依赖 Repository 接口