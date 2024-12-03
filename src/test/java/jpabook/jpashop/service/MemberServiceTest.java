package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


//@ExtendWith(SpringExtension.class) //@SpringBootTest에 이미 정의됨.(중복)
@SpringBootTest
@Transactional //테스트 케이스에서 사용될땐: 테스트 끝나면 다 롤백
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
//    @Autowired EntityManager em;//롤백이지만 db에 쿼리 남기는걸 보고싶으면

    @Test
//    @Rollback(false) //@Transactional에서 기본적으로 롤백을 해버림. false로하면 안함
    //롤백하는 이유: 테스트가 반복해서 돼야하므로, db에 데이터가 남으면 안됨
    public void 회원가입()throws Exception{
        //given
        Member member=new Member();
        member.setName("kim");
        //when
        Long savedId = memberService.join(member);
        //then
//        em.flush(); //영속성 컨텍스트에 들어가는 member에 있는게, db에 쿼리로 반영됨
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(savedId));
//        Assertions.assertEquals(member,memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1=new Member();
        member1.setName("kim");

        Member member2=new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        assertThrows(IllegalStateException.class, ()->{memberService.join(member2);}); //IllegalStateException 던지니까 성공!

        //when
//        memberService.join(member1);
//        try{ //위의 assertThrows와 같다
//            memberService.join(member2); //예외가 발생해야 함!!: 여기서 끝나고 then까지 가면 안됨
//        }catch (IllegalStateException e){
//            return; //try 후 IllegalStateException 발생.so, return이 떨어지므로 테스트 성공
//        }

        //then
        //JUnit4의 @Test(expected=~~Exception.class)일 경우에만 fail();
//        fail("예외가 발생했어야함: 여기까지 코드 오면 안 됨");
    }
}