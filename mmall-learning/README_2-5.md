

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

## 4.2 Windows配置
1.修改server.xml配置端口；1，2，3依次类推

    都修改百分位，可以配置0-9，10台服务器。
    8005->>>8105->>>8205->>>8305
    8080->>>8180->>>8280->>>8380
    8009->>>8109->>>8209->>>8309

## 4.3 Nginx反向代理配置
1.server{}部分注释掉，新增include vhost/*.conf;
```json

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    #server {
        #listen       8080;
        #server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        #location / {
        #    root   html;
        #    index  index.html index.htm;
        #}

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        #error_page   500 502 503 504  /50x.html;
        #location = /50x.html {
        #    root   html;
        #}

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    #}

	##########################vhost#####################################
    include vhost/*.conf;

    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```
2.新建文件夹vhost,新建文件www.tomcat.nginx.study.conf
```json
#服务器的集群
upstream www.tomcat.nginx.study{#服务器集群名字
  server www.tomcat.nginx.study:8180 weight=1;#服务器配置   weight是权重的意思，权重越大，分配的概率越大。
  server www.tomcat.nginx.study:8280 weight=1;#服务器配置   weight是权重的意思，权重越大，分配的概率越大。
  server www.tomcat.nginx.study:8380 weight=1;#服务器配置   weight是权重的意思，权重越大，分配的概率越大。
}

#当前的Nginx的配置
server {
   listen       80;#监听80端口，可以改成其他端口
   server_name  localhost;##############   当前服务的域名

   location / {
       proxy_pass http://www.tomcat.nginx.study;  
       proxy_redirect default;
   }


   error_page   500 502 503 504  /50x.html;
   location = /50x.html {
       root   html;
   }
}
```

[Mac上Tomcat集群及Nginx负载均衡配置](https://my.oschina.net/duhongming52java/blog/1796375)

[Redis 那些事儿](https://my.oschina.net/duhongming52java/blog/1647877)
redis主从配置：
```
slaveof <masterip> <masterport>
```