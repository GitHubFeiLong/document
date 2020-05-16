

# RabbitMQ

## 1.MQ简介

MQ全称为Message Queue,消息队列（MQ）是一种应用程序对应用程序的通信方法。

![1589552412417](..\typora-user-images\1589552412417.png)

## 2.使用场景

### 2.1.流量削峰

##### 流量削峰的由来

主要是还是来自于互联网的业务场景，例如，马上即将开始的春节火车票抢购，大量的用户需要同一时间去抢购；以及大家熟知的阿里双11秒杀， 短时间上亿的用户涌入，瞬间流量巨大（高并发），比如：200万人准备在凌晨12:00准备抢购一件商品，但是商品的数量缺是有限的100-500件左右。

这样真实能购买到该件商品的用户也只有几百人左右， 但是从业务上来说，秒杀活动是希望更多的人来参与，也就是抢购之前希望有越来越多的人来看购买商品。

但是，在抢购时间达到后，用户开始真正下单时，秒杀的服务器后端缺不希望同时有几百万人同时发起抢购请求。

我们都知道服务器的处理资源是有限的，所以出现峰值的时候，很容易导致服务器宕机，用户无法访问的情况出现。



### 	2.2 日志处理

![1589604749493](..\typora-user-images\1589604749493.png)

### 	2.3 应用解耦

![1589620058029](..\typora-user-images\1589620058029.png)

### 	2.4 异步处理

![1589620463579](..\typora-user-images\1589620463579.png)

![1589552609597](..\typora-user-images\1589552609597.png)



## 3. RabbitMQ简介

​	**简介**

​			RabbitMQ采用Erlang语言开发，是实现了高级消息队列协议(AMQP Advanced Message Queuing Protocol)	的开源消息中间件。

​	**优点：**

​		性能很好，延时低

​		吞吐量到万级

​		有良好的管理界面管理工具

​		社区相对比较活跃

​	**缺点：** 吞吐量相对低



## 4. AMQP 协议

​	**Advanced Message Queuing Protocol 高级消息队列协议**

![1589552849951](..\typora-user-images\1589552849951.png)

## 5. Windows环境下单节点安装

### 下载安装包

下载RabbitMQ

https://www.rabbitmq.com/install-windows.html

查看该RabbitMQ所需的Erlang版本

https://www.rabbitmq.com/which-erlang.html

![1589594885182](..\typora-user-images\1589594885182.png)

下载erlang https://www.erlang.org/downloads

![1589594947922](..\typora-user-images\1589594947922.png)

### 安装erlang：

双击默认安装，选择安装目录，目录不能有中文字符，计算机名不能有中文字符

配置erlang环境变量

打开cmd输入erl

![1589597994604](..\typora-user-images\1589597994604.png)



### 安装rabbitmq

![1589599707110](..\typora-user-images\1589599707110.png)

激活rabbitMQ UI界面：

进入rabbit的sbin目录下运行

![1589598250718](..\typora-user-images\1589598250718.png)



重启服务：

![1589598540640](..\typora-user-images\1589598540640.png)

浏览器访问：http://localhost:15672/
输入username：guest
输入password:guest

![1589595207562](..\typora-user-images\1589595207562.png)

​	账号：guest 密码：guest





## 6.RabbitMQ 管理界面使用

​	浏览器地址栏输入：localhost:15672

​	用户名：guest	密码：guest

![1589599026627](..\typora-user-images\1589599026627.png)



### 	添加用户

​			**Tags**

​				Admin:超级管理员

​				Monitoring：监控者，可以查看节点信息

​				Policymaker：策略制定者（镜像）

​				Management：

​				Impersonator：

​				None

![1589600574884](..\typora-user-images\1589600574884.png)

​	添加了一个admin 密码123456，Tags：Administrator

![1589600757375](..\typora-user-images\1589600757375.png)



### 	添加Virtual Host

​			Virtual Host相当于mysql的 db

​			添加：一般以“/”开头



