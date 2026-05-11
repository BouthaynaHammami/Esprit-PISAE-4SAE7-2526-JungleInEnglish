package tn.esprit.Books_Clubs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "BookClubUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    String firstName;
    String lastName;

    @Column(unique = true)
    String email;

    String password;

    LocalDateTime lastLogin;

    String cv;

    @Enumerated(EnumType.STRING)
    Role role;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    Wallet wallet;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Transaction> transactions;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<MembershipRequest> membershipRequests;

    @OneToMany(mappedBy = "decidedByMember")
    @JsonIgnore
    List<MembershipRequest> decidedRequests;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<ParticipationClub> clubParticipations;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<TrainingParticipation> trainingParticipations;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<ExcursionParticipation> excursionParticipations;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Order> orders;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Rental> rentals;
}