package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

  //  @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
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
}