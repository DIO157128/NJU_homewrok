# checkstyle警告说明

| 日期      | 修改者 | 内容梗概                                 |
| --------- | ------ | ---------------------------------------- |
| 2023.3.31 | 郑启睿 | 创建文档，编写“运行”和“警告详情”中的内容 |
| 2023.3.31 | 邢俊杰 | 添加“警告详情”中的11，12，13             |

本文档为组内人员进行checkstyle警告修复的指导手册，仅供组内人员使用，**并非课程要求的checkstyle分析报告，请助教和老师不要认错了，课程要求的分析报告名为Report_checkstyle_X**

## 运行

下载checkstyle-9.3-all.jar，并在cmd中运行

```
java -jar checkstyle-9.3-all.jar -c /sun_checks.xml -f xml -o 项目代码分析/分析报告/checkstyle/checkstyle_errors_1.xml src/main/java/uk/ac/wlv
```

## 警告详情

对于警告需要有所鉴别，总结其出错原因和修改的准则
**原则**：少改动代码，处理时候不盲目大意，git提交多写点改动的情况。

### 需要解决的问题

1. 注解问题
   缺少注解，点击问题即可跳转到缺少注解的地方

 ![image-20230331184216268](./asserts/image-20230331184216268.png)

注解不全，补齐就行，每一个@的值都要完整

![image-20230331190346162](./asserts/image-20230331190346162.png)例如：@return后面要有东西，不完整的return是不行的

2. 空格问题

![image-20230331184600779](./asserts/image-20230331184600779.png)![image-20230331185247669](./asserts/image-20230331185247669.png)

符号前后，for/if/while/强制类型转换/||/&&/switch之后都要有空格，;前不需要空格

3. 魔术数字

   ![image-20230331185126274](./asserts/image-20230331185126274.png)魔术数字的意思是莫名其妙、不知道怎么来的数字，需要一些注释进行解释，解释过后可以无视

4. 函数的括号![image-20230331190031838](./asserts/image-20230331190031838.png)

   函数{                                 而不是函数

   ​	；												{

   }													  ；

   ​														}

5. 命名问题![image-20230331190728171](./asserts/image-20230331190728171.png)要遵守命名规则（这条报错应该是因为在方法名中使用了_而不是驼峰）

6. 数组定义格式问题

   ![img](./asserts/wps1.jpg) 

   只需要将括号移至前面即可![img](./asserts/wps2.jpg)

    更正后：

   ![img](./asserts/wps3.jpg)

7. 无用导入的包可以直接删除

   ![img](./asserts/wps4.jpg)

8.  工具类没有必要添加public

   ![img](./asserts/wps5.jpg) 





### 可以忽略的问题

1. ![image-20230331184737977](./asserts/image-20230331184737977.png)
2. ![image-20230331190012386](./asserts/image-20230331190012386.png)

### 需要斟酌的问题

1. ![image-20230331184837502](./asserts/image-20230331184837502.png)

2. ![image-20230331185547524](./asserts/image-20230331185547524.png)

   检查局部变量或参数是否会遮蔽在相同类中定义的字段。

3. ![image-20230331190254068](./asserts/image-20230331190254068.png)

### 命名规则

常量驼峰命名，类名首字母大写，常量全大写（待补充与修改）

参考：[(8条消息) 最全详解CheckStyle的检查规则_ncss checkstyle_javaRoger的博客-CSDN博客](https://blog.csdn.net/rogerjava/article/details/119322285)

