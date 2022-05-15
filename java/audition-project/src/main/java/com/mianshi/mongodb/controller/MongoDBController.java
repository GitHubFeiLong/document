// package com.mianshi.mongodb.controller;
//
// import com.mianshi.mongodb.document.User;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import java.util.List;
//
// /**
//  * 类描述：
//  *
//  * @author msi
//  * @version 1.0
//  * @date 2022/4/16 21:11
//  */
// @RestController
// @RequestMapping("/mongo")
// public class MongoDBController {
//
//     //~fields
//     //==================================================================================================================
//     @Autowired
//     private MongoTemplate mongoTemplate;
//
//     //~methods
//     //==================================================================================================================
//     @GetMapping("/demo")
//     public String demo() {
//
//         List<User> all = mongoTemplate.findAll(User.class);
//
//         return "hello world";
//     }
//
// }