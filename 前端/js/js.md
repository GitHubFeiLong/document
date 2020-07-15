## 数组

### 数组过滤

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



## 数组删除元素

### splice

使用数组的**splice**方法删除数组中指定范围元素。

该方法有两个参数：第一个参数表示删除元素起始位置，第二个参数表示删除元素的结束下标。

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

