INSERT INTO borrower (id, name, phone, email) VALUES ('B001', '张三', '13800138001', 'zhangsan@example.com');
INSERT INTO borrower (id, name, phone, email) VALUES ('B002', '李四', '13800138002', 'lisi@example.com');
INSERT INTO borrower (id, name, phone, email) VALUES ('B003', '王五', '13800138003', 'wangwu@example.com');

INSERT INTO library_resource (id, title, resource_type, status, author, isbn, publisher, pages) VALUES ('R001', 'Java编程思想', 'BOOK', 'AVAILABLE', 'Bruce Eckel', '9787111213826', '机械工业出版社', 880);
INSERT INTO library_resource (id, title, resource_type, status, author, isbn, publisher, pages) VALUES ('R002', '设计模式', 'BOOK', 'AVAILABLE', 'GoF', '9787111075752', '机械工业出版社', 416);
INSERT INTO library_resource (id, title, resource_type, status, author, isbn, publisher, pages) VALUES ('R003', 'Spring实战', 'BOOK', 'AVAILABLE', 'Craig Walls', '9787115417305', '人民邮电出版社', 464);

INSERT INTO library_resource (id, title, resource_type, status, issue_number, publish_date, category) VALUES ('R004', '自然杂志', 'MAGAZINE', 'AVAILABLE', '2024-01', '2024-01-15', '科技');
INSERT INTO library_resource (id, title, resource_type, status, issue_number, publish_date, category) VALUES ('R005', '读者', 'MAGAZINE', 'AVAILABLE', '2024-02', '2024-02-01', '文学');

INSERT INTO library_resource (id, title, resource_type, status, director, duration_minutes, genre) VALUES ('R006', '星际穿越', 'DVD', 'AVAILABLE', 'Christopher Nolan', 169, '科幻');
INSERT INTO library_resource (id, title, resource_type, status, director, duration_minutes, genre) VALUES ('R007', '地球脉动', 'DVD', 'AVAILABLE', 'David Attenborough', 90, '纪录片');

INSERT INTO library_resource (id, title, resource_type, status, format, file_size_mb, download_url) VALUES ('R008', '算法导论', 'EBOOK', 'AVAILABLE', 'PDF', 45, 'https://example.com/ebooks/clrs.pdf');
