package com.data.es;

import java.sql.*;

/**
 * 类描述：
 * 测试Elasticsearch 的Jdbc使用sql操作 （1前提 es拥有白金版功能
 *
 *  kibana中管理-》许可管理 开启白金版试用）
 * @Author msi
 * @Date 2021-07-26 20:10
 * @Version 1.0
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class ElasticsearchJdbsTest {
    public static void main(String[] args) {
        //1创建连接
        try {
            Connection connection = DriverManager.getConnection("jdbc:es://http://localhost:9200");
            //2创建statement
            Statement statement = connection.createStatement();
            //3执行sql语句
            ResultSet resultSet = statement.executeQuery("select * from tvs");
            //4获取结果
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
                System.out.println(resultSet.getString(3));
                System.out.println(resultSet.getString(4));
                System.out.println("======================================");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }







    }
}
