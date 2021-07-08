package com.data.jpa.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-08 22:00
 * @Version 1.0
 */
@Entity
@Table(name = "class_room")
public class ClassRoomPO {
    @Id
    private Long id;
    private String name;
    private Date createDate;

    private Date updateDate;

    @OneToMany(mappedBy = "classRoomPO")
    private Set<StudentPO> studentPOS;
}
