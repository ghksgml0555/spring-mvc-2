package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //resolveException > 넘어온 Exception을 보고 정상적인 ModelAndView를 반환해준다.

        try{
            if(ex instanceof IllegalArgumentException){
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                //새로운 빈값의 모델앤뷰를 반환하면 정상적인 흐름으로 리턴되다가 WAS까지 리턴된다. (예외를 삼켜버린다)
                //정상 리턴되다가 서블릿컨테이너에 가면 sendError호출되고 SC400으로 왔네 라고 생각
                return new ModelAndView();
            }
        }catch (IOException e){//sendError사용시 IOException을 잡아야한다.
            log.error("resolver ex", e);
        }

        //null로 리턴하면 예외가 터져서 다시 날라간다.
        return null;
    }
}
