# CSS 选择器

> 其它小知识点：
>
> CSS 全称：Cascading style sheets
>
> 样式表的组成：规则  --->  选择器+声明块 ---> 声明 ---> CSS合法的属性名+属性值
>
> 浏览器渲染样式表的顺序:从右往左

## 基本选择器

例如：

```html
<div>
    <input type="text" class="input-class" id="input-id">
    <input type="passweord" class="input2-class" id="input2-id">
</div>
```

### 通配符选择器(`*`)

```css
* {
    margin:0;
    padding:0;
}
```

### 元素选择器（任何一个HTML元素）

```css
body{
    color:pink;
}
```

### 类选择器（`.`）

```css
.input-class {
    font:10px/20px '微软雅黑';
}
```

### ID选择器（`#`）

```css
#input-id{
    width:100px;
}
```

### 后代选择器（`空格`）

```css
div input {
    height:75px;
}
```

### 选择器的分组（`用逗号隔开`）

```css
div,input{
    background-color:pink;
}
```

### 基本选择器的扩展

```html
<ul>
    <li id='li'>1</li>
    <p>heloo</p>
    <li>3</li>
    <li>4</li>
</ul>
```

#### 子元素选择器

```css
ul > li{}
```

> 也可称为直接后代选择器,此类选择器只能匹配到直接后代，不能匹配到深层次的后代元素

#### 相邻兄弟选择器

```css
// 匹配#li后面紧跟的li元素，必须紧跟
#li + li {}
```

> 使用+来选择，它只会匹配紧跟着的兄弟元素
>

#### 通用兄弟选择器

```css
// 兄弟是li元素的被匹配
#li ~ li {}
```

> 使用~来选择。匹配后面的同胞元素（不需要紧跟）
>

#### 选择器分组

```css
h1,h2,h3{color: pink;} 
```

> 此处的逗号我们称之为结合符

## 属性选择器

### 存在和值属性选择器

#### 第一类

[attr]：该选择器选择包含 attr 属性的所有元素，不论 attr 的值为何。

#### 第二类

[attr=val]：该选择器仅选择 attr 属性被赋值为 val 的所有元素。

#### 第三类

 [attr~=val]：该选择器仅选择 attr 属性的值（以空格间隔出多个值）中有包含 val 值的所有元素，比如位于被空格分隔的多个类（class）中的一个类。

### 子串值属性选择器

1. [attr|=val] : 选择attr属性的值以val（包括val）或`val-`开头的元素

2. [attr^=val] : 选择attr属性的值以val开头（包括val）的元素。
3. [attr$=val] : 选择attr属性的值以val结尾（包括val）的元素。
4. [attr*=val] : 选择attr属性的值中包含字符串val的元素。

## 伪类与伪元素选择器

### 链接伪类

> 注意 `:link，:visited，:target`是作用于链接元素的！

#### 锚点伪类

1. link：表示作为超链接，并指向一个未访问的地址的所有锚（`a:link{}`）
2. visited：表示作为超链接，并指向一个已访问的地址的所有锚(`a:visited{}`)

#### 目标伪类

1. target 代表一个特殊的元素，它的id是URI的片段标识符（`:target `）

   > 锚点被点击了，URL上会有锚点的位置，然后就会匹配该锚点的元素

### 动态伪类

1. hover：悬浮到目标元素上(`a:hover`)
2. active：表示匹配被用户激活的元素（点击按住时）(`a:active`)

> 由于a标签的:link和:visited可以覆盖了所有a标签的状态，所以当:link，:visited，:hover，:active同时出现在a标签身上时 :link和:visited不能放在最后！！！
>
> 只有下列的属性才能被应用到已访问链接(:visited选择器)：
>
> color
> background-color
> border-color

### 表单相关伪类

1. enabled：匹配可编辑的表单（`input:enabled`）
2. disabled：匹配被禁用的表单（`input:disabled`）
3. checked：匹配被选中的表单(`input:checked`)
4. focus：匹配获焦的表单

### 结构性伪类（`:xxx`）

> index的值从1开始计数！！！！
> index可以为变量n(只能是n)
> index可以为even odd

+ nth-child(n)

  > 匹配子元素，有一个参数，可以写数字，表示具体的第几个子元素，也可以写变量表达式（变量只能是n），也可以写关键字（odd奇数，enen 偶数）

