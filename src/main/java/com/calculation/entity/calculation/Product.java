package com.calculation.entity.calculation;

import com.calculation.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "measurement")
    private String measurement;
    @Column(name = "price")
    private Double price;
}
