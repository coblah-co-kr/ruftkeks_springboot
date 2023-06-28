package com.example.ruftkeks_java_spring.picture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findAllBy();
    Optional<Picture> findById(Long id);
}
