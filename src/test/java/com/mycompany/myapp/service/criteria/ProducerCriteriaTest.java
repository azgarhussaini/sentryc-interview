package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProducerCriteriaTest {

    @Test
    void newProducerCriteriaHasAllFiltersNullTest() {
        var producerCriteria = new ProducerCriteria();
        assertThat(producerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void producerCriteriaFluentMethodsCreatesFiltersTest() {
        var producerCriteria = new ProducerCriteria();

        setAllFilters(producerCriteria);

        assertThat(producerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void producerCriteriaCopyCreatesNullFilterTest() {
        var producerCriteria = new ProducerCriteria();
        var copy = producerCriteria.copy();

        assertThat(producerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(producerCriteria)
        );
    }

    @Test
    void producerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var producerCriteria = new ProducerCriteria();
        setAllFilters(producerCriteria);

        var copy = producerCriteria.copy();

        assertThat(producerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(producerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var producerCriteria = new ProducerCriteria();

        assertThat(producerCriteria).hasToString("ProducerCriteria{}");
    }

    private static void setAllFilters(ProducerCriteria producerCriteria) {
        producerCriteria.id();
        producerCriteria.producerName();
        producerCriteria.createdAt();
        producerCriteria.sellerId();
        producerCriteria.distinct();
    }

    private static Condition<ProducerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProducerName()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getSellerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProducerCriteria> copyFiltersAre(ProducerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProducerName(), copy.getProducerName()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getSellerId(), copy.getSellerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
