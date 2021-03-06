Tiny Framework
=========================
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.tinygroup/tiny/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.tinygroup/tiny)


![tiny](http://git.oschina.net/uploads/images/2016/1216/171626_4ebfd8ce_97800.png "tiny-logo")

##项目简介
值得拥有的企业级j2ee应用开发框架套件，专业团队开发，完整的生态体系，活跃的社区氛围，无限的水平扩展能力，7*24不间断运维能力。
![tiny框架演变](http://git.oschina.net/uploads/images/2017/0110/183041_663e9023_97800.png "tiny框架演变")
##命名由来
Tiny的本意是微不足道的意思，Tiny的构建者认为：

1. J2EE开发框架是非常复杂的，只有把框架分解成非常细小、可控的部分，并且对每个细小、可控的部分都有一个最优解或相对最优解， 那么整个方案也就可以非常不错的落地。
2. 再好的框架也有过时的时候，再优秀的架构师也有视野盲区，再NB的写手也无法实现所有的部分。
3. 只有建立一个良好的软件生态圈，让广大开发者使用者都在生态圈在找到自己的位置，才是有生命的模式。

##设计理念
1. 使用灵活，可以整个使用，也可以只用其一个或几个部分
2. 学习成本低，上手容易
3. 核心的稳定性，核心部分使用尽量少的第三方框架及包
4. 方便的外延性，不影响对第三方框架的使用
5. 现有资产的可延续性，不管以前有什么软件资产，只要愿意，都可以方便的集成、复用
6. 易于知识积累，真正做到越用越强
7. 易于集群与水平扩展，能做到不间断提供服务


##设计原则
1. 约定优于配置
   通过约定减少代码及配置量
2. 减法原则
   从技术经理、技术骨干到开发人员，工作量范围与内容越来越少
3. 下级服从上级原则
   开发人员无条件服从技术骨干，技术骨干无条件服从技术经理
4. 自动组装原则
   复用资产，由框架进行自动组装，避免大量的系统配置
5. DRY原则
   让各个软件相关参与者不要做重复的事情
6. 配置集中原则
7. 模块化原则

##项目目录简介

	├── codegen      //代码生成器相关,包括文档生成和代码生成
	├── db           //数据库相关
	├── ext          //扩展实现目录，目前缓存扩展实现全在该目录，包括jcscache、ehcache、rediscache
	├── fc           //逻辑流程组件目录
	├── framework    //框架核心目录，Tiny项目最关键的组件在该目录，如cepcore、context、service等
	├── htmlparser   //HTML解析
	├── mgt          //服务组件目录
	├── pfc          //页面流程组件目录
	├── sample       //示例目录，包含hello四个示例
	├── studioext    //Eclipse组件目录，开发人员可以配置依赖在Eclipse插件中调用相关组件。
	├── template     //模板语言目录，包含模板引擎及扩展
	├── tinyflow     //流程引擎
	├── vfs          //虚拟文件系统，为各类文件系统提供了一个统一操作接口
	├── web          //web层开发相关
	├── xmlparser    //XML语言解析

##引用或借鉴的第三方组件
* Apache Commons工具类
* Spring框架
* ASM字节码工具
* 谷歌的guaua
* JSON工具，如阿里巴巴的fastjson
* 数据库驱动，如mysql、derby
* xstream
* jstl
* log4j
* jsqlparser
* webx
* mvel

##捐助
项目的发展离不开你的支持，请作者喝杯咖啡吧！

![支付宝捐助二维码](http://git.oschina.net/uploads/images/2016/0512/100018_5efd5e7c_97800.png "支付宝")
![微信捐助二维码](http://git.oschina.net/uploads/images/2016/0512/100855_ff8c0b55_97800.png "微信")