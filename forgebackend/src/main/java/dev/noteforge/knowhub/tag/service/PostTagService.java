package dev.noteforge.knowhub.tag.service;

import dev.noteforge.knowhub.tag.dto.TagDTO;
import dev.noteforge.knowhub.tag.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostTagService {
    private final PostTagRepository postTagRepository;

    //editor 화면에 postTag.id를 가져오기 위해 필요
    public List<TagDTO> getByPostId(Long postId) {
        return postTagRepository.findTagsByPostId(postId);
    }
}
