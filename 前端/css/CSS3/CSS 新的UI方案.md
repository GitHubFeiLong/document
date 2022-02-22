# CSS 新的UI方案

## 文本新增样式

### opacity

```css
opacity:0.5;
```

opacity属性指定了一个元素的透明度

默认值：1.0    不可继承

### rgba

```css
background-color:rgba(0,0,0,.5)
```

指定透明度

### 文字阴影

下面的示例有两层文字阴影（多层使用逗号分隔）：

```css
text-shadow: red 10px 10px 2px, pink 20px 20px 2px; 
```

​		text-shadow用来为文字添加阴影，而且可以添加多层，阴影值之间用逗号隔开。（多个阴影时，第一个阴影在最上边）

默认值：none        不可继承 

值:

1. color:  可选。可以在偏移量之前或之后指定。如果没有指定颜色，则使用UA（用户代理）选择的颜色。

2. offset-x offset-y: 必选。这些长度值指定阴影相对文字的偏移量。

   > offset-x: 指定水平偏移量，若是负值则阴影位于文字左边。
   > offset-y: 指定垂直偏移量，若是负值则阴影位于文字上面。
   >
   > 如果两者均为0，则阴影位于文字正后方(如果设置了`blur-radius` 则会产生模糊效果)。
   >    

3. blur-radius：可选。这是 `length` 值。如果没有指定，则默认为0。值越大，模糊半径越大，阴影也就越大越淡

> 模糊背景
>
> ```css
> filter: blur(1px);
> ```

### 文字描边

```css
-webkit-text-stroke: 3px red;
```

只有webkit内核才支持：-webkit-text-stroke（准确的来说不能算是css3的东西，但需要大家知道）

### 文字排版

+ direction:控制文字的方向,一定要配合`unicode-bidi:bidi-override`;来使用

  ```css
  direction:rtl;
  unicode-bidi:bidi-override;
  ```

+ text-overflow :确定如何向用户发出未显示的溢出内容信号。它可以被剪切，显示一个省略号（'...'）

  ```css
  //white-space:nowrap;
  overflow: hidden;
  text-overflow:ellipsis;
  ```

## 盒模型新增样式

### 盒模型阴影

**box-shadow** 

```css
box-shadow: <inset> 10px 10px 5px, <rgba> 20px 20px 7px;
```

> 第一个参数可以设置阴影往元素内显示（inset），也可以设置颜色（rgba）

​		以逗号分割列表来描述一个或多个阴影效果，可以用到几乎任何元素上。 如果元素同时设置了 border-radius ，阴影也会有圆角效果。多个阴影时和多个 text-shadows 规则相同(第一个阴影在最上面)。

默认值:  none    不可继承

值：

1. inset：默认阴影在边框外。使用inset后，阴影在边框内。关键字(内 外阴影)

2. offset-x offset-y：这是头两个 `length `值，用来设置阴影偏移量。

   > offset-x : 设置水平偏移量，如果是负值则阴影位于元素左边。
   > offset-y: 设置垂直偏移量，如果是负值则阴影位于元素上面。
   > 如果两者都是0，那么阴影位于元素后面。这时如果设置了`blur-radius` 或`spread-radius` 则有模糊效果。

3. blur-radius：这是第三个 `<length>` 值。值越大，模糊面积越大，阴影就越大越淡。不能为负值。默认为0，此时阴影边缘锐利。

4.  spread-radius：这是第四个 `<length>` 值。取正值时，阴影扩大；取负值时，阴影.收缩。默认为0，此时阴影与元素同样大。
5. color： 阴影颜色，如果没有指定，则由浏览器决定

### 倒影

> webkit内核 文字描边 背景镂空

```css
-webkit-box-reflect:below;
```

-webkit-box-reflect  设置元素的倒影（准确的来说不能算是css3的东西，但需要大家知道）

默认值:none  不可继承

值：（必须是123的顺序）
第一个值 倒影的方向

+ above（上倒影）
+ below(下倒影), 
+ right（右倒影）, 

