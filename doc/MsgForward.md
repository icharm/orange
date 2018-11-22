# MsgForward 消息转发
为用户提供消息转发到洋葱酱（微信服务号）的接口。

## Functions List
前台： 注册、登录、获取Sercet
后台： 管理Sercet，管理转发的消息、统计转发的消息。


## 数据库设计
### mf_user
Name | Type | Remark
:----------- | :-----------: | -----------:
id | int(11) | 自增主键
user_id | varchar(30) | user表外键
secret | varchar(30) | 秘钥
today_count | int | 今日发送次数
total_count | int | 累计发送次数
created_at | datetime | 创建时间
updated_at | datetime | 更新时间

### mf_record
Name | Type | Remark
:----------- | :-----------: | -----------:
id | int(11) | 自增主键 
user_id | varchar(30) | user表外键
content | text | 消息体（json格式）
status | varchar(10) | 状态
created_at | datetime | 创建时间
updated_at | datetime | 更新时间