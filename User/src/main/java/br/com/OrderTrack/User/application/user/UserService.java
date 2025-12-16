package br.com.OrderTrack.User.application.user;

import br.com.OrderTrack.User.application.user.dto.SignInDTO;
import br.com.OrderTrack.User.application.user.dto.SignUpDTO;
import br.com.OrderTrack.Order.infrastructure.configuration.security.TokenService;
import br.com.OrderTrack.Order.infrastructure.user.UserEntity;
import br.com.OrderTrack.Order.infrastructure.profile.IProfileRepository;
import br.com.OrderTrack.Order.infrastructure.user.IUserRepository;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository repository;

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    public void signUp(@Valid SignUpDTO dto) {

        var signedUpUser = repository.existsByEmail(dto.email());
        if (signedUpUser) { throw new ValidationException("UserEntity already exists"); }

        var password = passwordEncoder.encode(dto.password());
        var profile = profileRepository.findByName("ROLE_CONSUMER").orElseThrow(() -> new ValidationException("ProfileEntity not found"));

        var user = new UserEntity(dto, password, profile);
        repository.save(user);
    }

    public String singIn(@Valid SignInDTO dto) {
        var authenticationManager = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = manager.authenticate(authenticationManager);

        return tokenService.generateToken((UserEntity) Objects.requireNonNull(authentication.getPrincipal()));
    }

    @NotNull
    @Override
    public UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow(() -> new ValidationException("UserEntity not found"));
    }
}
