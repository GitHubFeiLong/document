# 一些工作中遇到的API

# 集合

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

### 有null的值进行排序

​		下面的集和 list 中的元素 topic 的属性 topicNum 可能为null值，这时使用上面的直接排序就会出现异常。

​		下面的代码重点语句：`Comparator.comparing(topic -> topic.getTopicNum(), Comparator.nullsLast(Integer::compareTo))`,除了`nullsLast` 还有`nullsFirst`

> `nullsLast` : null的记录放在后面

```java
 list.stream().sorted(Comparator.comparing(topic -> topic.getTopicNum(), Comparator.nullsLast(Integer::compareTo)))
                .forEach(f->{
                    int num = Optional.ofNullable(f.getTopicNum()).orElse(0) + i.get();
                    f.setTopicNum(num);
                    // 自增
                    i.getAndIncrement();
                });
```

自定义排序方式

```java
 HashMap<String, Integer> keyMap = new HashMap(){{
            put("单选题", 1);
            put("多选题", 2);
            put("判断题", 3);
            put("填空题", 4);
            put("主观题", 5);
        }};

        List<String> list1 = Arrays.asList("主观题","填空题","单选题","判断题","多选题");
        list1.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return keyMap.get(o1).compareTo(keyMap.get(o2));
            }
        });

        log.info(list1.toString());
```







## 流合并 concat

```java
 Stream.concat(p.getOldLists().stream(),p.getNowLists().stream())
     .distinct().sorted(Comparator.comparing(BRcAAchedule::getSdate)).forEach(o->{
		// CODE
 });
```



## ListIterator

集合循环时根据条件添加删除元素

```java
List list = new ArrayList<>(Arrays.asList(new String[]{"1","2","3"}));
        System.out.println("list = " + list);
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.equals("3")) {
//                iterator.remove();
                iterator.add("4");
            }
        }
```



## 分组

```java
List<Apple> appleList = new ArrayList<>();//存放apple对象集合
 
Apple apple1 =  new Apple(1,"苹果1",new BigDecimal("3.25"),10);
Apple apple12 = new Apple(1,"苹果2",new BigDecimal("1.35"),20);
Apple apple2 =  new Apple(2,"香蕉",new BigDecimal("2.89"),30);
Apple apple3 =  new Apple(3,"荔枝",new BigDecimal("9.99"),40);
 
appleList.add(apple1);
appleList.add(apple12);
appleList.add(apple2);
appleList.add(apple3);

//List 以ID分组 Map<Integer,List<Apple>>
Map<Integer, List<Apple>> groupBy = appleList.stream().collect(Collectors.groupingBy(Apple::getId));
 
System.err.println("groupBy:"+groupBy);
{1=[Apple{id=1, name='苹果1', money=3.25, num=10}, Apple{id=1, name='苹果2', money=1.35, num=20}], 2=[Apple{id=2, name='香蕉', money=2.89, num=30}], 3=[Apple{id=3, name='荔枝', money=9.99, num=40}]}
```





## LocalDateTime

```java

//获取秒数
Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
//获取毫秒数
Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

```

