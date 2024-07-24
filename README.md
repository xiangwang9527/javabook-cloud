这是《Java深度探索：开发基础、高级技术与工程实践》的第二篇《应用技术篇》的配套源代码。

《应用技术篇》包括第8章支付系统设计模式，第9章认证与权限，第10章可靠消息最终一致性，第11章用Netty实现物联网，第12章规则引擎与风控系统，第13章Nginx与OpenResty，共六章。

- 第8章讲解了针对业务、产品、技术、运营部门先后提出的数次需求改进，笔者在支付系统中尝试使用了若干种设计模式，以应对和拥抱这种变化，提升开发效率。所用到的设计模式包括策略模式、构造器模式、模板方法模式、外观模式、状态模式、备忘录模式等。阅读这一章前需要读者先具备一些设计模式的相关知识，这样理解起来会更容易一些。

- 第9章讲解了一般应用系统中后台管理的权限设计问题。从借鉴Spring Security框架拦截机制开始，到实现自定义的RBAC权限系统，让读者知晓权限设计的关键技术点有哪些。接着介绍并实现当前流行的OAuth 2.0开放授权机制，并以Github代替微信实现了OAuth 2.0的第三方登录。同时还展示了如何接受其他应用的申请，成为“第三方”。之后介绍了动态令牌的原理，并将之与OAuth 2.0结合。

- 第10章讲解了分布式事务的“521”知识：五大解决方案、两大定理和一个框架，对它们进行了较为详细的说明和比较后，最终基于Spring Cloud Alibaba实现了可靠消息最终一致性方案。通过对代码的讲解，清楚地阐明了该方案的关键点在于保存并发送事务消息和实现事务消息轮询。

- 第11章讲解了支撑IoT（Internet of Things，物联网）发展的核心技术——Netty及其背后的RPC机制，演示了XML-RPC、JSON-RPC和gRPC这三种不同的RPC实现，最后再以一个实际案例，向读者展示了如何通过Netty及自定义的通信协议，来完成物联网服务端与终端之间的双向通信和数据采集。

- 第12章讲解了规则引擎Drools是如何嵌入在应用程序中，通过使用预定义的语义模块编写业务决策，接受数据输入，解释业务规则，然后以此来实现业务约束，抵御各类薅羊毛的高风险行为的，同时也介绍了另外两种轻量级的规则引擎Groovy和Aviator。然后在此基础上，介绍了基于大数据实施风控的Clickhouse和Flink，尤其是指标采样存储、行为序列聚合和模式事件匹配这三种核心技术。

- 第13章讲解了轻量级、高性能的HTTP和反向代理服务器Nginx，以及它能实现的各种流量控制功能，包括负载均衡、流量限制、流量拷贝等。借助于Nginx和OpenResty的LUA脚本，OpenResty把Nginx“升级”为一个强大的Web应用服务器，并通过其协程特性，能轻松构建出处理上万并发请求的超高性能Web应用。更能随心所欲地制定复杂的访问控制、业务处理和安全检测规则。

其中：

- javabook-core：是所有其他子模块都需要依赖的子模块；

- javabook-application：对应《第8章 支付系统的设计模式》和《第9章 认证与授权》的源码；

- javabook-transaction：对应《第10章 可靠消息最终一致性》的源码；

- javabook-iotandnetty：对应《第11章 用Netty实现物联网》的源码；

- javabook-riskcontrol：对应《第12章 规则引擎与风控系统》的源码；

- javabook-nginxlua：对应《第13章 Nginx与OpenResty》的源码。