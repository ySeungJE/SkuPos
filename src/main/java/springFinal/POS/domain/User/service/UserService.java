package springFinal.POS.domain.User.service;

import springFinal.POS.domain.User.User;
import springFinal.POS.web.dto.LoginDto;

public interface UserService {
    User join(User user);
    User login(LoginDto loginDto);
}
