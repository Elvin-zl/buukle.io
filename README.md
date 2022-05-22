# buukle : generator-plus

#### 介绍

布壳儿脚手架服务

原型管理 : 集中管理自定义的archetype ,并能一键在线直接根据archetype生成项目代码

逆向管理 : 可自由灵活配置多数据源,自定义模板,并根据模板一键在线生成前后端代码,支持一键导入本地项目结构

#### 演示地址
[generator-plus](http://generator-plus.buukle.top/generator-plus/index.html#/gen/configures/configures-list)

测试账号 : buukle / buukle 

#### 使用说明

0.  工作台

![工作台](https://images.gitee.com/uploads/images/2021/0906/003210_c6cebb5c_1694096.png "屏幕截图.png")

一. 原型管理

主要根据项目(服务)骨架文件, 生成基础的项目代码

1. 新建archetype

先在本地生成 archetype.jar 文件 教程参考 : https://www.jianshu.com/p/9ad32c78ef9f ,然后上传 .jar 文件到本模块:

![输入图片说明](https://images.gitee.com/uploads/images/2021/0917/221914_a62f312e_1694096.png "屏幕截图.png")

1.1 生成代码框架


在新建完的archetype 列表页操作栏点击生成,在弹出模态框中,设置 artifactId,groupId,version等信息,点击底部执行,即可生成代码

![输入图片说明](https://images.gitee.com/uploads/images/2021/0917/222143_f080bcd9_1694096.png "屏幕截图.png")

1.2 下载代码

在[原型日志] 菜单下,能够下载到最新生成的代码

![输入图片说明](https://images.gitee.com/uploads/images/2021/0917/222313_ba4f1f51_1694096.png "屏幕截图.png")

解压后即可看到生成的代码

![输入图片说明](https://images.gitee.com/uploads/images/2021/0917/222434_466419c5_1694096.png "屏幕截图.png")

用idea打开

![输入图片说明](https://images.gitee.com/uploads/images/2021/0917/222805_08d1d784_1694096.png "屏幕截图.png")

二. 逆向管理

在现有的项目基础上,根据数据库领域模型设计,逆向生成 服务端代码和前端代码

1.  新建连接

![新建并测试连接](https://images.gitee.com/uploads/images/2021/0906/003303_615b33f5_1694096.png "屏幕截图.png")

2.  新建模板

2.1 新建

![新建模板](https://images.gitee.com/uploads/images/2021/0906/003333_5eb8873f_1694096.png "屏幕截图.png")

2.2 文件名模式

支持灵活的自定义生成后的文件名与表名,模板名的关系

![输入图片说明](https://images.gitee.com/uploads/images/2021/0917/222953_2ddefe24_1694096.png "屏幕截图.png")

2.3 表名追加

![表名追加](https://images.gitee.com/uploads/images/2021/0907/232113_edef3727_1694096.png "屏幕截图.png")

3.  新建配置

在配置记录中,选择需要生成的连接,表清单,以及需要生成的模板清单,按照提示进行配置包名等表单项

![输入图片说明](https://images.gitee.com/uploads/images/2021/0906/003354_0ef81c4a_1694096.png "屏幕截图.png")

4.  生成代码

点击生成,会按照配置记录中的信息进行生成代码,并打成压缩包,根据配置的一键导入脚本生成.bat文件,也会打进压缩包中

![输入图片说明](https://images.gitee.com/uploads/images/2021/0906/003417_dba6602d_1694096.png "屏幕截图.png")

5.  下载代码

![输入图片说明](https://images.gitee.com/uploads/images/2021/0906/003449_c4766ded_1694096.png "屏幕截图.png")

6.  一键导入

将下载下来的zip源码压缩包解压,双击执行copy.bat脚本,将代码一键导入本地项目下 (暂时只支持windows版本的脚本,mac可以自行修改该脚本内容及文件后缀名)

#### 环境

java1.8

#### 软件架构

backend : springboot 2.5.4.RELEASE + mysql5.7以上 + beetl + mybatis-plus

frontend :  Antd ( ant-design-vue-pro)

#### 启动教程

0. 环境 jre
1. 到 https://gitee.com/buukle/generator-plus/releases/ 前后端聚合版本列表根据特性挑选一个,下载压缩包(zip,tar.gz),解压为jar包
2. 启动 : nohup java -jar generator-plus-web-x.x-RELEASE.jar >> /opt/logs/generator-plus/generator.log 2>&1 &
3. 访问 : http://127.0.0.1/generator-plus/index.html
4. 本地化\
   ① 执行ddl 
     位置 : ./generator-plus/sql/generator-plus.sql
   ② java -jar generator-plus-web-0.0.1-alpha.jar --spring.config.location=C:/application.yml 挂载外部配置启动\
   示例 application.yml 文件(请将数据源部分修改为你的自有数据源地址),并放到配置挂载目录下
````
##服务配置
server:
  port: 80
  tomcat:
    uri-encoding: UTF-8
    threads:
      max: 200
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      force: true
      enabled: true
##日志配置
logging:
  config: classpath:config/spring/springBoot-logbak-prod.xml
##spring生态配置
spring:
  ##应用名
  application:
    name: buukle-generator
  devtools:
    add-properties: false
  ##thymeleaf
  thymeleaf:
    cache: false
    prefix: classpath:/templates/generator/
    suffix: .html
    encoding: UTF-8
    mode: HTML5
  ##datasource
  datasource:
    url: jdbc:mysql://mysql.buukle.top:13306/generator-plus?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: ******
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    # 初始化，最小，最大连接数
    initialSize: 3
    minIdle: 3
    maxActive: 18
    # 获取数据库连接等待的超时时间
    maxWait: 60000
    # 配置监控统计拦截的filters,去掉后，监控界面的sql无法统计
    filters: stat,wall,log4j
  #支持multipart file
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 100MB
# mybatis-plus相关配置
mybatis-plus:
  # xml扫描，多个目录用逗号或者分号分隔（告诉 Mapper 所对应的 XML 文件位置）
  mapper-locations: classpath*:/config/mapper/*.xml
  # 以下配置均有默认值,可以不设置
  global-config:
    db-config:
      #主键类型 AUTO:"数据库ID自增" INPUT:"用户输入ID",ID_WORKER:"全局唯一ID (数字类型唯一ID)", UUID:"全局唯一ID UUID";
      id-type: auto
  configuration:
    # 是否开启自动驼峰命名规则映射:从数据库列名到Java属性驼峰命名的类似映射
    map-underscore-to-camel-case: true

````


#### 源码开发
##### (1) 后端
0. 执行ddl 位置 : ./generator-plus/sql/generator-plus.sql
1. 克隆前端整合分支 : git clone -b all-in-one https://gitee.com/buukle/generator-plus.git  
2. 更改本地maven settings 为 本项目根路径下的 settings.xml(使用自带的settings.xml也行,请确保中央仓库的连通性,本项目所有依赖均可从中央仓库或 阿里云镜像仓库拉取)
3. mvn clean
4. mvn install
5. 执行主类 GeneratorApplication main 方法即可成功运行,启动完成后, **访问 http://127.0.0.1/generator-plus/index.html 即可进入主界面**
6. 表模型中预留了用户,租户等字段,开源版本未接入用户中心,认证中心,权限体系和应用体系,使用固定静态账号密码 (测试账号 : admin / admin)登录,如有需要,可根据源码进行企业内部认证源体系对接
7. 如果想本地化数据源,请连接代码中的数据库,导出数据表信息到本地库,并修改application-dev.yaml application-dev.yaml 下的数据源信息为你本地库,或者公司库地址
8. **如果想使用本地settings,请确保中央仓库的连通性,本项目所有依赖均可从中央仓库或 阿里云镜像仓库拉取**
##### (2) 前端

1. 请clone generator-plus-front 仓库master分支进行开发,开发好后,将dist包下的文件(不包括dist文件夹)放入后端的 generator-plus-web/src/main/resources/static 目录下

#### 参与贡献

邮箱 : 18001268330@163.com   
微信 : 18001268330
