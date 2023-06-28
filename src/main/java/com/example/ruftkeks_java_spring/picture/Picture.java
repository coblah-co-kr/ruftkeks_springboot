package com.example.ruftkeks_java_spring.picture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picture {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImg;
    private String name;
    private String date;
    private String location;
    private String pictureContent;

    @ElementCollection
    private List<String> hashTag;

    @ElementCollection
    private List<String> pictureList;

    @ElementCollection
    private List<PictureComment> comments;

    @Builder
    public Picture(String profileImg, String name, String date,
                   String location, String pictureContent,
                   List<String> hashTag, List<String> pictureList)
    {
        this.profileImg = profileImg;
        this.name = name;
        this.date = date;
        this.location = location;
        this.pictureContent = pictureContent;
        this.hashTag = hashTag;
        this.pictureList = pictureList;
    }

    public Picture updatePicture(String profileImg, String name, String date,
                                 String location, String pictureContent,
                                 List<String> hashTag, List<String> pictureList) {
        this.profileImg = profileImg;
        this.name = name;
        this.date = date;
        this.location = location;
        this.pictureContent = pictureContent;
        this.hashTag = hashTag;
        this.pictureList = pictureList;
        return this;
    }

    public Picture updateComment(List<PictureComment> comments) {
        this.comments = comments;
        return this;
    }
}
