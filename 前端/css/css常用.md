# CSS 常用骚操作

## 内容过长时隐藏显示三个点

```css
overflow: hidden;
white-space: nowrap;
text-overflow: ellipsis;
```

> 如果出现排版错误
>
> 添加`vertical-align: top;`

## 内容过长时换行

```css
.content{
     word-break: break-all
}
```





## background-image 铺满元素

```css
display: block;
background-repeat: no-repeat;
background-position: center center;
background-size: 100% 100%;
```





## 垂直居中元素内部

```css
/*父元素*/
width:300px;
height:300px;
position:relative;
```



```css
/*子元素*/
width:100px;
height:100px;
position: absolute;
top: 50%;
transform: translate(0, -50%);
```



## 媒体查询

#### html需要添加meta标签

可以加多个meta标签

```html
<meta name="viewport" content="width=device-width, initial-scale=1">
```



注意，将小分辨率卸载最下面。

```css
/*屏幕最大可见宽度为414 （手机plus）*/
@media only screen and (max-device-width: 414px) {
    css code
}

/*最大宽度375px (普通手机)时生效*/
@media only screen and (max-device-width: 375px) {
    css code
}
```



### 过度

```css
/*过度属性*/
transition-property:background-color;
/*过渡时间*/
transition-duration:0.5s;
```

