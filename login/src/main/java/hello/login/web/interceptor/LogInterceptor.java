package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        /*post는 예외인 상황에서 안찍히니까 afterCompletion에서 찍어야한다.
           uuid를 afterCompletion에 넘겨야하는데 싱글톤이라 클래스 단위에 변수를 만들면
           큰일난다. request.setAttribute()로 넘기면 된다.*/

        request.setAttribute(LOG_ID,uuid);

        //보통 @RequestMapping시 핸들러는 HandlerMethod가 사용된다.
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨트롤러 메서드의 모든 정보가 포함.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //포스트핸들은 모델엔뷰도 반환해줘서 한번 찍어봄
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        //만약에 예외가 널이 아니면
        if(ex!=null){
            //로그찍을때 오류는 {}안하고 그냥 넣어도 된다.
            log.error("afterCompletion error!!", ex);
        }
    }
}
