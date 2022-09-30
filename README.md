![](./doc/assets/images/commons/screenshot.png)


<h1 align="center"><a href="http://www.jpress.cn" target="_blank"> JPress </a></h1>

<p align="center">
A product developed with Java, similar to WordPress, supports multi-site and multi -language automatic switching.(JPress started in 2015)
</p>


## Function

#### article module
-Elform management
-Eling classification
-Elis
-Elike search (supports SQL Like, Lucene, ES, Opensearch)

#### page module
-Page management
-Page classification
-Page comment

#### Recruitment module
-Job management
-A resume management
-Cuction classification
-Recruitment settings

#### Commodity module
-C product management
- Categories
- Product Reviews
-C product settings

#### Form module
-Form management
-Form drag and drag design
-Fosted into the article
-Form data collection
-Form data statistics

#### Attachment management
-Conneys list
-Stch into the attachment to the article, page
-Anding classification
-Video attachment

#### User Related
- User Management
- authority management
-The user tag
- group message
-Email group hair

#### system related
-Morphrase management
-Stock management
-Chat WeChat Management
- System Management
-Site management

## Features

#### template

-Moth templates are installed and uninstalled online
-Moth template is enabled and switched online
-On online editor and real -time effect
-D block dragging design of template mold
-The comprehensive template development document
-The ultimate template development experience


#### plugin

-It plug -in online installation, uninstallation
-In online enable and stop plug -in
-Stocks online update
-Profile to add a new Controller to the plug -in
-Profile to add a new Handler to the plug -in
-Prink new interceptor to the plug -in
-Prink new HTML, CSS and JS to the plug -in
-Profile to create a new database table in the plug -in and the corresponding Model
-Card different databases in the plug -in
-Profile to dynamically expand the background menu and user center menu via plug -in
-At plug -in menu to support the management of user authority settings
-Attae is stopped: All Controller, Handler, Intercepter of the plugin are automatically removed automatically
-In plug -in: All resources of the plugin are deleted


#### User

-Sugin independently, register for entrance
-Ne mobile phone text messages, mailbox activation functions
-The user center (comment management, personal data management, etc.)
-In WeChat browsing, automatically obtain user information through WeChat authorization


#### characters and permissions

-Prodity management
-The automatic, maintenance -free dictionary (automatic discovery of background routing, plug -in installation and unloading automatic allocation corresponding)
-The distribution of characters and permissions
-The user multi -character function
-Surora


#### WeChat

-Conal WeChat public account docking
-Conhe WeChat public account keyword automatic reply
-The WeChat public account menu settings
-Chat WeChat public account operation plug -in
-In the use of plug -in flexible expansion of various WeChat marketing functions
-Che WeChat Mini Program docking, configuration




#### Multi -site

-Suctile site binds independent domain names
-Suctile site binds independent secondary catalogs
-Su support site binds languages ​​in different regions
-In support visiting the main site is to automatically turn to the sub -site according to the language jump


#### seo

-To settings of each article, page and product independent SEO settings
-BAIDU API's real -time push
-Baidu and Google's automatic ping submission
-Sitemap automatically generates, backstage supports custom opening and closure
-The support of Robots.txt reptile spider
-The pseudo -static support for the whole station, support custom opening suffix


#### other

-Wordpress, hexo, jekyll, WeChat public account and other articles are introduced one click
-Cride the article to switch CKEDITOR and Markdown editor at will
-The maximized and invasive article writing experience
-Docker one -click deployment
-Alibaba Cloud, Tencent Cloud CDN online configuration
-Alibaba Cloud, Tencent Cloud SMS verification (user registered mobile phone verification)
-Fone automatic configuration can automatically synchronize Alibaba Cloud OSS
-The comprehensive API interface configuration management
-... (more waiting for you to discover)


## 交流

- Official website:[http://www.jpress.cn](http://www.jpress.cn)
- Plug -in list:[Click here](http://www.jpress.cn/article/category/plugin)
- Template list:[Click here](http://www.jpress.cn/article/category/template)
- QQ group: 591396171 ，288397536


## Help documentation

- [User manual](http://doc.jpress.cn/manual/)
- [Development documentation](http://doc.jpress.cn/development/)
- [Template development](http://doc.jpress.cn/development/template/template_introduce.html)
- [Plug -in development](http://doc.jpress.cn/development/addon/start.html)
- [Secondary development](hhttp://doc.jpress.cn/development/dev/start.html)
- [API interface](http://doc.jpress.cn/development/api/start.html)
- [Video tutorial](http://www.ketang8.com/course/5)
- [JPress-VIP member](http://www.jpress.cn/article/vip)


## 广告

- A easy-to-use online code format tool:[http://www.CodeFormat.CN](http://www.codeformat.cn)


## run JPress


**exist Docker Run**

```
curl -O https://gitee.com/JPressProjects/jpress/raw/master/docker-compose.yml && \
docker-compose up -d
```

**Installation and operation through the 80 port on Alibaba Cloud (or Tencent Cloud)**

```
wget https://gitee.com/JPressProjects/jpress/raw/master/install.sh && \
bash install.sh 80
```

> One -click installation video tutorial: [http://www.ketang8.com/course/study?chapterId=184](http://www.ketang8.com/course/study?chapterId=184)


**Run through development tools such as Eclipse or IDEA**

- 1. Install Java, Maven and other development environments on the computer
- 2. Download the source code and import Eclipse or IDEA
- 3. **Root directory**,implement `mvn clean package` Command compile
-4. Right-click running in development tools `starter/src/main/java/io.jpress.Starter` Lower `main` method
- 5. Access through the browser `http://127.0.0.1:8080`, For automatic installation, the installation process will automatically build a library and build a table

> JPress Download, import, run video tutorial, link: https://pan.baidu.com/s/1bqbQ9_HjF95EW4qrQvOSag Extraction code: 5jw8 


**Notice!Notice!Notice!After the first run of JPress, if it is executed again `mvn clean package` The command, Jpress will re -install the process.**
>
> Solution: JPress will be in the installation process, it will be`starter/target/classes` Generated in the directory `jboot.properties` and `install.lock` Two files,
> We need to copy these two files to `starter/src/main/resource` Table of contents. 
> 
> The reason is: whether Jpress is installed, decide in these two files.With these two files, Jpress does not follow the installation process, and the installation process is not available.When we execute `mvn clean` When the command,
> Maven Clear `starter/target` All files in the directory, so that these two files are lost, Jpress will take the installation process again。
> Only copy these two files to`starter/src/main/resource` Directory, execute again `mvn clean package` Only when the command can ensure that these two files will not be lost and do not go through the installation process.




## JPress Communication group

- QQ group 1: 591396171
- QQ group 2: 288397536


WeChat communication group:

![](./doc/assets/images/commons/wechat-group.png)
