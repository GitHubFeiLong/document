# ES 

## ES6

### let

+  变量不能重复声明

```javascript
let a;
let b,c,d;
let f= 521, g = 'iloveyou', h = [];
// 报错 (Uncaught SyntaxError: Identifier 'star' has already been declared)
let star = '罗志祥';
let star = '罗志祥';
```

+ 块级作用域(还有 全局作用域，函数作用域， eval作用域）

```javascript
// var
{
    var girl = '周扬青';
}
	console.log(girl); // '周扬青'
// let
{
    let girl = '周扬青';
}
	console.log(girl); // girl is not defined
```

+ 不存在变量提升

  ```javascript
  console.log(song); // undefined
  var song = '恋爱达人';
  console.log(song1); // Cannot access 'song1' before initialization
  let song1 = '恋爱达人';
  ```

+ 不影响作用域链

  ```javascript
  {
      let school = '尚硅谷';
      function fn () {
      	console.log(school); //尚硅谷
      }
      // fn没有变量，会往上层找该变量
   	fn();
  }
  ```

> 使用循环时
>
> 如果使用 var 因为i没有块级作用域，循环结束后，i自增变为了3
>
> ```javascript
> for(var i = 0; i < 3; i++){}
> console.log(window.i); // 3
> ```

### const 声明常量

+ 一定要赋初始值
+ 一般常量使用大写
+ 不允许重复声明
+ 常量的值不能修改
+ 块级作用域(跟let类似)
+ 对于数组和对象的元素修改，不算对常量的修改，不会报错。

> 注意:对象属性修改和数组元素变化不会触发const错误
> 应用场景:声明对象类型使用const,非对象类型声明选择let

### 变量解构赋值

允许按照一定模式从数组和对象中提取值，对变量进行赋值，这被称为解构赋值

+ 数组的解构

  ```javascript
  const F4 = ['小沈阳', '刘能', '赵四', '宋小宝'];
  let [xiao, liu, zhao, song] = F4;
  console.log(xiao, liu, zhao, song);// 小沈阳 刘能 赵四 宋小宝
  ```

+ 对象的解构

  ```javascript
  const zhao = {
      name : '赵本山',
      age: '不详',
      xiaoping:  function () {
          console.log("我可以");
      }
  }
  
  let {name, age, xiaoping} = zhao;
  console.log(name, age, xiaoping);
  xiaoping()
  ```

+ 复杂解构

  ```javascript
  let wangfei = {
      name:'王菲',
      age:18,
      songs:['红豆','流年','暧昧'],
      history:[
          {name:'窦唯'},
          {name:'李亚鹏'},
          {name:'谢霆锋'},
      ]
  }
  let {songs:[one, two, three], history:[first, second, third]} = wangfei;
  ```

> 注意，变量和属性名称一致，不然结果是undefined
>
> 频繁使用对象方法、数组元素，就可以使用解构赋值形式

### 模板字符串

ES6 引入新的声明字符串的方式 [``], 

模板字符串（template string）是增强版的字符串， 用反引号（`）标识，特点：  

+ 声明

  ```javascript
  let str = `我也是一个字符串`;
  console.log(str, typeof str)//我也是一个字符串 string
  ```

+ 字符串中可以出现换行符  

  ```javascript
  let str = `<ul>
                  <li></li>
              </ul>`
  ```

+ 可以使用 ${xxx} 形式输出变量  

  ```javascript
  let name = '沈腾';
  let str = `${name}是我心目中最搞笑的演员！`; 
  console.log(str); // 沈腾是我心目中最搞笑的演员！
  ```

> 注意：当遇到字符串与变量拼接的情况使用模板字符串  

### 简化对象写法

ES6 允许再大括号里面，直接写入变量和函数，作为对象的属性和方法，这样的书写更加简洁

```javascript
let name = '尚硅谷';
let change = function () {
    console.log('我们可以改变你！！');
}
// 原来
const SCHOOL = {
    name:name,
    change:change,
    // 方法
    myMethod: function (){
        console.log('我是自定义方法！！');
    }
}
// 现在
const SCHOOL = {
    name,
    change,
    myMethod(){
        console.log('我是自定义方法！！');
    }
}
```

> 注意：对象简写形式简化了代码，所以以后用简写就对了  

### 箭头函数

ES6 允许使用 箭头（=>） 定义函数

+ this是静态的，this始终指向**函数声明时**所在作用域下的this值
+ 不能作为构造实例化对象
+ 不能使用 arguments 变量
+ 箭头函数的简写
  + 省略小括号，当形参有且只有一个的时候
  + 省略花括号，当代码只有一条语句的时候,return 必须省略，而且语句的执行结果就是函数的返回值

> call 方法调用 (可以更改函数内部this的值)，格式： 方法.call(具体作用域)。
>
> 箭头函数，适合与this无关的回调，定时器，数组的方法回调。箭头函数不适合 与 this有关的回调，事件回调，对象的方法



### 函数参数默认值

ES6 允许给函数参数赋值初始值

+ 形参初始值 具有默认值的参数，一般要靠后(潜规则)

  ```javascript
  function add (a, b, c=10) {
      return a + b + c;
  }
  console.log(add(1,2));
  ```

+ 与解构赋值结合

  ```javascript
  function connect({host='127.0.0.1', username,password,port}){
      let host = host;
      let username = username;
  }
  connect({
      host:'localhost',
      username:'root',
      password:'soft01',
      port:'3306'
  })
  ```

### rest 参数

ES 6 引入rest 参数，用于获取函数的实参，用来代替 arguments

```javascript
// ES5 arguments 获取实参的方式
function date () {
    console.log(arguments); // 是一个对象
}
date('a','b','c','d');

// ES6 rest 参数
function date (...args) {
    console.log(args);// 是一个数组
}
date('a','b','c','d');
```

> rest 参数必须要放到参数的最后



### spread扩展运算符

扩展运算符（spread）也是三个点（...）。它好比 rest 参数的逆运算，将一个数组转为用逗号分隔的参数序列，对数组进行解包。

```javascript
// 声明一个数组 ...
    const tfboys = ['易烊千玺', '王源', '王俊凯']; // => '易烊千玺', '王源', '王俊凯'

// 声明一个函数
function chunwan() {
    console.log(arguments);
}
chunwan(tfboys);
chunwan(...tfboys); // 等同于 chunwan('易烊千玺', '王源', '王俊凯')
```

+ 数组的合并

  ```javascript
  const kuaizi = ['王太利', '肖央'];
  const fenghuang = ['曾毅', '玲花'];
  console.log(kuaizi.concat(fenghuang));// '王太利', '肖央','曾毅', '玲花'
  const kuaizifenghuang = [...kuaizi, ...fenghuang];
  console.log(kuaizifenghuang);// '王太利', '肖央','曾毅', '玲花'
  ```

+ 数组的克隆

  ```javascript
  const sanzhihua = ['E', 'G', 'M'];
  const sanyecao = [...sanzhihua]; // ['E', 'G', 'M']
  console.log(sanyecao);// 'E', 'G', 'M'
  ```

+ 将伪数组转为真正的数组

  ```javascript
  const divs = document.querySelectorAll('div');
  console.log(divs);
  const divArr = [...divs];
  console.log(divArr);
  ```



### Symbol 数据类型

#### Symbol 基本使用

​		ES6 引入了一种新的原始数据类型 Symbol，表示独一无二的值。它是
JavaScript 语言的第七种数据类型，是一种类似于字符串的数据类型。
Symbol 特点 ：

+ Symbol 的值是唯一的，用来解决命名冲突的问题  
+ Symbol 值不能与其他数据进行运算  
+ Symbol 定义 的 对象属 性 不能 使 用 for…in 循 环遍 历 ，但 是可 以 使 用
  Reflect.ownKeys 来获取对象的所有键名  

> 注: 遇到唯一性的场景时要想到 Symbol  

#### Symbol 内置值  

除了定义自己使用的 Symbol 值以外， ES6 还提供了 11 个内置的 Symbol 值，指向语言内部使用的方法。 可以称这些方法为魔术方法，因为它们会在特定的场
景下自动执行。

![image-20210325155842963](ES6.assets/image-20210325155842963.png)

#### 创建Symbol

+ 方式1 Symbol()

  ```javascript
  let s = Symbol();
  console.log(s, typeof s);// Symbol() "symbol"
  let s2 = Symbol('尚硅谷');
  let s3 = Symbol('尚硅谷');
  console.log(s2 === s3);// false
  ```

+ 方式2 Symbol.for();

  ```javascript
  let s4 = Symbol.for('尚硅谷');
  let s5 = Symbol.for('尚硅谷');
  console.log(s4, typeof s4);// Symbol('尚硅谷') "symbol"
  console.log(s4 === s5);// true
  ```

+ 创建对象属性

  ```javascript
  // 向对象中添加方法 up down
  let game = {
      up(){
          console.log("raw up");
      },
      down(){
          console.log("raw down");
      }
  }
  // 如果game本来就有up方法，就会覆盖原up
  // game.up = function () {}
  
  // 声明一个对象
  let methods = {
      up: Symbol(),
      down: Symbol()
  }
  game[methods.up] = function () {
      console.log('new up 方法');
  }
  game[methods.down] = function () {
      console.log('new down 方法');
  }
  console.log(game);
  
  let youxi = {
      name:'狼人杀',
      [Symbol('say')]: function(){
          console.log('say');
      },
      [Symbol('do')]: function(){
          console.log('do');
      }
  }
  
  console.log(youxi);
  ```

> 不能进行数据运算和比较



### Iterator 迭代器

遍历器（Iterator）就是一种机制。它是一种接口，为各种不同的数据结构提供统一的访问机制。任何数据结构只要部署 Iterator 接口，就可以完成遍历操作。 

1.  ES6 创造了一种新的遍历命令 for...of 循环， Iterator 接口主要供 for...of 消费  
2. 原生具备 iterator 接口的数据(可用 for of 遍历)  
   1. Array
   2. Arguments
   3. Set
   4. Map
   5. String
   6. TypedArray
   7. NodeList
3. 工作原理
   1. 创建一个指针对象，指向当前数据结构的起始位置  
   2. 第一次调用对象的 next 方法，指针自动指向数据结构的第一个成员  
   3. 接下来不断调用 next 方法，指针一直往后移动，直到指向最后一个成员  
   4. 每调用 next 方法返回一个包含 value 和 done 属性的对象  

```javascript
// 申明一个数组
const xiyou = ['ts', 'swk', 'zbj', 'ss'];

// 使用 for ... of 遍历数组
for (let v of xiyou) {
    console.log(v);//'ts', 'swk', 'zbj', 'ss'
}
// 使用 for ... in 遍历数组
for (let v in xiyou) {
    console.log(v); // 0,1,2,3
}
// _proto 有 Symbol(Symbol.iterator) 方法
console.log(xiyou);

// 获取迭代器对象
let iterator = xiyou[Symbol.iterator]();
console.log(iterator);
// 调用迭代器对象的next方法,返回 {value: "ts", done: false} 对象，value表示当前值，done表示是否迭代完成
console.log(iterator.next());
console.log(iterator.next());
console.log(iterator.next());
console.log(iterator.next());
console.log(iterator.next()); // done 变为 true
```

#### 自定义迭代器遍历数据

```javascript
// 声明一个对象
const banji = {
    name : '终极一班',
    stus:[
        'xiaoming',
        'xiaoning',
        'xiaotian',
    ],
    // 自定义迭代器循环
    [Symbol.iterator](){
        // 索引变量
        let index = -1;
        return {
            next: () => {
                index++;
                return {value:this.stus[index], done:index>this.stus.length}
            }
        };
    }
}
// 遍历对象
for (let v of banji) {
    console.log(v);
}
```

> 需要自定义遍历数据的时候，要想到迭代器  



### generator 生成器

生成器函数是 ES6 提供的一种异步编程解决方案，语法行为与传统函数完全不同(生成器其实就是一个特殊函数，用来异步编程)

```javascript
function * gen(){
    // yield:函数代码的分隔符
    yield '一只没有耳朵';
    yield '一只没有尾巴';
    return '真奇怪';
}
let iterator = gen();
console.log(iterator.next());
console.log(iterator.next());
console.log(iterator.next());
```

+ \* 的位置没有限制  
+ 生成器函数返回的结果是迭代器对象，调用迭代器对象的 next 方法可以得到
  yield 语句后的值  
+ yield 相当于函数的暂停标记，也可以认为是函数的分隔符，每调用一次 next
  方法，执行一段代码  
+ next 方法可以传递实参，本次传递的参数值，就是上次yield的返回值  

### Promise  

Promise 是 ES6 引入的异步编程的新解决方案。语法上 Promise 是一个构造函数，
用来封装异步操作并可以获取其成功或失败的结果。
1) Promise 构造函数: Promise (excutor) {}
2) Promise.prototype.then 方法
3) Promise.prototype.catch 方法 

#### Promise  基础

创建实例：

```javascript
// 实例化 Promise 对象
const p = new Promise(function(resolve, reject){
    setTimeout(()=>{
        let data = '数据库中的用户数据';
        // resolve(data);
        let err = '数据读取失败';
        reject(err);
    }, 1000)
})

// 调用 Promise 对象的 then方法，第一个回调函数是成功回调函数，第二个是失败回调函数
p.then((value)=>{
    console.log(value);
},(reason)=>{
    console.log(reason);
})
```

#### then 方法

调用 then 方法，then方法的返回结果是 Promise 对象，对象状态由回调函数的执行结果决定。

如果回调函数中返回的结果是 非promise类型的属性，状态为成功，返回值为对象的成功的值

```javascript
const result = p.then(value=>{
    console.log(value);
    // 1.非promise类型的对象
    //return 123;
    // 2.是promise对象,根据里层返回成功还是失败
    // return new Promise((resolve, reject)=>{
    //     reject('123');
    // })
    // 3. 抛出错误,返回错误
    // throw new Error('出错了');
}, reason=>{
    console.warn(reason);
})
console.log(result);
```

> 这样根据then的返回值返回Promise对象，就能实现链式调用
>
> ```javascript
> p.then(value=>{
> 
> }, reason=>{
> 
> }).then(value=>{
> 
> }, reason=>{
> 
> })
> ```

#### catch 方法

catch方法只有一个参数，该参数就是失败回调函数

```javascript
p.catch(reason=>{
    console.error(reason);
})
```

### Set

ES6 提供了新的数据结构 Set（集合） 。它类似于数组，但成员的值都是唯
一的，集合实现了 iterator 接口，所以可以使用『扩展运算符』和『for…of…』进
行遍历，集合的属性和方法：  

+ size 返回集合的元素个数  
+ add 增加一个新元素，返回当前集合  
+ delete 删除元素，返回 boolean 值  
+ has 检测集合中是否包含某个元素，返回 boolean 值  
+ clear 清空集合，返回 undefined  

### Map

ES6 提供了 Map 数据结构。它类似于对象，也是键值对的集合。 但是“键”
的范围不限于字符串，各种类型的值（包括对象）都可以当作键。 Map 也实现了
iterator 接口，所以可以使用『扩展运算符』和『for…of…』进行遍历。 Map 的属
性和方法：
1) size 返回 Map 的元素个数
2) set 增加一个新元素，返回当前 Map
3) get 返回键名对象的键值
4) has 检测 Map 中是否包含某个元素，返回 boolean 值
5) clear 清空集合，返回 undefined  

#### class 类  

ES6 提供了更接近传统语言的写法，引入了 Class（类）这个概念，作为对
象的模板。通过 class 关键字，可以定义类。基本上， ES6 的 class 可以看作只是
一个语法糖，它的绝大部分功能， ES5 都可以做到，新的 class 写法只是让对象
原型的写法更加清晰、更像面向对象编程的语法而已。
知识点：
1) class 声明类
2) constructor 定义构造函数初始化
3) extends 继承父类
4) super 调用父级构造方法
5) static 定义静态方法和属性
6) 父类方法可以重写  

### 数值扩展

+  Number.EPSILON 是JavaScript 表示最小精度,EPSILON 属性的值接近于 2.2204466492503130808472633361816E-16

  ```javascript
  console.log(0.1+0.2 === 0.3);//false
  function equal (a, b) {
  	return Math.abs(a-b) < Number.EPSILON
  }
  console.log(0.1 + 0.2 === 0.3); // false
  console.log(equal(0.1 + 0.2, 0.3)); // true
  ```

+  二进制和八进制 16进制

  ```javascript
  let b = 0b1010; // 0b开头
  let b = 0o777; // 0o开头
  let b = 0x777; // 0x开头
  ```

+ Number.isFinite 检测 一个数值是否为有限数

  ```javascript
  console.log(Number.isFinite(100));//true
  console.log(Number.isFinite(100/0));//false
  console.log(Number.isFinite(Infinity));//false
  ```

+ Number.isNaN检测一个 数值是否为NaN

  ```javascript
  console.log(Number.isNaN(123));//false
  ```

+ Number.parseInt Number.parseFloat 字符串转数字

  ```javascript
  console.log(Number.parseInt('123.1'));// 123
  console.log(Number.parseFloat('123.1'));//123.1
  ```

+ Number.isInteger判断一个数是否为整数

  ```javascript
  console.log(Number.isInteger(5)); // true
  console.log(Number.isInteger(5.1));//false
  ```

+ Math.trunc将数字的小数部分抹掉

  ```javascript
  console.log(Math.trunc(3.4));// 3
  ```

+ Math.sign 判断一个数到底为正数负数还是零

  ```javascript
  console.log(Math.sign(21)); // 1
  console.log(Math.sign(-1)); // -1
  console.log(Math.sign(0)); // 0
  ```

### 对象方法扩展

+ Object.is判断两个值是否完全相等

  ```javascript
  console.log(Object.is(123,123)); // true
  console.log(Object.is(NaN,NaN)); // true
  console.log(NaN === NaN);//false
  ```

+ Object.assign对象的合并

  ```javascript
  const cfg1 = {
      host:'localhost',
      port:3306,
      name:'root',
      password:'root',
  }
  const cfg2 = {
      host:'127.0.0.1',
      port:33060,
      name:'root111',
      da:'root111',
  }
  
  let result = Object.assign(cfg1, cfg2);
  console.log(result); // 两个对象的并集，如果有相同属性，cfg2的顶替cfg1的
  ```

+ Object.setPrototypeOf 设置原型对象 Object.getPrototypeof

  ```javascript
  const school = {
  	name : '尚硅谷',
  }
  
  const cities = {
  	xiaoqu : ['北京', '上海', '深圳'],
  }
  Object.setPrototypeOf(school, cities);
  console.log(school);
  ```

### 模块化

模块化是指将一个大的程序文件，拆分成许多小的文件，然后将小文件组合起来  

#### 模块化的好处

模块化的优势有以下几点：
1) 防止命名冲突
2) 代码复用
3) 高维护性  

#### 模块化规范产品  

ES6 之前的模块化规范有：
1) CommonJS => NodeJS、 Browserify
2) AMD => requireJS
3) CMD => seaJS

#### ES6 模块化语法

模块功能主要由两个命令构成： export 和 import。

+ export 命令用于规定模块的对外接口
+ import 命令用于输入其他模块提供的功能  

#### 基本使用

##### export 导出

```javascript
// 方式1：
export const a = 'xxx';
export function A(){};
// 方式2：
export {a, A}
// 方式3
export default A;
```

##### import 引入

```javascript
// 1.通用的导入方式
import * as m1 from "./src/js/m1.js";
// 2.解构赋值形式(因为可能会出现重名，可以使用 as设置别名)
import {a, A} from "./src/js/m1.js";
import {a as aa, A} from "./src/js/m1.js";
import {default as m1} from "./src/js/m1.js";
// 3.简便形式 针对默认暴露
import m1 from "./src/js/m1.js";
```

> 使用时，要在script标签中添加 type='module'
>
> ```javascript
> <script type="module"></script>
> ```

## ES7

+ Array.prototype.includes（Includes 方法用来检测数组中是否包含某个元素，返回布尔类型值）

  ```javascript
  const mingzhu = ['西游记', '红楼梦', '三国演义', '水浒传'];
  console.log(mingzhu.includes('西游记')); // true
  console.log(mingzhu.includes('金瓶梅')); // false
  ```

+ 指数操作符 **与 Math.pow 结果相同  

  ```javascript
  console.log(2**10); // 1024
  console.log(Math.pow(2, 10)); // 1024
  ```

## ES8

### async 和 await  

async 和 await 两种语法结合可以让异步代码像同步代码一样  

#### async 函数  

1. async 函数的返回值为 promise 对象

2. promise 对象的结果由 async 函数执行的返回值决定  

   ```javascript
   async function fn () {
       // 1. 返回的结果不是一个 Promise 类型的对象，其结果就是一个成功的Promise对象。
       // return '尚硅谷';
       // 2. 抛出异常：返回的结果是一个失败的Premise
       // throw new Error('出错了')
       // 3. 返回的结果是一个Promise对象
       return new Promise((resolve, reject) => {
           reject('成功');
       })
   }
   ```

#### await 表达式  

1. await 必须写在 async 函数中  

2. await 右侧的表达式一般为 promise 对象
3. await 返回的是 promise 成功的值
4. await 的 promise 失败了, 就会抛出异常, 需要通过 try...catch 捕获处理  

```javascript
const p = new Promise((resolve, reject) => {
    // resolve('成功的值！');
    reject('成功的值！');
})
// await 要放在 async 函数中。
async function main () {
    try {
        let result = await p;
        console.log(result);
    } catch (e) {
        console.warn(e);
    }
}
```

### Object.values 和 Object.entries  

1. Object.keys()方法返回一个给定对象的所有键的数组
2. Object.values()方法返回一个给定对象的所有可枚举属性值的数组
3. Object.entries()方法返回一个给定对象自身可遍历属性 [key,value] 的数组  

### Object.getOwnPropertyDescriptors  

该方法返回指定对象所有自身属性的描述对象  

```javascript
console.log(Object.getOwnPropertyDescriptors(school));
// 和上面一样的结构
Object.create(null,{
    name:{
        // 设置值
        value:'尚硅谷',
        // 属性特性
        writable:true,
        configurable:true,
        enumerable:true
    }
})
```

## ES9

### Rest/Spread 属性

Rest 参数与 spread 扩展运算符在 ES6 中已经引入，不过 ES6 中只针对于数组，
在 ES9 中为对象提供了像数组一样的 rest 参数和扩展运算符  

### 正则表达式命名捕获组

```javascript
 function connect({host, port, ...user}){
     console.log(host);
     console.log(port);
     console.log(user);
 }
connect({
    host:'127.0.0.1', 
    port:3306,
    username:'root', 
    password:'root'
})
```

ES9 允许命名捕获组使用符号`?<name>` ,这样获取捕获结果可读性更强  

```javascript
// 原
let str = '<a href="www.baidu.com">百度</a>';
// 提取url 与 标签文本
const reg = /<a href="(.*)">(.*)<\/a>/;
let result = reg.exec(str);    
console.log(result);
// 0 是str
console.log(result[1]);
console.log(result[2]);
// 现
let str = '<a href="www.baidu.com">百度</a>';
const reg = /<a href="(?<url>.*)">(?<text>.*)<\/a>/;
let result = reg.exec(str);    
console.log(result);
console.log(result.groups.url);
console.log(result.groups.text);
```



### 对象合并

```javascript
const skillOne = {
    q : '天音波'
}
const skillTwo = {
    w : '金钟罩'
}
const skillThree = {
    e : '天雷破'
}
const skillFour = {
    r : '猛龙摆尾'
}
// ...skillOne => q:'天音波'
const mangseng = {...skillOne,...skillTwo,...skillThree,...skillFour}
console.log(mangseng);
```



### 正则表达式反向断言

ES9 支持反向断言，通过对匹配结果前面的内容进行判断，对匹配进行筛选。  

```javascript
//声明字符串
let str = 'JS5211314 你知道么 555 啦啦啦';
//正向断言
const reg = /\d+(?=啦)/;
const result = reg.exec(str);
//反向断言
const reg = /(?<=么)\d+/;
const result = reg.exec(str);
console.log(result);
```

### 正则表达式 dotAll 模式  

正则表达式中点.匹配除回车外的任何单字符，标记『s』 改变这种行为，允许行
终止符出现  

```javascript
let str = `
<ul>
    <li>
        <a>肖生克的救赎</a>
        <p>上映日期: 1994-09-10</p>
    </li>
    <li>
        <a>阿甘正传</a>
        <p>上映日期: 1994-07-06</p>
    </li>
</ul>`;
//声明正则
const reg = /<li>.*?<a>(.*?)<\/a>.*?<p>(.*?)<\/p>/gs;
//执行匹配
const result = reg.exec(str);
let result;
let data = [];
while(result = reg.exec(str)){
	data.push({title: result[1], time: result[2]});
}
//输出结果
console.log(data);
```



## ES10

### Object.fromEntries

1. 将二维数组转为map（里层的第一个元素为key，第二个元素为value，除此之外忽略其它元素）

   ```javascript
   const result = Object.fromEntries([
       ['name', '尚硅谷'],
       ['xueke', 'java,大数据,前端,云计算'],
   ])
   console.log(result);//{name: "尚硅谷", xueke: "java,大数据,前端,云计算"}
   ```

2. 将map集合转为对象

   ```javascript
   const m = new Map();
   m.set('name', 'FS')
   const result = Object.fromEntries(m);
   console.log(m); // Map对象
   console.log(result);//{name:'fs'}
   ```

3. 将对象转为二维数组

   ```javascript
   const arr = Object.entries({
       name:'尚硅谷'
   })
   console.log(arr);
   ```

### trimStart 和 trimEnd

+ trimStart 去掉左边的空格
+ trimEnd 去掉右边的空格

### Array.prototype.flat 与 flatMap

扁平化数组，将多维数组转换为低维数组

#### flat

1. 二维转一维

   ```javascript
   const arr = [1,2,3,4,[5,6]];
   console.log(arr.flat()); //[1,2,3,4,5,6]
   ```

2. 三维转一维

   ```javascript
   const arr = [1,2,3,4,[5,6,[7,8]]];
   // flat 参数为深度 是一个数字，默认值为1
   console.log(arr.flat(2)); // [1,2,3,4,5,6,7,8] 
   ```

#### flatMap

没看出区别来

```javascript
const arr = [1, 2, 3, 4];
// const result = arr.map(item => item * 10);
const result = arr.flatMap(item => item * 10);
console.log(result);
```

### Symbol.prototype.description

获取创建Symbol时的标识名称

```javascript
let s = Symbol('尚硅谷');
console.log(s.description); // 尚硅谷
```



## ES11

### String.prototype.matchAll  

例：

```javascript
let str = `
<ul>
<li>
<a>肖生克的救赎</a>
<p>上映日期：1994-09-10</p>
</li>
<li>
<a>阿甘正传</a>
<p>上映日期：1994-07-06</p>
</li>
</ul>
`
// 声明正则
let reg = /<li>.*?<a>(.*?)<\/a>.*?<p>(.*?)<\/p>/sg

// 调用方法
const result = str.matchAll(reg);
console.log(result);

// for (let v of result) {
//     console.log(v);
// }

const arr = [...result];
console.log(arr);
```



### 类的私有属性

类的私有属性使用`#`符号，被`#`修饰的成员只能在类中使用，在外部使用类的实例对象访问也会出错。

```javascript
class Person {
    // 公有属性
    name;
    // 私有属性
    #age;
    #weight;
    // 构造方法
    constructor(name, age, weight){
        this.name = name;
        this.#age = age;
        this.#weight = weight;
    }
intro () {
    console.log(this.#weight);
                console.log(this.#age);

                }
}
// 实例化
const girl = new Person('小红', 18, '45kg');
console.log(girl);
console.log(girl.name);
girl.intro()
//  Private field '#age' must be declared in an enclosing class
// console.log(girl.#age);
// console.log(girl.#weight);
```

### Promise.allSettled

```javascript
// 声明两个Promise对象
const p1 = new Promise((resolve, reject) => {
    setTimeout(()=>{
        resolve('商品数量 - 1')
    }, 1000)
})

const p2 = new Promise((resolve, reject) => {
    setTimeout(()=>{
        // resolve('商品数量 - 2')
        reject('出错了');
    }, 1000)
})
```

#### allSettled

调用 allSettled 方法，不管p1,p2成功与否都返回成功

```javascript
const result = Promise.allSettled([p1,p2]);
console.log(result);
```

#### all

all 方法，在都成功时才返回成功，在其中一个返回失败时，整体返回失败。

```javascript
const result = Promise.all([p1,p2])
console.log(result);
```

### 可选链操作符（`?.`）

```javascript
function main (config) {
    // 原来为了判断参数是否有确定的属性，需要逐层判断
    // const dbHost = config && config.db && config.db.host;
    // 现在直接使用可选链操作符`?.` 不会报错，顶多undefined
    const dbHost = config?.db?.host;
    console.log(dbHost);
}
main({
    db:{
        host:'127.0.0.1',
        username:'root'
    },
    catch:{
        host:'localhost',
        username:'root'
    }
})
```

### 动态 import 导入

使用import then，此时then方法返回的是模块对象

```javascript
import('./05.hello.js').then(module => {
    module.hello();
});
```

### globalThis 对象  

获取当前作用域顶级的this，不论环境是window还是node。都是它们的顶级this

```javascript
console.log(globalThis);//window
```

