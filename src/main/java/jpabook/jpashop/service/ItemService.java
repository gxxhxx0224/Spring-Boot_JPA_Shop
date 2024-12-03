package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService { //상품서비스는 상품리포지토리를 단순하게 위임하고 있는 클래스. 경우에 따라서 위임만 하는 클래스를 굳이 만들어야 하는지 고민해볼 필요가 있다
    private final ItemRepository itemRepository;

    @Transactional //readOnly=true면 저장 안 됨
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    /*
    준영속 엔터티를 수정하는 방법 1: *변경 감지 기능(더티 체킹)* -> 이렇게 해라. 이게 보통 더 나은 방법임
    영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){ //param: 파라미터로 넘어온 준영속 상태의 엔터티
        Item findItem = itemRepository.findOne(itemId); //영속상태, 같은 엔터티를 조회함
        
        //파라미터 너무 많다 싶으면 DTO 하나 만들어서 넘겨줘도 되고
        
        //데이터 수정(원래는 Setter말고 다른거로 해야함)
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);

        //위처럼 Setter쓰지말고 웬만하면 엔티티 안에서 바로 추적가능한 메서드 만들어서 해라
   //   findItem.change(name,price,stockQuantity);

        //itemRepository.save(findItem); 호출할 필요 없다!
//        return findItem;
    }

    /*
    방법2: 병합(Merge)사용(비추)x` -> 방법 1의 코드를 em.merge(itemParam)하나로 퉁치는 것
    동작방식이 완전히 똑같음. findItem을 반환해줌. 하지만,

    더티체킹은 원하는 속성만 선택해서 변경 가능하지만,
    머지는 모든 속성이 변경된다.(병합 시 값 없으면 null로 업데이트할 위험도 있음)->모든 필드를 교체하므로
    @Transactional
    public void update(Item itemParam){ //itemParam: 파라미터로 넘어온 준영속 상태의 엔터티
        Item mergeItem = em.merge(itemParam);
    }
    
     */


    public List<Item>findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
