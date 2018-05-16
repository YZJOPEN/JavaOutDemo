# 外出登记Demo轻应用开发指南

## 一. 前期准备

### 1. 开发环境准备

1. 开发工具：Eclipse + jdk1.8 + Tomcat 8.5

2. 开发语言：Java + Html5 + Vue.js

3. 开发框架：Sping + SpringMVC + Mybatis

4. 数据库   ：Mysql

5. 测试工具：postman, Fiddler

### 2. 轻应用准备

参考轻应用创建指南 https://open.yunzhijia.com/gitbook-wiki/self-build-app/appCreateGuide.html ，获取appId和appSecret

## 二. Demo本地部署过程

### 1. 下载demo

从提供的github地址pull工程代码或者直接download

![demo下载](https://i.imgur.com/dDsyyCu.png)

### 2.新建web工程 

在eclipse或者idea编辑器下，新建web工程（示例使用eclipse）

![工程新建](https://i.imgur.com/Qz4fwWK.png)

![工程新建](https://i.imgur.com/y7fwtd5.png)

### 3.替换文件夹 

Copy第一步下载的JavaOutDemo文件夹下的文件替代新建工程的webRoot（或者webContent）和 src文件夹

![文件替换](https://i.imgur.com/50N9TqI.png)

### 4. 修改appconfig.properties文件

修改以下参数：
1. 服务器IP及PORT
2. 数据库服务器IP
3. 自己新建的轻应用的appSecret
4. 云之家管理中心通讯录秘钥
5. 数据库用户名和密码

![参数修改](https://i.imgur.com/eXRx0SS.png)

### 5.部署项目

部署项目到tomcat服务器

![项目部署](https://i.imgur.com/8rYWxLI.png)


### 6. 项目部署成功界面

![部署成功](https://i.imgur.com/36PTpjL.png)

## 三. 工程架构

### 1. 项目工程目录结构

![目录结构](https://i.imgur.com/pk34UqQ.png)    

### 2. 核心配置文件介绍

1. appconfig.properties ：轻应用相关参数配置，数据库配置
2. spring-core.xml : spring核心配置文件
3. spring-mvc.xml : springMvc配置文件
4. spring-mybatis.xml : mybatis配置文件
5. log4j.properties : 日志

## 四. 外出登记功能介绍

### 1. 权限查看
### 2. 全局搜索
### 3. 外出登记
### 4. 办事签到
### 5. 登记撤回
### 6. 转发分享
### 7. 其他
