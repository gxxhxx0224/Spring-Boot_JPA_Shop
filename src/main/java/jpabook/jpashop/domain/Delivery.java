package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery" ,fetch=FetchType.LAZY)//@OneToOne의 근심:일대일 관계에서는 FK를 어디든 둘 수 있음(어디에 두냐에 따라 장단점이 있긴함)
    //주로 액세스를 많이 하는 곳에 FK를 놓는걸 선호! -> Order의 delivery가 관계의 주인
    private Order order;

    @Embedded //내장타입이므로 써줌
    private Address address;

    //enum 타입엔 @Enumerated 넣어줘야함
    @Enumerated(EnumType.STRING) //EnumType은 꼭 STRING!:X` ORDINAL(Default):1,2,3,.../STRING.  예를들면 READY:0, CAMP:1 이런식. 근데 중간에 XXX 다른 상태가 들어가면 망한다
    private DeliveryStatus status; //READY, CAMP
}
