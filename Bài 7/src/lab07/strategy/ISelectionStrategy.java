package lab07.strategy;

import java.util.List;

public interface ISelectionStrategy<T> {
    T select(List<T> candidates);
}
