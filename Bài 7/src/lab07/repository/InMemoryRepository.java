package lab07.repository;

import lab07.repository.IRepository;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import lab07.model.IIdentifiable;
import lab07.exception.DuplicateIdException;

public class InMemoryRepository<T extends IIdentifiable> implements IRepository<T>{
    private final Map<String, T> items = new LinkedHashMap<>();

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Không chấp nhận phần tử null.");
        }
        String id = item.getId();
        if (items.containsKey(id)) {
            throw new DuplicateIdException(id);
        }
        items.put(id, item);
    }

    @Override 
    public boolean remove(String id){
        return id != null && items.remove(id) != null;
    }

    @Override 
    public T findById(String Id){
        return Id == null ? null: items.get(Id);
    }

    @Override
    public List<T> getAll(){
        return List.copyOf(items.values());
    }

    @Override
    public List<T> findAll(Predicate<? super T> condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Điều kiện lọc không được null.");
        }
        return items.values().stream().filter(condition).toList();
    }
}

