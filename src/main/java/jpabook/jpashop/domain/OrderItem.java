package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED) //30번줄과 동일함. 생성메서드 방식 통일을 위해 밖에서 new 생성() 막음
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) //OrderItem 입장에서 Item과의 관계
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; //주문 당시의 가격(주문가격)
    private int count; //주문 당시의 수량(주문수량)

    //생성메서드 방식 통일을 위해 밖에서 new 생성() 막음
    protected OrderItem() { //jpa는 protected까지 기본생성자 생성 가능하다고 함(private도 되는거같은데?)
    }

    //==생성 메서드==//
    // new OrderItem()으로 생성하고 그러면 유지보수 어려워짐. 못하게 막아야됨.
    public static OrderItem createOrderItem(Item item,  int orderPrice, int count){
        OrderItem orderItem=new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //재고 minus
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel(){
        getItem().addStock(count); //재고수량 원복(add)!
    }

    //==조회 로직==//
    /*
    주문상품 전체 가격 조회
     */
    public int getTotalPrice(){
        return getOrderPrice()*getCount();
    }

}
