package com.bravewhool.bicycleAPI.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "bicycle_images")
@NoArgsConstructor
@Getter
@Setter
public class BicycleImage implements Persistable<String> {

    @Id
    private String id;

    @Column
    private String url;

    @ManyToOne
    private Bicycle bicycle;

    @Override
    public boolean isNew() {
        return true;
    }
}
