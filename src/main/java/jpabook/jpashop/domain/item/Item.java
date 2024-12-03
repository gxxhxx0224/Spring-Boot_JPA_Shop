package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
//상속관계매핑이라, 상속관계 전략을 지정해야 함. 부모클래스에 잡아줌
@Inheritance(strategy = InheritanceType.JOINED) //JOINED:가장 정규화된 스타일, SINGLE_TABLE: 한테이블에 다 때려박음, TABLE_PER_CLASS:(지금 기준)Album,Book,Movie 3 테이블만 나오는 전략
@DiscriminatorColumn(name="dtype") //Album,Book,Movie의 @DiscriminatorValue와 세트
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    //name,price,stockQuantity는 공통속성
    //이걸 상속할 클래스들을 만들어야함!
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category>categories=new ArrayList<>();

    //==비즈니스 로직==// : 재고(stock) 증가 로직
    //엔티티 자체가 해결할 수 있는 것은, 엔티티 안에 비즈니스 로직을 넣는게 좋다.(객체지향적)
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }

    //stock 감소 로직
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock<0)
            throw new NotEnoughStockException("need more stock");

        this.stockQuantity=restStock;
    }

}
