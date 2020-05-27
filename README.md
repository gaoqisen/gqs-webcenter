<p align="center" >
    <img src="http://ku.90sjimg.com/element_origin_min_pic/16/12/22/61a01c9132884760d78c4207a532e346.jpg" width="150">
    <h3 align="center">GQS-WEBCENTER</h3>
    <p align="center">
        GQS-WEBCENTER, 一个轻量级的WEB中心，简单的权限管理和登录。
        <br>
        <a href="https://github.com/gaoqisen/gqs-webcenter"><strong>-- GitHub --</strong></a>
        <br>
        <br>
        <a href="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-job/">
            <img src="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-job/badge.svg" >
        </a>
        <a href="https://github.com/gaoqisen/gqs-webcenter/">
            <img src="https://img.shields.io/github/stars/gaoqisen/gqs-webcenter" >
        </a>
        <a href="http://www.gnu.org/licenses/gpl-3.0.html">
         <img src="https://img.shields.io/badge/license-GPLv3-blue.svg" >
        </a>
    </p>
</p>

## 一、简介

### 1.1 概述

WebCenter是一个简单的权限管理小工具，目的是为了简化开发不用考虑系统登录、菜单权限、接口权限。利用SpringBoot进行简单配置就可以进行业务逻辑的开发。

### 1.2 特性

1. 简单：安装好WebCenter服务端后，后端项目引入gqs_webcenter_client.jar。前端利用webc脚手架初始化项目，即可进行业务逻辑的开发。
2. 动态：服务端可以动态配置各个系统的菜单、菜单图标、角色、权限、以及REST接口的权限。

### 1.3 功能

1. 用户管理：增加多个用户，管理用户的账号密码等
2. 角色管理：动态分配角色的权限，前端页面进行权限控制之后，可以实现不同的角色登录展示不同的菜单。
3. REST接口管理：各个系统注册到服务端后可以配置接口的调用权限（所有人可以访问、登录后可以访问、必须有权限才可以访问）
4. 系统管理：给各个系统分配clientId和密匙，用于系统的单点登录
5. 菜单管理：管理各个系统的菜单、路由、图标等。

### 1.4 下载


| 介绍 | 地址 | 
| --- | --- | 
| 后端 | https://github.com/gaoqisen/gqs-webcenter |  
| 前端 | https://github.com/gaoqisen/webcenter-vue-cli |


### 1.4 环境

Maven3+
Jdk1.8+
Mysql5.7+
Vue3.10.0+
Webpack4.0.0+

## 二、快速入门

