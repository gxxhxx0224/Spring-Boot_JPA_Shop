package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //스프링빈으로 등록(@Component가 들어있어서 컴포넌트 스캔대상)
@RequiredArgsConstructor
public class MemberRepository {

    //스프링이 EntityManger를 만들어서 여기다 주입(Injection)해줌
    //스프링이 생성한 JPA의 엔티티 매니저를 지가 여기다 주입해줌
//    @PersistenceContext //Service와 마찬가지로 @RequiredArgsConstructor쓰면 영속성 컨텍스트 생략가능
    private final EntityManager em;

//    //EntityManagerFactory를 직접 주입받을수 있음(거의 쓸 일 없음)
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    public void save(Member member) {
        //영속성 컨텍스트에 멤버 엔티티(객체)를 넣음
        //그럼 나중에 트랜잭션이 commit되는 시점에 db에 반영됨(db에 insert 쿼리 ㄱ)
        em.persist(member); //영속성 컨텍스틑 pk와 밸류가 있는데 id가 PK가 됨
    }

    public Member findOne(Long id) { //단건조회
        return em.find(Member.class, id); //(타입,PK)
    }

    public List<Member> findAll() {
        //JPQL은 SQL과 비슷하지만 다름
        // sql은 테이블 대상 쿼리, JPQL는 엔티티 객체를 대상으로 쿼리
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
