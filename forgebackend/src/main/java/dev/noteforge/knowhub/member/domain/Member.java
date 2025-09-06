package dev.noteforge.knowhub.member.domain;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.member.enums.MemberStatus;
import dev.noteforge.knowhub.comment.domain.Comment;
import dev.noteforge.knowhub.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    //private String role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleType role;

    private String nickname;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 사용자(member) 1 : N 게시물(post)
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    private String profileImageName;    // 사용자 프로필 이미지 원본파일 명

    private String profileImagePath;    // 사용자 프로필 이미지 서버 저장 경로

    private String profileImageUrl;     // 사용자 프로필 이미지 공개 경로

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;  // 사용자 활성/비활성 

    private boolean deleted = false; // true = 탈퇴   // 탈퇴 회원일 경우 true (1) 로

  public Member(String email, String password, RoleType role, String nickname) {
        this.username = email;  // username에도 email 들어가게(email을 아이디 처럼 사용하고 싶어서)
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
    }

}
