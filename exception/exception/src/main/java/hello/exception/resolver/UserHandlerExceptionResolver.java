package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try{

            if(ex instanceof UserException){
                log.info("UserException resolver to 400");
                //http헤더가 json인경우와 json이 아닌경우로 구분해서 처리
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if("application/json".equals(acceptHeader)){
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex",ex.getClass());
                    errorResult.put("message",ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult);

                    //모델엔뷰를 반환해야해서 response에 세팅해줘야한다.
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);
                    //이렇게 해두면 예외는 먹어버리지만 정상적리턴이 되기때문에
                    //서블릿컨테이너까지 response가 전달된다.
                    return new ModelAndView();
                }else {
                    // TEXT/HTML 등이 넘어올때
                    return new ModelAndView("error/500");
                }
            }

        }catch (IOException e){
            log.error("resolver ex", e);
        }

        return null;
    }
}
