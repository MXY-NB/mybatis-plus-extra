<p align="center">
  <a href="https://gitee.com/qingyu-mo/mybatis-plus-extra/" target="_blank">
   <img alt="Mybatis-Plus-Join-Logo" src="https://mybatisplusjoin.com/lg.png">
  </a>
</p>
<h1 align="center">MyBatis-Plus-Extra</h1>
<p align="center">
	<strong>🍬懒人福音，进一步增强mybatis-plus的功能，觉得不错就点个⭐支持一下吧 (☆▽☆)。</strong>
</p>

<p align="center">
	<a target="_blank" href="https://central.sonatype.com/artifact/io.gitee.qingyu-mo/mybatis-plus-extra">
		<img src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fs01.oss.sonatype.org%2Fservice%2Flocal%2Frepo_groups%2Fpublic%2Fcontent%2Fio%2Fgitee%2Fqingyu-mo%2Fmybatis-plus-extra%2Fmaven-metadata.xml&label=Maven%20Central
" />
	</a>
	<a target="_blank" href="https://license.coscl.org.cn/MulanPSL2">
		<img src="https://img.shields.io/:license-MulanPSL2-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href='https://gitee.com/qingyu-mo/mybatis-plus-extra/stargazers'>
		<img src='https://gitee.com/qingyu-mo/mybatis-plus-extra/badge/star.svg?theme=gvp' alt='star'/>
	</a>
</p>

-------------------------------------------------------------------------------

[**🌎English Documentation**](README-EN.md)

-------------------------------------------------------------------------------

## 📚简介

`MyBatis-Plus-Extra`在mybatis-plus的基础上进行扩展，增加了多表查询，函数查询(例如：sum)，子查询，
json字段查询(有待完善，现在只有两种方法)。同时也增加了真正的批量插入，批量更新。

-------------------------------------------------------------------------------

## 📦使用

### 🍊Maven
在项目的pom.xml的dependencies中加入以下内容:

```xml
<dependency>
    <groupId>io.gitee.qingyu-mo</groupId>
    <artifactId>mybatis-plus-extra</artifactId>
    <version>1.0.7.5</version>
</dependency>
```

### ⚙️添加插件
#### spring boot 注解方式:

```jave
@Bean
public MybatisPlusJoinInterceptor mybatisPlusJoinInterceptor() {
    return new MybatisPlusJoinInterceptor(new JoinInterceptor());
}

@Bean
public DefaultSqlInjectorPlus mySqlInjector(){
    return new DefaultSqlInjectorPlus();
}
```

#### spring xml 注解方式:
```xml
<bean class="com.qingyu.mo.mybatisplus.interceptor.MybatisPlusJoinInterceptor" id="mybatisPlusJoinInterceptor"/>

<bean class="com.qingyu.mo.mybatisplus.injector.DefaultSqlInjectorPlus" id="mySqlInjector"/>
```
### 🔖继承
* mapper继承BaseMapperPlus (必选)
* service继承IServicePlus (可选)
* serviceImpl继承ServiceImplPlus (可选)

### 🔖示例
#### 查询:
```java
xxxService.joinList(JoinWrappers.<A>lambdaQuery()
    .leftJoin(A::getId, B::getParentId)
    .jSelect() // 查询join的全部属性，默认不查
    .eq(A::getId, 1L)
    .jEq(B::getParentId, 1L) // 主体为联表而不是主表时，只要使用原本mybatis-plus方法带j即可
    .jOrderByAsc(B::getSort)
    ...
);

// 分页查询
xxxMapper.joinSelectPage(IPage<B> page, JoinWrappers.<B>lambdaQuery()
    .sum(B::getPrice) // 查询sum(price)
    .rightJoin(B::getParentId, A::getId)
    .joinOn(i->i.jEq(A::getXXX, xxx)) // 当联表查询不止一个条件时
    .innerJoin(B::getId, C::getId)
    .joinOn(i->i.jEq(C::getXXX, xxx)) // 想joinOn哪个联表就写在其连接语句后面
    .jChildSelect(D.class, JoinWrappers.<D>lambdaQuery().jEq(xx,xx), alias) // alias值来自于一个子查询
    .sumEq(B::getPrice, 1) // 查询B价格总和等于1的记录
    .groupBy(B::getXX)
    ...
);
```
#### 批量新增:
```java
xxxMapper.batchInsert(list); // 覆盖了mybatis-plus的方法,实现真正的批量插入

xxxService.saveBatch(list); // 覆盖了mybatis-plus的方法,实现真正的批量插入
```

#### 批量更新:
```java
xxxMapper.batchUpdateById(list);
xxxService.updateBatchById(list); // 覆盖了mybatis-plus的方法,实现真正的批量更新

xxxMapper.batchUpdateByIdWithNull(list);
xxxService.updateBatchByIdWithNull(list); // mybatis-plus默认不更新null值，想用又只能配某个字段一直可以更新null值
// 此方法配合注解 @UpdateNull 即可实现指定属性在需要时可以更新为null值
```

#### 删除:
```java
xxxMapper.PhysicalDelete(JoinWrappers...);

xxxService.physicalRemove(JoinWrappers...); // 使用了逻辑删除但又想用物理删除
```

#### 查询:
```java
xxxMapper.joinSelectDeletedList(JoinWrappers...);

xxxService.joinDeletedList(JoinWrappers...); // 使用了逻辑删除但又想查询已经被逻辑删除了的记录
```

### 🔖注解
#### IgnoreInsert: 批量插入时忽略此标注属性
#### IgnoreUpdate: 批量更新时忽略此标注属性
#### UpdateNull: 批量更新时标注此属性可以更新为null值

-------------------------------------------------------------------------------

## 🏗️添砖加瓦

### 🎋分支说明

两个分支，功能如下：

| 分支     | 作用                                                          |
|--------|---------------------------------------------------------------|
| master | 主分支，release版本使用的分支，与中央库提交的jar一致，不接收任何pr或修改 |
| develop | 开发分支，默认为下个版本的SNAPSHOT版本，接受修改或pr                 |

### 🐞提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本、mybatis-plus版本和相关依赖库版本。

- [Gitee issue](https://gitee.com/qingyu-mo/mybatis-plus-extra/issues)

### 🧬贡献代码的步骤

1. 在Gitee上fork项目到自己的repo。
2. 把fork过去的项目也就是你的项目clone到你的本地。
3. 修改代码（记得一定要修改develop分支）。
4. commit后push到自己的库（develop分支）。
5. 登录Gitee在你首页可以看到一个 pull request 按钮，点击它，填写一些说明信息，然后提交即可。
6. 等待维护者合并。

### 📐PR遵照的原则

欢迎任何人添砖加瓦，贡献代码，不过维护者是一个强迫症患者，为了照顾病人，需要提交的pr（pull request）符合一些规范，规范如下：

1. 注释完备，尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，如果愿意，也可以加上你的大名。
2. 我们如果关闭了你的issue或pr，请不要诧异，这是我们保持问题处理整洁的一种方式，你依旧可以继续讨论，当有讨论结果时我们会重新打开。
