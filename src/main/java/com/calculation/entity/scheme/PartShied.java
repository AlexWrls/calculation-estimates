package com.calculation.entity.scheme;

import com.calculation.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "part_shield")
public class PartShied extends BaseEntity implements Comparable<PartShied> {

    @Column(name = "shield_id")
    private Long shieldID;

    @Column(name = "phase")
    private Integer phase;

    @Column(name = "name_protect")
    private String nameProtect;

    @Column(name = "rcd_current")
    private Integer RCDCurrent;

    @Column(name = "description_protect")
    private String descriptionProtect;

    @Column(name = "current_protect")
    private Integer currentProtect;

    @Column(name = "name")
    private String name;

    @Column(name = "cable")
    private String cable;

    @Column(name = "length")
    private Integer length;

    @Column(name = "rated_current")
    private Double ratedCurrent;

    @Column(name = "usage_rate")
    private Double usageRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shield_id", insertable = false,updatable = false)
    private Shield shield;


    @Override
    public int compareTo(PartShied group) {
        return (int) (this.getId()-group.getId());
    }
}
