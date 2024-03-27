package com.bravewhool.bicycleAPI.entity;

import com.bravewhool.bicycleAPI.entity.enums.BicycleFrameType;
import com.bravewhool.bicycleAPI.entity.enums.BicycleType;
import com.bravewhool.bicycleAPI.entity.enums.MaterialType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bicycles")
@NoArgsConstructor
@Getter
@Setter
public class Bicycle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    private boolean sale;

    private BigDecimal price;

    @Column(name = "wheel_size")
    private BigDecimal wheelSize;

    private String color;

    private String description;

    private String brand;

    @Column(name = "brake_type")
    private String brakeType;

    private BigDecimal weight;

    private Integer guarantee;

    private Long quantity;

    @OneToMany(mappedBy = "bicycle")
    private List<BicycleImage> images;
}
