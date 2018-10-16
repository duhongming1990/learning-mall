

# 2 Lombok框架集成及原理解析

## 2.1 JavaBean常用组合
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    
## 2.2 慎用@Data
别加@Data太全了，普通的pojo有上面四个注解即可！

## 2.3 比较对象是否相等
    @EqualsAndHashCode(of = "id")
    
## 2.4 对象打印
    @ToString(exclude="createTime")
    
## 2.5 日志打印
    @Slf4j
    @Log4j
    
## 2.6 工具
[Java Decompiler](http://jd.benow.ca/)

# 3 Maven环境隔离应用场景及验证实战

## 3.1 实际的项目环境
    本地开发环境Local
    开发环境Dev
    测试环境Beta
    线上环境Prod
    
## 3.2 Maven环境隔离配置
```xml
<build>
  <resources>
    <resource>
      <directory>src/main/resources.${deploy.type}</directory>
      <excludes>
        <exclude>*.jsp</exclude>
      </excludes>
    </resource>
    <resource>
      <directory>src/main/resources</directory>
    </resource>
  </resources>
</build>

<profiles>
  <profile>
    <id>dev</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
      <deploy.type>dev</deploy.type>
    </properties>
  </profile>
  <profile>
    <id>beta</id>
    <properties>
      <deploy.type>beta</deploy.type>
    </properties>
  </profile>
  <profile>
    <id>prod</id>
    <properties>
      <deploy.type>prod</deploy.type>
    </properties>
  </profile>
</profiles>
```

# 4 Tomcat集群演进及环境搭建
## 4.1 Mac/Linux配置
1.修改.bash_profile环境变量
```
#tomcat环境配置
export CATALINA_1_BASE='/usr/local/Cellar/tomcat@8/8.5.28-1.28/libexec'
export CATALINA_1_HOME='/usr/local/Cellar/tomcat@8/8.5.28-1.28/libexec'
export TOMCAT_1_HOME='/usr/local/Cellar/tomcat@8/8.5.28-1.28/libexec'

export CATALINA_2_BASE='/usr/local/Cellar/tomcat@8/8.5.28-2.28/libexec'
export CATALINA_2_HOME='/usr/local/Cellar/tomcat@8/8.5.28-2.28/libexec'
export TOMCAT_2_HOME='/usr/local/Cellar/tomcat@8/8.5.28-2.28/libexec'

export CATALINA_3_BASE='/usr/local/Cellar/tomcat@8/8.5.28-3.28/libexec'
export CATALINA_3_HOME='/usr/local/Cellar/tomcat@8/8.5.28-3.28/libexec'
export TOMCAT_3_HOME='/usr/local/Cellar/tomcat@8/8.5.28-3.28/libexec'

#tomcat启动配置
alias tomcat1='/usr/local/Cellar/tomcat@8/8.5.28-1.28/libexec/bin/startup.sh'
alias tomcat2='/usr/local/Cellar/tomcat@8/8.5.28-2.28/libexec/bin/startup.sh'
alias tomcat3='/usr/local/Cellar/tomcat@8/8.5.28-3.28/libexec/bin/startup.sh'
```
2.修改catalina.sh配置变量；1，2，3依次类推
```
# OS specific support.  $var _must_ be set to either true or false.
export CATALINA_BASE=$CATALINA_1_BASE;
export CATALINA_HOME=$CATALINA_1_HOME;
```
3.修改server.xml配置端口；1，2，3依次类推

    都修改百分位，可以配置0-9，10台服务器。
    8005->>>8105->>>8205->>>8305
    8080->>>8180->>>8280->>>8380
    8009->>>8109->>>8209->>>8309

