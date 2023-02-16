package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){

        //세션 생성

        /*createSession에 HttpServletResponse객체가 필요한데 이건 인터페이스이다.
        구현체는 원래 톰캣같은 애들이 별도로 제공해서 테스트가 어려운데 이럴때 스프링에서
        제공하는 MockHttpServletResponse를 사용(가짜로 리스폰스 기능 테스트할 수 있게해줌)
        로그인했다고 가정하고 서버가 그정보를 세션에 담아서 응답을 보낸다. */
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장
        //(웹브라우져에서 응답에서 나온 쿠키값을 가지고 서버로 전송)
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회(서버에서 확인)
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();
    }
}
