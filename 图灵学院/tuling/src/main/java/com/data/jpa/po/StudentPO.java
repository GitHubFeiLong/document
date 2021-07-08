package com.data.jpa.po;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-08 21:59
 * @Version 1.0
 */
@Entity
@Table(name = "student")
public class StudentPO {
    @Id
    private Long id;

    private String username;

    private Long classRoomId;

    private Date createDate;

    private Date updateDate;

    @ManyToOne(targetEntity =ClassRoomPO.class )
//    @JoinColumn(name="id")//设置在article表中的关联字段(外键)
    private ClassRoomPO classRoomPO;
}
