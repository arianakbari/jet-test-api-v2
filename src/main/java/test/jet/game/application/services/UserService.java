package test.jet.game.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test.jet.game.infrastructure.persistence.repositories.PlayerJpaRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private PlayerJpaRepository playerJpaRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return playerJpaRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Player not found"));
            }
        };
    }
}
