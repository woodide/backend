package com.system.wood.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {


    private final MemberRepository memberRepository;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->{
            throw new EntityNotFoundException(
                    String.format("memberId가 %d인 멤버가 존재하지 않습니다.", memberId)
            );
        });
    }
}
