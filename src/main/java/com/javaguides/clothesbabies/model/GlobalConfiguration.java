package com.javaguides.clothesbabies.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GLOBAL_CONFIGURATION")
@Getter
@Setter
public class GlobalConfiguration {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "value")
    private String value;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}
