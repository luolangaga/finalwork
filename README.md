# 图书馆管理系统

基于 Spring Boot 后端 + Java GUI 客户端的图书馆管理系统。

## 技术栈

| 层次 | 技术 | 说明 |
|------|------|------|
| 后端框架 | Spring Boot 3.2.5 | 自动配置、内嵌Tomcat、RESTful API |
| 持久层 | Spring Data JPA + H2 | 内存数据库，零配置启动 |
| 前端客户端 | Java Swing | 原生GUI，跨平台兼容 |
| 通信协议 | HTTP/REST + JSON | 前后端解耦 |
| 构建工具 | Maven | 依赖管理与项目构建 |
| Java版本 | JDK 17+ | 使用switch表达式、pattern matching等特性 |

## 环境要求

- **JDK 17** 或更高版本（必须）
- **Maven 3.8+**（必须，用于编译构建）
- 网络连接（首次构建时Maven需要下载依赖）

验证环境：
```bash
java -version    # 应显示 17 或更高
mvn -version     # 应显示 3.8 或更高
```

## 项目结构

```
library-management-system/
├── pom.xml                                    # Maven配置文件
├── README.md                                  # 本文档
├── DESIGN.md                                  # 设计原则文档
└── src/
    ├── main/
    │   ├── java/com/library/
    │   │   ├── LibraryApplication.java        # Spring Boot启动类
    │   │   ├── controller/                    # REST控制器
    │   │   │   ├── ResourceController.java
    │   │   │   ├── BorrowController.java
    │   │   │   └── BorrowerController.java
    │   │   ├── service/                       # 业务服务层
    │   │   │   ├── ResourceService.java
    │   │   │   ├── BorrowService.java
    │   │   │   └── BorrowerService.java
    │   │   ├── repository/                    # 数据访问层
    │   │   │   ├── ResourceRepository.java
    │   │   │   ├── BorrowerRepository.java
    │   │   │   └── BorrowRecordRepository.java
    │   │   ├── model/
    │   │   │   ├── entity/                    # JPA实体类
    │   │   │   │   ├── LibraryResource.java   # 抽象基类
    │   │   │   │   ├── Book.java              # 书籍
    │   │   │   │   ├── Magazine.java          # 杂志
    │   │   │   │   ├── DVD.java               # DVD
    │   │   │   │   ├── EBook.java             # 电子书
    │   │   │   │   ├── Borrower.java          # 借阅者
    │   │   │   │   └── BorrowRecord.java      # 借阅记录
    │   │   │   ├── dto/                       # 数据传输对象
    │   │   │   │   ├── ResourceDTO.java
    │   │   │   │   └── BorrowRequest.java
    │   │   │   ├── factory/                   # 工厂模式
    │   │   │   │   └── ResourceFactory.java
    │   │   │   └── policy/                    # 策略模式
    │   │   │       ├── BorrowPolicy.java
    │   │   │       ├── StudentBorrowPolicy.java
    │   │   │       ├── TeacherBorrowPolicy.java
    │   │   │       └── PublicBorrowPolicy.java
    │   │   ├── manager/
    │   │   │   └── ResourceManager.java       # 集合框架资源管理器
    │   │   ├── client/                        # Java Swing GUI客户端
    │   │   │   ├── MainFrame.java
    │   │   │   ├── BorrowPanel.java
    │   │   │   ├── ReturnPanel.java
    │   │   │   └── QueryPanel.java
    │   │   ├── config/
    │   │   │   └── WebConfig.java             # CORS配置
    │   │   └── util/                          # 工具类
    │   │       ├── HttpClientUtil.java
    │   │       ├── CollectionUtils.java
    │   │       ├── ValidationUtil.java
    │   │       └── DateUtil.java
    │   └── resources/
    │       ├── application.yml                # Spring Boot配置
    │       └── data.sql                       # 初始化数据
    └── test/java/com/library/                 # 测试类
        ├── LibraryApplicationTest.java
        ├── UtilTest.java
        ├── PolicyAndFactoryTest.java
        └── IntegrationTest.java
```

## 编译与运行

### 第一步：克隆项目
```bash
git clone https://gitee.com/swunjavaclass/finalwork.git
cd finalwork
```

### 第二步：编译项目
```bash
# 编译（跳过测试）
mvn clean compile -DskipTests

# 或者编译并运行测试
mvn clean compile
```

> 首次编译Maven会下载依赖，可能需要几分钟，请耐心等待。

### 第三步：启动后端服务
```bash
# 方式一：直接运行（推荐开发时使用）
mvn spring-boot:run

# 方式二：先打包再运行
mvn clean package -DskipTests
java -jar target/library-management-system-1.0.0.jar
```

启动成功后会看到：
```
Started LibraryApplication in X.XX seconds
```

后端默认运行在 `http://localhost:8080`。

### 第四步：启动GUI客户端

GUI客户端集成在同一个项目中，通过运行MainFrame启动：

