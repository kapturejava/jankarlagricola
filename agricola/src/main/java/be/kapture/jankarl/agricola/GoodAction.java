package be.kapture.jankarl.agricola;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class GoodAction {

    private final Map<GoodType, Integer> goods;

    private GoodAction(Map<GoodType, Integer> goods) {
        this.goods = goods;
    }

    public static GoodAction create(GoodType goodType, int nr) {
        checkNotNull(goodType);
        checkArgument(nr > 0);

        return new GoodAction(ImmutableMap.of(goodType, nr));
    }

    public static GoodAction create(GoodType goodType1, int nr1, GoodType goodType2, int nr2) {
        checkNotNull(goodType1);
        checkNotNull(goodType2);
        checkArgument(nr1 > 0 && nr2 > 0);
        checkArgument(goodType1 != goodType2);

        if (goodType1.compareTo(goodType2)<0) {
            return new GoodAction(ImmutableMap.of(goodType1, nr1, goodType2, nr2));
        } else {
            return new GoodAction(ImmutableMap.of(goodType2, nr2, goodType1, nr1));
        }
    }

    public Map<GoodType, Integer> getGoods(){
        return this.goods;
    }

    public GoodAction add(GoodAction other){
        checkArgument(goods.size() == 1 && other.goods.keySet().equals(this.goods.keySet()));

        int addAmount = extractSingleton(other.goods.values());
        int currentAmount = extractSingleton(this.goods.values());

        GoodType goodType = extractSingleton(this.goods.keySet());
        return GoodAction.create(goodType, currentAmount + addAmount);
    }

    private <T> T extractSingleton(Collection<T> set){
        return set.iterator().next();
    }

    @Override
    public String toString() {
        return "GoodAction{" +
                "goods=" + goods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodAction that = (GoodAction) o;

        return goods != null ? goods.equals(that.goods) : that.goods == null;
    }

    @Override
    public int hashCode() {
        return goods != null ? goods.hashCode() : 0;
    }
}