![1589602462124](..\typora-user-images\1589602462124.png)

​	添加后：

![1589602488747](..\typora-user-images\1589602488747.png)

### 	授权：

​		设置admin访问vhost01

​		点击用户名

![1589602543601](..\typora-user-images\1589602543601.png)



​		选择Virtual Host，然后Set permission

![1589602610893](..\typora-user-images\1589602610893.png)



![1589602692971](..\typora-user-images\1589602692971.png)



## 7.RabbitMQ 消息种类

### 	7.1 simple	简单队列

![1589607294455](..\typora-user-images\1589607294455.png)



​	**P:消息生产者**

​	**红色：队列**

​	**C：消费者**



​	**3个对象：生产者 队列 消费者**



#### 		7.1.1获取连接工具类 ConnectionUtil：

```java
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class ConnectionUtil {


    /**
     * 获取MQ的连接
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 设置服务地址
        factory.setHost("localhost");

        // AMQP 5672
        factory.setPort(5672);

        // vhost
        factory.setVirtualHost("/vhost01");

        // 用户名
        factory.setUsername("admin");

        // 密码
        factory.setPassword("123456");

        return factory.newConnection();
    }
}
```

#### 		7.1.2消息生产者：Send

```java
public class Send {
    private static final String QUERE_NAME="test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取一个连接
        Connection connection = ConnectionUtil.getConnection();

        // 从连接中获取一个通道
        Channel channel = connection.createChannel();

        // 声明一个队列
        channel.queueDeclare(QUERE_NAME, false, false, false, null);

        String message = "hello simple !";

        channel.basicPublish("", QUERE_NAME, null, message.getBytes());

        System.out.println("send message");

        channel.close();
        connection.close();

    }
}
```

#### 		7.1.3 消费者：Recv

```java
public class Recv {
    private static final String QUERE_NAME="test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();

        // 创建频道
        Channel channel = connection.createChannel();

        // 队列声明
        channel.queueDeclare(QUERE_NAME, false, false,false, null);

        // 定义队列的消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            // 获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                super.handleDelivery(consumerTag, envelope, properties, body);
                String msgString = new String(body,"utf-8");
                System.out.println("new api recv: " + msgString);
            }
        };

        // 监听队列
        channel.basicConsume(QUERE_NAME, true, defaultConsumer);
    }
}
```



#### 		7.1.4 简单队列的不足：

​						耦合性高，生产者一一对应消费者（如果我想有多个消费者消费队列中消息，这时候就不行），队				列名变更，这时候得同时变更



### 7.2 work queues    工作队列，公平分发轮询分发

![1589607306821](..\typora-user-images\1589607306821.png)

#### 	7.2.1 **为什么会出现工作队列：**

​			simple 队列是一一对应的，而且我们实际开发，生产者发送消息是毫不费力的，而消费者一般是要跟业务	相结合的，消费者接收到消息之后就需要处理，可能需要花费时间，这时候队列就会积压了很多消息



#### 	7.2.2 分发轮询

#### 7.2.2.1 消息生产者：Send

```java

public class Send {

    public static final String QUEUE_NAME = "test_work_queue";
    /**
     *                  |--> C2
     * P ---> Queue ----|
     *                  |--> C1
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发送消息
        for (int i = 0; i < 50; i++) {
            String msg= "send hello " + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("【WQ】 send msg = " + msg);
            Thread.sleep(i*20);
        }
        // 关闭资源
        channel.close();
        connection.close();

    }
}
```



#### 	7.2.2.2 消费者：Recv1：

```java

public class Recv1 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body, "utf-8");
                System.out.println("Recv [1] msg = " + msg);
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("Recv [1] done!");
                }
            }
        };

        // 监听队列
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
```

#### 	7.2.2.3 消费者：Recv2

```java
public class Recv2 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        // 定义一个消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body, "utf-8");
                System.out.println("Recv [2] msg = " + msg);
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    System.out.println("Recv [2] done!");
                }
            }
        };

        // 监听队列
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);
    }
}
```