![https://gaoqisen.github.io/GraphBed/202005/20200527215308.png](https://gaoqisen.github.io/GraphBed/202005/20200527215308.png)

### 2.1 运行Webcenter服务端

#### 2.1.1 基础组件依赖

1. 服务端依赖Redis和Mysql，请先安装。
2. 请下载项目源码并解压，获取 “WebCenter数据库初始化SQL脚本(/db/webcenter.sql)” 并执行。

#### 2.1.2 获取源码并运行

1. 可以通过下载源码进行编译获取jar包，源码结构如下：
    
    ```
     ──gqs-webcenter
       ├── db  // 数据库
       │   └── webcenter.sql // 数据库初始化脚本
       ├── gqs-webcenter-client  // 客服端
       ├── gqs-webcenter-common  // 通用工具
       ├── gqs-webcenter-component  // 通用组件
       │   ├── gqs-webcenter-redis  // redis组件
       │   ├── gqs-webcenter-webapi  // swagger组件
       ├── gqs-webcenter-console  // 控制台，需要编译打包的模块
       ├── gqs-webcenter-sample  // 简单的客服端例子
       ├── gqs-webcenter-service  // 服务层，数据访问层
       ├── gqs-webcenter-webpage  // 前端项目,build之后将静态文件打包到了 gqs-webcenter-console的resource/public里面
       └── readme.md  // 项目介绍
    ```
2. 部署成功之后访问http://localhost:8000， 出现如下页面表示部署成功。默认登录账号admin,登录密码admin。
        ![https://gaoqisen.github.io/GraphBed/202005/20200523232105.png](https://gaoqisen.github.io/GraphBed/202005/20200523232105.png)
3. 也可以直接下载Release后的jar包进行安装。
    -  在启动命令时进行参数配置如：
    ```
        nohup java -Xms1024m -Xmx1024m -jar webcenter-console-1.0.0.jar --spring.database.username=root --spring.database.password=123456 --spring.redis.password=123456 >/dev/null 2>&1 &
    ```
    
    - 或者在jar包同级目录下新建config目录并把下面的配置写在application.yml文件中，mysql和redis的配置改成本地的。
    ```
    server:
      port: 8000
    spring:
      application:
        name: webcenter-console
      # 返回json格式
      jackson:
        default-property-inclusion: non_null
        date-format: yyyy-MM-dd HH:mm:ss
        time-zone: GMT+8
      ### redis配置
      redis:
        host: localhost
        password: 123456
        port: 6379
        timeout: 60000
      ### 数据库配置
      datasource:
        username: root
        password: 123456
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/bsp?characterEncoding=utf8&useSSL=false
    mybatis:
      mapper-locations: classpath:mapping/*.xml
      type-aliases-package: com.gaoqisen.webcenter.entity
    webcenter:
      client:
        ### 前后端分离：true表示前后端分离，用于单点登录时重定向到前端的地址。false表示非前后端分离，可以不用配置host和prot
        forestage: true
        host: localhost
        port: 8080
    ```
    
### 2.2 创建Maven后端

1. Maven引入依赖

    ```
    <dependency>
        <groupId>com.github.gaoqisen</groupId>
        <artifactId>gqs-webcenter-client</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
2. 添加application.yml的配置

    ```
    spring:
      jackson:
        default-property-inclusion: non_null
        date-format: yyyy-MM-dd HH:mm:ss
        time-zone: GMT+8
      application:
        name: webcenter-sample
      # 需要和服务端的redis是同一个
      redis:
        host: localhost
        password: 123456
        port: 6379
        timeout: 60000
    webcenter:
      # 服务端
      server:
        host: localhost
        port: 8000
        clientid: WZUIIXWZUIIX
        secretkey: qOIWRbzeFvOnXUYTspfSt2ibfJPe1vtG
      # 客服端配置，是否前后端分离，用于单点登录的地址跳转。forestage为false时，host和port可以不写
      client:
        forestage: true
        host: localhost
        port: 8081
    ```
3. 创建WebCenterConfig.java文件，用于将客户端交给spring管理

    ```
    @Configuration
    public class WebCenterConfig extends WebMvcConfigurerAdapter {
    
        @Bean
        public SecurityInterceptor securityInterceptor() {
            return new SecurityInterceptor();
        }
    
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(securityInterceptor()).excludePathPatterns("/static/*")
                    .excludePathPatterns("/error").addPathPatterns("/**");
    
        }
        @Bean
        @DependsOn("webCenterConsole")
        public WebCenterClientBeanFactory springClientBeanFactory() {
            return new WebCenterClientBeanFactory();
        }
    
    
        @Bean
        public WebCenterInitializing webCenterInitializing() {
            return new WebCenterInitializing();
        }
    
        @Bean
        public WebCenterConsole webCenterConsole(){
            WebCenterConsole webCenterConsole = new WebCenterConsole();
            return webCenterConsole;
        }
    
        @Bean
        @DependsOn("redisConnectionFactory")
        public ApiController apiController() {
            return new ApiController();
        }
    
        @Bean
        public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            template.setValueSerializer(jackson2JsonRedisSerializer);
            template.afterPropertiesSet();
            return template;
        }
    }
    ```
    
#### 2.3 创建Vue前端

```
// 全局安装webcenter客服端脚手架
npm install webc -g
// 按照成功之后执行webc命令, 查看是否安装成功
webc 
// 查看所有的脚手架
webc list
// 初始化一个名为test的前端项目（集成了动态菜单和单点登录）
webc init webcenter test
```

- Vue项目引入的主要插件。

    | 名称 | 介绍 | 版本 |
    | --- | --- | --- |
    | element-ui | 饿了么后端UI框架 | 2.8.2 |
    |fortawesome | 图标库 | 5.13 |
    |vue-router | 路由 | 3.0.7 |
    |vue-cookie | cookie管理| 1.1.4 |
    |vuex | 状态管理 | 3.3.0 |
    |axios | HTTP库|0.19.2 |

## 三、功能介绍

- WebCenter服务端启动之后，提供单点登录、动态菜单、动态权限功能。新建的SpringBoot项目依赖了webcenter-client.jar后集成服务端功能，客服端与服务端之间的通信通过Redis的异步消息队列实现。
- 前端Vue通过npm搭建了一个脚手架，通过webc初始化项目之后，自动实现了动态路由，菜单直接在服务端的菜单管理里面进行配置。

## 四、数据库结构


| 名称 | 表名 |描述  |
| --- | --- | --- |
|sys_code | 系统编码表| 用来保存系统信息、clientId、密匙等 |
|sys_code_menu | 系统菜单关联表| 系统和菜单的对应关系 |
|sys_menu | 菜单表| 保存菜单和目录信息 |
|sys_rest  | rest接口表| 保存各个系统的REST接口和权限 |
|sys_role | 角色表| 角色的相关信息 |
|sys_role_menu | 角色菜单关联表| 角色可以查看的菜单 |
|sys_role_rest | 角色接口关联表| 角色可以访问的接口 |
|sys_user | 用户表| 用户的相关信息 |
|sys_user_role| 角色表| 用户关联的角色，一个用户对应多个角色 |
