package br.com.OrderTrack.Order.infrastructure.user;

import br.com.OrderTrack.Order.application.user.dto.SignUpDTO;
import br.com.OrderTrack.Order.infrastructure.profile.ProfileEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String  email;
    private String password;

    @ManyToMany
    @JoinTable(name = "users_profiles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "profile_id"))
    private List<ProfileEntity> profileEntities = new ArrayList<>();

    public UserEntity(@Valid SignUpDTO dto, String password, ProfileEntity profileEntity) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = password;
        this.profileEntities = List.of(profileEntity);
    }

    public Boolean isAdmin() { return this.profileEntities.stream().anyMatch(ProfileEntity::isAdmin); }
    public Boolean isConsumer() { return this.profileEntities.stream().anyMatch(ProfileEntity::isConsumer); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.profileEntities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
