# 设计原则总结

## 1. 开闭原则 (OCP)

### 资源类型扩展
- 所有资源继承自LibraryResource抽象基类
- 新增资源类型只需创建子类，无需修改现有代码
- ResourceFactory通过注册表管理资源创建，支持动态扩展

### 借阅规则策略
- BorrowPolicy接口定义借阅规则
- 不同借阅者类型对应不同策略实现
- 新增借阅者类型只需创建新策略类

## 2. 单一职责原则 (SRP)

| 类 | 职责 |
|---|------|
| LibraryResource | 管理资源属性和状态 |
| Borrower | 管理借阅者信息和借阅限制 |
| BorrowRecord | 管理借阅记录和逾期检测 |
| ResourceFactory | 管理资源创建逻辑 |
| BorrowPolicy | 管理借阅规则 |
| ResourceManager | 管理资源集合操作 |

## 3. 里氏替换原则 (LSP)

- 所有子类(Book, Magazine, DVD, EBook)可完全替代基类LibraryResource
- 系统所有操作通过基类引用完成，不依赖具体子类
- 子类只扩展行为，不改变基类契约

## 4. 接口隔离原则 (ISP)

- BorrowPolicy接口只包含借阅规则相关方法
- Repository接口只包含数据访问方法
- 客户端只依赖需要的接口方法

## 5. 依赖倒置原则 (DIP)

- Service层依赖Repository接口而非实现类
- 通过Spring依赖注入实现解耦
- 高层模块不依赖低层模块，两者都依赖抽象

## 集合框架应用

| 集合类型 | 用途 | 选型理由 |
|---------|------|---------|
| HashMap | 资源ID快速查找 | O(1)查找复杂度 |
| TreeSet | 按标题排序索引 | 自动排序、去重 |
| ArrayList | 资源列表展示 | 有序、支持索引 |
| Stream API | 过滤、分组、统计 | 函数式编程、代码简洁 |
