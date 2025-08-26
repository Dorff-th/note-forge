package dev.noteforge.knowhub.tag.domain;

import dev.noteforge.knowhub.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_tag")
@Getter
@Setter
@IdClass(PostTag.class)
@NoArgsConstructor
public class PostTag {

    /*@Id
    @Column(nullable = false, unique = true)
    private Long postId;

    @Id
    @Column(nullable = false, unique = true)
    private Long tagId;*/

    @Id
    @ManyToOne
    @JoinColumn(name = "postId", insertable = false, updatable = false)
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagId", insertable = false, updatable = false)
    private Tag tag;

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }
}
