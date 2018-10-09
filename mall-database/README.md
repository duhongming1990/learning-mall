# mall-database

## 第3章 数据表结构设计（表结构，表关系，索引，时间戳）
[返回主目录](../README.md)

### 3.1 索引类型
    普通索引 Normal
    唯一索引 Unique
    全文索引 FullText
    空间索引 Spatial
    
### 3.2 索引方法
    HASH
    BTREE
    
### 3.2存疑 项目中用的都是BTREE而我们对user表都是=值查询，是否应该用HASH呢？
在存储引擎为MyISAM和InnoDB的表中只能使用BTREE，其默认值就是BTREE；
在存储引擎为MEMORY或者HEAP的表中可以使用HASH和BTREE两种类型的索引，其默认值为HASH。

### 3.3 查业务问题的后悔药
    create_time datetime 数据创建时间
    update_time datetime 数据更新时间

