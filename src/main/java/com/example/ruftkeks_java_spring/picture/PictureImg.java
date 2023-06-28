package com.example.ruftkeks_java_spring.picture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PictureImg {
    private Long id;
    private List<PictureComment> comments;
}