# Weather-Service 

> `weather-service`为一个天气提醒器，爬取深圳的天气并通过`QQ 邮箱`发送邮件提醒。

# 如何使用 How to Use

* `clone`或`donwload`到本地后导入到`idea`或`eclipse`中。
* 使用`maven`命令进行打包（可直接点击`idea`的`Lifecycle`进行打包）。
* 打包后上传至`Linux`，编写`crontab`命令执行启动脚本。
* **直接修改`resources/constant.property`文件，设置发送者邮箱、发送人、收件人、重试次数等。将会在下次爬取时生效。**

# 待完成 To Do List 

* 可添加微信提醒接口。
* 可添加短信提醒接口。
* 未来七天天气情况。
* 在`constant.property`中配置地区而非`url`。
* 添加其他邮箱发送，如`google`、`163`等。

# 说明链接 Description Link 

> 项目说明链接如下 
 
* [cnblogs](http://www.cnblogs.com/leesf456/p/7629176.html)  
* [github](https://leesf.github.io/technique/2017/10/08/crawl-weather-infomation-and-send-email/)
