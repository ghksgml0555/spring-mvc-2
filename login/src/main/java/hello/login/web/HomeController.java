package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

  //  @GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name ="memberId", required = false)Long memberId, Model model) {
        //memberId 스트링이지만 Long으로 스프링이  >많이 뒤에서 설명한다고함
        if (memberId == null) {
            return "home";
        }

        //로그인성공사용자
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) { //쿠키가 너무 옛날에 많들어 졌거나해서 db에 없을시 홈으로보내기
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);

        //로그인
        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        //로그인하지 않은 처음들어온 사용자도 세션이 만들어지면 안되니까 false를 준다.
        HttpSession session = request.getSession(false);
        if(session==null){
            return "home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
            //이렇게 해두면 세션을 뒤져서 어트리부트를 꺼내서 값을 넣어준다

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {
        //이렇게 해두면 세션을 뒤져서 어트리부트를 꺼내서 값을 넣어준다

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}