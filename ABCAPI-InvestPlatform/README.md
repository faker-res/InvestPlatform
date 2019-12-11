# Demo

根据本demo新建项目，需要修改的点如下：

### #1、/src/main/resources/application.yml
	spring:
	  application:
	    name: service-demo
修改此处的name，在公司所有spring boot项目中保证唯一（重点），后续内网互调都是使用此name
	
	server:
  		port: 9999
修改此处的port，在公司所有spring boot项目中保证唯一（因公司现采用docker部署，也可以不唯一，但为保证不出问题，建议唯一）

### #2、/src/pom.xml
	<groupId>la.niub.abcapi</groupId>
	<artifactId>service-demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>
修改此处的artifactId，和#1的name一致

	<name>service-demo</name>
	<description></description>
修改此处的name，和#1的name一致

### #3、/src/docker.conf
	RUN_JAR="target/service-demo-0.0.1-SNAPSHOT.jar"
修改此处的RUN_JAR，"service-demo"和#1的name一致

### #4、/src/abc_service.shf
	RUN_JAR="target/service-demo-0.0.1-SNAPSHOT.jar"
修改此处的RUN_JAR，"service-demo"和#1的name一致

### #5、la.niub.abcapi.servicedemo包名
包名中的"servicedemo"建议修改成和项目名称匹配的包名

### #6、la.niub.abcapi.servicedemo.Application.main方法
用此方法开始运行

### #7、demo url
	http://localhost:9999/index
	http://localhost:9999/timeline?stock_code=000001.SZ&start_time=0930