+ left（左倒影）

第二个值，倒影的距离 长度单位

第三个值，渐变

​	linear-gradient 线性渐变

 ```css
-webkit-box-reflect:left 10px linear-gradient(-90deg,rgba(0,0,0,1) 0,rgba(0,0,0,0) 80%);
 ```

### resize

```css
resize:both; 
overflow:auto;
```

resize CSS 属性允许你控制一个元素的可调整大小性。

> 一定要配合overflow：auto使用

默认值：none  不可继承

值：

+ none 元素不能被用户缩放。
+ both 允许用户在水平和垂直方向上调整元素的大小。
+ horizontal 允许用户在水平方向上调整元素的大小。 
+ vertical 允许用户在垂直方向上调整元素的大小。

### box-sizing

下面的例子，在不使用`box-sizing` 属性时，元素的宽高均为320px。

```css
.box{
	width:200px;
	height:200px;
	padding:50px;
	border:10px solid #000;
}
```

添加 `box-sizing:border-box;` 后，元素的宽高均为200px,

```css
.box{
	width:200px;
	height:200px;
	padding:50px;
	border:10px solid #000; 
    box-sizing:border-box;
}
```

box-sizing 属性用于更改用于计算元素宽度和高度的默认的 CSS 盒子模型。可以使用此属性来模拟不正确支持CSS盒子模型规范的浏览器的行/列为。

默认值：content-box  不可继承

值

+ content-box 默认值，标准盒子模型。 width 与 height 只包括内容的宽和高， 不包括边框（border），内边距（padding），外边距（margin）。

  > 注意: 内边距, 边框 & 外边距 都在这个盒子的外部。 比如. 如果 .box {width: 350px}; 而且 {border: 10px solid black;} 那么在浏览器中的渲染的实际宽度将是370px;
  > 尺寸计算公式：
  >        width = 内容的宽度，
  >        height = 内容的高度。
  >        宽度和高度都不包含内容的边框（border）和内边距（padding）。

+ border-box ：width 和 height 属性包括内容，内边距和边框，但不包括外边距。这是当文档处于 Quirks模式 时 Internet Explorer使用的盒模型。

  > 这里的维度计算为：
  > width = border + padding + 内容的  width，
  > height = border + padding + 内容的 height。

## 新增UI样式

### 圆角

border-radius： 用来设置边框圆角。当使用一个半径时，确定一个圆形；当使用两个半径时，确定一个椭圆，这个(椭)圆与边框的交集形成圆角效果。

默认值  :  0      不可继承

值：

+ 固定的px值定义圆形半径或椭圆的半长轴，半短轴。不能用负值，使用百分数定义圆形半径或椭圆的半长轴，半短轴。水平半轴相对于盒模型的宽度；垂直半轴相对于盒模型的高度。不能用负值。

  > 这是一个简写属性，用来设置 
  > border-top-left-radius,
  > border-top-right-radius,
  > border-bottom-right-radius ，
  > border-bottom-left-radius

+  半径的第一个语法取值可取1~4个值: 

  + border-radius: radius 

    > border-radius: 50px 100px 100px 50px;

+    半径的第二个语法取值也可取1~4个值

  > border-radius:200px/100px 0;

> 注意
> 百分比值
> 在旧版本的 Chrome 和 Safari 中不支持。(fixed in Sepember 2010)
>  在 11.50 版本以前的 Opera 中实现有问题。
> Gecko 2.0 (Firefox 4) 版本前实现不标准：水平半轴和垂直半轴都相对于盒子模型的宽度。
> 在旧版本的 iOS (iOS 5 之前) 和 Android 中 (WebKit 532 之前) 不支持。

### 边框图片

#### border-image-source

```css
border: 50px solid;
border-image-source: url('./img/border-image.png')
```

定义使用一张图片来代替边框样式；如果值为none，则仍然使用border-style 定义的样式。

默认值：none   不可继承

#### border-image-slice

