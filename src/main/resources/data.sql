INSERT INTO library_resource (id, title, resource_type, status, author, isbn, publisher, pages, borrower_id, borrow_date, due_date) VALUES ('R001', 'Java编程思想', 'BOOK', 0, 'Bruce Eckel', '9787111213826', '机械工业出版社', 880, NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, author, isbn, publisher, pages, borrower_id, borrow_date, due_date) VALUES ('R002', '设计模式', 'BOOK', 0, 'GoF', '9787111075752', '机械工业出版社', 416, NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, author, isbn, publisher, pages, borrower_id, borrow_date, due_date) VALUES ('R003', 'Spring实战', 'BOOK', 0, 'Craig Walls', '9787115417305', '人民邮电出版社', 464, NULL, NULL, NULL);

INSERT INTO library_resource (id, title, resource_type, status, issue_number, publish_date, category, borrower_id, borrow_date, due_date) VALUES ('R004', '自然杂志', 'MAGAZINE', 0, '2024-01', '2024-01-15', '科技', NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, issue_number, publish_date, category, borrower_id, borrow_date, due_date) VALUES ('R005', '读者', 'MAGAZINE', 0, '2024-02', '2024-02-01', '文学', NULL, NULL, NULL);

INSERT INTO library_resource (id, title, resource_type, status, director, duration_minutes, genre, borrower_id, borrow_date, due_date) VALUES ('R006', '星际穿越', 'DVD', 0, 'Christopher Nolan', 169, '科幻', NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, director, duration_minutes, genre, borrower_id, borrow_date, due_date) VALUES ('R007', '地球脉动', 'DVD', 0, 'David Attenborough', 90, '纪录片', NULL, NULL, NULL);

INSERT INTO library_resource (id, title, resource_type, status, format, file_size_mb, download_url, borrower_id, borrow_date, due_date) VALUES ('R008', '算法导论', 'EBOOK', 0, 'PDF', 45, 'https://example.com/ebooks/clrs.pdf', NULL, NULL, NULL);

INSERT INTO borrower (id, name, phone, email) VALUES ('B001', '张三', '13800138001', 'zhangsan@example.com');
INSERT INTO borrower (id, name, phone, email) VALUES ('B002', '李四', '13800138002', 'lisi@example.com');
INSERT INTO borrower (id, name, phone, email) VALUES ('B003', '王五', '13800138003', 'wangwu@example.com');