+ first-child（last_child）

  > 父元素下的第一个子元素(最后一个子元素)

+ nth-last-child(n)

  > 匹配父元素下子元素的倒数第几个
  >
  > ```css
  > ul > li:nth-last-child(3) {
  > 	background: #f00;
  > }
  > ```

+ only-child

  > 匹配只有一个子元素的元素

+ nth-of-type(n)

  > 注意：n从0开始
  >
  > 匹配子元素中指定类型的元素的第几个被匹配
  >
  > 匹配子元素是div且是第二个子元素
  >
  > ```css
  > div:nth-of-type(2) {
  > 	color: #f00;
  > }
  > ```

+ first-of-type

  > 匹配子元素是div，且子元素是div的第一个元素
  >
  > ```css
  > div:first-of-type{}
  > ```

+ last-of-type

  > 匹配子元素是div，且子元素是div的最后一个元素
  >
  > ```css
  > div:last-of-type{}
  > ```

+ nth-last-of-type(n)

  > 匹配子元素是div，且子元素是div,从后往前数的第n个元素

+ only-of-type

  > 子元素只有一个目标元素
  >
  > ```css
  > div:only-of-type {
  > 	background: #f00;
  > }
  > ```

+ not

  > 取反
  >
  > ```css
  > div > a:not(:last-of-type) {}
  > ```

+ empty

  > 空元素,没有内容,没有空格回车
  >
  > ```html
  > <div></div>
  > ```

### 伪元素（`::`）

+ after:后面

  ```css
  div::after {
  	content: "我在内容的后面";
  }
  ```

+ before：前面

  ```css
  div::before {
  	content: "我在内容的前面";
  }
  ```

+ firstLetter：内容第一个字符

  ```css
  div::first-letter {
  	color: #f00;
  	font-size: 24px;
  	font-weight: bold;
  }
  ```

+ firstLine：内容第一行

  ```css
  div::first-line {
  	color: #f00;
  	font-weight: bold;
  }
  ```

+ selection：文字被选中时的样式

  ```css
  div::selection {
  	background: red;
  	color: pink;
  }
  ```

> 使用a标签的 link visited 需要放在 hover 和active之前。

## 声明的优先级

## 选择器的特殊性

​		选择器的特殊性由选择器本身的组件确定，特殊性值表述为4个部分，如    0,0,0,0
一个选择器的具体特殊性如下确定：
​       1.对于选择器中给定的ID属性值，加 0,1,0,0
​       2.对于选择器中给定的各个类属性，属性选择，或伪类，加 0,0,1,0
​       3.对于选择器中的给定的各个元素和伪元素，加0,0,0,1
​       4.通配符选择器的特殊性为0,0,0,0
​       5.结合符对选择器特殊性没有一点贡献
​       6.内联声明的特殊性都是1,0,0,0
​       7.继承没有特殊性

特殊性 1,0,0,0 大于所有以0开头的特殊性选择器的特殊性最终都会授予给其对应的声明如果多个规则与同一个元素匹配，而且有些声明互相冲突时，特殊性越大的越占优势

注意：id选择器和属性选择器 

div[id="test"]（0,0,1,1） 和 #test（0,1,0,0）           

## 重要性             

​		有时某个声明比较重要，超过了所有其他声明，css2.1就称之为重要声明并允许在这些声明的结束分号之前插入  !important  来标志。


​		必须要准确的放置  !important  ，否则声明无效。 !important 总是要放在声明的最后，即分号的前面。

​		标志为 !important的声明并没有特殊的特殊性值，不过要与非重要声明分开考虑。
 		实际上所有的重要声明会被浏览器分为一组，重要声明的冲突会在其内部解决，非重要声明也会被分为一组，非重要声明的冲突也会在其内部解决，如果一个重要声明与非重要声明冲突，胜出的总是重要声明。

## 继承

​		继承没有特殊性，甚至连0特殊性都没有
​		0特殊性要比无特殊性来的强

## 来源

css样式的来源大致有三种
  创作人员
  读者
  用户代理   

权重：
   读者的重要声明
   创作人员的重要声明
   创作人员的正常声明
   读者的正常声明
   用户代理的声明

## 层叠

层叠：
   1.找出所有相关的规则，这些规则都包含一个选择器
   2.计算声明的优先级
          先按来源排序
          在按选择器的特殊性排序
          最终按顺序