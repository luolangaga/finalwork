# 图书馆管理系统 - 项目文档

## 1 系统概述

图书馆管理系统是一个基于微服务架构设计的综合性图书资源管理平台，以 Spring Boot 为核心后端，搭配 .NET 10 扩展服务，通过 RabbitMQ 消息中间件实现服务间异步通信与解耦。系统提供 Java Swing 桌面客户端与 Vue 3 Web 前端两种访问方式，满足不同场景下的用户需求。数据持久化采用 PostgreSQL 企业级关系数据库，后端服务通过 Docker 容器化部署，桌面客户端通过 Inno Setup 构建安装包，实现了从开发到部署的全流程现代化。

系统的核心设计理念是"分层解耦、模式驱动"。Spring Boot 作为主后端负责资源管理、借阅流程等核心业务，.NET 10 作为辅助服务负责数据分析与通知等扩展功能，两者通过 RabbitMQ 进行消息传递，实现了技术栈的灵活组合与独立演进。在数据层，PostgreSQL 凭借其卓越的 SQL 标准兼容性、丰富的数据类型支持与强大的扩展能力，为系统提供了坚实可靠的数据存储基础。

## 2 技术栈总览

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
| 客户端打包 | Inno Setup | Windows 安装包、内嵌 JRE |
| Java 版本 | JDK 17+ | switch 表达式、pattern matching |

## 3 系统架构

### 3.1 整体架构

系统采用前后端分离的微服务架构，整体分为五个核心层次：前端展示层、API 网关层、业务服务层、消息中间件层和数据持久层。前端展示层包含 Java Swing 桌面客户端与 Vue 3 Web 前端，两者均通过 HTTP/REST 协议与后端 API 交互。业务服务层由 Spring Boot 与 .NET 10 两个微服务组成，Spring Boot 作为核心业务引擎负责资源管理、借阅流程与借阅者管理，.NET 10 作为扩展业务引擎负责数据统计分析、报表生成与逾期通知。两个服务之间通过 RabbitMQ 消息中间件进行异步通信，实现了服务间的松耦合与独立部署能力。

数据持久层采用 PostgreSQL 作为统一数据库，Spring Boot 通过 Spring Data JPA 访问 PostgreSQL，.NET 10 通过 Entity Framework Core 访问同一 PostgreSQL 实例的不同 Schema，实现了数据共享与逻辑隔离的平衡。Docker Compose 编排方案确保了开发、测试与生产环境的一致性，大幅降低了部署复杂度与运维成本。

### 3.2 架构层次说明

| 层次 | 组件 | 职责 |
|------|------|------|
| 前端展示层 | Swing / Vue 3 | 用户交互、数据展示、表单提交 |
| API 网关层 | Nginx | 请求路由、认证鉴权、限流熔断 |
| 核心业务层 | Spring Boot | 资源管理、借阅流程、借阅者管理 |
| 扩展业务层 | .NET 10 | 数据分析、报表、通知、监控 |
| 消息中间件 | RabbitMQ | 异步通信、事件驱动、服务解耦 |
| 数据持久层 | PostgreSQL | 企业级数据存储、JSONB、全文检索 |

## 4 Spring Boot 后端

### 4.1 分层架构

Spring Boot 后端采用经典的三层架构设计，严格遵循职责分离原则。Controller 层负责接收 HTTP 请求、参数校验与响应封装；Service 层负责核心业务逻辑处理，包括资源管理、借阅流程控制与借阅者管理；Repository 层基于 Spring Data JPA 实现数据访问，通过接口定义即可获得 CRUD 操作与自定义查询能力，无需编写实现代码。

在对象模型方面，系统采用 Entity-DTO 分离策略。JPA 实体类（Entity）映射数据库表结构，包含持久化逻辑；数据传输对象（DTO）负责前后端数据交换，避免暴露内部数据结构。Controller 接收 DTO 对象，Service 层在 DTO 与 Entity 之间转换，Repository 操作 Entity 对象，三层之间通过不同的对象类型实现了清晰的边界隔离。

### 4.2 项目结构

