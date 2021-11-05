package com.calculation.entity.calculation;

import com.calculation.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "orders")
public class Orders extends BaseEntity {

    @Column(name = "calculation_id")
    private Long calculationId;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "measurement")
    private String measurement;
    @Column(name = "price")
    private Double price;
    @Column(name = "count")
    private int count;
    @Column(name = "summary")
    private Double summary;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id", insertable = false,updatable = false)
    private Calculation calculation;

}
