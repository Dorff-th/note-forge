package dev.noteforge.knowhub.post.service;

import dev.noteforge.knowhub.attachment.domain.Attachment;
import dev.noteforge.knowhub.attachment.dto.AttachmentViewDTO;
import dev.noteforge.knowhub.attachment.dto.FileSaveResultDTO;
import dev.noteforge.knowhub.attachment.enums.UploadType;
import dev.noteforge.knowhub.attachment.repository.AttachmentRepository;
import dev.noteforge.knowhub.attachment.repository.ImageUploadRepository;
import dev.noteforge.knowhub.attachment.service.AttachmentService;
import dev.noteforge.knowhub.attachment.util.FormatFileSize;
import dev.noteforge.knowhub.attachment.util.GeneralFileUtil;
import dev.noteforge.knowhub.comment.repository.CommentRepository;
import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.common.util.StringUtils;
import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.repository.MemberRepository;
import dev.noteforge.knowhub.category.domain.Category;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.dto.PostDTO;
import dev.noteforge.knowhub.post.dto.PostDetailDTO;
import dev.noteforge.knowhub.post.dto.PostRequestDTO;
import dev.noteforge.knowhub.post.dto.PostUpdateDTO;

import dev.noteforge.knowhub.category.repository.CategoryRepository;
import dev.noteforge.knowhub.post.repository.PostRepository;

