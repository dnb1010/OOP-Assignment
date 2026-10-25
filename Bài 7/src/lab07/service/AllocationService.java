package lab07.service;

import java.util.List;
import java.util.function.Predicate;

import lab07.model.TeachingDevice;
import lab07.repository.IRepository;
import lab07.strategy.ISelectionStrategy;

public class AllocationService<T extends TeachingDevice> {
    private final IRepository<T> repository;
    private ISelectionStrategy<T> strategy;

    public AllocationService(IRepository<T> repository, ISelectionStrategy<T> strategy) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository không được null.");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy không được null.");
        }
        this.repository = repository;
        this.strategy = strategy;
    }

    public T allocate(Predicate<? super T> condition) {
        List<T> candidates = repository.findAll(device ->
                device.isReadyForUse() && (condition == null || condition.test(device)));
        T selected = strategy.select(candidates);
        selected.borrow();
        return selected;
    }

    public void changeStrategy(ISelectionStrategy<T> strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy không được null.");
        }
        this.strategy = strategy;
    }
}
