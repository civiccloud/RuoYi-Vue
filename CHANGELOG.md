## 更新日志


### 1.0.0

*2020-04-21*
 Initial commit

*2021-04-24*
 新增收款方信息表

CREATE TABLE pms_basic_payment_info(
id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键' ,
account_name VARCHAR(32) NOT NULL   COMMENT '账户名称' ,
account_bank VARCHAR(32)    COMMENT '账户开户行' ,
account_number VARCHAR(32) NOT NULL   COMMENT '账号' ,
province BIGINT    COMMENT '账户所在省份' ,
user_id VARCHAR(32)    COMMENT '用户ID' ,
is_delete VARCHAR(1)   DEFAULT 0 COMMENT '乐观锁' ,
create_user VARCHAR(32)    COMMENT '创建人' ,
create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
update_user VARCHAR(32)    COMMENT '更新人' ,
update_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
PRIMARY KEY (id)
) COMMENT = '收款信息表 ';