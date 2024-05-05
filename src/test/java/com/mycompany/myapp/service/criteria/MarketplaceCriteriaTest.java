package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MarketplaceCriteriaTest {

    @Test
    void newMarketplaceCriteriaHasAllFiltersNullTest() {
        var marketplaceCriteria = new MarketplaceCriteria();
        assertThat(marketplaceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void marketplaceCriteriaFluentMethodsCreatesFiltersTest() {
        var marketplaceCriteria = new MarketplaceCriteria();

        setAllFilters(marketplaceCriteria);

        assertThat(marketplaceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void marketplaceCriteriaCopyCreatesNullFilterTest() {
        var marketplaceCriteria = new MarketplaceCriteria();
        var copy = marketplaceCriteria.copy();

        assertThat(marketplaceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(marketplaceCriteria)
        );
    }

    @Test
    void marketplaceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var marketplaceCriteria = new MarketplaceCriteria();
        setAllFilters(marketplaceCriteria);

        var copy = marketplaceCriteria.copy();

        assertThat(marketplaceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(marketplaceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var marketplaceCriteria = new MarketplaceCriteria();

        assertThat(marketplaceCriteria).hasToString("MarketplaceCriteria{}");
    }

    private static void setAllFilters(MarketplaceCriteria marketplaceCriteria) {
        marketplaceCriteria.id();
        marketplaceCriteria.description();
        marketplaceCriteria.sellerInfoId();
        marketplaceCriteria.distinct();
    }

    private static Condition<MarketplaceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getSellerInfoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MarketplaceCriteria> copyFiltersAre(MarketplaceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getSellerInfoId(), copy.getSellerInfoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
