<p align="center">
  <a href="https://gitee.com/qingyu-mo/mybatis-plus-extra/" target="_blank">
   <img alt="Mybatis-Plus-Join-Logo" src="https://mybatisplusjoin.com/lg.png">
  </a>
</p>
<h1 align="center">MyBatis-Plus-Extra</h1>
<p align="center">
	<strong>🍬 Lazy Gospel, further enhance the function of mybatis plus. If you think it's good, just click on it ⭐ Please support it (☆▽☆).</strong>
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

[**🌎Chinese Documentation**](README.md)

-------------------------------------------------------------------------------

## 📚简介

`MyBatis-Plus-Extra`On the basis of mybatis plus, we have extended it by adding multi table queries, function queries (such as sum), and subqueries,
JSON field query (to be improved, currently there are only two methods). At the same time, it also adds true batch insertion and batch updates.

-------------------------------------------------------------------------------

## 📦使用

### 🍊Maven
Add the following content to the dependencies of pom.xml in the project:

```xml
<dependency>
    <groupId>io.gitee.qingyu-mo</groupId>
    <artifactId>mybatis-plus-extra</artifactId>
    <version>1.0.7.5</version>
</dependency>
```

### ⚙️Add plugins
#### spring boot Annotation method:

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

#### spring xml Annotation method:
```xml
<bean class="com.qingyu.mo.mybatisplus.interceptor.MybatisPlusJoinInterceptor" id="mybatisPlusJoinInterceptor"/>

<bean class="com.qingyu.mo.mybatisplus.injector.DefaultSqlInjectorPlus" id="mySqlInjector"/>
```
### 🔖inherit
* mapper extend BaseMapperPlus (required)
* service extend IServicePlus (optional)
* serviceImpl extend ServiceImplPlus (optional)

### 🔖Example
#### query:
```java
xxxService.joinList(JoinWrappers.<A>lambdaQuery()
    .leftJoin(A::getId, B::getParentId)
    .jSelect() // Query all attributes of the join, default not to check
    .eq(A::getId, 1L)
    .jEq(B::getParentId, 1L) // When the main body is a linked table instead of a main table, simply use the original mybatis-plus method with j
    .jOrderByAsc(B::getSort)
    ...
);

// Pagination Query
xxxMapper.joinSelectPage(IPage<B> page, JoinWrappers.<B>lambdaQuery()
    .sum(B::getPrice) // query sum(price)
    .rightJoin(B::getParentId, A::getId)
    .joinOn(i->i.jEq(A::getXXX, xxx)) // When a linked table query has more than one condition
    .innerJoin(B::getId, C::getId)
    .joinOn(i->i.jEq(C::getXXX, xxx)) // Write the join table you want to join after its join statement
        .jChildSelect(D.class, JoinWrappers.<D>lambdaQuery().jEq(xx,xx), alias) // The alias value comes from a subquery
    .sumEq(B::getPrice, 1) // Query records where the total price of B equals 1
    .groupBy(B::getXX)
    ...
);
```
#### batchInsert:
```java
xxxMapper.batchInsert(list); // Covering the method of mybatis-plus, achieving true batch insertion

xxxService.saveBatch(list); // Covering the method of mybatis-plus, achieving true batch insertion
```

#### batchUpdate:
```java
xxxMapper.batchUpdateById(list);
xxxService.updateBatchById(list); // Covering the method of mybatis-plus, achieving true batch update

xxxMapper.batchUpdateByIdWithNull(list);
xxxService.updateBatchByIdWithNull(list); // Mybatis plus does not update null values by default, and if you want to use it, you can only match a certain field to keep updating null values
//This method, combined with annotation @ Updatenull, can achieve the specified property to be updated to a null value when needed
```

#### delete:
```java
xxxMapper.PhysicalDelete(JoinWrappers...);

xxxService.physicalRemove(JoinWrappers...); // Used logical deletion but wanted to use physical deletion
```

#### query:
```java
xxxMapper.joinSelectDeletedList(JoinWrappers...);

xxxService.joinDeletedList(JoinWrappers...); // Used logical deletion but wanted to query records that have already been logically deleted
```

### 🔖annotate
#### IgnoreInsert: Ignoring this annotation attribute during batch insertion
#### IgnoreUpdate: Ignoring this annotation attribute during batch updates
#### UpdateNull: When batch updating, indicate that this property can be updated to a null value

-------------------------------------------------------------------------------

## 🏗️Add bricks and tiles

### 🎋Branch Description

Two branches, with the following functions:

| branch     | role                                                          |
|--------|---------------------------------------------------------------|
| master | The main branch, the branch used in the release version, is consistent with the jar submitted by the central repository and does not accept any PR or modifications |
| develop | Development branch, defaults to the next version of SNAPSHOT, accepts modifications or PR                 |

### 🐞Provide bug feedback or suggestions

Please provide feedback on the JDK version, mybatis plus version, and related dependency library versions being used.

- [Gitee issue](https://gitee.com/qingyu-mo/mybatis-plus-extra/issues)

### 🧬Steps to contribute code

1. Fork projects on Gitee to your own repo.
2. Clone forked past projects, which are also your projects, to your local location.
3. Modify the code (remember to make sure to modify the develop branch).
4. Push to your own library (development branch) after commit.
5. Log in to Gitee and you will see a pull request button on your homepage. Click on it, fill in some explanatory information, and then submit it.
6. Waiting for maintainer to merge.

### 📐Principles followed by PR

Everyone is welcome to contribute their work and code, but the maintainer is an OCD patient. In order to take care of the patient, 
it is necessary to submit a PR (pull request) that meets some standards. The standards are as follows:

1. The annotations are complete, especially for each newly added method, which should be marked with method description, 
parameter description, return value description, and other information in accordance with Java documentation specifications. If desired, your name can also be added.
2. If we close your issue or PR, please don't be surprised. This is one way for us to keep the problem handling clean. 
You can still continue the discussion, and we will reopen it when there are discussion results.