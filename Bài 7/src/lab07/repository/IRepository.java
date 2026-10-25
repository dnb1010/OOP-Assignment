package lab07.repository;

import java.util.List;
import java.util.function.Predicate;
import lab07.model.IIdentifiable;

public interface IRepository <T extends IIdentifiable>{
    void add(T item);

    boolean remove(String id);

    T findById(String id);

    List<T> getAll();

    List<T> findAll(Predicate<? super T> condition);
}