| 包 | 说明 |
|----|------|
| controller/ | REST 控制器：ResourceController、BorrowController、BorrowerController |
| service/ | 业务服务层：ResourceService、BorrowService、BorrowerService |
| repository/ | 数据访问层：ResourceRepository、BorrowerRepository、BorrowRecordRepository |
| model/entity/ | JPA 实体类：LibraryResource（抽象基类）、Book、Magazine、DVD、EBook、Borrower、BorrowRecord |
| model/dto/ | 数据传输对象：ResourceDTO、BorrowRequest |
| model/factory/ | 工厂模式：ResourceFactory 统一创建资源对象 |
| model/policy/ | 策略模式：BorrowPolicy 接口及 Student/Teacher/Public 三种实现 |
| client/ | Java Swing GUI 客户端：MainFrame、BorrowPanel、ReturnPanel、QueryPanel |
| config/ | 配置类：WebConfig（CORS）、RabbitMQConfig |
| util/ | 工具类：HttpClientUtil、CollectionUtils、ValidationUtil、DateUtil |
| manager/ | 工具类：ResourceManager |

### 4.3 核心设计模式

Spring Boot 后端在设计中运用了多种经典设计模式，以提升代码的可维护性与可扩展性：

- **工厂模式（ResourceFactory）**：统一创建 Book、Magazine、DVD、EBook 四种资源对象，封装实例化逻辑。调用方通过 ResourceFactory.create(dto) 即可创建对应类型的资源实例，无需关心具体的构造过程。新增资源类型只需添加子类并修改工厂方法，符合开闭原则。

- **策略模式（BorrowPolicy）**：定义借阅规则接口 BorrowPolicy，由 StudentBorrowPolicy、TeacherBorrowPolicy、PublicBorrowPolicy 三种实现分别对应学生（最多借 5 本 / 30 天）、教师（最多借 10 本 / 60 天）与公众（最多借 3 本 / 14 天）的差异化借阅规则。BorrowService 通过注入不同的 BorrowPolicy 实例即可切换借阅策略，无需修改业务逻辑。

- **模板方法模式**：LibraryResource 抽象基类定义了资源管理的公共模板方法，子类通过重写特定钩子方法实现差异化行为，如不同资源类型的借阅期限计算逻辑。borrow() 和 returnResource() 方法使用 final 关键字确保模板逻辑不被覆盖。

- **依赖注入（DI）**：Service 层依赖 Repository 接口而非具体实现，通过 Spring IoC 容器自动注入。在测试中可轻松替换为 Mock 实现，实现了业务逻辑与数据访问的彻底解耦。

## 5 .NET 10 后端服务

.NET 10 作为系统的辅助后端服务，承担数据分析、报表生成、逾期通知与系统监控等扩展功能。选择 .NET 10 的核心优势在于：卓越的运行时性能（ASP.NET Core 在 TechEmpower 基准测试中吞吐量位居前列）、原生 AOT 编译支持（毫秒级启动，适合容器化部署）、跨平台原生支持（Windows/Linux/macOS）、成熟的异步编程模型（async/await）以及与 Java 生态的互补性。

.NET 服务采用 Minimal API 风格开发，配合 Entity Framework Core 访问 PostgreSQL 数据库，通过 RabbitMQ.Client 订阅 Spring Boot 发布的借阅事件消息，异步执行统计更新与通知推送逻辑。两个服务共享同一 PostgreSQL 实例，Spring Boot 使用 public schema，.NET 使用 analytics schema，实现数据共享与逻辑隔离。

## 6 消息中间件 RabbitMQ

### 6.1 角色与价值

RabbitMQ 作为系统的消息中间件，承担着 Spring Boot 与 .NET 10 两个后端服务之间异步通信的核心职责。在微服务架构下，服务间的同步 HTTP 调用会引入紧耦合、级联失败与性能瓶颈等问题。RabbitMQ 通过引入消息队列机制，实现了服务间的异步解耦，使得各服务可以独立开发、独立部署、独立扩展。

### 6.2 消息流转设计

| Exchange | Queue | 生产者 | 消费者 | 消息类型 |
|----------|-------|--------|--------|----------|
| library.event | borrow.created | Spring Boot | .NET 10 | 借阅创建事件 |
| library.event | borrow.returned | Spring Boot | .NET 10 | 归还完成事件 |
| library.event | resource.changed | Spring Boot | .NET 10 | 资源变更事件 |
| library.event | notification.send | .NET 10 | Spring Boot | 通知发送命令 |

### 6.3 可靠性保障

- **消息持久化**：Exchange 和 Queue 均声明为 durable，消息投递时设置 delivery_mode=2
- **发布确认**：生产者开启 Publisher Confirm 机制，确认消息成功投递
- **消费确认**：消费者采用手动 ACK 模式，仅在业务逻辑处理成功后才发送确认
- **死信队列**：为每个业务队列配置 DLX，消费失败超过 3 次的消息转入死信队列
- **消息幂等性**：每条消息携带唯一 MessageId，确保不会被重复处理

