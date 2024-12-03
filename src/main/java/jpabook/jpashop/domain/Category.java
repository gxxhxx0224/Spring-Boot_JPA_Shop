package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany //중간테이블 매핑(CATEGORY_ITEM).
    @JoinTable(name = "category_item", //실무에선 이렇게 못함. 그냥 JPA가 이런것도 된다~
            joinColumns = @JoinColumn(name = "category_id"), //중간 테이블에 있는 category_id
                inverseJoinColumns = @JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();

    //한 엔티티 내에서 셀프로 양방향 연관관계를 걸었다.(다른 엔티티 매핑하는거랑 똑같은 방식)
    //내 부모는 누구지?
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent;

    //자식은 누구지?
    @OneToMany(mappedBy = "parent")
    private List<Category>child=new ArrayList<>(); //자식은 여러개가 오니까

    //==연관관계 편의메서드
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
