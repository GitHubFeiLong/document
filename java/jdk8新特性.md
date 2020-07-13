## Default Methods for Interfaces

Java 8允许我们利用default关键字向接口添加非抽象方法实现。这个特性也称为虚拟扩展方法。

这是我们的第一个例子:

```java
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
```

除了抽象方法计算外，接口公式还定义了默认方法sqrt。具体类只需实现抽象方法计算。默认方法sqrt可以开箱即用。

```java
Formula formula = new Formula() {
    @Override
    public double calculate(int a) {
        return sqrt(a * 100);
    }
};

formula.calculate(100);     // 100.0
formula.sqrt(16);           // 4.0
```

该公式被实现为一个匿名对象。代码相当冗长:6行代码就可以完成这样一个简单的sqrt(a * 100)计算。我们将在下一节看到，在Java 8中有一种更好的实现单个方法对象的方法。

## Lambda expressions

让我们从一个简单的例子开始，如何排序的字符串列表在以前的版本的Java:

```java
List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return b.compareTo(a);
    }
});
```

静态实用程序方法集合。sort接受一个列表和一个比较器，以便对给定列表中的元素进行排序。您经常发现自己创建了匿名比较器并将它们传递给sort方法。

Java 8提供了更短的语法，lambda表达式，而不是整天创建匿名对象:

```java
Collections.sort(names, (String a, String b) -> {
    return b.compareTo(a);
});
```

如你所见，代码更短，更容易阅读。但它变得更短:

```java
Collections.sort(names, (String a, String b) -> b.compareTo(a));
```

对于一个行方法主体，可以跳过大括号{}和return关键字。但它变得更短:

```java
names.sort((a, b) -> b.compareTo(a));
```

List现在有一个排序方法。java编译器也知道参数类型，所以您也可以跳过它们。让我们更深入地研究lambda表达式如何在野外使用。

## Functional Interfaces(函数式接口)

lambda表达式如何适应Java的类型系统?每个lambda都对应于由接口指定的给定类型。所谓的函数接口必须包含一个抽象方法声明。该类型的每个lambda表达式都将被匹配到这个抽象方法。由于默认方法不是抽象的，您可以自由地将默认方法添加到函数接口。只要接口只包含一个抽象方法，我们就可以使用任意接口作为lambda表达式。为了确保您的接口满足需求，您应该添加@FunctionalInterface a

Example:

```java
@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}
```

```java
Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
Integer converted = converter.convert("123");
System.out.println(converted);    // 123
```

请记住，如果省略了@FunctionalInterface注释，那么代码也是有效的。

## Method and Constructor References(方法和构造函数引用)

利用静态方法引用，可以进一步简化上述示例代码:

```java
Converter<String, Integer> converter = Integer::valueOf;
Integer converted = converter.convert("123");
System.out.println(converted);   // 123
```

Java 8允许您通过::关键字传递方法或构造函数的引用。上面的示例展示了如何引用静态方法。但我们也可以引用对象方法:

```java
class Something {
    String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}
```

```java
Something something = new Something();
Converter<String, String> converter = something::startsWith;
String converted = converter.convert("Java");
System.out.println(converted);    // "J"
```

让我们看看`::`关键字如何在构造函数中工作。首先，我们定义一个例子类与不同的构造函数:

```java
class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
```

接下来，我们指定一个人工厂接口，用于创建新的人:

```java
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
```

与手工实现工厂不同，我们通过构造函数引用将所有东西粘在一起:

```java
PersonFactory<Person> personFactory = Person::new;
Person person = personFactory.create("Peter", "Parker");
```

我们通过Person::new创建对Person构造函数的引用。Java编译器通过匹配PersonFactory.create的签名自动选择正确的构造函数。

## Lambda Scopes

Accessing outer scope variables from lambda expressions is very similar to anonymous objects. You can access final variables from the local outer scope as well as instance fields and static variables.

从lambda表达式访问外部范围变量非常类似于匿名对象。您可以从局部外部范围以及实例字段和静态变量访问最终变量。

### Accessing local variables

我们可以从lambda表达式的外部范围读取最终局部变量:

```java
final int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

但是与匿名对象不同的是，变量num不必声明为final。此代码也是有效的:

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

但是，对于要编译的代码来说，num必须是隐式的final。下列程式码无法编译:

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);
num = 3;
```

从lambda表达式中写入num也是被禁止的。

### Accessing fields and static variables 访问字段和静态变量

与局部变量不同，我们可以从lambda表达式中读写实例字段和静态变量。这种行为在匿名对象中很常见。

