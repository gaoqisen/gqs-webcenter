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

之前开发了好几个后台的管理系统，每次都需要写一遍登录逻辑、菜单路由、用户管理、角色管理、权限管理这些功能。为了以后再开发这种系统的时候不用再写这些重复的程序，就把这些通用的功能抽象成为了一个简单的权限管理服务(WebCenter)，WebCenter专门负责单点登录、权限管理、菜单配置、用户管理、角色管理、系统管理等。为了在创建系统的时候更简单，于是开发了一个gqs-webcenter-client.jar并上传到了中央仓库，之后创建系统的时候只需要引入这个jar包就可以集成WebCenter。每次创建项目的时候后端都需要添加配置引入jar包，前端都需要配置动态路由，为了简化到都不用复制重复的代码，于是开发了一个web-cli的npm[脚手架](#jsjjs)。在安装脚手架之后就可以直接初始化项目并进行业务逻辑的开发。

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

#### 方式一(docker 推荐)

> 该docker-compose安装了redis、mysql和webcenter3个服务，是否暴露端口可以自行修改。如果本地已经安装了mysql和redis可以只安装my_webcenter容器，安装的时候需要将容器之前的容器名改为db_mysql和db_redis并创建net_webcenter网络将db_mysql和db_redis加入到该网络中。启动成功之后将sql导入到数据库中访问 http://localhost:8000 即可。docker容器间可以参考: https://www.cnblogs.com/kevingrace/p/6590319.html

```
version: '2.0'
services:
  db_mysql:
    restart: always
    image: mysql:5.7.22
    container_name: db_mysql
    ports:
      - 3306:3306
    environment:
      TZ: Asia/Shanghai
      MYSQL_ROOT_PASSWORD: 123456
    command:
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
      --max_allowed_packet=128M
      --sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO"
    volumes:
      - ./mysqldata:/var/lib/mysql
    networks:
      - net
  db_redis:
    image: redis
    container_name: db_redis
    command: redis-server --requirepass 123456
    restart: always
    ports:
      - "6379:6379"
    volumes:
      - ./redisdata:/data
    networks:
      - net
  my_webcenter:
    image: registry.cn-hangzhou.aliyuncs.com/gqs/webcenter:1.0.1
    container_name: my_webcenter
    ports:
      - "8000:8000"
    networks:
      - net
networks:
  net:
    external: 
      # 请先创建net_webcenter网络
      name: net_webcenter
```

#### 方式二(jar包)

1.  环境准备

    - 请先安装Redis、Mysql、JDK1.8。
    - 获取 “WebCenter数据库初始化SQL脚本(/db/[webcenter.sql](https://github.com/gaoqisen/gqs-webcenter/blob/master/db/webcenter.sql))” 并执行。

2. 获取jar包

    - github: https://github.com/gaoqisen/gqs-webcenter/releases
    - gitee: https://gitee.com/gaoqisen/gqs-webcenter/releases
    - 网盘: https://c-t.work/s/8db595c7ac4f44   密码: 7x8yr4
3. 启动jar包

    ```
    // 默认mysql密码123456，redis密码123456启动
    nohub jara -Xms1024m -Xmx1024m -jar webcenter-console-1.0.0.jar
    // 修改mysql密码，redis密码
    nohup java -Xms1024m -Xmx1024m -jar webcenter-console-1.0.0.jar --spring.database.username=root --spring.database.password=23456 --spring.redis.password=23456
    ```

即可访问http://localhost:8000 。

### 2.2 客户端搭建(前后端不分离)

```
// 全局安装webcenter客服端脚手架
npm install webc-cli -g
// 之前有安装的需要更新到1.0.3版本
npm update webc-cli -g
// 安装成功之后执行webc命令, 查看是否安装成功
webc 
// 快速搭建项目(搭建的直接就是SpringBoot项目,通过IntelliJ IDEA可以直接运行，前端Vue代码放在src/main/resources/webpage目录下)
webc boot
// 进入到前端目录
cd demo/src/main/resources/webpage
// 安装依赖，如果没有安装cnpm需要先安装：npm install cnpm
cnpm install
// 启动前端服务
npm run dev
```
![https://gaoqisen.github.io/GraphBed/202006/20200602095927.png](https://gaoqisen.github.io/GraphBed/202006/20200602095927.png)

- 快速搭建的项目main方法启动之后接口通过http://localhost:8001/ArtifactID 访问
- 前端项目启动后，开发环境通过http://localhost:8080 访问即可
- 本地开发时将webcenter.client.forestage配置设置为true，方便本地vue开发。打包上线时改为false。
- 打包上线运行时，需要先在/src/main/resources/webpage目录下npm run build(将vue静态文件打包到resources/public里面)之后在maven clean install。
- 打包上线后ArtifactID就是上下文路径，通过http://localhost:8001/ArtifactID 进行访问，如：http://localhost:8001/demo
- 前后端不分离搭建后，打包为一个jar包，静态文件通过SpringBoot内置的tomcat访问。前后端分离的项目前端静态文件可以用nginx进行代理。(前后端不分离的搭建成功之后就可以开发业务逻辑了)
 - 开发环境搭建完成之后，需要启动3个服务。（Webcenter服务端:8000, Vue前端8080，Maven后端8001）
 
> IntelliJ IDEA打开vue的node_modules会出现假死现象，[解决办法](https://www.dvy.com.cn/2017/11/22/4535.html)： Editor>>File Types>>在Ignore files and folders中添加;node_modules
 
### 2.3 客户端搭建(前后端分离)

![https://gaoqisen.github.io/GraphBed/202005/20200527215308.png](https://gaoqisen.github.io/GraphBed/202005/20200527215308.png)

#### 2.3.1 创建Maven后端

> 创建出springBoot项目之后可以把配置文件改为yml格式，因为后面的配置都是基于yml的。如果需要用properties格式的话可以在https://www.toyaml.com/index.html 里面进行转换。创建成功之后引入maven依赖、添加配置、创建WebCenterConfig.java即可。

1. Maven引入依赖

    ```
    <dependency>
        <groupId>com.github.gaoqisen</groupId>
        <artifactId>gqs-webcenter-client</artifactId>
        <version>1.0.1</version>
    </dependency>
    
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
        # 通过服务端的系统管理里面获取
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

> 完成上面的搭建之后，启动Maven后端和Vue前端就可以直接开发自己的业务逻辑了。

## 三、功能介绍

### 3.1 系统配置

给各个系统分配clientId和密匙，应用名称必须和客户端的spring.application.name一致。每新建一个系统都需要生成不同的clientId和密钥，并更改系统的配置。

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

- 后端接口通过@ApiOperation注解标识接口名称，如：@ApiOperation("接口备注")
- rest接口有3种权限：公开、登录、权限。客户端启动之后自动注册接口到服务端默认为公开所有人都可以访问的权限。改为登录接口之后，访问的权限就必须登录之后才可以访问。需要权限的接口级别最高必须在权限里面给角色配置了权限才可以访问。

## 四、<span id = "jsjjs">脚手架介绍</span>

```
// 命令
webc -h // 查看命令帮助
webc -V // 查看版本号
webc add // 增加模版
webc list // 查看所有的模版
webc init webcenter sample  // 通过webcenter模版创建一个名为sample的项目
webc boot // 快速创建一个springBoot项目
```

- webc add: 可以自己写前端的脚手架。会替换package.json里面用户信息描述等。
- webc boot: 创建的是一个固定的格式，包含springBoot和Vue前端的代码。vue代码在resources/webpage里面。在本地开发的时候可以启动两个端口如：8001后端代码，8080前端代码。后面打包的时候在webpage目录下运行npm run build就会把前端的静态代码打包到resources/public里面，在项目根目录下运行maven clean install就可以把前端代码和后端代码打成一个包，开发的时候可以是前后端分离的开发模式，打包之后就是一个jar包，简化了前后端分离的nginx配置。如果需要前后端分离部署的话，只需要把resources/public里面的静态文件拷贝到其他目录下面，nginx指定路径就可以了。

## 五、结构

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

### 4.3 Vue项目引入的主要插件。

| 名称 | 介绍 | 版本 |地址|
| --- | --- | --- | --- |
| element-ui | 饿了么后端UI框架 | 2.8.2 |https://element.eleme.cn/2.8/#/zh-CN/component/installation|
|fortawesome | 图标库 | 5.13 |http://www.fontawesome.com.cn/faicons/|
|vue-router | 路由 | 3.0.7 |https://cn.vuejs.org/v2/guide/routing.html|
|vuex | 状态管理 | 3.3.0 |https://vuex.vuejs.org/zh/|
|axios | HTTP库|0.19.2 |http://www.axios-js.com/zh-cn/docs/|

### 4.4 Vue结构

```
├── build  // 构建
├── config  // 配置文件
├── index.html  // 入口文件
├── node_modules  // 依赖下载的包
├── package.json // 依赖
├── src 
 |  ├── App.vue  // 入口
 |  ├── assets  // 静态文件
 |  ├── components  // 组件
 |  ├── main.js  // 入口js文件
 |  ├── router  // 路由
 |  ├── store  // 状态状态管理
 |  ├── utils  // 工具
 |   └── views  // 视图，需要开发的代码位置
└── static  // 静态文件
```

## 六、问答

1. SpringBoot项目依赖了webcenter-client.jar后集成了那些功能？
    答:  提供了单点登录、动态菜单、动态权限功能。
2. 前端Vue通过脚手架初始化之后有那些功能？
    答：实现了动态路由，菜单直接在服务端的菜单管理里面进行配置。
3. 客服端与服务端之间如何通信？
    答: 之间的通信通过Redis的异步消息队列实现。
    
## 七、参考
    
- 动态路由：https://github.com/renrenio/renren-fast-vue
- 脚手架开发：https://juejin.im/post/5c94fef7f265da60fd0c15e8
- Maven上传jar包: https://www.sojson.com/blog/250.html