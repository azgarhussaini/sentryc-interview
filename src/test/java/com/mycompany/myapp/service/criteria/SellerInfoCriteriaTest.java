package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SellerInfoCriteriaTest {

    @Test
    void newSellerInfoCriteriaHasAllFiltersNullTest() {
        var sellerInfoCriteria = new SellerInfoCriteria();
        assertThat(sellerInfoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void sellerInfoCriteriaFluentMethodsCreatesFiltersTest() {
        var sellerInfoCriteria = new SellerInfoCriteria();

        setAllFilters(sellerInfoCriteria);

        assertThat(sellerInfoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void sellerInfoCriteriaCopyCreatesNullFilterTest() {
        var sellerInfoCriteria = new SellerInfoCriteria();
        var copy = sellerInfoCriteria.copy();

        assertThat(sellerInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(sellerInfoCriteria)
        );
    }

    @Test
    void sellerInfoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sellerInfoCriteria = new SellerInfoCriteria();
        setAllFilters(sellerInfoCriteria);

        var copy = sellerInfoCriteria.copy();

        assertThat(sellerInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(sellerInfoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sellerInfoCriteria = new SellerInfoCriteria();

        assertThat(sellerInfoCriteria).hasToString("SellerInfoCriteria{}");
    }

    private static void setAllFilters(SellerInfoCriteria sellerInfoCriteria) {
        sellerInfoCriteria.id();
        sellerInfoCriteria.marketplaceName();
        sellerInfoCriteria.url();
        sellerInfoCriteria.country();
        sellerInfoCriteria.externalId();
        sellerInfoCriteria.marketplaceIdId();
        sellerInfoCriteria.distinct();
    }

    private static Condition<SellerInfoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMarketplaceName()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getCountry()) &&
                condition.apply(criteria.getExternalId()) &&
                condition.apply(criteria.getMarketplaceIdId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SellerInfoCriteria> copyFiltersAre(SellerInfoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMarketplaceName(), copy.getMarketplaceName()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getCountry(), copy.getCountry()) &&
                condition.apply(criteria.getExternalId(), copy.getExternalId()) &&
                condition.apply(criteria.getMarketplaceIdId(), copy.getMarketplaceIdId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
