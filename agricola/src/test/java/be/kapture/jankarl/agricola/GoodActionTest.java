package be.kapture.jankarl.agricola;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

class GoodActionTest {

    public static final GoodAction WOODACTION3 = GoodAction.create(GoodType.WOOD, 3);

    @Test
    void createSingle_BorderCases() {
        assertThatNullPointerException().isThrownBy(() -> GoodAction.create(null, 1));

        assertThatIllegalArgumentException().isThrownBy(() -> GoodAction.create(GoodType.CLAY, -1));
        assertThatIllegalArgumentException().isThrownBy(() -> GoodAction.create(GoodType.CLAY, 0));
    }

    @Test
    void createDouble_BorderCases() {
        assertThatNullPointerException().isThrownBy(() -> GoodAction.create(null, 1, GoodType.CLAY, 2));
        assertThatNullPointerException().isThrownBy(() -> GoodAction.create(GoodType.WOOD, 1, null, 2));

        assertThatIllegalArgumentException().isThrownBy(() -> GoodAction.create(GoodType.CLAY, 0, GoodType.WOOD, 1));
        assertThatIllegalArgumentException().isThrownBy(() -> GoodAction.create(GoodType.CLAY, 1, GoodType.WOOD, 0));

        assertThatIllegalArgumentException().isThrownBy(() -> GoodAction.create(GoodType.CLAY, 1, GoodType.CLAY, 2));
    }

    @Test
    void create() {
        assertThat(GoodAction.create(GoodType.GRAIN, 1, GoodType.CLAY, 2)
                .getGoods())
                .containsExactly(entry(GoodType.CLAY, 2), entry(GoodType.GRAIN, 1));

        assertThat(GoodAction.create(GoodType.REED, 2, GoodType.WOOD, 1)
                .getGoods())
                .containsExactly(entry(GoodType.WOOD, 1), entry(GoodType.REED, 2));

        assertThat(GoodAction.create(GoodType.WOOD, 3)
                .getGoods())
                .containsExactly(entry(GoodType.WOOD, 3));

        assertThat(GoodAction.create(GoodType.GRAIN, 1)
                .getGoods())
                .containsExactly(entry(GoodType.GRAIN, 1));
    }

    @Test
    void create_Immutable() {
        GoodAction goodActionDouble = GoodAction.create(GoodType.GRAIN, 1, GoodType.CLAY, 2);

        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
                goodActionDouble.getGoods()
                        .put(GoodType.WOOD, 3));

        GoodAction goodActionSingle = GoodAction.create(GoodType.GRAIN, 1);

        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
                goodActionSingle.getGoods()
                        .put(GoodType.WOOD, 3));
    }

    @Test
    void add() {
        GoodAction action3 = GoodAction.create(GoodType.WOOD, 3);
        Function<GoodAction, GoodAction> addWoodAction3 = action3::add;
        assertThat(action3)
                .returns(GoodAction.create(GoodType.WOOD, 6), addWoodAction3)
                .returns(GoodAction.create(GoodType.WOOD, 9), addWoodAction3.andThen(addWoodAction3));

        GoodAction goodActionClay3 = GoodAction.create(GoodType.CLAY, 3);
        Function<GoodAction, GoodAction> addClayAction = goodActionClay3::add;
        assertThat(goodActionClay3)
                .returns(GoodAction.create(GoodType.CLAY, 6), addClayAction)
                .returns(GoodAction.create(GoodType.CLAY, 9), addClayAction.andThen(addClayAction));
    }

    @Test
    void add_BorderCases() {
        assertThatNullPointerException().isThrownBy(() -> WOODACTION3.add(null));

        assertThatIllegalArgumentException().isThrownBy(() -> WOODACTION3.add(GoodAction.create(GoodType.CLAY, 3)));
    }

    @Test
    void add_NotAllowedForDoubles() {
        GoodAction actionWoodAndClay = GoodAction.create(GoodType.WOOD, 3, GoodType.CLAY, 2);
        assertThatIllegalArgumentException().isThrownBy(() ->
                actionWoodAndClay.add(GoodAction.create(GoodType.CLAY, 3)));

        assertThatIllegalArgumentException().isThrownBy(() -> actionWoodAndClay.add(actionWoodAndClay));
    }

}