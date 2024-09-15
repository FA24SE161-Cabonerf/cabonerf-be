insert into subscription_type(subscription_name,description,project_limit,usage_limit,annual_cost,monthly_cost,can_create_organization)
values ('Basic','Người dùng cá nhân với hạn chế cơ bản',5,1000,99.99,9.99,false),
       ('Pro','Người dùng chuyên nghiệp với nhiều tính năng hơn',20,10000,199.99,19.99,true),
       ('Enterprise', 'Doanh nghiệp với khả năng mở rộng tối đa',100,100000,999.99,99.99,true);

insert into user_verify_status(status_name,description)
values ('Pending','Người dùng đã đăng ký nhưng chưa được xác minh'),
       ('Verified','Người dùng đã được xác minh thành công'),
       ('Suspended','Người dùng bị tạm ngừng xác minh');

insert into user_status(status_name,description)
values ('Active','Người dùng đang hoạt động bình thường'),
       ('Inactive','Người dùng không hoạt động'),
       ('Banned','Người dùng đã bị cấm truy cập hệ thống');

insert into  role(name,status)
values ('System Admin',true),
       ('Company Admin',true),
       ('Organization Manager',true),
       ('Organization LCA Staff',true);