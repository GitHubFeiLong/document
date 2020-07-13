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

