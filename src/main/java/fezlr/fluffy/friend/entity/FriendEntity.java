package fezlr.fluffy.friend.entity;

import fezlr.fluffy.profile.entity.ProfileEntity;
import fezlr.fluffy.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "friends")
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //to get profile id - getUser().getProfile().getId()
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private UserEntity friend;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}