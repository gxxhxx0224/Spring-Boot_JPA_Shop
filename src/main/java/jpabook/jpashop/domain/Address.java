package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable //jpa의 내장타입. "어딘가에 내장될 수 있다."
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() { //public은 개나소나 호출하니까 jpa에선 protected까지 허용해줌
    } //jpa 스펙상 만든거구나~ 손대지말자~ 함부로 new로 생성하면 안되겠네~ 정도

    //변경 불가능한게 좋다.(@Setter는
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

