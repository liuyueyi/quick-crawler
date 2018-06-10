# quick-crawler
> java实现的爬虫框架

从头开始， 一步一步的实现一个可用的爬虫框架，每个地方加一个里程碑的tag，主要用于记录这个工程的诞生过程



## tag 记录列表

### 1. [v0.001](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.001)

> 实现了一个最简单，最基础的爬虫, 处于能用的阶段


### 2. [v0.002](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.002)

> 利用HttpClient来替代jdk的http请求；新增http参数配置


### 3. [v0.003](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.003)

> 实现深度爬网页

- 支持正向、逆向链接过滤
- 在内存中保存爬取记录，用于去重过滤
- 提供爬取完成后的回调方法，用于结果处理


### 4. [v0.004](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.004)

> 实现爬取队列

- 每个Job只执行当前网页的抓取，将网页中满足深度抓取的链接塞入队列
- 新增Fetcher类，用于控制抓去任务


### 5. [v0.005](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.005)

> 实现Job任务中爬取 + 结果解析的分离； 完成任务结束的标识设定

- 新增 ResultFilter 实现爬取网页的分析， 并将满足条件的链接塞入爬取队列
- 新增 JobCount 来记录任务的爬取完成数， 以此完成整个任务的结束标识设定


### 6. [v0.006](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.006)

> 添加日志埋点


### 7. [v.0.007](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.007)

> 新增动态配置信息支持

- 采用配置文件方式，支持配置信息的动态变更

### 8. [v.0.008](https://github.com/liuyueyi/quick-crawler/releases/tag/v0.008)

> 对象池的实现

- 采用对象池来管理Job任务的创建

## 相关博文

一灰灰Blog地址: [https://blog.hhui.top/](https://blog.hhui.top/)

所有QuickCrawel相关博文汇总: [QuickCrawel java爬虫归档](https://blog.hhui.top/hexblog/categories/%E6%8A%80%E6%9C%AF/Quick%E7%B3%BB%E5%88%97%E9%A1%B9%E7%9B%AE/QuickCrawler/)

- [Java 动手写爬虫: 一、实现一个最简单爬虫](https://blog.hhui.top/hexblog/2017/06/27/Java-%E5%8A%A8%E6%89%8B%E5%86%99%E7%88%AC%E8%99%AB-%E4%B8%80%E3%80%81%E5%AE%9E%E7%8E%B0%E4%B8%80%E4%B8%AA%E6%9C%80%E7%AE%80%E5%8D%95%E7%88%AC%E8%99%AB/)
- [Java 动手写爬虫: 二、深度爬取](https://blog.hhui.top/hexblog/2017/06/30/Java-%E5%8A%A8%E6%89%8B%E5%86%99%E7%88%AC%E8%99%AB-%E4%BA%8C%E3%80%81-%E6%B7%B1%E5%BA%A6%E7%88%AC%E5%8F%96/)
- [Java 动手写爬虫: 三、爬取队列](https://blog.hhui.top/hexblog/2017/07/07/Java-%E5%8A%A8%E6%89%8B%E5%86%99%E7%88%AC%E8%99%AB-%E4%B8%89%E3%80%81%E7%88%AC%E5%8F%96%E9%98%9F%E5%88%97/)
- [Java 动手写爬虫: 四、日志埋点输出 & 动态配置支持](https://blog.hhui.top/hexblog/2017/07/27/Java-%E5%8A%A8%E6%89%8B%E5%86%99%E7%88%AC%E8%99%AB-%E5%9B%9B%E3%80%81%E6%97%A5%E5%BF%97%E5%9F%8B%E7%82%B9%E8%BE%93%E5%87%BA-%E5%8A%A8%E6%80%81%E9%85%8D%E7%BD%AE%E6%94%AF%E6%8C%81/)
- [Java 动手写爬虫: 五 对象池](https://blog.hhui.top/hexblog/2017/08/06/Java-%E5%8A%A8%E6%89%8B%E5%86%99%E7%88%AC%E8%99%AB-%E4%BA%94-%E5%AF%B9%E8%B1%A1%E6%B1%A0/)
