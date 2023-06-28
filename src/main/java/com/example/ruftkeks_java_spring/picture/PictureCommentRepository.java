package com.example.ruftkeks_java_spring.picture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PictureCommentRepository extends JpaRepository<PictureComment, Long> {
    Optional<PictureComment> findById(Long id);
}
