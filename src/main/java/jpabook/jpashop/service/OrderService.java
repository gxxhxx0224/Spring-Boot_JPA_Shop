package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
    주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔터티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
//        OrderItem orderItem1=new OrderItem(); //protected 기본생성자()로 막아둠

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //Order보면 cascade 걸려 있어서, 하나만 저장해도 orderItem이랑 Delivery가 자동으로 persist됨
        //cascade의 범위: 명확하게 나누긴 애매하지만, Order의 경우: Order가 Delivery를 관리하고 Order가 OrderItem을 관리한다. 이 정도에서만 쓰는게 좋다.
        //참조하는게 주인이 private owner인 경우에만 cacse 쓰는게 좋다 : Delivery,OrderItem은 Order말고 아무데서도 참조 안 함(D,OI가 다른걸 참조할 수 있지만 다른데선 얘네 참조 안 함)
        orderRepository.save(order); //다른데서도 참조한다? cascase 막쓰면 안됨!

        return order.getId();
    }

    /*
    주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔터티 조회

        //엔티티의
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        //도메인 모델 패턴
        // 1(엔티티에 핵심 비즈니스 로직을 몰아넣음. 서비스는 엔티티에 필요한 요청을 위임)
        order.cancel();

        /*트랜잭셔널 스크립트(서비스 계층)에서 위처럼 비즈니스 로직을 다 사용할 수 밖에 없다.(sql 직접 다루는 스타일로 할 땐)
        하지만 jpa를 활용하면 Order에서처럼 엔티티 안에 있는 데이터만 바꾸면,
        jpa가 알아서 바뀐 변경 포인트들을 더티체킹(변경 내역 감지)가 일어나면서
        변경된 내역들을 찾아서 db에 업데이트 쿼리가 쫙쫙 날아감
         */
    }

    /*
    검색
     */
    public List<Order> findOrders(OrderSearch orderSearch){
        List<Order> orders = orderRepository.findAllByString(orderSearch);
        return orders;
    }
}
