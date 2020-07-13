# CSS 常用骚操作

## 内容过长时隐藏显示三个点

```css
overflow: hidden;
white-space: nowrap;
text-overflow: ellipsis;
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

