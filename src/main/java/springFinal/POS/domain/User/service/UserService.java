package springFinal.POS.domain.User.service;

import springFinal.POS.domain.User.User;
import springFinal.POS.web.dto.LoginDto;

public interface UserService {
    public User join(User user);
    public User login(LoginDto loginDto);
    void initUser();
}
