package com.bravewhool.bicycleAPI.entity;

import com.bravewhool.bicycleAPI.entity.enums.BicycleFrameType;
import com.bravewhool.bicycleAPI.entity.enums.BicycleType;
import com.bravewhool.bicycleAPI.entity.enums.MaterialType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bicycles")
@NoArgsConstructor
@Getter
@Setter
public class Bicycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BicycleType bicycleType;

    @Column(name = "material")
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;

    @Column(name = "frame_type")
    @Enumerated(EnumType.STRING)
    private BicycleFrameType frameType;

    @Column(name = "sale")
    private boolean sale;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "wheel_size")
    private BigDecimal wheelSize;

    @Column(name = "color")
    private String color;

    @OneToMany
    @JoinColumn(name = "bicycle_id")
    private List<BicycleImage> images;
}
