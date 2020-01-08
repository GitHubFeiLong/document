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

