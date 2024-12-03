package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OldMemberRepositoryTest {

    @Autowired
    OldMemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember=memberRepository.find(savedId);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember).isEqualTo(member);
        System.out.println("findMember == member : "+(findMember==member));

    }

}