import dev.noteforge.knowhub.tag.domain.PostTag;
import dev.noteforge.knowhub.tag.domain.Tag;
import dev.noteforge.knowhub.tag.repository.PostTagRepository;
import dev.noteforge.knowhub.tag.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;


    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    private final ImageUploadRepository imageUploadRepository;

    private final GeneralFileUtil generalFileUtil;
    private final AttachmentRepository attachmentRepository;

    private final TagService tagService;
    private final PostTagRepository postTagRepository;

    private final AttachmentService attachmentService;

    private final CommentRepository commentRepository;

    //post 페이징(목록)
    public PageResponseDTO<PostDTO> getPostList(PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by(Sort.Direction.valueOf(requestDTO.getDirection().toString()), requestDTO.getSort()));


        Page<PostDTO> result =  postRepository.findAllPosts(pageable);
        List<PostDTO> dtoList = result.getContent();

        return new PageResponseDTO<>(requestDTO, result.getTotalElements(), dtoList, 10);

    }

    //특정 태그의 posts(목록)
    public PageResponseDTO<PostDTO> getPostListByTag(String tagName, PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by(Sort.Direction.valueOf(requestDTO.getDirection().toString()), requestDTO.getSort()));


        Page<PostDTO> result =  postRepository.findAllPostsByTag(tagName, pageable);
        List<PostDTO> dtoList = result.getContent();

        return new PageResponseDTO<>(requestDTO, result.getTotalElements(), dtoList, 10);

    }

    //post 상세
    public Optional<PostDetailDTO> getPost(Long id) {

        return postRepository.findPostDetail(id)
                .map(post -> {
                    PostDetailDTO dto = new PostDetailDTO();
                    dto.setId(post.getId());
                    dto.setTitle(post.getTitle());
                    dto.setContent(post.getContent());
                    dto.setCategoryId(post.getCategoryId());
                    dto.setCategoryName(post.getCategoryName());
                    dto.setMemberId(post.getMemberId());
                    dto.setUsername(post.getUsername());
                    dto.setNickname(post.getNickname());
                    dto.setCreatedAt(post.getCreatedAt());
                    dto.setUpdatedAt(post.getUpdatedAt());

                    // ✅ 첨부파일 조회 추가
                    List<AttachmentViewDTO> attachments =
                            attachmentService.attachmentsByPostId(post.getId(), UploadType.ATTACHMENT)
                                    .stream()
                                    .map(attachment -> AttachmentViewDTO.builder()
                                            .id(attachment.getId())
                                            .originalName(attachment.getOriginFileName())
                                            .fileSizeText(FormatFileSize.formatFileSize(attachment.getFileSize()))
                                            .uploadType(attachment.getUploadType())
                                            .build())
                                    .collect(Collectors.toList());

                    dto.setAttachments(attachments);

                    return dto;
                });

    }

    @Transactional
    public Post createPost(PostRequestDTO dto, Member member) {
        //Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()->new IllegalArgumentException("카테고리가 없습니다."));

        Post post = new Post(dto.getTitle(), dto.getContent(), member, category);

        Post savedPost = postRepository.save(post);



        //첨부파일 저장
        if(dto.getAttachments() != null && !dto.getAttachments().isEmpty()  ) {
            List<FileSaveResultDTO> fileSaveResultDTO= generalFileUtil.saveFiles(dto.getAttachments(), savedPost.getId());
            //fileSaveResultDTO.forEach(fileSaveDTO->log.debug("fileSaveDTO :" , fileSaveDTO.toString()));

            List<Attachment> attachments = fileSaveResultDTO.stream()
                    .map(saveDto -> Attachment.builder()
                            .post(post)
                            .fileName(saveDto.getFileName())
                            .originFileName(saveDto.getOriginFileName())
                            .fileType(saveDto.getFileType())
                            .fileSize(saveDto.getSize())
                            .fileUrl(saveDto.getFileUrl())
                            .uploadType(saveDto.getUploadType())
                            .uploadedAt(LocalDateTime.now())
                            .build())
                    .toList();

            attachmentRepository.saveAll(attachments);
        }

        //에디터에 첨부된 이미지 파일 정보에 저장된 post id 업데이트
        /*String tempKey = dto.getTempKey();
        boolean existsByTempKey = imageUploadRepository.existsByTempKey(tempKey);
        if(existsByTempKey) {
            imageUploadRepository.updatePostIdByTempKey(savedPost.getId(), tempKey);
        }*/

        //태그 & post_tag 관계 저장
        if (dto.getTags() != null) {
            String[] tagArray = dto.getTags().split(",");
            for (String tagName : tagArray) {
                                Tag tag = tagService.getOrCreateTag(tagName);
                PostTag postTag = new PostTag(savedPost, tag);
                postTagRepository.save(postTag);
            }
        }

        return savedPost;
    }

    //Post 수정요청할때 필요한 해당 post 작성자(Member) id값을 조회하기 위해 필요
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }


    @Transactional
    public Post  editPost(PostUpdateDTO dto) {

        Post post = postRepository.findById(dto.getId()).orElseThrow(()->new IllegalArgumentException("해당 post가 없음!"));
        if(dto.getAttachments() != null && !dto.getAttachments().isEmpty()  ) {
            List<FileSaveResultDTO> fileSaveResultDTO= generalFileUtil.saveFiles(dto.getAttachments(), dto.getId());
            //fileSaveResultDTO.forEach(fileSaveDTO->log.debug("fileSaveDTO :" , fileSaveDTO.toString()));

            //Post post = postRepository.findById(dto.getId()).orElseThrow(()->new IllegalArgumentException("해당 post가 없음!"));

            List<Attachment> attachments = fileSaveResultDTO.stream()
                    .map(saveDto -> Attachment.builder()
                            .post(post)
                            .fileName(saveDto.getFileName())
                            .originFileName(saveDto.getOriginFileName())
                            .fileType(saveDto.getFileType())
                            .fileSize(saveDto.getSize())
                            .fileUrl(saveDto.getFileUrl())
                            .uploadType(saveDto.getUploadType())
                            .uploadedAt(LocalDateTime.now())
                            .build())
                    .toList();

            attachmentRepository.saveAll(attachments);
        }

        //첨부된 파일중 삭제 대상 첨부 파일 삭제
        if( dto.getDeleteIds() != null && !dto.getDeleteIds().isEmpty()) {
            attachmentRepository.deleteAllByIdInBatch(dto.getDeleteIds());
        }

        //삭제 대상 tag id들을 List<Long> 타입으로 변환후 삭제 쿼리 실행
        List<Long> deleteTagIds = StringUtils.toLongList(dto.getDeleteTagIds());
        if(deleteTagIds != null && !deleteTagIds.isEmpty()) {
            postTagRepository.deleteByPostIdAndTagIdIn(post.getId(), deleteTagIds);
        }

        //태그 & post_tag 관계 저장
        if (dto.getTags() != null) {
            String[] tagArray = dto.getTags().split(",");
            for (String tagName : tagArray) {
                Tag tag = tagService.getOrCreateTag(tagName);
                PostTag postTag = new PostTag(post, tag);
                postTagRepository.save(postTag);
            }
        }

        // 본문/제목/카테고리 업데이트
        int updatedCount = postRepository.updatePostById(dto);
        if (updatedCount == 0) {
            throw new EntityNotFoundException("업데이트 실패!");
        }
        // 다시 조회
        Post updatedPost = postRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("업데이트 후 조회 실패!"));


        return updatedPost;
    }


    //Post 삭제
    @Transactional
    public void deletePost(Long id) {

        // 1. 하위 엔티티 먼저 삭제
        commentRepository.deleteByPostId(id);
        postTagRepository.deleteByPostId(id);
        attachmentRepository.deleteByPostId(id);

        postRepository.deleteById(id);
    }


}
