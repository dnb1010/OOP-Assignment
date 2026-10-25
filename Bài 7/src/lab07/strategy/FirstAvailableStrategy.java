/*Đồng Ngọc Bảo */
package lab07.strategy;

import lab07.exception.NoSuitableItemException;
import java.util.List;



public class FirstAvailableStrategy<T> implements ISelectionStrategy<T> {
    @Override
    public T select(List<T> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new NoSuitableItemException();
        }
        return candidates.get(0);
    }
}