package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
    //트랜잭션 안에서 데이터 변경하는 것은 @Transactional이 꼭 있어야함(클래스 레벨의 public 메서드들은 트랜잭션이 걸려들어감)
//JPA의 모든, 데이터 변경 or 로직들은 가급적이면 트랜잭션 안에서 다 실행돼야함. so, @Transactional 있어야함(jakarta보단 spring게 나음)
@Transactional(readOnly = true) //쓰기가 아닌 읽기만 하면 readOnly=true시 JPA가 조회하는 곳에선 성능최적화! 읽기엔 가급적 쓰자). 쓰기/읽기 뭐가 주냐에 따라 내가 선택
//@AllArgsConstructor //필드 모든걸 가지고 생성자 만들어줌
@RequiredArgsConstructor //AllArgsConstructor보다 더 나음: final 있는 필드만 생성자 만듬(요새 많이 씀)
public class MemberService {

    //@Autowired //스프링이 스프링빈에 등록돼있는 멤버리포지토리를 Injection해줌
    private final MemberRepository memberRepository; //필드 인젝션

    //요새 많이씀. 스프링 뜰 때 생성자에서 인젝션해줌
//    @Autowired //테스트, main에서 new MemberService(@@@); 이렇게 직접 주입해줘야함. 안놓칠 수 있음
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    } //생성자가 딱 하나만 있는 경우엔 @Autowired 생략해도 자동으로 인젝션해줌

    //    //Setter Injection: 필드 인젝션은 테스트 등 멤버리포지토리에 액세스해서 바꾸고 싶을때 못바꿈
//    @Autowired //세터 인젝션은 테스트코드 같은거 작성할때 mock 같은걸 내가 memberRepository에 직접 주입해줄 수 있음)
//    //치명적 단점: 런타임 시점에 누군가 이걸 바꿀 수 있다. 근데 
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /*
        회원가입
         */
    @Transactional //얘는 쓰기니까 디폴트인 readOnly=false
    public Long join(Member member){
        validateDuplicateMember(member); //중복회원 검증
        memberRepository.save(member);
        return member.getId(); //항상 값이 있다는게 보장됨(Member의 @GeneratedValue)
    }


    private void validateDuplicateMember(Member member) { //중복회원 검증 메서드
        //문제있으면 EXCEPTION <-> 없으면 다음로직(save)
        List<Member> findMembers = memberRepository.findByName(member.getName()); //같은 이름이 동시에 save하면 여기도 뚫리니까, 멤버의 이름을 유니크 제약조건으로 잡아라!
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체조회
    public List<Member>findMembers(){
        return memberRepository.findAll();
    }

    //1건 조회
    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }
}