```css
border: 50px solid;
border-image-source: url('./img/border-image.png');
border-image-slice:33.333% 33.333%;

// fill值：将裁剪后中间的图片设置成content的背景
border-image-slice:33.3333% 33.3333% fill;
```

border-image-slice 属性会通过规范将 border-image-source  的图片明确的分割为9个区域：四个角，四边以及中心区域。
并可指定偏移量

![image-20210329135410151](CSS 新的UI方案.assets/image-20210329135410151.png)

默认值：100%    不可继承。值得百分比参照于image本身！！

#### border-image-repeat

border-image-repeat 定义图片如何填充边框。或为单个值，设置所有的边框；或为两个值，分别设置水平与垂直的边框。

默认值：stretch  不可继承

值：
   stretch （拉伸）
   repeat，round（平铺）

#### border-image-width

border-image-width 定义图像边框宽度。 
默认值：1   不可继承

#### border-image-outset

border-image-outset属性定义边框图像可超出边框盒的大小

默认值：0  不可继承

正值： 可超出边框盒的大小

### 背景

#### background-color

background-color 会设置元素的背景色

默认值：  transparent    不可继承

> 平铺整个border-box

#### background-image

background-image属性用于为一个元素设置一个或多个背景图像，图像在绘制时，以z轴方向堆叠的方式进行。先指定的图像会在之后指定的图像上面进行绘制。

注意：background-color会在image之下进行绘制，边框和内容会在image之上进行绘制

默认值：none   不可继承

> 默认从padding-box开始绘制，从border-box开始剪裁，css3中有多背景，默认绘制时尺寸是自己的位图像素

#### background-repeat

background-repeat CSS 属性定义背景图像的重复方式。背景图像可以沿着水平轴，垂直轴，两个轴重复，或者根本不重复。

 默认值：repeat 不可继承

值： 
   repeat-x   =    repeat no-repeat
   repeat-y   =    no-repeat repeat
   repeat      =    repeat repeat 
   no-repeat  =    no-repeat no-repeat 

  第一个值代表水平方向。
  第二个值代表垂直方向。

#### background-position

指定背景位置的初始位置
默认值：0% 0%   不可继承

值：
   百分比：参照尺寸为背景图片定位区域的大小减去背景图片的大小  
          第一个值：元素在水平方向的位移
          第二个值：元素在垂直方向的位移 

   关键字：
       top left and left top
          Same as '0% 0%'.
       top, top center, and center top
          Same as '50% 0%'.
       right top and top right
           Same as '100% 0%'.
       left, left center, and center left
          Same as '0% 50%'.
       center and center center
          Same as '50% 50%'.
       right, right center, and center right
          Same as '100% 50%'.
       bottom left and left bottom
          Same as '0% 100%'.
       bottom, bottom center, and center bottom
          Same as '50% 100%'.
       bottom right and right bottom
          Same as '100% 100%'.

如果只有一个值被指定，则这个值就会默认设置背景图片位置中的水平方向，与此同时垂直方向的默认值被设置成50%。

#### background-attachment

background-attachment 决定背景是在视口中固定的还是随包含它的区块滚动的。

默认值：scroll 不可继承 

值：
   fixed
       此关键字表示背景相对于视口固定。即使一个元素拥有滚动机制，背景也不会随着元素的内容滚动 。
   scroll
       此关键字表示背景相对于元素本身固定， 而不是随着它的内容滚动

#### background-origin

设置背景的渲染的起始位置
       border-box：边框开始
       padding-box：padding开始
       content-box：内容开始

#### background-clip

> 设置文字裁剪
>
> ```css
> .box{
> 	width:300px;
> 	height:300px;
> 	background:url(tg.png) no-repeat center;
> 	-webkit-background-clip:text;
> }
> ```

设置背景裁剪位置

#### background-size 

background-size 设置背景图片大小

默认值：auto auto  不可继承

值：
   百分比：  指定背景图片相对背景区（background positioning area）的百分比。背景区由background-origin设置，默认为盒模型的内容区与内边距
   auto：  以背景图片的比例缩放背景图片。

