package org.oezdag.control;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.oezdag.entity.Todo;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TodoService {

    private final Map<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    public List<Todo> list() {
        return store.values().stream()
                .sorted(Comparator.comparingLong(t -> t.id))
                .toList();
    }

    public Todo create(Todo input) {
        Todo t = new Todo();
        t.id = seq.getAndIncrement();
        t.title = input.title;
        t.done = false;
        store.put(t.id, t);
        return t;
    }

    public Optional<Todo> setDone(long id, boolean value) {
        Todo t = store.get(id);
        if (t == null) return Optional.empty();
        t.done = value;
        return Optional.of(t);
    }

    public boolean delete(long id) {
        return store.remove(id) != null;
    }
}
