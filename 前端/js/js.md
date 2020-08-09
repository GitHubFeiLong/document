# 数组

## 数组循环

### 方法一：普通for循环

```javasc
for(i = 0; i <= arr.length; i++){console.log(arr[i])}
```

### 方法二：forEach循环

```javasc
var arr = ['北京', '上海', '广州', '重庆', '天津'];
arr.forEach(function (item, index, arr) {
    console.log(val, key, arr);
})
```

### 方法三：for..in 循环

```javasc
var arr = ['北京','上海','广州','重庆','天津'];

for(var key in arr){  // 自定义变量,存储索引 0 1 2 ...
	console.log(key,arr[key]); 
}
```



## 数组排序

```javascript
// 注意数字11 小于 2
arr.sort(function(a,b){
	return a - b;
})
```





## 数组过滤

```javascript
/**
 * @method reduce
 * @param {number}   item   当前迭代的数组元素
 * @param {number}   index  当前迭代的数组元素下下标
 * @param {array}    array  原数组
 */
  let arr = [1,2,6,3,4,5];
  let res = arr.filter(function(item,index,array){
      //元素值，元素的索引，原数组。
      return (item>3);
  });
  console.log(res);//[6, 4, 5]
```



## 数组拼接

```javascript
var a = [1,2,3];
var b = [11,22,33];

var c = a.concat(b);
console.log(c);// [1, 2, 3, 11, 22, 33]
```



## 数组转成字符串

```javascript
// 在js中，我们有时会返回字符串做页面的标签，但是一长串的字符串很难维护。这时，我们可以将其拆分成几个字符串数组，然后进行join拼接返回。
//例如：
let arr = [
    '<div>',
    '<button type="button" class="btn btn-danger"  singleSelected=true>删 除</button>',
    '</div>'
]
// 这里使用空格进行拼接
return arr.join('');
```



## 字符串数组转成数字数组

例如将["08","02"] 转成[8,2]

```javasc
var arr = ["08","02"];
arr.map(Number);
console.log(arr);	//[8,2]
```



## 数组删除元素

### splice

使用数组的**splice**方法删除数组中指定范围元素，方法返回被删除的元素 数组。

该方法有两个参数：第一个参数表示删除元素起始位置，第二个参数表示删除几个元素。

该方法会返回被删除的元素组成的一个数组，原数组会被改变

```javascript
var arr = [1,2,3]
var del_arr = arr.splice(0,1);
console.log(del_arr); //[1]
console.log(arr);// [2,3]
```





# Event

## target

范例：

```javascript
// 使用event.target设置样式
/// 获取点击的文本
console.log(event.target.text);
// 设置点击对象的样式
event.target.style.color = 'red';

//转换成JQuery
$(event.target).addClass('test-class');
```



# 使用JavaScript设置/获取CSS 变量

## 使用javascript设置页面的css3变量

范例：

```css
/*定义css变量*/
:root {
    --nav-head-bgcolor:#e3e4e5;
}
```

```javasc
// 修改上面的css变量。
document.documentElement.style.setProperty('--nav-head-bgcolor','red')
```



## 使用javascript获取页面定义的css3变量

```javascript

getComputedStyle(document.documentElement).getPropertyValue('--my-variable-name');
```



