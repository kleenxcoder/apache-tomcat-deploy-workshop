# Deploying Spring Boot application to Apache Tomcat
> This will give you a short overview how to configure server and your project to be able to use maven to deploy your source to tomcat.

## Setup
- In this workshop you'll need the latest Java version. Java 8 is the baseline for this workshop.
- You'll need a newer, 3.1, version of Apache Maven installed.
- Use ure desired IDE Apache NetBeans, Eclipse, IntelliJ IDEA, Visual Studio Code or vi
- Patch your IDE with lombok https://projectlombok.org/setup/overview
- Runnable local installation of Apache Tomcat https://tomcat.apache.org/

## Apache Tomcat configuration

### tomcat-users.xml
```xml
<tomcat-users>
    <role rolename="manager-gui"/>  
        <role rolename="manager-script"/>   
        <user username="manager" password="password" roles="manager-gui,manager-script" />  
</tomcat-users>
```

## Maven setting

### settings.xml
> Usually stored in either in your maven installation folder or your .m2 folder. Windows user got to %UserProfile%\.m2

```xml
<servers>  
    <server>
       <id>LocalhostServer</id>
       <username>manager</username>
       <password>password</password>
    </server>
</servers>
```

## Client side configuration

## Security
> I recomand to use a different phrase than "password" for authentification on you system.

## Tested with
- Jdk 8 & Tomcat 7
- Jdk 8 & Tomcat 8
- Jdk 8 & Tomcat 9

## Contact
> If you have any questions don't hesitate to reach out to me. Probably twitter will be the fastest way!

twitter: https://twitter.com/kleenxcoder

udemy: https://www.udemy.com/user/peter-stritzinger/

www: http://www.kleenxcoder.com/

youtube: https://www.youtube.com/channel/UCd3SWZz1vebfUffRhi43BEg
