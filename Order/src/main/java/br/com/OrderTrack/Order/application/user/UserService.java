package br.com.OrderTrack.Order.application.user;

import br.com.OrderTrack.Order.application.user.dto.SignUpDTO;
import br.com.OrderTrack.Order.infrastructure.user.UserEntity;
import br.com.OrderTrack.Order.infrastructure.profile.IProfileRepository;
import br.com.OrderTrack.Order.infrastructure.user.IUserRepository;
import br.com.OrderTrack.Order.application.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository repository;

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signUp(@Valid SignUpDTO dto) {

        var signedUpUser = repository.existsByEmail(dto.email());
        if (signedUpUser) { throw new ValidationException("UserEntity already exists"); }

        var password = passwordEncoder.encode(dto.password());
        var profile = profileRepository.findByName("ROLE_CONSUMER").orElseThrow(() -> new ValidationException("ProfileEntity not found"));

        var user = new UserEntity(dto, password, profile);
        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow(() -> new ValidationException("UserEntity not found"));
    }
}
