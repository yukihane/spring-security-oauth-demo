package com.example;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll(String username) {
        return repository.findByUsernameOrderByDeadlineDescIdDesc(username);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public Task findOne(long id) {
        return repository.findById(id).orElse(null);
    }

    public void save(Long id, Task task) {
        final LocalDateTime now = LocalDateTime.now();
        if (id != null) {
            Task res = repository.findById(id).get();
            res.setTitle(task.getTitle());
            res.setDetail(task.getDetail());
            res.setDeadline(task.getDeadline());
            res.setFinished(task.isFinished());
            res.setUpdatedAt(now);
        } else {
            task.setCreatedAt(now);
            task.setUpdatedAt(now);
            repository.save(task);
        }
    }

    public void remove(long id) {
        repository.deleteById(id);
    }

}
