# 第12章 Spring Schedule实现定时关单
# 12.1 配置xml
```xml
    <task:annotation-driven/>
    <context:component-scan base-package="com.mmall.task"/>
```
```java
    @Component
    @Scheduled(cron = "0/1 * * * * ?")
```
# 12.2 MySQL行锁和表锁
    SELECT ... FOR UPDATE(悲观锁)
    使用InnoDB引擎
    Row-Level Lock（明确的主键）
    Table-Level Lock（无明确的主键）
eg：

    明确指定主键，并且有结果集，Row-Level Lock：id=66
    明确指定主键，并且无结果集，无Lock：id=-100
    无主键，Table-Level Lock：name='iphone'
    主键不明确，Table-Level Lock：id <> 66
    主键不明确，Table-Level Lock：id like 66
    
# 第13章 Redis分布式锁原理