注意：
  单值时，这个值指定图片的宽度，图片的高度隐式的为auto
  两个值: 第一个值指定图片的宽度，第二个值指定图片的高度      

#### background

background 是CSS简写属性，用来集中设置各种背景属性。background 可以用来设置一个或多个属性:background-color, background-image, background-position, background-repeat, background-size, background-attachment。 

默认值：    不可继承
background-image: none
background-position: 0% 0%
background-size: auto auto
background-repeat: repeat
background-origin: padding-box
background-clip: border-box
background-attachment: scroll
background-color: transparent

顺序无关

> 实现图片垂直水平居中
>
> ```css
> body:after{
>     content: "";
>     display: inline-block;
>     height: 100%;
>     vertical-align: middle;
> }
> img{
>     vertical-align: middle;
> }
> ```

### 渐变

CSS 渐变 是在 CSS3 Image Module 中新增加的图片类型；使用 CSS 渐变可以在两种颜色间制造出平滑的渐变效果. 用它代替图片，可以加快页面的载入时间、减小带宽占用。同时，因为渐变是由浏览器直接生成的，它在页面缩放时的效果比图片更好，因此你可以更加灵活、便捷的调整页面布局。


浏览器支持两种类型的渐变：线性渐变 (linear)，通过 linear-gradient 函数定义，以及 径向渐变 (radial)，通过 radial-gradient 函数定义.

#### 线性渐变	

​		为了创建一个线性渐变，你需要设置一个起始点和一个方向（指定为一个角度）。你还要定义终止色。终止色就是你想让浏览器去平滑的过渡过去，并且你必须指定至少两种，当然也会可以指定更多的颜色去创建更复杂的渐变效果。

1. 默认从上到下发生渐变

 ```css
linear-gradient(red,blue);
 ```

2. 改变渐变方向(to  top|bottom|right|left | top left | top right | bottom left | bottom right)：

```css
linear-gradient(to top,red,blue);
```

3. 使用角度

 ```css
linear-gradient(45deg,red,blue);
 ```

4. 颜色节点的分布（第一个不写为0%，最后一个不写为100%）

```css
linear-gradient(red 长度或者百分比,blue 长度或者百分比);
```

> 上面的`长度或者百分比` ：第一个表示在指定的长度或百分比之前，全是 纯red色， 第二个表示在指定长度或百分比之后全为纯blue色。 中间有过度色。

5. 重复渐变

```css
 repeating-linear-gradient(60deg,red 0,blue 30%);
```

> 重复的次数是，容器的长度 / 渐变两个长度之差
>
> 全纯色：
>
> ```css
> background-image: repeating-linear-gradient(red 100px ,red 200px,blue 200px, blue 300px)
> ```

#### 径向渐变

radial-gradient() 函数创建一个`<image>`，用来展示由原点（渐变中心）辐射开的颜色渐变

1. 默认均匀分布

```css
radial-gradient(red,blue);
```

2. 不均匀分布

```css
radial-gradient(red 50%,blue 70%);
```

> 50% 之前是纯色的red，50~70 红-蓝的渐变，70之后是纯蓝

3. 改变渐变的形状

```css
radial-gradient(circle ,red,blue)
```

   circle
   ellipse（默认为椭圆）

4. 渐变形状的大小

```css
 radial-gradient(closest-corner  circle ,red,blue)
```

> closest-side   最近边
> farthest-side  最远边
> closest-corner 最近角
> farthest-corner 最远角  （默认值）

5. 改变圆心

```css
radial-gradient(closest-corner  circle at 10px 10px,red,blue); 
```



## 层级

1. 浮动提升半层，只有在浮动的情况下，才需要考虑元素分两层，定位元素提一层，相对定位会在文档流你有残留。
2. z-index为1怎么都会比a高;z-index为-1怎么都会比a低

## 包含块

初始包含块：和视窗大小位置尺寸一样的矩形，滚动系统滚动条会影响初始包含块的位置。

### 禁止系统滚动条

```css
html,body{
    height:100%;
    overflow:hidden
}
```

