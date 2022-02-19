# CURL

[curl官网](https://curl.se/)

支持：DICT, FILE, FTP, FTPS, GOPHER, GOPHERS, HTTP, HTTPS, IMAP, IMAPS, LDAP, LDAPS, MQTT, POP3, POP3S, RTMP, RTMPS, RTSP, SCP, SFTP, SMB, SMBS, SMTP, SMTPS, TELNET and TFTP. curl supports SSL certificates, HTTP POST, HTTP PUT, FTP uploading, HTTP form based upload, proxies, HTTP/2, HTTP/3, cookies, user+password authentication (Basic, Plain, Digest, CRAM-MD5, SCRAM-SHA, NTLM, Negotiate and Kerberos), file transfer resume, proxy tunneling and more.

> Curl用于命令行或脚本中传输数据。 curl也被用于汽车，电视机，路由器，打印机，音频设备，移动电话，平板电脑，机顶盒，媒体播放器，是互联网传输引擎的数千个软件应用在超过100亿的安装。  

[操作手册](https://curl.se/docs/manpage.html)

[Curl Cookbook](https://catonmat.net/cookbooks/curl)

## 基本使用

### 查看网页源码

直接在curl命令后加上网址，就可以看到网页源码。我们以网址www.baidu.com为例

```shell
$ curl www.baidu.com
```

```cmd
<!DOCTYPE html>
<!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=IE=Edge><meta content=always name=referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link=#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div class=s_form> <div class=s_form_wrapper> <div id=lg> <img hidefocus=true src=//www.baidu.com/img/bd_logo1.png width=270 height=129> </div> <form id=form name=f action=//www.baidu.com/s class=fm> <input type=hidden name=bdorz_come value=1> <input type=hidden name=ie value=utf-8> <input type=hidden name=f value=8> <input type=hidden name=rsv_bp value=1> <input type=hidden name=rsv_idx value=1> <input type=hidden name=tn value=baidu><span class="bg s_ipt_wr"><input id=kw name=wd class=s_ipt value maxlength=255 autocomplete=off autofocus></span><span class="bg s_btn_wr"><input type=submit id=su value=百度一下 class="bg s_btn"></span> </form> </div> </div> <div id=u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id=ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
```

#### -o

如果要把这个网页保存下来，可以使用`-o`参数，这就相当于使用`wget`命令了。

```cmd
$ curl -o 文件名 www.baidu.com
```

上卖弄命令将`www.baidu,com`保存成`文件名`文件。

#### -O

`-O`参数将服务器的响应保存成文件，并将URL的最后部分当作文件名。

```cmd
$ curl -O www.baidu.com
```



### 显示头信息

#### -i

`-i`参数可以显示响应头信息

```cmd
$ curl -i www.baidu.com
```

上面命令收到服务器回应后先输出服务器的响应头，然后空一行，再输出网页源代码。

```txt
HTTP/1.1 200 OK
Accept-Ranges: bytes
Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform
Connection: keep-alive
Content-Length: 2381
Content-Type: text/html
Date: Fri, 18 Feb 2022 01:45:42 GMT
Etag: "588604dc-94d"
Last-Modified: Mon, 23 Jan 2017 13:27:56 GMT
Pragma: no-cache
Server: bfe/1.0.8.18
Set-Cookie: BDORZ=27315; max-age=86400; domain=.baidu.com; path=/

<!DOCTYPE html>
<!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=IE=Edge><meta content=always name=referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link=#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div class=s_form> <div class=s_form_wrapper> <div id=lg> <img hidefocus=true src=//www.baidu.com/img/bd_logo1.png width=270 height=129> </div> <form id=form name=f action=//www.baidu.com/s class=fm> <input type=hidden name=bdorz_come value=1> <input type=hidden name=ie value=utf-8> <input type=hidden name=f value=8> <input type=hidden name=rsv_bp value=1> <input type=hidden name=rsv_idx value=1> <input type=hidden name=tn value=baidu><span class="bg s_ipt_wr"><input id=kw name=wd class=s_ipt value maxlength=255 autocomplete=off autofocus></span><span class="bg s_btn_wr"><input type=submit id=su value=百度一下 class="bg s_btn"></span> </form> </div> </div> <div id=u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id=ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
```

#### -I

使用`-I`参数向服务器发出HEAD请求，然后会将服务器返回的HTTP响应头打印出来。

```cmd
$ curl -I www.baidu.com
```

```txt
HTTP/1.1 200 OK
Accept-Ranges: bytes
Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform
Connection: keep-alive
Content-Length: 277
Content-Type: text/html
Date: Fri, 18 Feb 2022 01:46:46 GMT
Etag: "575e1f72-115"
Last-Modified: Mon, 13 Jun 2016 02:50:26 GMT
Pragma: no-cache
Server: bfe/1.0.8.18
```

`--head`参数等同于`-I`。

```cmd
$ curl --head www.baidu.com
```



### 显示通信过程

`-v`参数可以显示一次htt通信的整个过程，包括端口连接和请求头信息。

```cmd
$ curl -v www.baidu.com
```

```txt
* Rebuilt URL to: www.baidu.com/
*   Trying 14.215.177.38...
* TCP_NODELAY set
* Connected to www.baidu.com (14.215.177.38) port 80 (#0)
> GET / HTTP/1.1
> Host: www.baidu.com
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 200 OK
< Accept-Ranges: bytes
< Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform
< Connection: keep-alive
< Content-Length: 2381
< Content-Type: text/html
< Date: Fri, 18 Feb 2022 01:50:05 GMT
< Etag: "588604dc-94d"
< Last-Modified: Mon, 23 Jan 2017 13:27:56 GMT
< Pragma: no-cache
< Server: bfe/1.0.8.18
< Set-Cookie: BDORZ=27315; max-age=86400; domain=.baidu.com; path=/
```

如果你想查看更多的详细通信过程，可以使用`--trace`选项。

```cmd
$ curl --trace output.txt www.baidu.com
```

或者

```cmd
$ curl --trace-ascii output.txt www.baidu.com
```

运行后打开output.txt文件查看。

### 发送表单信息

发送表单信息有GET和POST两种方法。GET方法相对简单，只要把数据附在网址后面就行。

```cmd
$ curl example.com/form.cgi?data=xxx
```

POST方法必须把数据和网址分开，curl就要用到--data参数。

```cmd
$ curl -X POST --data "data=xxx" example.com/form.cgi
```

如果你的数据没有经过表单编码，还可以让curl为你编码，参数是`--data-urlencode`。

```cmd
$ curl -X POST --data-urlencode "date=April 1" example.com/form.cgi
```

`-d` 参数用于发送POST请求的数据体

```cmd
$ curl -d'login=emma＆password=123'-X POST https://google.com/login
# 或者
$ curl -d 'login=emma' -d 'password=123' -X POST  https://google.com/login
```

#### -d

使用`-d`参数以后，HTTP请求会自动加上请求头`Content-Type:application/x-www-form-urlencoded`。并且会自动将请求方法转为POST，因此可以省略`-X POST`。

`-d`参数可以读取本地文件的数据，然后向服务器发送。

```cmd
$ curl -d '@data.txt' www.example.com
```

上面命令读取`data.txt`文件的内容，作为请求体向服务器发送。

#### --data-urlencode

`-data-urlencode`参数等同于`-d`，发送POST请求的数据体，区别在于会自动将发送的数据进行URL编码。

```cmd
$ curl --data-urlencode 'comment=hello world' www.example.com
```

上面的代码中，发送的数据`hello world`之间有一个空格，需要进行URL编码。





### HTTP请求方式

curl的默认HTTP请求方式是**GET**，使用`-X`参数可以支持其它请求方式。

```cmd
$ curl -X POST www.example.com
```

```cmd
$ curl -X DELETE www.example.com
```

### 文件上传

假定文件上传的表单是下面这样：

```html
<form method="POST" enctype='multipart/form-data' action="upload.cgi">
    <input type=file name=upload>
    <input type=submit name=press value="OK">
</form>
```

你可以用curl这样上传文件：

```cmd
$ curl --form upload=@localfilename --form press=OK [URL]
```

#### -F

`-F`参数用来向服务器上传二进制文件。

```cmd
$ curl -F 'file=@photo.png' www.example.com
```

上面的命令会给HTTP请求加上请求头`Content-Type:multipart/form-data`,然后将文件`photo.png`作为`file`字段上传。

`-F` 参数可以指定MIME类型

```cmd
$ curl -F 'file=@photo.png;type=image/png' www.example.com
```

上面命令制定MIME类型为`image/png`,否则curl会把MIME类型设为`application/octet-stream`。

`-F`参数也可以执行文件名。

```cmd
$ curl -F 'file=@photo.png;filename=me.png' www.example.com
```

上面命令中，原始文件名为`photo.png`，但是服务器接收时的文件名为`me.png`。

### 请求头

#### Referer字段

有时你需要在http request头信息中，提供一个referer字段，表示你是从哪里跳转过来的。

#### --referer

```cmd
$ curl --referer http://www.a.com http://www.example.com
```

#### -e

`-e`参数用来设置HTTP的请求头`Referer`,表示请求的来源。

```cmd
$ curl -e 'http://www.a.com' http://www.example.com
```

上面命令将`Referer`标头设为`http://www.a.com`。

#### **User Agent字段**

这个字段是用来表示客户端的设备信息。服务器有时会根据这个字段，针对不同设备，返回不同格式的网页，比如手机版和桌面版。

curl可以这样模拟：

```cmd
$ curl --user-agent "[User Agent]" [URL]
```

#### 添加请求头信息

#### --header

有时需要在http request之中，自行增加一个头信息。`--header`参数就可以起到这个作用。

```cmd
　$ curl --header "Content-Type:application/json" http://example.com
```

#### -H

也可以使用`-H` ，`-H`可以多次使用，达到一次添加多个请求头的效果

```cmd
$ curl -H "Content-Type:application/json" -H "Authorization: Bearer xxx" www.baidu.com
```

上面命令添加了两个请求头，分别是`Content-Type:application/json`,`Authorization: Bearer xxx`

### cookie

使用`--cookie`参数，可以让curl发送cookie。

```cmd
$ curl --cookie "name=xxx" www.example.com
```

也可以使用`-b`参数发送Cookie

```cmd
$ curl -b 'foo=bar' www.example.com
```

也可以一次发送多个cookie

```cmd
$ curl -b 'foo1=bar1;foo2=bar2' www.example.com
```

上面的命令发送了两个cookie，分别是`foo1`和`foo2`

> 至于具体的cookie的值，可以从http response头信息的`Set-Cookie`字段中得到。

`-c cookie-file`可以保存服务器返回的cookie到文件，`-b cookie-file`可以使用这个文件作为cookie信息，进行后续的请求。

```cmd
$ curl -c cookies.txt http://example.com
$ curl -b cookies.txt http://example.com
```

### Basic 认证

#### --user

有些网站使用了基本认证方式(用户名:密码)，这是curl需要使用`--user`参数

```cmd
$ curl --user name:password www.baidu.com
```

#### -u

`-u`参数和`--user`参数实现的功能是一样的。

```cmd
$ curl -u 'bob:12345' www.example.com
```

上面命令设置用户名为`bob`，密码为`12345`，然后将其转为 HTTP 标头`Authorization: Basic Ym9iOjEyMzQ1`。

curl 能够识别 URL 里面的用户名和密码。

```cmd
$ curl https://bob:12345@google.com/login
```

上面命令能够识别 URL 里面的用户名和密码，将其转为上个例子里面的 HTTP 标头。

```cmd
$ curl -u 'bob' https://google.com/login
```

上面命令只设置了用户名，执行后，curl 会提示用户输入密码。

## 高级

### -G

`-G` 参数用来构造URL的查询字符串。

```cmd
$ curl -G -d 'q=kit' -d 'count=20' www.example.com
```

上面的命令会发送一个GET请求，实际请求的URL为`www.example.com?q=kit&count=20`。如果省略`-G`，会发出一个POST请求。

如果数据需要URL编码，可以结合 `--data-urlencode`参数。

```cmd
$ curl -G --data-urlencode 'comment=hello world' www.example.com
```



### -s

`-s`参数将不输出错误和进度信息。

```cmd
$ curl -s www.example.com
```

上面命令一旦发生错误，不会显示错误信息。不发生错误的话，会正常显示运行结果。

如果想让curl不产生任何输出，可以使用下面的命令。

```cmd
$ curl -s -o /dev/null www.example.com
```

### -S

`-S` 参数指定子只输出错误信息，通常与`-s`一起使用。

```cmd
$ curl -s -o /dev/null www.example.com
```

上面命令没有任何输出，除非发生错误。



### -X

`-X` 参数指定HTTP的请求方法。

```cmd
$ curl -X POST www.example.com
```

上面命令对`www.example.com`发出POST请求

