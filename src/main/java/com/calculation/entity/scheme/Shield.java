package com.calculation.entity.scheme;


import com.calculation.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "shield")
@Immutable
public class Shield extends BaseEntity {

    @Column(name = "building_id")
    private Long buildingId;
    @Column(name = "name_shield")
    private String nameShield;
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
    @Column(name = "cable")
    private String cable;
    @Column(name = "length")
    private Integer length;
    @Column(name = "rated_current")
    private Double ratedCurrent;
    @Column(name = "rated_power")
    private Double ratedPower;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "shield_id",insertable = false,updatable = false)
    private List<PartShied> groups;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", insertable = false,updatable = false)
    private Building building;
}
