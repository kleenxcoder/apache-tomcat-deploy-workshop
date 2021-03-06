# Deploying Java application to Apache Tomcat
[![Apache 2.0](https://img.shields.io/github/license/making/yavi.svg)](https://www.apache.org/licenses/LICENSE-2.0)

> The accompanying code for this workshop is [on Github](https://github.com/kleenxcoder/apache-tomcat-deploy-workshop).<br/>
This will give you a short overview how to configure server and your project to be able to use maven to deploy your source to tomcat.
<br/>

## 1 Setup
- In this workshop you'll need the latest Java version. Java 8 is the baseline for this workshop.
- You'll need a newer, 3.1, version of Apache Maven installed.
- Use ure desired IDE Apache NetBeans, Eclipse, IntelliJ IDEA, Visual Studio Code or vi
- Patch your IDE with lombok https://projectlombok.org/setup/overview
- Runnable local installation of Apache Tomcat https://tomcat.apache.org/
- Open project [apache-tomcat-deploy-workshop](code-java/apache-tomcat-deploy-workshop/) in your IDE
<br/>

## 2 Apache Tomcat configuration

### tomcat-users.xml

[sample code](config/apache-tomcat/tomcat-users.xml)

```xml
    <role rolename="manager-gui"/>  
    <role rolename="manager-script"/>   
    <user username="manager" password="password" roles="manager-gui,manager-script" />
```
<br/>

## 3 Maven setting

### settings.xml
> Usually stored in your maven installation folder or your .m2 folder. Windows user got to %UserProfile%\\.m2

[sample code](config/apache-maven/settings.xml)

#### Adding server profiles

```xml
<profiles>
	<profile>
		<id>localhost</id>
		<activation>
			<activeByDefault>true</activeByDefault>
		</activation>
		<properties>
			<tomcat-server>localhost</tomcat-server>
			<tomcat-url>http://localhost:8080/manager/text</tomcat-url>
		</properties>
	</profile>
</profiles>
```

#### Adding credentials

```xml
<servers>  
    <server>
       <id>LocalhostServer</id>
       <username>manager</username>
       <password>password</password>
    </server>
</servers>
```
<br/>

## 4 Project configuration
> To make use of tomcat7-maven-plugin we need to add the plugin to our build section.

### pom.xml

[sample code](code-java/apache-tomcat-deploy-workshop/pom.xml)

```xml
<plugin>
	<groupId>org.apache.tomcat.maven</groupId>
	<artifactId>tomcat7-maven-plugin</artifactId>
	<version>2.2</version>
	<configuration>
		<url>${tomcat-url}</url>
		<server>${tomcat-server}</server>
		<path>/${project.artifactId}</path>
	</configuration>
</plugin>
```
<br/>

## 5 Deployment to Apache Tomcat

### Manually using manager
> Assuming your java project creates a WAR file you are ready to go. If not  After changing tomcat-users.xml you are ready to go for manual deployment. Open http://localhost:8080/manager/ in your browser and use credentials stored in tomcat-users.xml to login. Got to "WAR file to deploy", select your WAR file and press the "deploy" button.

### Automated using command line
> Previously we have set our profile 'localhost' activeByDefault = true. In this case when you do not provide a profile our localhost will be used by default. We can deploy our application to localhost by using following command

```powershell
mvn tomcat7:deploy
```

> In real life you will have several servers and several passwords. In this case settings.xml comes in handy. We have one place for setting up our infrastucture and managing credentials. Make sure not to store these settings in pom.xml to avoid duplicate work and unnecessary storege and configuration duplication.<br/><br/>
Have a closer look at at [pom.xml](code-java/apache-tomcat-deploy-workshop/pom.xml) where I already have provided a second sever. All you need to do is to replace localhost by the name of your host. To deploy our example application to this sever all we need to do is pass the server id by using prefix '-P'

```powershell
mvn tomcat7:deploy -P<server-id>
mvn tomcat7:deploy -Pdevelopment
```

> Please keep in mind that if you repeat the deploy command the second execution will fail. In this case you need to run redeploy.
```powershell
mvn tomcat7:redeploy -Pdevelopment
```

### Further commands
Tomcat7 maven plugin supports further goals. Hence see documentation http://tomcat.apache.org/maven-plugin-trunk/tomcat7-maven-plugin/plugin-info.html

<br/>

## 6 Testing time
> I have attached a spring boot application to this repository [apache-tomcat-deploy-workshop](code-java/apache-tomcat-deploy-workshop/). You can either run it locally in your IDE as spring boot application or deploy it to your tomcat. Both ways should end up in the same result. You should get response from following REST service http://localhost:8080/apache-tomcat-deploy-workshop/kleenxcoder/

<br/>

## 7 Security
> I recomand to use a different phrase than "password" for authentification on your system.
<br/>

## 8 Troubleshooting
> To make your project running in Apache Tomcat servlet container we need to make use of Spring Boot ServletInitializer.

### New spring boot java project
> When creating a new java project by using [start.spring.io](https://start.spring.io/) and selecting WAR packaging you do not need to do anything. Spring initializr will create a seperate file called ServletInitializer.java which does the job.

### Srping boot java project without ServletInitializer
> When you have an older spring boot application you will need to do some modifiactions to get it running. In this case go a head and use your favorite search engine and lookup for "Deploy a Spring Boot WAR into a Tomcat Server".

### Non spring projects
> For non spring application use your favorite search engine and lookp for "How to Deploy a WAR File to Tomcat".

### Skipping non-war project
> When running the deploy tast you get no error but the tomcat7-maven-plugin says 'Skipping non-war project'. In this case you need to set

```xml
<packaging>war</packaging>
```

### Error while uploading WAR
> Usually this happen when your WAR file is bigger than 50MB. This is the default limit set under webapps\manager\WEB-INF\web.xml <br/> Add a 0 at the end to increase size to 500MB.

```xml
    <multipart-config>
      <!-- 50MB max -->
      <max-file-size>52428800</max-file-size>
      <max-request-size>52428800</max-request-size>
      <file-size-threshold>0</file-size-threshold>
    </multipart-config>
```

<br/>

## 9 Spring Profiles
> You have several possibilities to set spring active profile. <br/> :warning: **Changes might require restart of Apache Tomcat**: Be aware that your changes might not show any affect before a restart!

### Addin VM Parameter
> Independendet wether you do in on Windows or Unix you only need to add the following code at startup VM parameter
```xml
-Dspring.profiles.active=dev
```

### Using setenv file
> Just got to %CATALINA_HOME%/bin and create create dependent on your operating system:

#### On Windows setenv.bat
```xml
JAVA_OPTS=%JAVA_OPTS% -Dspring.profiles.active=dev
```

#### On Unix setenv.sh
```xml
JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=dev"
```

## Using web.xml
> You might also want to set the environment in your web.xml file. Therefore drop in the following sniplet
```xml
    <context-param>
       <param-name>spring.profiles.active</param-name>
       <param-value>dev</param-value>
    </context-param>
```

## Using catalina.properties
> A very common way is to set catalina.properties inside %CATALINA_HOME%/conf folder:
```xml
# Spring Profile
spring.profiles.active=dev
```

<br/>


## 10 References
- https://github.com/apache/tomcat-maven-plugin
<br/>

## 11 Contact
> If you have any questions don't hesitate to reach out to me. Probably twitter will be the fastest way!

twitter: https://twitter.com/kleenxcoder

udemy: https://www.udemy.com/user/peter-stritzinger/

www: http://www.kleenxcoder.com/

youtube: https://www.youtube.com/channel/UCd3SWZz1vebfUffRhi43BEg
