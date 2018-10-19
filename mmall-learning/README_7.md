# 第7章 Redis分布式算法原理、分布式连接池实战及验证

## 7.1 传统分布式算法
![](src/main/resources/images/redis_node_4.png)
当增加一个redis节点后，命中率为5/20=25%
![](src/main/resources/images/redis_node_5.png)

## 7.2 Consistent Hash一致性算法原理
key1-object1(key-value键值对)顺时针保存到就近的KeyA-Cache A（服务器节点）中。
![](src/main/resources/images/consistent_hash.png)
增加节点影响范围（逆时针）：
![](src/main/resources/images/consistent_hash_add.png)
删除节点影响范围（逆时针）：
![](src/main/resources/images/consistent_hash_delete.png)

## 7.3 Hash倾斜性
B上没有任何数据节点：
![](src/main/resources/images/hash_unbalnace.png)

## 7.4 通过虚拟节点解决Hash倾斜性
![](src/main/resources/images/virtual_node.png)