```java
class Lambda4 {
    static int outerStaticNum;
    int outerNum;

    void testScopes() {
        Converter<Integer, String> stringConverter1 = (from) -> {
            outerNum = 23;
            return String.valueOf(from);
        };

        Converter<Integer, String> stringConverter2 = (from) -> {
            outerStaticNum = 72;
            return String.valueOf(from);
        };
    }
}
```

### Accessing Default Interface Methods 访问默认接口方法

还记得第一部分的公式例子吗?接口公式定义了一个默认方法sqrt，可以从包括匿名对象在内的每个公式实例访问该方法。这不适用于lambda表达式。默认方法不能从lambda表达式中访问。下列程式码无法编译:

```java
Formula formula = (a) -> sqrt(a * 100);
```

## Built-in Functional Interfaces 内置函数式接口

JDK 1.8 API包含许多内置的函数接口。其中一些在旧版本的Java中非常有名，比如Comparator或Runnable。那些现有的接口被扩展为通过@FunctionalInterface注释来支持Lambda。

但是Java 8 API也提供了许多新的功能接口，使您的工作更轻松。其中一些新的接口在谷歌Guava库中已经广为人知。即使您熟悉这个库，也应该密切关注一些有用的方法扩展如何扩展这些接口。

### Predicates

Predicates是一个参数的布尔值函数。该接口包含各种将谓词组合成复杂逻辑术语的默认方法(and, or, negate)

```java
Predicate<String> predicate = (s) -> s.length() > 0;

predicate.test("foo");              // true
predicate.negate().test("foo");     // false

Predicate<Boolean> nonNull = Objects::nonNull;
Predicate<Boolean> isNull = Objects::isNull;

Predicate<String> isEmpty = String::isEmpty;
Predicate<String> isNotEmpty = isEmpty.negate();
```

### Functions

函数接受一个参数并产生一个结果。默认方法可用于将多个函数链接在一起(组合，然后)。

```java
Function<String, Integer> toInteger = Integer::valueOf;
Function<String, String> backToString = toInteger.andThen(String::valueOf);

backToString.apply("123");     // "123"
```

### Suppliers

Suppliers生产给定的通用类型的结果。与Functions不同，Suppliers不接受参数。

```
Supplier<Person> personSupplier = Person::new;
personSupplier.get();   // new Person
```

### Consumers

Consumers表示要在单个输入参数上执行的操作。

```java
Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
greeter.accept(new Person("Luke", "Skywalker"));
```

### Comparators

Comparators在较早的Java版本中非常有名。Java 8向接口添加了各种默认方法。

```java
Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

Person p1 = new Person("John", "Doe");
Person p2 = new Person("Alice", "Wonderland");

comparator.compare(p1, p2);             // > 0
comparator.reversed().compare(p1, p2);  // < 0
```

## Optionals

Optionals不是功能接口，而是防止NullPointerException的实用工具。这是下一节的一个重要概念，因此让我们快速了解一下选项是如何工作的。

Optionals是一个简单的值容器，它可以是null或非null。考虑一个可能返回非空结果但有时什么也不返回的方法。在Java 8中，不是返回null，而是返回一个可选的值。

```java
Optional<String> optional = Optional.of("bam");

optional.isPresent();           // true
optional.get();                 // "bam"
optional.orElse("fallback");    // "bam"

optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
```

## Streams

```
A java.util.Stream represents a sequence of elements on which one or more operations can be performed. Stream operations are either intermediate or terminal. While terminal operations return a result of a certain type, intermediate operations return the stream itself so you can chain multiple method calls in a row. Streams are created on a source, e.g. a java.util.Collection like lists or sets (maps are not supported). Stream operations can either be executed sequentially or parallely.

Streams are extremely powerful, so I wrote a separate Java 8 Streams Tutorial. You should also check out Sequency as a similiar library for the web.

Let's first look how sequential streams work. First we create a sample source in form of a list of strings:

译文：
java.util。流表示可以在其上执行一个或多个操作的元素序列。流操作可以是中间操作，也可以是终端操作。终端操作返回特定类型的结果，而中间操作返回流本身，因此可以在一行中链接多个方法调用。流是在一个源上创建的，例如java.util。列表或集合之类的集合(不支持映射)。流操作可以顺序执行，也可以并行执行。
流是非常强大的，所以我编写了一个单独的Java 8流教程。你也应该检查出作为一个类似的图书馆为网络。
让我们首先看看顺序流是如何工作的。首先，我们创建一个字符串列表形式的样本源:
```

