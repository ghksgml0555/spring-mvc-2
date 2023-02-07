package hello.itemservice.domain.item;

public enum ItemType {

    BOOK("도서"), FOOD("음식"), ETC("기타");
    //ENUM은 속성을 가질 수 있다.

    private final String description;

    ItemType(String description){
        this.description = description;
    }

}
