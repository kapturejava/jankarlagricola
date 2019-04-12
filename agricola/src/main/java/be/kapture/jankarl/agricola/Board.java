package be.kapture.jankarl.agricola;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    static final List<GoodAction> GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE = List.of(
            GoodAction.create(GoodType.CLAY, 1),
            GoodAction.create(GoodType.REED, 1),
            GoodAction.create(GoodType.WOOD, 3),
            GoodAction.create(GoodType.GRAIN, 1)
    );
    static final List<GoodAction> GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE = List.of(
            GoodAction.create(GoodType.FOOD, 1, GoodType.WOOD, 1),
            GoodAction.create(GoodType.FOOD, 1, GoodType.CLAY, 1),
            GoodAction.create(GoodType.FOOD, 1, GoodType.REED, 1),
            GoodAction.create(GoodType.FOOD, 1)
    );

    private final List<GoodAction> goodActions;

    private Board(List<GoodAction> goodActions) {
        this.goodActions = new ArrayList<>(goodActions);
    }

    public static Board create(int nrOfPlayers) {
        return new Board(Lists.newArrayList(Iterables.concat(GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE, GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE)));
    }

    public List<GoodAction> getGoodActions(){
        return goodActions;
    }

    public Board fetchAction(GoodAction goodAction){
        List<GoodAction> remainingGoodActions = goodActions
                .stream()
                .filter(g -> !g.equals(goodAction))
                .collect(Collectors.toList());

        return new Board(remainingGoodActions);
    }

    public Board nextTurn(){

        List<GoodAction> newGoodActions = GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE.stream()
                .map(this::toNewGoodAction)
                .collect(Collectors.toList());
        newGoodActions.addAll(GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE);

        return new Board(new ArrayList<>(newGoodActions));
    }

    private GoodAction toNewGoodAction(GoodAction a) {
        Optional<GoodAction> matchingGoodAction = goodActions.stream()
                .filter(goodAction -> goodAction.getGoods().size() == 1)
                .filter(goodAction -> extractSingleton(goodAction.getGoods().keySet()).equals(extractSingleton(a.getGoods().keySet())))
                .findFirst();

        return matchingGoodAction.map(b -> b.add(a)).orElse(a);

    }

    private <T> T extractSingleton(Collection<T> set){
        return set.iterator().next();
    }
}