```java
List<String> stringCollection = new ArrayList<>();
stringCollection.add("ddd2");
stringCollection.add("aaa2");
stringCollection.add("bbb1");
stringCollection.add("aaa1");
stringCollection.add("bbb3");
stringCollection.add("ccc");
stringCollection.add("bbb2");
stringCollection.add("ddd1");
```

Java 8中的集合得到了扩展，因此您可以通过调用Collection.stream()或Collection.parallelStream()来创建流。下面几节解释最常见的流操作。

### Filter

Filter接受一个谓词来过滤流的所有元素。这个操作是中间的，它使我们能够对结果调用另一个流操作(forEach)。ForEach接受为过滤后的流中的每个元素执行使用者。ForEach是一个终端操作。它是空的，所以我们不能调用另一个流操作。

```java
stringCollection
    .stream()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa2", "aaa1"
```

### Sorted

Sorted是返回已排序的流视图的中间操作。元素按自然顺序排序，除非传递自定义比较器。

```java
stringCollection
    .stream()
    .sorted()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa1", "aaa2"
```

请记住，已排序只创建已排序的流视图，而不需要操作已备份集合的排序。stringCollection的顺序没有改变:

```java
System.out.println(stringCollection);
// ddd2, aaa2, bbb1, aaa1, bbb3, ccc, bbb2, ddd1
```

### Map

中间操作map通过给定的函数将每个元素转换成另一个对象。下面的示例将每个字符串转换为大写字符串。但是您也可以使用map将每个对象转换成另一种类型。结果流的泛型类型取决于传递给map的函数的泛型类型。

```java
stringCollection
    .stream()
    .map(String::toUpperCase)
    .sorted((a, b) -> b.compareTo(a))
    .forEach(System.out::println);

// "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
```

#### Map进行过滤

```java
Map<Integer, User> maps = new HashMap<>();
        maps.put(1, new User(1,"1"));
        maps.put(2, new User(2,"2"));
        maps.put(3, new User(3,"3"));
        maps.put(4, new User(4,"4"));

Map<Integer, User> collect = maps.entrySet().stream()
                .filter(v -> v.getValue().getName().equals("2") || v.getValue().getName().equals("1"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

collect.entrySet().forEach(p->{
            log.info("id:{},value:{}", p.getKey(), p.getValue());
        });
// 输出：
//09:05:05.423 [main] INFO broadcast.maps.MapDemo - id:1,value:User(id=1, name=1)
//09:05:05.432 [main] INFO broadcast.maps.MapDemo - id:2,value:User(id=2, name=2)
```





### Match

可以使用各种匹配操作来检查某个谓词是否与stream匹配。所有这些操作都是终端操作，并返回一个布尔结果。

```java
boolean anyStartsWithA =
    stringCollection
        .stream()
        .anyMatch((s) -> s.startsWith("a"));

System.out.println(anyStartsWithA);      // true

boolean allStartsWithA =
    stringCollection
        .stream()
        .allMatch((s) -> s.startsWith("a"));

System.out.println(allStartsWithA);      // false

boolean noneStartsWithZ =
    stringCollection
        .stream()
        .noneMatch((s) -> s.startsWith("z"));

System.out.println(noneStartsWithZ);      // true
```

#### Count

Count是一个终端操作，返回流中元素的长度。

```java
long startsWithB =
    stringCollection
        .stream()
        .filter((s) -> s.startsWith("b"))
        .count();

System.out.println(startsWithB);    // 3
```

### Reduce

这个终端操作使用给定的函数对流的元素进行约简。结果是一个可选的持有减少的值。

```java
Optional<String> reduced =
    stringCollection
        .stream()
        .sorted()
        .reduce((s1, s2) -> s1 + "#" + s2);

reduced.ifPresent(System.out::println);
// "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"
```

## Parallel Streams

如上所述，流可以是顺序的，也可以是并行的。对顺序流的操作在单个线程上执行，而对并行流的操作在多个线程上并发执行。

下面的示例演示了通过使用并行流来提高性能是多么容易。

首先，我们创建一个大的独特元素列表:

```java
int max = 1000000;
List<String> values = new ArrayList<>(max);
for (int i = 0; i < max; i++) {
    UUID uuid = UUID.randomUUID();
    values.add(uuid.toString());
}
```

现在我们测量排序这个集合流所花费的时间。

### Sequential Sort

```java
long t0 = System.nanoTime();

long count = values.stream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("sequential sort took: %d ms", millis));

// sequential sort took: 899 ms
```

### Parallel Sort 并行排序

