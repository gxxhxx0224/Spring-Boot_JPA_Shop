package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    /*
    - @Valid: MemberForm에 있는 @NotEmpty 등등 Validation 해줌
    - BindingResult: 원래는 form에서 오류 있으면 튕기는데, validate 다음에 쓰면 오류를 result에 담겨서 코드가 실행됨!
    - Member가 아니라 MemberForm을 넣는 이유: 컨트롤러(화면)에서 넘어올때 밸리데이션과 실제 도메인이 원하는 밸리데이션이 다를 수 있음.
        엔터티(Member)를 화면에 왔다갔다하는 폼 데이터를 쓰기 시작하면 안맞음.
        so, 화면에 딱 맞는 폼 데이터를 맞들고 그걸로 데이터를 받아쓰는게 나음!
     */
    public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(),form.getStreet(), form.getZipcode());

        Member member=new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }
    @GetMapping("/members")
    public String list(Model model){
        /*
        이건 단순하니까 List로 그대로 다 뿌렸지, 
        실무적으로 복잡해지면 DTO로 변환해서 화면에 꼭 필요한 데이터만 가지고 출력하는걸 권장
         */
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);

        return "members/memberList";
    }
}
