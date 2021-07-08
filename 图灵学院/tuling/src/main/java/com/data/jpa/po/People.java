package com.data.jpa.po;

import javax.persistence.*;
import java.util.Date;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-08 22:26
 * @Version 1.0
 */
@Entity
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;//id
    @Column(name = "name", nullable = true, length = 20)
    private String name;//姓名
    @Column(name = "sex", nullable = true, length = 1)
    private String sex;//性别
    @Column(name = "birthday", nullable = true)
    private Date birthday;//出生日期
    @OneToOne(cascade=CascadeType.ALL)//People是关系的维护端，当删除 people，会级联删除 address
    @JoinColumn(name = "address_id", referencedColumnName = "id")//people中的address_id字段参考address表中的id字段
    private Address address;//地址
}
