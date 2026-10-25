/*Đồng Ngọc Bảo 202418849 */
package lab07.strategy;

import lab07.exception.NoSuitableItemException;

import java.util.List;
import java.util.function.ToDoubleFunction;

public class HighestPriorityStrategy<T> implements ISelectionStrategy<T> {
    private final ToDoubleFunction<? super T> scoreSelector;

    public HighestPriorityStrategy(ToDoubleFunction<? super T> scoreSelector) {
        if (scoreSelector == null) {
            throw new IllegalArgumentException("scoreSelector không được null.");
        }
        this.scoreSelector = scoreSelector;
    }

    @Override 
    public T select(List<T> candidates){
        if (candidates == null || candidates.isEmpty()){
            throw new NoSuitableItemException();
        }  
        T best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (T candidate: candidates){
            double score = scoreSelector.applyAsDouble(candidate);
            if (best == null || score > bestScore){
                best = candidate;
                bestScore = score;
            }
        }
        return best;
    }
}
