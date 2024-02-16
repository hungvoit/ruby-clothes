package com.javaguides.clothesbabies.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Categories")
@Getter
@Setter
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "parentId")
    public String parentId;

    @Column(name = "code")
    public String code;

    @Column(name = "name")
    public String name;

    @Column(name = "status")
    public String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    public Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    public Date updateDate;
}
