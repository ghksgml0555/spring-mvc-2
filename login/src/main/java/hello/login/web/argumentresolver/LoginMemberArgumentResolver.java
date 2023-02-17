package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {



    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        //컨트롤러 호출전에 파라미터에 @Login애노테이션이 붙어있는가
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        //파라미터의 파라미터 타입 > @Login이 붙은 파라미터의 타입 > Member가 온다
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        //HttpServletRequest가 필요한데 사용하려면 webRequest에서 getNativeRequest를 뽑아야한다.
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if(session==null){
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
