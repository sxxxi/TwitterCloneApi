package ca.sxxxi.twitter_clone_backend.entity;

import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.utils.Mappable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Cacheable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class UserEntity implements Mappable<User>, UserDetails {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String pfp;
    @NonNull
    @Column(name = "first_name")
    private String firstName;
    @NonNull
    @Column(name = "last_name")
    private String lastName;
    @NonNull
    private String hash;

//    @JsonIgnore
    @Formula("concat(first_name, ' ', last_name)")
    private String fullName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "follower")
    private List<UserFollowEntity> following = List.of();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "followee")
    private List<UserFollowEntity> followers = List.of();

    @Override
    public User toModel() {
        return new User(id, pfp, firstName, lastName);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return hash;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
