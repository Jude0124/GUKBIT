package com.gukbit.service;

import com.gukbit.repository.MemberRepository;
import com.gukbit.model.MemberModel;

public class MemberService {

  MemberRepository memberRepository;

  //회원가입 (유저 인서트)
  public int setInsert(MemberModel memberVO) throws Exception{
    int result = memberRepository.setInsert(memberVO);
    return result;
  }

}
