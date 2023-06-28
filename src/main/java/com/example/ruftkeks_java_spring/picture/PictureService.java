package com.example.ruftkeks_java_spring.picture;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PictureService {

    private final PictureRepository pictureRepository;

    private final PictureCommentRepository pictureCommentRepository;

    public Picture create(String profileImg, String name, String date,
                          String location, String pictureContent,
                          List<String> hashTag, List<String> pictureList) {
        Picture picture = Picture.builder()
                .profileImg(profileImg)
                .name(name)
                .date(date)
                .location(location)
                .hashTag(hashTag)
                .pictureContent(pictureContent)
                .pictureList(pictureList)
                .build();
        this.pictureRepository.save(picture);
        return picture;
    }

    public Boolean updateComment(Long id, List<PictureComment> comments) {
        Optional<Picture> picture = this.pictureRepository.findById(id);
        if (picture.isPresent()) {
            Picture picture1 = picture.get().updateComment(comments);

            for (PictureComment comment: comments) {
                if (comment.getId() == null) {
                    this.pictureCommentRepository.save(comment);
                } else {
                    Optional<PictureComment> pictureComment = this.pictureCommentRepository.findById(comment.getId());
                    if (!pictureComment.isPresent()) {
                        this.pictureCommentRepository.save(comment);
                    }
                }
            }
            this.pictureRepository.save(picture1);
            return true;
        }
        return false;
    }

    public List<Picture> getAllPictureInfo() {
        return this.pictureRepository.findAllBy();
    }
    public Optional<Picture> getById(Long id) {
        return this.pictureRepository.findById(id);
    }
}