```java
long t0 = System.nanoTime();

long count = values.parallelStream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("parallel sort took: %d ms", millis));

// parallel sort took: 472 ms
```

正如您所看到的，这两个代码段几乎相同，但是并行排序大约快50%。您所要做的就是将stream()更改为parallelStream()。

## Maps

如前所述，映射不直接支持流。Map接口本身没有stream()方法，但是您可以通过`map.keySet().stream()`, `map.values().stream()` and `map.entrySet().stream()`在Map的键、值或条目上创建专门的流。

此外，映射支持用于执行常见任务的各种新方法。

```java
Map<Integer, String> map = new HashMap<>();

for (int i = 0; i < 10; i++) {
    map.putIfAbsent(i, "val" + i);
}

map.forEach((id, val) -> System.out.println(val));
```

上面的代码应该是自我解释的:putIfAbsent防止我们写额外的如果null检查;forEach接受使用者对映射的每个值执行操作。



这个例子展示了如何利用函数在map上计算代码:

```java
map.computeIfPresent(3, (num, val) -> val + num);
map.get(3);             // val33

map.computeIfPresent(9, (num, val) -> null);
map.containsKey(9);     // false

map.computeIfAbsent(23, num -> "val" + num);
map.containsKey(23);    // true

map.computeIfAbsent(3, num -> "bam");
map.get(3);             // val33
```

接下来，我们学习如何删除一个给定键的条目，只有当它当前映射到一个给定的值:

```java
map.remove(3, "val3");
map.get(3);             // val33

map.remove(3, "val33");
map.get(3);             // null
```

另一个有用的方法:

```java
map.getOrDefault(42, "not found");  // not found
```

map的合并条目非常简单:

```java
map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9

map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9concat
```

如果键不存在，则将键/值放入映射中，否则将调用合并函数来更改现有的值。

## Date API

Java 8在包“Java .time”下包含一个全新的日期和时间API。新的Date API可以与[Joda-Time]库相媲美，但是它是[不一样的]。下面的示例涵盖了这个新API的最重要部分。

### Clock

Clock 提供对当前日期和时间的访问。Clock知道一个时区，可以使用它而不是' System.currentTimeMillis() '来检索当前时间(从Unix纪元算起，单位为毫秒)。在时间线上的这样一个瞬时点也由“瞬时”类表示。可以使用in创建遗留的 `java.util.Date` 对象。

```
Clock clock = Clock.systemDefaultZone();
long millis = clock.millis();

Instant instant = clock.instant();
Date legacyDate = Date.from(instant);   // legacy java.util.Date
```

### Timezones

时区由“ZoneId”表示。可以通过静态工厂方法轻松地访问它们。Timezones 定义了偏移量，这对于在瞬间和本地日期和时间之间进行转换非常重要。

```
System.out.println(ZoneId.getAvailableZoneIds());
// prints all available timezone ids

ZoneId zone1 = ZoneId.of("Europe/Berlin");
ZoneId zone2 = ZoneId.of("Brazil/East");
System.out.println(zone1.getRules());
System.out.println(zone2.getRules());

// ZoneRules[currentStandardOffset=+01:00]
// ZoneRules[currentStandardOffset=-03:00]
```

### LocalTime

LocalTime表示没有时区的时间，例如晚上10点或17:30:15。下面的示例为上面定义的时区创建两个本地时间。然后我们比较两个时间，并计算两个时间之间的小时和分钟的差异。

```
LocalTime now1 = LocalTime.now(zone1);
LocalTime now2 = LocalTime.now(zone2);

System.out.println(now1.isBefore(now2));  // false

long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

System.out.println(hoursBetween);       // -3
System.out.println(minutesBetween);     // -239
```

LocalTime提供了各种工厂方法来简化新实例的创建，包括时间字符串的解析。

```
LocalTime late = LocalTime.of(23, 59, 59);
System.out.println(late);       // 23:59:59

DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.GERMAN);

LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
System.out.println(leetTime);   // 13:37
```

### LocalDate

LocalDate表示一个不同的日期，例如2014-03-11。它是不可变的，工作方式完全类似于LocalTime。该示例演示了如何通过加减天数、月份或年份来计算新的日期。请记住，每个操作都返回一个新实例。

```
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
LocalDate yesterday = tomorrow.minusDays(2);

LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
System.out.println(dayOfWeek);    // FRIDAY
```

从字符串解析LocalDate与解析LocalTime一样简单:

```
DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.GERMAN);

LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
System.out.println(xmas);   // 2014-12-24
```

