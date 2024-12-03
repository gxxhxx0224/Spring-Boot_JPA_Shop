package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    //고객, 아이템을 모두 선택할 수 있어야하므로 dependency가 좀 필요함
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId, @RequestParam("count")int count){
        /*컨트롤러는 이렇게 식별자만 넘겨주고, 핵심로직은 OrderService의 order메서드 같은 곳에!
        서비스 계층에선 아무래도 엔터티 등에 더 많이 의존하니까.
        그럼 영속성 컨텍스트가 존재하는 상태에서 조회가능

         */
     // @RequestParam: FormSubmit 방식으로 오면, html에서 select태그에서 선택된 id의 value가 넘어옴

        //Long orderId= 아래일경우
        orderService.order(memberId,itemId,count);
        return "redirect:/orders"; //+ "/orderId"
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId")Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
