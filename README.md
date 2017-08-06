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

- [Java 动手写爬虫: 一、实现一个最简单爬虫](https://blog.zbang.online/articles/2017/07/05/1499239054423.html)
- [Java 动手写爬虫: 二、深度爬取](https://blog.zbang.online/articles/2017/07/05/1499239349163.html)
- [Java 动手写爬虫: 三、爬取队列](https://blog.zbang.online/articles/2017/07/07/1499401540323.html)
- [Java 动手写爬虫: 四、日志埋点输出 & 动态配置支持](https://blog.zbang.online/articles/2017/07/27/1501130050920.html)
- [Java 动手写爬虫: 五 对象池](https://blog.zbang.online/articles/2017/08/06/1502027484605.html)
