# Vue 

## Vue 基本代码

1. 导入Vue包： 

   ```html
   <script src="../lib/vue.min.js"></script>
   ```

   

2. 创建一个容器 

   ```ht
   <div id="app"></div>
   ```

3. 创建一个Vue的实例(在script标签下)

   ```html
   var vm = new Vue({
               el: '#app', // 表示当前我们new 的这个Vue实例，要控制页面上的那个区域
               // 这里的data 就是MVVM 中的M，专门保存每个页面的数据
               data: { // data 属性中，存放的是el中要用到的数据
                   msg: '欢迎学习Vue' // 通过Vue提供的指令，很方便的就能把数据渲染到页面上，程序员不在手动操作DOM元素了【前端的Vue之类的框架，不提倡我们去手动的操作DOM元素了】
               }
           });
   ```

   

4. 使用插值表达式 {{var}}给标签赋值

   ```html
   <div id="app">
       <p>{{msg}}</p>
   </div>
   ```

   

范例：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <!-- 1.导入Vue包 -->
    <script src="../lib/vue.min.js"></script>
</head>
<body>
    <!-- 将来new的Vue的实例，会控制这个元素中的所有内容 -->
    <!-- Vue 实例所控制的这个元素区域，就是我们的V -->
    <div id="app">
        <p>{{msg}}</p>
    </div>

    <script>
        // 2. 创建一个Vue的实例
        // 当我们导入包之后，在浏览器的内存中么就多了一个Vue的构造函数
        // 注意：我们 new 出来的 vm对象就是我们MVVM中 VM调度者
        var vm = new Vue({
            el: '#app', // 表示当前我们new 的这个Vue实例，要控制页面上的那个区域
            // 这里的data 就是MVVM 中的M，专门保存每个页面的数据
            data: { // data 属性中，存放的是el中要用到的数据
                msg: '欢迎学习Vue' // 通过Vue提供的指令，很方便的就能把数据渲染到页面上，程序员不在手动操作DOM元素了【全段的Vue之类的框架，不提倡我们去手动的操作DOM元素了】

            }
        });
    </script>
</body>
</html>
```



## Vue v-cloak 插值表达式

1. 插值表达式{{}}
   插值表达式{{}}：有闪烁
2. v-cloak
   v-cloak 能够解决 插值表达式闪烁的问题,只会替换自己的这个占位符，不会把整个元素的内容清空
3. v-text
   v-text 是没有闪烁的问题,会覆盖元素中原本的内容
4. v-html 
   将字符串中的标签解析
5. v-bind
   是vue中提供的用于**属性绑定**的指令，指令可以被简写为 :要绑定的属性
6. v-on
   中提供了 v-on: **事件绑定** 机制



```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
     <style>
         /* [v-cloak]{
             display: none;
         } */
     </style>
</head>
<body>
    <div id="app">
        <!-- 使用 v-cloak 能够解决 插值表达式闪烁的问题 -->
        <p v-cloak>++++++{{msg}}-------</p>
        <!-- 默认 v-text 是没有闪烁的问题 -->
        <!-- v-text 会覆盖元素中原本的内容，但是插值表达式，只会替换自己的这个占位符，不会把整个元素的内容清空 -->
        <h4 v-text="msg">====</h4>

        <div>{{msg2}}</div>
        <div v-text="msg2"></div>
        <div v-html="msg2"></div>

        <!-- v-bind:是vue中提供的用于绑定属性的指令 -->
        <!-- 注意v-bind： 指令可以被简写为 :要绑定的属性 -->
        <!-- v-bind 中，可以写合法的JS表达式 -->
        <!-- <input type="button" value="按钮" v-bind:title="mytitle + '额外的'">
        <input type="button" value="按钮" :title="mytitle"> -->

        <!-- vue 中提供了 v-on: 事件绑定 机制 -->
        <!-- <input type="button" value="按钮" v-on:click="show" v-on:mouseover="show"> -->
        <input type="button" value="按钮" @click="show" @mouseover="show">

        
    </div>

    <!-- 1.导入Vue包 -->
    <script src="../lib/vue.min.js"></script>

    <script>
        var vm = new Vue({
            el:"#app",
            data:{
                msg:123,
                msg2:'<h1>哈哈 我是一个H1</h1>',
                mytitle:"这是自定义的title" 
            },
            methods:{ // 这个 method属性中定义了当前vue实例的所有可用的方法
                show:function(){
                    alert("show");
                }
            }
        });
    </script>
</body>
</html>

