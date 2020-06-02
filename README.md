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


### 1.5 环境

Maven3+ 
Jdk1.8+ 
Mysql5.7+ 
Vue3.10.0+ 
Webpack4.0.0+ 

## 二、快速入门

### 2.1 运行Webcenter服务端

#### 2.1.1 基础组件依赖

1. 服务端依赖Redis和Mysql，请先安装。
2. 请下载项目源码并解压，获取 “WebCenter数据库初始化SQL脚本(/db/webcenter.sql)” 并执行。

#### 2.1.2 获取源码并运行

1. 可以通过下载源码进行编译获取jar包，源码结构如下：
    
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

### 2.2 前后端不分离搭建客服端

> 通过脚手架搭件项目之后，直接main方法启动即可。

![https://gaoqisen.github.io/GraphBed/202006/20200602095927.png](https://gaoqisen.github.io/GraphBed/202006/20200602095927.png)

```
// 全局安装webcenter客服端脚手架
npm install webc-cli -g
// 之前有安装的需要更新到1.0.2版本
npm update webc-cli -g
// 安装成功之后执行webc命令, 查看是否安装成功
webc 
// 快速搭建项目如上图
webc boot
```

- ArtifactID就是上下文路径，搭建成功启动后，通过http://localhost:8001/ArtifactID 进行访问，如：http://localhost:8001/demo
- 打包上线运行时，需要先在/src/main/resources/webpage目录下npm run build(将vue静态文件打包到resources/public里面)之后在maven clean install。
 - 本地开发时可以webcenter.client.forestage配置改为true，方便本地vue开发。
 
### 2.3 前后端分离搭建客服端

![https://gaoqisen.github.io/GraphBed/202005/20200527215308.png](https://gaoqisen.github.io/GraphBed/202005/20200527215308.png)

    
#### 2.3.1 创建Maven后端

> 创建出springBoot项目之后可以把配置文件改为yml格式，因为后面的配置都是基于yml的。如果需要用properties格式的话可以在https://www.toyaml.com/index.html 里面进行转换。创建成功之后引入maven依赖、添加配置、创建WebCenterConfig.java即可。

1. Maven引入依赖

    ```
    <dependency>
        <groupId>com.github.gaoqisen</groupId>
        <artifactId>gqs-webcenter-client</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    # 发布版本(如果没有oss.sonatype.org仓库的话，需要添加仓库)
    <repositories>
      <repository>
        <id>sonatypeSnapshots</id>
        <name>Sonatype Release</name>
        <releases>
          <enabled>true</enabled>
        </releases>
        <snapshots>
          <enabled>false</enabled>
        </snapshots>
        <url>https://oss.sonatype.org/content/groups/public</url>
      </repository>
    </repositories>
    
    ### pom.xml测试用例
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    	<modelVersion>4.0.0</modelVersion>
    	<parent>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-parent</artifactId>
    		<version>2.2.2.RELEASE</version>
    		<relativePath/> <!-- lookup parent from repository -->
    	</parent>
    	<groupId>com.example</groupId>
    	<artifactId>demo</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    	<name>demo</name>
    	<description>Demo project for Spring Boot</description>
    
    	<properties>
    		<java.version>1.8</java.version>
    	</properties>
    
    	<dependencies>
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-web</artifactId>
    		</dependency>
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-test</artifactId>
    			<scope>test</scope>
    		</dependency>
    		<dependency>
    			<groupId>com.github.gaoqisen</groupId>
    			<artifactId>gqs-webcenter-client</artifactId>
    			<version>1.0.0</version>
    		</dependency>
    	</dependencies>
    </project>
    ```
2. 添加application.yml的配置

    ```
    server:
      port: 8001
      servlet:
        context-path: /sample
    spring:
      jackson:
        default-property-inclusion: non_null
        date-format: yyyy-MM-dd HH:mm:ss
        time-zone: GMT+8
      application:
        name: webcenter-sample
      ### 需要和服务端的redis是同一个
      redis:
        host: localhost
        password: 123456
        port: 6379
        timeout: 60000
    webcenter:
      # 服务端
      server:
        host: localhost
        # 如果服务端端口改了的话，此处的端口应保持一致
        port: 8000
        clientid: WZUIIXWZUIIX
        secretkey: qOIWRbzeFvOnXUYTspfSt2ibfJPe1vtG
      ### 客服端配置，是否前后端分离，用于单点登录的地址跳转。forestage为false时，host和port可以不写
      client:
        forestage: true
        host: localhost
        port: 8080
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
            template.afterPropertiesSet();
            return template;
        }
    }
    ```
    
#### 2.3.2 创建Vue前端

```
// 全局安装webcenter客服端脚手架
npm install webc-cli -g
// 安装成功之后执行webc命令, 查看是否安装成功
webc 
// 查看所有的脚手架
webc list
// 初始化一个名为sample的前端项目（集成了动态菜单和单点登录）
webc init webcenter sample
```

- Vue项目引入的主要插件。

    | 名称 | 介绍 | 版本 |地址|
    | --- | --- | --- | --- |
    | element-ui | 饿了么后端UI框架 | 2.8.2 |https://element.eleme.cn/2.8/#/zh-CN/component/installation|
    |fortawesome | 图标库 | 5.13 |http://www.fontawesome.com.cn/faicons/|
    |vue-router | 路由 | 3.0.7 |https://cn.vuejs.org/v2/guide/routing.html|
    |vuex | 状态管理 | 3.3.0 |https://vuex.vuejs.org/zh/|
    |axios | HTTP库|0.19.2 |http://www.axios-js.com/zh-cn/docs/|

## 三、功能介绍

> 完成上面的搭建之后，启动Maven后端和Vue前端就可以直接开发自己的业务逻辑了。

### 3.1 系统配置

给各个系统分配clientId和密匙，应用名称必须和客户端的spring.application.name一致。

### 3.2 权限配置

权限配置在新建角色的时候进行配置，如果需要修改权限要在修改角色里面进行修改（修改操作是将之前角色和权限的关联信息全部删除之后，新增选择的权限）。修改角色的权限之后，需要用户退出后重新登陆生效。前端的页面权限用如下代码实现：

```
// 权限将斜杠改为冒号即可。@PathVariable类型的接口去掉/{*}如:
// sys/menu/save, sys:menu:save
// sys/menu/info/{id}, sys:menu:info
<el-button v-if="isAuth('sys:menu:save')" >新增</el-button>
```

### 3.3 菜单配置

菜单分为目录和菜单两种，需要单独给每个系统添加菜单和目录，目录可以多层级。路由就是创建的.vue文件的路径。如:/sys/log, 就在views/sys里面创建log.vue。动态路由就会自动路由到log.vue里面。菜单创建好之后需要给对应的角色赋予菜单权限，子系统退出后重新登录生效。

### 3.4 REST接口配置

rest接口有3种权限：公开、登录、权限。客户端启动之后自动注册接口到服务端默认为公开所有人都可以访问的权限。改为登录接口之后，访问的权限就必须登录之后才可以访问。需要权限的接口级别最高必须在权限里面给角色配置了权限才可以访问。

## 四、结构

### 4.1 Webcenter项目结构

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

### 4.2 数据库结构

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

## 五、问答

1. SpringBoot项目依赖了webcenter-client.jar后集成了那些功能？
    答:  提供了单点登录、动态菜单、动态权限功能。
2. 前端Vue通过脚手架初始化之后有那些功能？
    答：实现了动态路由，菜单直接在服务端的菜单管理里面进行配置。
3. 客服端与服务端之间如何通信？
    答: 之间的通信通过Redis的异步消息队列实现。