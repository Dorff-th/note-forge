package dev.noteforge.knowhub.admin.service;

import dev.noteforge.knowhub.admin.post.service.AdminPostService;
import dev.noteforge.knowhub.attachment.domain.Attachment;
import dev.noteforge.knowhub.attachment.repository.AttachmentRepository;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.comment.repository.CommentRepository;
import dev.noteforge.knowhub.post.repository.PostRepository;
import dev.noteforge.knowhub.tag.domain.PostTag;
import dev.noteforge.knowhub.tag.domain.Tag;
import dev.noteforge.knowhub.tag.repository.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 후 자동 롤백
class AdminPostServiceTest {

    @Autowired
    private AdminPostService adminPostService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    //@Test
    void 게시글_여러개_일괄삭제() {
        // given
        Post post1 = postRepository.save(new Post("제목1", "내용1"));
        Post post2 = postRepository.save(new Post("제목2", "내용2"));

        // 댓글 추가
        //commentRepository.save(new Comment("댓글1", post1));
        //commentRepository.save(new Comment("댓글2", post2));

        // 태그 추가
        postTagRepository.save(new PostTag(post1, new Tag("Spring")));
        postTagRepository.save(new PostTag(post2, new Tag("JPA")));

        // 첨부파일 추가
        attachmentRepository.save(new Attachment(post1, "file1.png"));
        attachmentRepository.save(new Attachment(post2, "file2.png"));

        List<Long> ids = List.of(post1.getId(), post2.getId());

        // when
        adminPostService.deletePosts(ids);

        // then
        assertThat(postRepository.findAllById(ids)).isEmpty();
        assertThat(commentRepository.findAll()).isEmpty();
        assertThat(postTagRepository.findAll()).isEmpty();
        assertThat(attachmentRepository.findAll()).isEmpty();
    }

}