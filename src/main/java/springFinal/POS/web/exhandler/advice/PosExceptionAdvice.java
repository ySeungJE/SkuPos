package springFinal.POS.web.exhandler.advice;


import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import springFinal.POS.web.dto.MessageDTO;
import springFinal.POS.web.exhandler.PosErrorResult;

@Slf4j
@org.springframework.web.bind.annotation.RestControllerAdvice(basePackages = {"springFinal.POS.web"})
public class PosExceptionAdvice {
    @ExceptionHandler
    public MessageDTO validationFailHandler(ConstraintViolationException e) {
        MessageDTO messageDTO = new MessageDTO("아이템 재고가 모자랍니다");
        return messageDTO;
    }
}
