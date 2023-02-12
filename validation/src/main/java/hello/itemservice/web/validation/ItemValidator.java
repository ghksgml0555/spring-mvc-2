package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    //스프링이 제공하는 Validator 인터페이스를 구현한다.
    @Override
    public boolean supports(Class<?> clazz) { //supports >검증가능한 자원인지 검사
        //파라미터로 넘어오는 clazz가 Item혹은 Item의 자식클래스인가? >isAssignableFrom()
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) { //검증
        Item item = (Item) target;
        //Errors에 bindingResult넣어서 사용. (에러스의 자식이 bindingResult) 다형성


        //검증로직
        if(!StringUtils.hasText(item.getItemName())){
            //rejectValue(필드명,에러코드(required.item.itemName에서 required만 넣어도된다.), 파라미터(오브젝트배열), 디폴트메세지) >오브젝트는 이미 알고 있기때문에
            errors.rejectValue("itemName","required");
        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice()>1000000){
            errors.rejectValue("price","range", new Object[]{1000,1000000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() >9999){
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity()!=null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //그냥 reject()는 필드가 아닌 오브젝트에 대한 에러 >필드명도 필요없어진다.
                errors.reject("totalPriceMin", new Object[]{10000,resultPrice}, null);
            }
        }
    }
}
