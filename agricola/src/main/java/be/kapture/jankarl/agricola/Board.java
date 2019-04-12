package be.kapture.jankarl.agricola;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    private List<GoodAction> replenishableActions;
    private List<GoodAction> notReplenishableActions;


    public Board(List<GoodAction> replenishableActions, List<GoodAction> notReplenishableActions) {
        this.replenishableActions = replenishableActions;
        this.notReplenishableActions = notReplenishableActions;
    }

    public static Board create(int nrOfPlayers) {
        return new Board(GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE, GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE);
    }

    public List<GoodAction> getGoodActions(){
        return Lists.newArrayList(Iterables.concat(replenishableActions, notReplenishableActions));
    }

    public Board fetchAction(GoodAction goodAction){
        List<GoodAction> newReplenishableActions = replenishableActions
                .stream()
                .filter(g -> !g.equals(goodAction))
                .collect(Collectors.toList());

        List<GoodAction> newNotReplenishableActions = notReplenishableActions
                .stream()
                .filter(g -> !g.equals(goodAction))
                .collect(Collectors.toList());

        return new Board(newReplenishableActions, newNotReplenishableActions);
    }

    public Board nextTurn(){

        List<GoodAction> newReplenashables = GOOD_ACTIONS_START_1_PLAYER_REPLENISHABLE.stream()
                .map(this::toNewGoodAction)
                .collect(Collectors.toList());


        return new Board(newReplenashables, GOOD_ACTIONS_START_1_PLAYER_NOT_REPLENISHABLE);
    }

    private GoodAction toNewGoodAction(GoodAction a) {
        Optional<GoodAction> matchingGoodAction = replenishableActions.stream()
                .filter(goodAction -> goodAction.getGoods().size() == 1)
                .filter(goodAction -> extractSingleton(goodAction.getGoods().keySet()).equals(extractSingleton(a.getGoods().keySet())))
                .findFirst();

        return matchingGoodAction.map(b -> b.add(a)).orElse(a);

    }

    private <T> T extractSingleton(Collection<T> set){
        return set.iterator().next();
    }
}
