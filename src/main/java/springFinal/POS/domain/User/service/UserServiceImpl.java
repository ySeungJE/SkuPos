package springFinal.POS.domain.User.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springFinal.POS.domain.User.User;
import springFinal.POS.domain.User.repository.UserRepository;
import springFinal.POS.web.dto.LoginDto;

import static springFinal.POS.web.contoller.MyPosController.LOGIN_USER;

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
        if(user==null) return null;
        return user.getPassword().equals(loginDto.getPassword()) ? user : null;
    }

    @Override
    public User getSessionUser(HttpServletRequest request) {
        User session = (User) request.getSession().getAttribute(LOGIN_USER);

        User user = userRepository.findById(session.getId()).orElse(null);
        return user;
    }
}
