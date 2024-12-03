package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

@Repository //컴포넌트 스캔의 대상이 되는 어노테이션 중 하나
public class OldMemberRepository { //엔티티같은걸 찾아줌. DAO라고 생각하면됨

    @PersistenceContext //스프링부트가 엔티티 매니저를 주입해줌
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId(); //왜 아이디만 반환하지? -> 커맨드랑 쿼리를 분리해라.
        //사이드 이펙트를 일으키는 커맨드성이기 때문에, 저장하고나면 가급적 리턴값 안만듬. id정도 있으면 다음에 다시 조회가능하니까 이정도만!
    }
    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