<!-- 1. 如何定义一个基本的Vue代码结构 -->
<!-- 2. 插值表达式 和 v-text -->
<!-- 3. v-cloak -->
<!-- 4. v-html -->
<!-- 5. v-bind Vue提供的属性绑定机制  缩写是 ‘:’ -->
<!-- 6. v-on Vue提供的事件绑定机制   缩写是 ’@‘ -->
```



## Vue 跑马灯效果

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>

     <!-- 1.导入Vue包 -->
     <script src="../lib/vue.min.js"></script>
</head>
<body>
    

    <div id="app">
        <input type="button" value="浪起来" @click="lang">
        <input type="button" value="低调" v-on:click="stop">
        <h4>{{msg}}</h4>
    </div>

    <script>
        // 注意在 vm 实例中，如果想要调用 data 上的数据，或者想要调用 methods 中的方法， 必须通过 this.数据属性名 或 this.方法名 来进行
        // 访问，这里的this，就表示我们 new 出来的 VM 实例对象
        var vm = new Vue({
            el:"#app",
            data:{
                msg:"猥琐发育，别浪~~",
                intervalId:null // 在data商定一 定时器Id
            },
            methods:{
                lang(){
                    if (this.intervalId != null)return false;
                    this.intervalId = setInterval(() => {
                        console.log(this.msg);
                        var start = this.msg.substring(0,1);
                        var end = this.msg.substring(1);
                        this.msg = end + start;
                    }, 1000);
                   
                    // 注意： VM实例，会监听自己身上 data 中所有数据的改变，只要数据一发生变化，就会自动把最新的数据，从data上同步到页面中去：
                    // 【好处：程序员只需要关心数据，不需要考虑考虑如何重新渲染DOM页面】
                },
                stop(){ //停止
                    console.log("stop");  
                    clearInterval(this.intervalId)    
                    this.intervalId = null          
                }

            }
        })
    </script>
</body>
</html>
```



## 事件修饰符

1. stop：使用 .stop 阻止冒泡
2. prevent：使用 .prevent 阻止默认行为
3. capture：使用 .capture 实现捕获触发事件的机制
4. self：使用 .self 实现只有点击当前元素时候才触发事件处理函数
5. once：使用 .once 只触发一次事件处理函数

**stop 和 self的区别：**
.self 只会阻止自己身上冒泡行为的触发，并不会真正阻止冒泡的行为

范例：

```html
<!DOCTYPE html>
<html lang="cn">
<head>
   <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width,initial-scale=1.0">
   <meta http-equiv="X-UA-Compatible" content="ie=edge">
   <script src="../lib/vue.min.js"></script>
   <title>Document</title>
   <style>
       .outer{
           height:200px;
           background-color: darkred;
       }
       .inner {
           height:150px;
           background-color: darkblue;
       }
   </style>
</head>
<body>
   
<div id='app'>
    <!-- 1、使用 .stop 阻止冒泡 -->
    <!-- <div class="inner" @click="devHandler">
        <input type="button" value="戳他" @click.stop="btnHandler">
    </div> -->

    <!-- 2、使用 .prevent 阻止默认行为 -->
    <!-- <a href="http://www.baidu.com" @click.prevent="link">跳转百度</a> -->

    <!-- 3、使用 .capture 实现捕获触发事件的机制 -->
    <!-- <div class="inner" @click.capture="devHandler">
        <input type="button" value="戳他" @click="btnHandler">
    </div> -->

    <!-- 4、使用 .self 实现只有点击当前元素时候才触发事件处理函数 -->
    <!-- <div class="inner" @click.self="devHandler">
        <input type="button" value="戳他" @click="btnHandler">
    </div>  -->

    <!-- 5、 使用 .once 只触发一次事件处理函数-->
    <a href="http://www.baidu.com" @click.prevent.once="link">跳转百度</a>

    <!-- 演示： .stop 和 .self 的区别 -->
    <!-- <div class="outer" @click="div2Handler">
        <div class="inner" @click="divHandler">
            <input type="button" value="戳他" @click.stop="btnHandler">
        </div>
    </div> -->

    <!-- .self 只会阻止自己身上冒泡行为的触发，并不会真正阻止冒泡的行为 -->
    <!-- <div class="outer" @click="div2Handler">
        <div class="inner" @click.self="divHandler">
            <input type="button" value="戳他" @click="btnHandler">
        </div>
    </div>  -->

</div>
<script>
var vm = new Vue({
    el:'#app',
    data : {
        msg:"a",
    },
    methods:{
        divHandler(){
            console.log("这是 inner div的点击事件");
        },
        btnHandler(){
            console.log("这是 触发了按钮的点击事件");
        },
        link(){
            console.log("这是 a标签点击的事件");
        },
        div2Handler(){
            console.log("这是点击的 outer div");
        }
    }
})
</script>
</body>
</html>
```



## Vue v-model 实现双向绑定

 v-bind: 只能实现数据的单向绑定，从 M 自动绑定到 V， 无法实现数据的双向绑定,使用 v-model 指令，可以实现 表单元素(input select checkbox textarea)和 Mode 中数据的双向数据绑定

**注意**：v-model 只能运用在表单元素中

范例：

```html
<!DOCTYPE html>
<html lang="cn">
<head>
   <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width,initial-scale=1.0">
   <meta http-equiv="X-UA-Compatible" content="ie=edge">
   <script src="../lib/vue.min.js"></script>
   <title>Document</title>
</head>
<body>
   
<div id='app'>
    <h4>{{msg}}</h4>

    <!-- v-bind: 只能实现数据的单向绑定，从 M 自动绑定到 V， 无法实现数据的双向绑定 -->
    <!-- <input type="text" v-bind:value="msg"> -->
    
    <!-- 使用 v-model 指令，可以实现 表单元素和 Mode 中数据的双向数据绑定 -->
    <!-- 注意：v-model 只能运用在表单元素中 -->
    <!-- input select checkbox textarea -->
    <input type="text" v-model="msg">
    
</div>
<script>
var vm = new Vue({
    el:'#app',
    data : {
        msg:"大家都是好学生"
    }
})
</script>
</body>
</html>
```

