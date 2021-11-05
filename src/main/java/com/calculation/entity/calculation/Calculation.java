package com.calculation.entity.calculation;

import com.calculation.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

import java.util.List;


@Entity
@Data
@Table(name = "calculation")
public class Calculation extends BaseEntity {

    @Column(name = "estimate_id")
    private Long estimateId;
    @Column(name = "address")
    private String address;
    @Column(name = "calculation_price")
    private Double calculationPrice;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id",insertable = false,updatable = false)
    private List<Orders> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id", insertable = false,updatable = false)
    private Estimated estimated;

}
