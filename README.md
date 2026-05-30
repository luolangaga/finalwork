# 图书馆管理系统

基于 Spring Boot 后端 + Java GUI 客户端的图书馆管理系统。

## 技术栈

- 后端：Spring Boot 3.x + Spring Data JPA + H2
- 前端：Java Swing
- 通信：HTTP/REST + JSON
- 构建：Maven

## 项目结构

```
src/main/java/com/library/
├── LibraryApplication.java
├── controller/
│   ├── ResourceController.java
│   ├── BorrowController.java
│   └── BorrowerController.java
├── service/
│   ├── ResourceService.java
│   ├── BorrowService.java
│   └── BorrowerService.java
├── repository/
│   ├── ResourceRepository.java
│   ├── BorrowerRepository.java
│   └── BorrowRecordRepository.java
├── model/
│   ├── entity/
│   │   ├── LibraryResource.java
│   │   ├── Book.java
│   │   ├── Magazine.java
│   │   ├── DVD.java
│   │   ├── EBook.java
│   │   ├── Borrower.java
│   │   └── BorrowRecord.java
│   ├── dto/
│   │   ├── ResourceDTO.java
│   │   └── BorrowRequest.java
│   ├── factory/
│   │   └── ResourceFactory.java
│   └── policy/
│       ├── BorrowPolicy.java
│       ├── StudentBorrowPolicy.java
│       ├── TeacherBorrowPolicy.java
│       └── PublicBorrowPolicy.java
├── manager/
│   └── ResourceManager.java
├── client/
│   ├── MainFrame.java
│   ├── BorrowPanel.java
│   ├── ReturnPanel.java
│   └── QueryPanel.java
├── config/
│   └── WebConfig.java
└── util/
    ├── HttpClientUtil.java
    ├── CollectionUtils.java
    ├── ValidationUtil.java
    └── DateUtil.java
```

## 运行方式

### 后端启动
```bash
mvn spring-boot:run
```

### 客户端启动
```bash
mvn clean package -DskipTests
java -jar target/library-management-system-1.0.0.jar
```

## REST API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/resources | 获取所有资源 |
| GET | /api/resources/{id} | 按ID查询资源 |
| GET | /api/resources/type/{type} | 按类型查询 |
| GET | /api/resources/search?keyword=xx | 关键词搜索 |
| POST | /api/resources | 添加资源 |
| DELETE | /api/resources/{id} | 删除资源 |
| POST | /api/borrow/borrow | 借阅资源 |
| POST | /api/borrow/return | 归还资源 |
| GET | /api/borrow/records/{borrowerId} | 查询借阅记录 |
| GET | /api/borrow/overdue | 查询逾期记录 |
| GET | /api/borrowers | 获取所有借阅者 |
| POST | /api/borrowers | 添加借阅者 |

## 设计原则

- **开闭原则(OCP)**：抽象基类 + 工厂模式 + 策略模式
- **单一职责原则(SRP)**：每个类只负责一个职责
- **里氏替换原则(LSP)**：子类可完全替代基类
- **接口隔离原则(ISP)**：BorrowPolicy细粒度设计
- **依赖倒置原则(DIP)**：Service依赖Repository接口