## 7 前端系统

### 7.1 Vue 3 Web 前端

Web 前端采用 Vue 3 框架，搭配 Vite 构建工具与 Element Plus 组件库，为读者提供现代化的浏览器访问体验。主要功能模块包括：资源查询与浏览、在线借阅与预约、个人借阅记录、数据可视化看板（ECharts）、逾期提醒与通知中心。

### 7.2 Java Swing 桌面客户端

Java Swing 桌面客户端面向图书馆管理员，提供功能完整的桌面操作体验。主要包含四个功能面板：MainFrame 主窗口负责整体布局与导航，BorrowPanel 借阅面板处理借阅操作，ReturnPanel 归还面板处理归还操作，QueryPanel 查询面板支持多条件资源检索。客户端通过 HttpClientUtil 工具类与 Spring Boot 后端 REST API 通信。

## 8 PostgreSQL 数据库

### 8.1 为什么选择 PostgreSQL

PostgreSQL 是全球最先进的开源关系数据库管理系统，系统选择 PostgreSQL 主要基于六大核心优势：SQL 标准兼容性最强、JSONB 原生支持、丰富的数据类型、卓越的并发与性能、强大的扩展生态、可靠性与数据完整性。

### 8.2 数据模型设计

| 表名 | 核心字段 | 说明 |
|------|----------|------|
| library_resource | id, title, type, borrow_days, extra_attrs(JSONB) | 资源主表 |
| borrower | id, name, email, type | 借阅者信息 |
| borrow_record | id, borrower_id, resource_id, borrow_date, return_date | 借阅记录 |
| notification | id, user_id, type, content, status | 通知记录（.NET schema） |
| statistics_snapshot | id, date, borrow_count, return_count | 统计快照（.NET schema） |

### 8.3 PostgreSQL 在 Docker 中的配置

使用 postgres:16-alpine 轻量镜像，通过环境变量配置数据库名与认证信息，数据文件挂载至 Docker Volume 确持久化。

## 9 REST API 接口

### 9.1 Spring Boot 核心 API

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

### 9.2 .NET 10 扩展 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/statistics/trends | 借阅趋势统计 |
| GET | /api/statistics/hot-resources | 热门资源排名 |
| POST | /api/reports/generate | 生成借阅报表 |
| GET | /api/notifications/{userId} | 查询用户通知 |

## 10 Docker 容器化部署

### 10.1 容器编排方案

| 服务 | 镜像基础 | 端口 | 说明 |
|------|----------|------|------|
| spring-boot | eclipse-temurin:17-jre | 8080 | Java 核心业务服务 |
| dotnet-api | dotnet/aspnet:10.0 | 8081 | .NET 扩展业务服务 |
| rabbitmq | rabbitmq:3-management | 5672/15672 | 消息中间件 |
| postgresql | postgres:16-alpine | 5432 | PostgreSQL 数据库 |
| vue-web | nginx:alpine | 80 | Vue 3 前端静态资源 |

### 10.2 部署架构

生产环境部署架构采用 Nginx 作为反向代理，将外部请求根据路径规则分发至对应的后端服务或前端静态资源。所有服务运行在 Docker 容器中，通过 Docker 网络内部通信。PostgreSQL 数据文件通过 Docker Volume 持久化至宿主机目录。

## 11 设计原则

- **开闭原则（OCP）**：通过抽象基类与工厂模式、策略模式，新增类型只需添加子类/实现接口
- **单一职责原则（SRP）**：每个类只负责一个职责
- **里氏替换原则（LSP）**：子类可完全替代基类
- **接口隔离原则（ISP）**：接口仅定义相关方法
- **依赖倒置原则（DIP）**：依赖接口而非实现，通过 Spring DI 注入

## 12 环境与运行

### 12.1 开发环境要求

| 工具 | 版本 | 用途 |
|------|------|------|
| JDK | 17+ | Spring Boot 运行与 Swing 客户端 |
| .NET SDK | 10.0 | .NET 后端编译与运行 |
| Node.js | 18+ | Vue 3 前端构建 |
| Docker | 24+ | 容器化部署 |
| Maven | 3.8+ | Java 项目构建 |
| PostgreSQL | 16 | 数据库（Docker 内置或独立部署） |

### 12.2 快速启动

```bash
docker-compose up -d
```

启动完成后：
- Spring Boot 服务运行在 8080 端口
- .NET 服务运行在 8081 端口
- RabbitMQ 管理界面运行在 15672 端口
- PostgreSQL 运行在 5432 端口
- Vue 3 Web 前端运行在 80 端口