```bash
# 在另一个终端窗口运行
mvn exec:java -Dexec.mainClass="com.library.client.MainFrame"
```

或者用IDE（IntelliJ IDEA）直接运行 `com.library.client.MainFrame` 的main方法。

### 第五步：验证系统

1. **后端API测试**：浏览器访问 http://localhost:8080/api/resources ，应返回JSON格式的资源列表
2. **H2控制台**：浏览器访问 http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:library`
   - 用户名: `sa`
   - 密码: 空
3. **GUI客户端**：在MainFrame窗口中操作借阅、归还、查询

## REST API 接口

### 资源管理
| HTTP方法 | 路径 | 说明 | 请求体 |
|---------|------|------|--------|
| GET | /api/resources | 获取所有资源列表 | 无 |
| GET | /api/resources/{id} | 按ID查询资源 | 无 |
| GET | /api/resources/type/{type} | 按类型查询资源(BOOK/MAGAZINE/DVD/EBOOK) | 无 |
| GET | /api/resources/search?keyword=xx | 按关键词搜索资源 | 无 |
| POST | /api/resources | 添加新资源 | ResourceDTO JSON |
| DELETE | /api/resources/{id} | 删除资源 | 无 |

### 借阅管理
| HTTP方法 | 路径 | 说明 | 参数 |
|---------|------|------|------|
| POST | /api/borrow/borrow?borrowerId=xx&resourceId=xx | 借阅资源 | Query参数 |
| POST | /api/borrow/return?resourceId=xx | 归还资源 | Query参数 |
| GET | /api/borrow/records/{borrowerId} | 查询借阅记录 | 无 |
| GET | /api/borrow/overdue | 查询逾期记录 | 无 |

### 借阅者管理
| HTTP方法 | 路径 | 说明 | 请求体 |
|---------|------|------|--------|
| GET | /api/borrowers | 获取所有借阅者 | 无 |
| GET | /api/borrowers/{id} | 按ID查询借阅者 | 无 |
| POST | /api/borrowers | 添加借阅者 | Borrower JSON |
| DELETE | /api/borrowers/{id} | 删除借阅者 | 无 |

### API使用示例
```bash
# 查询所有资源
curl http://localhost:8080/api/resources

# 按类型查询书籍
curl http://localhost:8080/api/resources/type/BOOK

# 搜索关键词
curl "http://localhost:8080/api/resources/search?keyword=Java"

# 借阅资源
curl -X POST "http://localhost:8080/api/borrow/borrow?borrowerId=B001&resourceId=R001"

# 归还资源
curl -X POST "http://localhost:8080/api/borrow/return?resourceId=R001"

# 查询逾期记录
curl http://localhost:8080/api/borrow/overdue
```

## 初始化数据

系统启动时自动加载以下测试数据（data.sql）：

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
| 编号 | 姓名 | 邮箱 |
|------|------|------|
| B001 | 张三 | zhangsan@example.com |
| B002 | 李四 | lisi@example.com |
| B003 | 王五 | wangwu@example.com |

## 配置说明

配置文件位于 `src/main/resources/application.yml`：

```yaml
server:
  port: 8080                    # 后端服务端口

spring:
  datasource:
    url: jdbc:h2:mem:library   # H2内存数据库（重启后数据清空）
    # 如需持久化，改为: jdbc:h2:file:./data/library
  jpa:
    hibernate:
      ddl-auto: update         # 自动建表(update/create/create-drop/validate)
  h2:
    console:
      enabled: true            # H2 Web控制台开关
```

## 运行测试

```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=LibraryApplicationTest
mvn test -Dtest=IntegrationTest
mvn test -Dtest=PolicyAndFactoryTest
mvn test -Dtest=UtilTest
```

## 常见问题

**Q: 启动报错 "Unsupported class file major version"**
A: Java版本不对，需要JDK 17+。运行 `java -version` 检查。

**Q: 端口8080已被占用**
A: 修改 `application.yml` 中的 `server.port` 为其他端口，如 `8081`。

**Q: GUI客户端连接后端失败**
A: 确保后端已启动且运行在 `localhost:8080`。如修改了端口，需同步修改 `HttpClientUtil.java` 中的 `BASE_URL`。

**Q: Maven下载依赖很慢**
A: 可以配置国内Maven镜像（阿里云），编辑 `~/.m2/settings.xml` 添加镜像源。

**Q: 数据重启后丢失**
A: 默认使用H2内存数据库，重启即清空。修改 `application.yml` 中数据源URL为 `jdbc:h2:file:./data/library` 可持久化到文件。

## 设计原则

详见 [DESIGN.md](DESIGN.md)

- **开闭原则(OCP)**：抽象基类 + 工厂模式 + 策略模式
- **单一职责原则(SRP)**：每个类只负责一个职责
- **里氏替换原则(LSP)**：子类可完全替代基类
- **接口隔离原则(ISP)**：BorrowPolicy细粒度设计
- **依赖倒置原则(DIP)**：Service依赖Repository接口
