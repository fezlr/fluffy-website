package fezlr.fluffy.profile.entity;

import fezlr.fluffy.profile.enums.Gender;
import fezlr.fluffy.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "profiles")
public class ProfileEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @OneToOne(mappedBy = "profile")
    private UserEntity user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "city")
    private String city;

    @Column(length = 1000, name = "about_me")
    private String aboutMe;

    @Column(name = "main_photo_url")
    private String mainPhotoUrl;

    @Builder.Default
    @Column(name = "is_complete")
    private boolean isComplete = false;
}