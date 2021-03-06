## Cache Aside Pattern
(1) 读的时候，先走缓存，缓存没有的话，那么读取数据库，将数据库中读出的数据再写入缓存
(2)更新的时候，先删除缓存，然后再更新数据库
## 数据库、缓存双写不一致的情况
有这么一个场景，如下图所示：
![efe3d42e7d204c383934e7ff636849cb.png](en-resource://database/1965:1)
当有一个请求A，用于读取某个商品的数据量，如果redis中没有，那么读取数据库，然后更新到redis中；还有一个请求B，用于更新数据库中商品的数量。请求A和请求B并发执行，这样会出现redis中数据和数据库中数据不一致的情况。
情况1：
> 可能请求A读取数据库获取到结果时候请求B更新数据库还未完成，这样会导致请求A塞入到Redis中的数据是旧数据。

情况1有一种方法可以解决，就是请求B更新完数据库后立马将数据更新到缓存，但是这样又会出现情况2导致双写不一致。
情况2：
> 因为请求A和请求B是分为多步的，如果请求B中刷新redis的操作在请求A刷新redis之前，并且请求B读取mysql获取到结果在请求A之前，那么也会导致redis中存放的是旧数据。

- 数据库、缓存双写不一致的解决方案

以商品实体举例（商品实体有两个字段——id和count）
将（删除缓存、更新数据库）操作A和（查询数据库，覆盖缓存）操作B这两个操作串行化处理。为了不影响整体的性能，创建n和线程池，异步处理这些请求，根据id进行hash计算出对应哪个线程进行处理。这里有一个优化操作是过滤掉无用的操作B（什么时候操作A无效呢？就是在操作A之后第一个操作B之后的操作B是无效的，因为数据库中的数据为从变过，无需从新覆盖相同的内容到redis中）
![caf54a5cb535d2921253782e434a5bad.png](en-resource://database/1967:0)

相关代码见github仓库


