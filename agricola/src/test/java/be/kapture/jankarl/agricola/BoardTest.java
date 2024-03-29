package be.kapture.jankarl.agricola;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void getGoodActions() {
        Board board = Board.create(1);
        List<GoodAction> startActions = Lists.newArrayList(Iterables.concat(
                Board.GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE,
                Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.stream().flatMap(Collection::stream).collect(toList())));

        assertThat(board.getGoodActions()).isEqualTo(startActions);

        Board boardAfterClay = board.fetchAction(GoodAction.create(GoodType.CLAY, 1));
        List<GoodAction> remainingGoodActions = listWithout(startActions, GoodAction.create(GoodType.CLAY, 1));

        assertThat(boardAfterClay.getGoodActions())
                .doesNotContain(GoodAction.create(GoodType.CLAY, 1))
                .isEqualTo(remainingGoodActions);

        assertThat(board.getGoodActions()).contains(GoodAction.create(GoodType.CLAY, 1));
    }

    private List<GoodAction> listWithout(Iterable<GoodAction> goodActionsStart1Player, GoodAction goodAction) {
        List<GoodAction> remainingGoodActions = Lists.newArrayList(goodActionsStart1Player);
        remainingGoodActions.remove(goodAction);
        return remainingGoodActions;
    }

    @Test
    void nextTurn_ReplenashablesAreAddedEachTurn() {
        Board board = Board.create(1)
                .fetchAction(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(0).get(0))
                .fetchAction(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(1).get(0));

        Board boardTurn2 = board.nextTurn();

        assertThat(boardTurn2.getGoodActions())
                .containsAll(flattenList(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE))
                .containsAll(Board.GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE.stream().map(t -> t.add(t)).collect(toList()));

        Board boardTurn3 = boardTurn2
                .fetchAction(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(0).get(0))
                .fetchAction(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(1).get(0))
                .nextTurn();

        assertThat(boardTurn3.getGoodActions())
                .containsAll(flattenList(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE))
                .containsAll(Board.GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE.stream().map(t -> t.add(t).add(t)).collect(toList()));

    }

    @Test
    void nextTurn_TakenActionIsReset() {
        GoodAction replenishableAction = Board.GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE.get(0);

        Board board = Board.create(1)
                .fetchAction(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(0).get(0))
                .fetchAction(replenishableAction);

        Board boardTurn2 = board.nextTurn();

        assertThat(boardTurn2.getGoodActions())
                .containsAll(flattenList(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE))
                .contains(replenishableAction);
    }

    @Test
    void nextTurn_ActionsTogether() {
        Board board = Board.create(1);
        Board board1a= board.fetchAction(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(0).get(0));

        assertThat(board1a.getGoodActions())
                .doesNotContainAnyElementsOf(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(0));

        Board board2 = board1a
                .fetchAction(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(1).get(0))
                .nextTurn();

        assertThat(board2.getGoodActions())
                .containsAll(Board.GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE.get(0));
    }


        private List<GoodAction> flattenList(List<List<GoodAction>> list) {
        List<GoodAction> newList = new ArrayList<>();
        list.forEach(newList::addAll);
        return newList;
    }



}