package com.calculation.entity.scheme;

import com.calculation.entity.Account;
import com.calculation.entity.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "building")
public class Building extends BaseEntity {

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "project")
    private String project;

    @Column(name = "name_create")
    private String nameCreate;

    @Column(name = "name_control")
    private String nameControl;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id",insertable = false,updatable = false)
    private List<Shield> shields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false,updatable = false)
    private Account account;
}
