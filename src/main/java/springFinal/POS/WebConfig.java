package springFinal.POS;


import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springFinal.POS.web.interceptor.LoginCheckInterceptor;

// 이런 필터 적용하는 등의 스프링 내부 로직 때문에 성능을 깎아먹는 정도는 미미하다
// 오히려 데이터베이스 쿼리나 외부 네트워크 같은 게 성능 다 깎아먹고 이런 건 바다의 모래알

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/join", "/login", "/error", "/notLoginUser");
    }

    String apiKey = "5874574837427237";
    String secretKey = "jAMTXhZI0eRE46qwC00y4tBUQy9KGwuLO38ny4ccuRJlQ3TwRkrNDJ6ny2hMu8H5oF3yPJzr4TboNQ2b";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}


//@Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new LoginMemberArgumentResolver());
//    }