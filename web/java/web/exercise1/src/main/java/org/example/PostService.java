package org.example;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> findById(int id);
    List<Post> findAll();
    void create(Post post);
    void update(Post post);
    void delete(int id);

}
