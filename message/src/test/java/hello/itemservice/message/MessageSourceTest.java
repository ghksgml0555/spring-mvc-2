package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@SpringBootTest //이걸 달아주면 스프링부트가 자동으로 돈다. >스프링부트가 자동으로 MessageSource등록
                // >기본으로 spring.messages.basename=messages가 있어서 messages.properites들 불러들임
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){
        //코드, 파라미터, 위치정보 순 >null이니까 디폴트가 실행됨(코드에 맞는 값 받음)
        String result = ms.getMessage("hello", null, null);
        Assertions.assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode(){
        Assertions.assertThatThrownBy(()-> ms.getMessage("no_code",null,null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void notFoundMessageCodeDefaultMessage(){
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        Assertions.assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void argumentMessage(){
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        Assertions.assertThat(message).isEqualTo("안녕 Spring");
        //안녕 {0} 에서 {0}이 Spring으로 치환된다.
    }

    @Test
    void defaultLang() {
        Assertions.assertThat(ms.getMessage("hello",null,null)).isEqualTo("안녕");
        Assertions.assertThat(ms.getMessage("hello",null, Locale.KOREA)).isEqualTo("안녕");

    }

    @Test
    void enLang() {
        Assertions.assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
