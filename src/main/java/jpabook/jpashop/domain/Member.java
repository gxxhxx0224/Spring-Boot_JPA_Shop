package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue //DB마다 다른데 쨋든, id값이 항상 생성되어 있는게 보장됨!
    @Column(name="member_id") //"테이블명_id" 뭔지 알기쉽잖아~ 한잔해~
    private Long id;

    private String name;

    @Embedded //@Embedded나 Embeddable 하나만 있어도 됨. 걍 두개로 많이 해줌)
    private Address address;

    //멤버 입장에선 1:M, 주문 입장에선 M:1
    @OneToMany(mappedBy = "member") // mappedBy(나는 주인 아님):"누구에 의해서 매핑됐나" -> 읽기전용
    private List<Order> orders=new ArrayList<>();

}
