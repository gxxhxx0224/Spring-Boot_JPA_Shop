package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //생성메서드 방식 통일을 위해 밖에서 new 생성() 막음
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) //얘를 연관관계 주인으로 잡아라!
    @JoinColumn(name="member_id")
    private Member member;

    //cascade는 주인이 private owner일때만 쓰자!
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // mappedBy(나는 주인 아님):"누구에 의해서 매핑됐나" -> 읽기전용
    private List<OrderItem> orderItems=new ArrayList<>();

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL) //@OneToOne의 근심:일대일 관계에서는 FK를  어디든(Order/Delivery) 둘 수 있음(어디에 두냐에 따라 장단점이 있긴함)
    //주로 액세스를 많이 하는 곳에 FK를 놓는걸 선호! -> Order의 delivery를 관계의 주인으로!
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 편의 메서드==
    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드== : 생성할 때부터 주문생성에 대한 비즈니스 로직을 완결.
    // 앞으로 생성하는 시점 변경해야되면, 애만 변경하면 됨
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem:orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==
    /*
    주문취소
     */
    public void cancel(){
        //비즈니스 로직에 대한 체크로직이 엔티티 안에 있음
        if(delivery.getStatus()==DeliveryStatus.COMP){ //이미 배송완료라면
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL); //상태 바꿔줌
        for(OrderItem orderItem: orderItems){ //this 사용가능.(강조할 때, 이름 같을 때나 사용하자~)
            orderItem.cancel(); //재고수량 원복!(add)
        }
    }

    //==조회 로직==//
    /*
    전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice=0;
        for(OrderItem orderItem: orderItems){
            totalPrice+=orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
