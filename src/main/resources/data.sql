INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R001', 'Java编程思想', 'BOOK', 'AVAILABLE', '{"author":"Bruce Eckel","isbn":"9787111213826","publisher":"机械工业出版社","pages":880}', NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R002', '设计模式', 'BOOK', 'AVAILABLE', '{"author":"GoF","isbn":"9787111075752","publisher":"机械工业出版社","pages":416}', NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R003', 'Spring实战', 'BOOK', 'AVAILABLE', '{"author":"Craig Walls","isbn":"9787115417305","publisher":"人民邮电出版社","pages":464}', NULL, NULL, NULL);

INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R004', '自然杂志', 'MAGAZINE', 'AVAILABLE', '{"issueNumber":"2024-01","publishDate":"2024-01-15","category":"科技"}', NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R005', '读者', 'MAGAZINE', 'AVAILABLE', '{"issueNumber":"2024-02","publishDate":"2024-02-01","category":"文学"}', NULL, NULL, NULL);

INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R006', '星际穿越', 'DVD', 'AVAILABLE', '{"director":"Christopher Nolan","durationMinutes":169,"genre":"科幻"}', NULL, NULL, NULL);
INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R007', '地球脉动', 'DVD', 'AVAILABLE', '{"director":"David Attenborough","durationMinutes":90,"genre":"纪录片"}', NULL, NULL, NULL);

INSERT INTO library_resource (id, title, resource_type, status, extra_attrs, borrower_id, borrow_date, due_date) VALUES ('R008', '算法导论', 'EBOOK', 'AVAILABLE', '{"format":"PDF","fileSizeMB":45,"downloadUrl":"https://example.com/ebooks/clrs.pdf"}', NULL, NULL, NULL);

INSERT INTO borrower (id, name, phone, email, type) VALUES ('B001', '张三', '13800138001', 'zhangsan@example.com', 'STUDENT');
INSERT INTO borrower (id, name, phone, email, type) VALUES ('B002', '李四', '13800138002', 'lisi@example.com', 'TEACHER');
INSERT INTO borrower (id, name, phone, email, type) VALUES ('B003', '王五', '13800138003', 'wangwu@example.com', 'PUBLIC');
