package com.springmvc.service;

import java.util.List;

import com.springmvc.domain.Member;

public interface MemberService {
	public Member Login(String memberId, String memberPw);
	public void join(Member member);
	boolean idCheck(String memberId);
	boolean nickCheck(String nickName);
	public Member getMember(String memberId);
	public boolean deleteMember(String memberId, String memberPw);
	public void updateMember(Member member, String memberId);
	public List<Member> getMembers();
	public Member getMemberFromNickName(String nickname);

}
