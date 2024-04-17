package com.bravewhool.bicycleAPI.models.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class SearchSpecification<T> {

    public Specification<T> getFinalSpecification(Map<String, List<EntitySearchCriteria>> entitiesSearchCriteria) {
        List<Specification<T>> finalSpecifications = new ArrayList<>();

        for (Map.Entry<String, List<EntitySearchCriteria>> entry : entitiesSearchCriteria.entrySet()) {
            List<Specification<T>> specificationsFromCriteria = entry.getValue().stream()
                    .map(this::createSpecification)
                    .toList();
            Specification<T> specificationWithSameField;
            boolean isAllEqualSearchOperator = entry.getValue().stream()
                    .allMatch(criteria -> criteria.getSearchOperator() == SearchOperator.EQUALS);

            if (isAllEqualSearchOperator) {
                specificationWithSameField = (root, query, criteriaBuilder) ->
                        criteriaBuilder.or(
                                getPredicates(root, query, criteriaBuilder, specificationsFromCriteria)
                        );
            } else {
                specificationWithSameField = (root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                getPredicates(root, query, criteriaBuilder, specificationsFromCriteria)
                        );
            }

            finalSpecifications.add(specificationWithSameField);
        }

        return Specification.allOf(finalSpecifications);
    }

    private Predicate[] getPredicates(Root<T> root, CriteriaQuery<?> query,
                                      CriteriaBuilder criteriaBuilder,
                                      List<Specification<T>> specifications) {

        return specifications.stream()
                .map(s -> s.toPredicate(root, query, criteriaBuilder))
                .toArray(Predicate[]::new);
    }

    private Specification<T> createSpecification(EntitySearchCriteria criteria) {
        return switch (criteria.getSearchOperator()) {
            case EQUALS ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(criteria.getFieldName()),
                            criteria.getValue().toString());
            case NOT_EQUALS ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(criteria.getFieldName()),
                            criteria.getValue().toString());
            case GREATER_THAN ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getFieldName()),
                            (BigDecimal) criteria.getValue());
            case LESS_THAN ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getFieldName()),
                            (BigDecimal) criteria.getValue());
            case LIKE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(criteria.getFieldName()),
                            criteria.getValue().toString());
            case I_LIKE -> (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(criteria.getFieldName())),
                    "%" + criteria.getValue().toString().toLowerCase() + "%"
            );
            case IS_TRUE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get(criteria.getFieldName()).as(Boolean.class));
            case IS_FALSE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(criteria.getFieldName()).as(Boolean.class));
        };

    }

}