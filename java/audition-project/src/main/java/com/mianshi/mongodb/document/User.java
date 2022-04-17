package com.mianshi.mongodb.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/4/16 21:09
 */
@Document("user")
public class User {

    //~fields
    //==================================================================================================================
    private String id;

    private String name;
    //~methods
    //==================================================================================================================


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}