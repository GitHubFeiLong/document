package com.data.jpa.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-10 13:26
 * @Version 1.0
 */
@Entity
@Table(name = "cst_customer")
@Data
public class CstCustomerPO {
    /**
     * IDENTITY:主键由数据库自动生成（主要是自动增长型）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String source;

    private String industry;

    private String level;

    private String address;

    private String phone;

}
