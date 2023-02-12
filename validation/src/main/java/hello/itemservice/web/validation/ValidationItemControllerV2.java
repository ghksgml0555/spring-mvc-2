package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /*기존코드@PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }*/
    /*기존코드@PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {
        //검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        //검증로직
        if(!StringUtils.hasText(item.getItemName())){
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice()>1000000){
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        if(item.getQuantity() == null || item.getQuantity() >9999){
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity()!=null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }

        }

        //검증에 실패하면 다시 입력 폼으로
        if(!errors.isEmpty()){
            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }*/
   // @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //BindingResult > item이 바인딩된 결과가 담긴다.

        //검증로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
            //필드단위의 에러는 FieldError(오브젝트명, 필드명, 메세지)
        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >9999){
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity()!=null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
                //특정 필드에 대한 에러가 아닌 글로벌 에러는 ObjectError(오브젝트명, 메세지)
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        //bindingResult에 에러가 있는지는 hasErrors()로 검사
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors", errors);
            //바인딩리절트는 자동으로 뷰에 넘어가서 모델에 담을필요가 없다.
            return "validation/v2/addForm";
        }

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //BindingResult > item이 바인딩된 결과가 담긴다.

        //검증로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),false,null,null, "상품 이름은 필수 입니다."));
        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(),false,null,null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),false,null,null, "수량은 최대 9,999 까지 허용합니다."));

        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity()!=null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item",null,null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
                //특정 필드에 대한 에러가 아닌 글로벌 에러는 ObjectError(오브젝트명, 메세지)
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        //bindingResult에 에러가 있는지는 hasErrors()로 검사
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors", errors);
            //바인딩리절트는 자동으로 뷰에 넘어가서 모델에 담을필요가 없다.
            return "validation/v2/addForm";
        }

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //BindingResult > item이 바인딩된 결과가 담긴다.

        //검증로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),false,new String[]{"required.item.itemName"},null, null));
        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000}, null));
        }
        if(item.getQuantity() == null || item.getQuantity() >9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999}, null));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity()!=null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000,resultPrice}, null));
                //특정 필드에 대한 에러가 아닌 글로벌 에러는 ObjectError(오브젝트명, 메세지)
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        //bindingResult에 에러가 있는지는 hasErrors()로 검사
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors", errors);
            //바인딩리절트는 자동으로 뷰에 넘어가서 모델에 담을필요가 없다.
            return "validation/v2/addForm";
        }

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //BindingResult > item이 바인딩된 결과가 담긴다.

        //검증로직
        if(!StringUtils.hasText(item.getItemName())){
            //rejectValue(필드명,에러코드(required.item.itemName에서 required만 넣어도된다.), 파라미터(오브젝트배열), 디폴트메세지) >오브젝트는 이미 알고 있기때문에
            bindingResult.rejectValue("itemName","required");
        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice()>1000000){
            bindingResult.rejectValue("price","range", new Object[]{1000,1000000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() >9999){
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity()!=null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //그냥 reject()는 필드가 아닌 오브젝트에 대한 에러 >필드명도 필요없어진다.
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        //bindingResult에 에러가 있는지는 hasErrors()로 검사
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors", errors);
            //바인딩리절트는 자동으로 뷰에 넘어가서 모델에 담을필요가 없다.
            log.info("error={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item,BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //BindingResult > item이 바인딩된 결과가 담긴다.

        //itemValidator는 @Component로 스프링 빈에 등록이 되어있다.
        itemValidator.validate(item, bindingResult);


        //검증에 실패하면 다시 입력 폼으로
        //bindingResult에 에러가 있는지는 hasErrors()로 검사
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors", errors);
            //바인딩리절트는 자동으로 뷰에 넘어가서 모델에 담을필요가 없다.
            log.info("error={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}
