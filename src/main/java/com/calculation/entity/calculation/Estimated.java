package com.calculation.entity.calculation;


import com.calculation.entity.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "estimate")
public class Estimated extends BaseEntity {

    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "contract")
    private String contract;
    @Column(name = "building")
    private String building;
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "customer")
    private String customer;
    @Column(name = "position")
    private String position;
    @Column(name = "estimate_price")
    private Double estimatePrice;
    @Column(name = "discount")
    private Double discount;
    @Column(name = "price")
    private Double price;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id",insertable = false,updatable = false)
    private List<Calculation> calculations;


}