#### 		7.2.2.4现象：

​			 	消费者1 和消费者2处理消息的数量是一样的

​				消费者1：偶数
​				消费者2：奇数
​				这种方式叫做轮询分发（round-robin）结果就是不管谁忙或者谁清闲 都不会多给一个消息。任务总是你一个我一个



#### 7.2.3 公平分发 fair dipatch

**需要手动回执**

```java
// MQ一次只发一个请求给消费者，当消费者处理完消息后会手动回执，然后MQ再发一个消息给消费者
channel.basicQos(1);

boolean autoAck = false; //false 手动回执,处理完消息后，告诉MQ
        channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);
```



#### 7.2.3.1 生产者

```java
public class Send {

    public static final String QUEUE_NAME = "test_work_queue";
    /**
     *                  |--> C2
     * P ---> Queue ----|
     *                  |--> C1
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        /**
         * 每个消费者 发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
         * 限制发送给同一个消费者 不得超过一条消息
         */
        channel.basicQos(1);

        // 发送消息
        for (int i = 0; i < 50; i++) {
            String msg= "send hello " + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("【WQ】 send msg = " + msg);
            Thread.sleep(i*5);
        }
        // 关闭资源
        channel.close();
        connection.close();

    }
}
```



### 7.2.3.2 消费者1

```java
public class Recv1 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);


        channel.basicQos(1);

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body, "utf-8");
                System.out.println("Recv [1] msg = " + msg);
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("Recv [1] done!");

                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // 监听队列
//        boolean autoAck = true; //自动应答
        boolean autoAck = false; //手动应答
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
```



###  7.2.3.3 消费者2

```java
public class Recv2 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        channel.basicQos(1);
        // 定义一个消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body, "utf-8");
                System.out.println("Recv [2] msg = " + msg);
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    System.out.println("Recv [2] done!");
                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // 监听队列
        boolean autoAck = false; //false 手动回执
        channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);

    }
}
```



#### 7.2.3.4 现象：

**消费者2 处理的消息比消费者1多，能者多劳**



### 7.3 publish/subscribe   发布订阅 订阅模式

![1589607321582](..\typora-user-images\1589607321582.png)

1.一个生产者，多个消费者
2.每个消费者都有自己的队列
3.生产者没有直接把消息发送到队列，而是发送到**交换机转换器 exchange**
4.每个队列都要绑定到交换机上
5.生产者发送的消息，经过交换机，到达队列。就能实现一个消息被多个消费者消费

#### 生产者

```java
public class Send {
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); //分发

        // 发送消息
        String msg = "hello ps";
        channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
        System.out.println("Send msg = " + msg);
        channel.close();
        connection.close();
    }
}
```

![1589616821844](..\typora-user-images\1589616821844.png)

消息哪去了？？ 丢失了，因为交换机没有存储的能力，在rabbitmq里面只有队列有存储的能力。
因为只是后还没有队列绑定到这个交换机，所以数据丢失了

注册 -》 邮件—》短信

#### 消费者1：Recv1

```java

public class Recv1 {
    public static final String QUEUE_NAME = "test_queue_fanout_email";
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        // 保证一次只分发一个
        channel.basicQos(1);

        // 绑定到交换机 转发器
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body, "utf-8");
                System.out.println("Recv [1] msg = " + msg);
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("Recv [1] done!");
                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // 监听队列
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
```

#### 消费者2：Recv2

```java
public class Recv2 {
    public static final String QUEUE_NAME = "test_queue_fanout_sms";
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        // 绑定到交换机 转发器
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        // 保证一次只分发一个
        channel.basicQos(1);
        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body, "utf-8");
                System.out.println("Recv [2] msg = " + msg);
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("Recv [2] done!");
                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // 监听队列
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
```

![1589617471317](..\typora-user-images\1589617471317.png)



### 7. 4.routing 路由选择通配符模式

![1589607338538](..\typora-user-images\1589607338538.png)

#### 生产者

