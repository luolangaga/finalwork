# 图书馆管理系统

Spring Boot + .NET + RabbitMQ + PostgreSQL + Vue 3 + Docker 微服务架构

---

## 部署指南（拉镜像即用）

### 1. 安装 Docker

- Windows/Mac: 下载 [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Linux: `curl -fsSL https://get.docker.com | bash`

验证安装：
```bash
docker -v
docker compose version
```

### 2. 下载部署文件

```bash
git clone https://github.com/luolangaga/finalwork.git
cd finalwork
```

### 3. 一键启动

```bash
docker compose -f docker-compose.deploy.yml up -d
```

首次会自动从 Docker Hub 拉取镜像，之后秒级启动。

### 4. 访问

| 服务 | 地址 |
|------|------|
| **Web 前端** | http://localhost |
| Spring Boot API | http://localhost:8080 |
| .NET API | http://localhost:8081 |
| RabbitMQ 管理 | http://localhost:15672 (guest/guest) |

### 5. 验证

```bash
# 查看是否全部启动
docker compose -f docker-compose.deploy.yml ps

# 测试 API
curl http://localhost:8080/api/resources
```

---

## 常用命令

```bash
# 启动
docker compose -f docker-compose.deploy.yml up -d

# 查看日志
docker compose -f docker-compose.deploy.yml logs -f

# 停止
docker compose -f docker-compose.deploy.yml down

# 停止并清空数据
docker compose -f docker-compose.deploy.yml down -v
```

---

## 配置

所有配置通过环境变量注入：

| 环境变量 | 默认值 | 说明 |
|----------|--------|------|
| `PG_PASSWORD` | `library_password` | PostgreSQL 密码 |
| `DOCKER_USERNAME` | `luolangaga` | Docker Hub 用户名（切换镜像源） |

```bash
# 自定义密码
PG_PASSWORD=myPass123 docker compose -f docker-compose.deploy.yml up -d

# 指定版本
SPRING_TAG=v1.0.0 docker compose -f docker-compose.deploy.yml up -d
```

---

## 架构

```
浏览器 → :80 (Nginx + Vue 3)
            ├── /api/resources/*      → spring-boot:8080
            ├── /api/borrow/*         → spring-boot:8080
            ├── /api/statistics/*     → dotnet-api:8081
            └── /api/notifications/*  → dotnet-api:8081

spring-boot ──→ postgresql:5432 (library/public)
spring-boot ──→ rabbitmq:5672     (发布 borrow.created 事件)

dotnet-api  ──→ postgresql:5432 (library/analytics)
dotnet-api  ──→ rabbitmq:5672     (消费 borrow.created 事件)
```

---

## REST API

### Spring Boot（:8080）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/resources | 获取所有资源 |
| GET | /api/resources/{id} | 按 ID 查询 |
| GET | /api/resources/type/{type} | 按类型查询 |
| GET | /api/resources/search?keyword= | 关键词搜索 |
| POST | /api/resources | 添加资源 |
| DELETE | /api/resources/{id} | 删除资源 |
| POST | /api/borrow/borrow | 借阅 |
| POST | /api/borrow/return | 归还 |
| GET | /api/borrow/overdue | 逾期记录 |

### .NET（:8081）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/statistics/trends | 借阅趋势 |
| GET | /api/statistics/hot-resources | 热门资源 |
| POST | /api/reports/generate | 生成报表 |
| GET | /api/notifications/{userId} | 用户通知 |

```bash
curl http://localhost:8080/api/resources
curl -X POST "http://localhost:8080/api/borrow/borrow?borrowerId=B001&resourceId=R001"
curl http://localhost:8081/api/statistics/trends
```

---

## 本地开发

```bash
# 仅 Spring Boot（H2 内存库，无需 Docker）
mvn spring-boot:run

# .NET
cd dotnet-service && dotnet run

# Vue 前端（热更新）
cd frontend && npm install && npm run dev

# 本地构建所有镜像
docker compose build
```

---

## 初始化数据

| 编号 | 标题 | 类型 | 可借 |
|------|------|------|------|
| R001 | Java编程思想 | BOOK | 30天 |
| R002 | 设计模式 | BOOK | 30天 |
| R003 | Spring实战 | BOOK | 30天 |
| R004 | 自然杂志 | MAGAZINE | 14天 |
| R005 | 读者 | MAGAZINE | 14天 |
| R006 | 星际穿越 | DVD | 7天 |
| R007 | 地球脉动 | DVD | 7天 |
| R008 | 算法导论 | EBOOK | 21天 |

| 编号 | 姓名 | 类型 | 限额 |
|------|------|------|------|
| B001 | 张三 | STUDENT | 5本 |
| B002 | 李四 | TEACHER | 10本 |
| B003 | 王五 | PUBLIC | 3本 |

---

## CI/CD

GitHub Actions 自动构建 3 个镜像并推送到 Docker Hub：

- `luolangaga/library-spring-boot`
- `luolangaga/library-dotnet-api`
- `luolangaga/library-vue-web`

需在 GitHub 仓库 Settings → Secrets 中配置：`DOCKER_USERNAME`、`DOCKER_PASSWORD`。

---

## 设计原则

详见 [DESIGN.md](DESIGN.md) — OCP / SRP / LSP / ISP / DIP 全部落地。