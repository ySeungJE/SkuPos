package springFinal.POS.domain.User.service;

import jakarta.servlet.http.HttpServletRequest;
import springFinal.POS.domain.User.User;
import springFinal.POS.web.dto.LoginDto;

public interface UserService {
    User join(User user);
    User login(LoginDto loginDto);

    User getSessionUser(HttpServletRequest request);
}