```java
public class Send {
    public static final String EXCHANGE_NAME = "test_exchange_direct";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        // exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String msg = "hello direct !";
        // routing key
        String routingKey = "info";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());

        System.out.println("send :" + msg);

        channel.close();
        connection.close();

    }
}
```

#### 消费者1：Recv1

```java
public class Recv1 {
    public static final String EXCHANGE_NAME = "test_exchange_direct";
    public static final String QUEUE_NAME = "test_queue_direct_1";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        // 绑定队列到交换机，并绑定 routingKey
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");

        // 保证一次只分发一个
        channel.basicQos(1);

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel){
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("Recv [1] msg = " + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    // 消息回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    System.out.println("Recv [1] done!");
                }
            }
        };

        // 监听队列 autoAck(消息应答):false 手动回执 (消息回执 channel.basicAck(envelope.getDeliveryTag(), false);)
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
```

#### 消费者2：Recv2

```java
public class Recv2 {
    public static final String EXCHANGE_NAME = "test_exchange_direct";
    public static final String QUEUE_NAME = "test_queue_direct_2";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "waring");

        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("Recv [2] msg = " + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    System.out.println("Recv [2] done!");
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
```





- 5.Topics 主题
- ![1589607347814](..\typora-user-images\1589607347814.png)
- 6.RPC 手动和自动确认消息
- ![1589607357351](..\typora-user-images\1589607357351.png)
- 7.队列的持久和非持久
- 8.rabbitmq的延迟队列











## 8.CentOS下RabbitMQ集群搭建

## 9.Haproxy实现负载均衡

## 10.Java操作RabbitMQ 集群

## 11.Spring Boot 集成RabbitMQ





JAVA 操作rabbitMQ

- 1.simple	简单队列
- 2.work queues    工作队列，公平分发轮询分发
- 3.publish/subscribe   发布订阅
- 4.routing 路由选择通配符模式
- 5.Topics 主题
- 6.手动和自动确认消息
- 7.队列的持久和非持久
- 8.rabbitmq的延迟队列

4.Spring AMQP Spring-Rabbit

5.场景demo MQ实现搜索引擎DIH增量

6.场景demo 未支付订单30分钟 取消

7大数据应用，类似百度统计 cnzz架构消息队列



## 消息应答与消息持久化

#### 消息应答

boolean autoAck = false; //false 手动回执

channel.basicConsume(QUEUE_NAME, autoAck, defaultConsumer);



boolean autoAck =true;（**自动确认模式**）一旦rabbitmq将消息分发给消费者，就会从内存中删除
这种情况下：如果杀死正在执行的消费者，就会丢失正在处理的消息。

boolean autoAck =false;（手动模式），如果有一个消费者挂掉，就会交付给其它消费者，rabbitmq支持消息应答，消费者发送一个消息应答，告诉rabbitmq这个消息我已经处理完成，你可以删了，然后rabbitmq就删除内存中的消息。

消息应答默认是打开的，默认是false

Message acknowledgment:

​	如果rabbitmq挂了，消息仍然会丢失



#### 消息持久化

```
Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,                             Map<String, Object> arguments) throws IOException;
```

**boolean durable = false;**

channel.queueDeclare(QUEUE_NAME, false, false, false,null);

**我们将程序中的boolean durable = false 改成true，是不可以的，尽管代码是正确的，他也不会运行成功！因为我们已经定义了一个叫test_work_queue ，这个queue是为持久化的，rabbitmq不允许重新定义 （不同参数）一个已存在的队列**



## Exchange (交换机 转发器)

一方面是接收生产者的消息，另一方面是向队列推送消息

匿名转发

```
void basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;
```

String exchange：交换机名字

channel.basicPublish("", QUERE_NAME, null, message.getBytes());

### 类型：

#### fanout（不处理路由键）

![1589626190528](..\typora-user-images\1589626190528.png)

#### Direct（处理路由键）

![1589626264675](..\typora-user-images\1589626264675.png)