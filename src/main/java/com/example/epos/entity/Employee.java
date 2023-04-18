package com.example.epos.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    @TableField(fill = FieldFill.INSERT) //Populate fields on insertion
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) //Populating fields on insert and update
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT) //Populate fields on insertion
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE) //Populating fields on insert and update
    private Long updateUser;

}
