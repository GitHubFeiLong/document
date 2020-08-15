# 一些工作中遇到的API

## 集合去重 distinct

```java
// 需要重写equals 和 hashcode方法
// 循环的内容已经去重了
list.stream().distinct().forEach(p->{
    
})
```

## 集合排序 sorted

```java
list1.stream().sorted(Comparator.comparing(BRcAAchedule::getSdate)).forEach(p->{
            System.out.println(p.toString());
        });
// 反序
list1.stream().sorted(Comparator.comparing(BRcAAchedule::getSdate).reversed()).forEach(p->{
            System.out.println(p.toString());
        });
```



## 流合并 concat

```java
 Stream.concat(p.getOldLists().stream(),p.getNowLists().stream())
     .distinct().sorted(Comparator.comparing(BRcAAchedule::getSdate)).forEach(o->{
		// CODE
 });
```





## LocalDateTime

```java

//获取秒数
Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
//获取毫秒数
Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

```