### LocalDateTime

LocalDateTime表示日期时间。它将上面几节中看到的日期和时间合并到一个实例中。' LocalDateTime '是不可变的，其工作方式类似于LocalTime和LocalDate。我们可以利用方法检索某些字段从日期时间:

```
LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
System.out.println(dayOfWeek);      // WEDNESDAY

Month month = sylvester.getMonth();
System.out.println(month);          // DECEMBER

long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
System.out.println(minuteOfDay);    // 1439
```

有了时区的附加信息，它可以转换成一个瞬间。瞬间可以很容易地转换为类型的遗留日期`java.util.Date`.

```
Instant instant = sylvester
        .atZone(ZoneId.systemDefault())
        .toInstant();

Date legacyDate = Date.from(instant);
System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014
```



#### 时间运算

```java
// 时间减（以 minus 开头，有秒，纳秒，分钟，小时，天，周，月，年）
LocalDateTime localDateTime = LocalDateTime.now();
localDateTime.minusDays(1);

// 时间加（以plus 开头，有秒，纳秒，分钟，小时，天，周，月，年）
LocalDateTime localDateTime = LocalDateTime.now();
localDateTime.plusDays(1);
```



#### 格式化时间

格式化date-times就像格式化dates or times一样。我们可以从自定义模式创建格式化器，而不是使用预定义的格式。

```
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
LocalDateTime localDateTime = LocalDateTime.parse("2020-07-10", formatter);

```

Unlike `java.text.NumberFormat` the new `DateTimeFormatter` 是不可变的，并且是线程安全的。

有关模式语法的详细信息，请阅读 [here](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html).

## Annotations

Java 8中的注释是可重复的。让我们直接来看一个例子。

首先，我们定义一个包装器注释，它包含一个实际注释的数组:

```
@interface Hints {
    Hint[] value();
}

@Repeatable(Hints.class)
@interface Hint {
    String value();
}
```

Java 8允许我们通过声明注释来使用同一类型的多个注释`@Repeatable`.

### Variant 1: Using the container annotation (old school)

```
@Hints({@Hint("hint1"), @Hint("hint2")})
class Person {}
```

### Variant 2: Using repeatable annotations (new school)

```
@Hint("hint1")
@Hint("hint2")
class Person {}
```

使用variant 2,java编译器隐式地设置了“@ hint”注释。这对于通过反射读取注释信息非常重要。

```
Hint hint = Person.class.getAnnotation(Hint.class);
System.out.println(hint);                   // null

Hints hints1 = Person.class.getAnnotation(Hints.class);
System.out.println(hints1.value().length);  // 2

Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
System.out.println(hints2.length);          // 2
```

虽然我们从未在“Person”类中声明“@ hint”注释，但它仍然可以通过“getAnnotation(Hints.class)”来读取。但是，更方便的方法是'getAnnotationsByType '，它允许直接访问所有带注释的' @Hint '注释。

此外，Java 8中注释的使用扩展到两个新目标:

```
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface MyAnnotation {}
```

## Where to go from here?

My programming guide to Java 8 ends here. If you want to learn more about all the new classes and features of the JDK 8 API, check out my [JDK8 API Explorer](http://winterbe.com/projects/java8-explorer/). It helps you figuring out all the new classes and hidden gems of JDK 8, like `Arrays.parallelSort`, `StampedLock` and `CompletableFuture` - just to name a few.

I've also published a bunch of follow-up articles on my [blog](http://winterbe.com/) that might be interesting to you:

- [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
- [Java 8 Nashorn Tutorial](http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/)
- [Java 8 Concurrency Tutorial: Threads and Executors](http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/)
- [Java 8 Concurrency Tutorial: Synchronization and Locks](http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/)
- [Java 8 Concurrency Tutorial: Atomic Variables and ConcurrentMap](http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/)
- [Java 8 API by Example: Strings, Numbers, Math and Files](http://winterbe.com/posts/2015/03/25/java8-examples-string-number-math-files/)
- [Avoid Null Checks in Java 8](http://winterbe.com/posts/2015/03/15/avoid-null-checks-in-java/)
- [Fixing Java 8 Stream Gotchas with IntelliJ IDEA](http://winterbe.com/posts/2015/03/05/fixing-java-8-stream-gotchas-with-intellij-idea/)
- [Using Backbone.js with Java 8 Nashorn](http://winterbe.com/posts/2014/04/07/using-backbonejs-with-nashorn/)

You should [follow me on Twitter](https://twitter.com/winterbe_). Thanks for reading!