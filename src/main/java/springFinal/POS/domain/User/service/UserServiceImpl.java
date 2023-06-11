package springFinal.POS.domain.User.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springFinal.POS.domain.User.User;
import springFinal.POS.domain.User.repository.UserRepository;
import springFinal.POS.web.dto.LoginDto;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User join(User user) {
        userRepository.save(user);
        return user;
    }
    @Override
    public User login(LoginDto loginDto) {
        User user = userRepository.findByLoginId(loginDto.getLoginId()).orElse(null);
        return user.getPassword().equals(loginDto.getPassword()) ? user : null;
    }
}
