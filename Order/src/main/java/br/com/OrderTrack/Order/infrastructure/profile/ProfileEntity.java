package br.com.OrderTrack.Order.infrastructure.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class ProfileEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public boolean isAdmin() { return this.name.equals("ROLE_ADMIN"); }
    public boolean isConsumer() { return this.name.equals("ROLE_CONSUMER"); }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
