package com.bravewhool.bicycleAPI.models.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EntitySearchCriteria {

    private String fieldName;

    private Object value;

    private SearchOperator searchOperator;
}
