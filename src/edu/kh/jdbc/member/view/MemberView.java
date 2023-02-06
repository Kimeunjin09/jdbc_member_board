package edu.kh.jdbc.member.view;

import edu.kh.jdbc.member.vo.Member;

public class MemberView {
	
	// 로그인 회원 정보 저장용 변수
	private Member loginMember = null; // memberView에서만 다룰 아이(전달받은 애를 저장)
	// 얘를 토대로 밑에서 내정보 조회 등등의 기능 수행
	// MainView에서 누가 로그인했느냐에 따라 계속 값이 바뀌고 그 값을 MemberView에서 사용

	public void memberMenu(Member loginMember) { // 전달받아와서 위에 멤버뷰에서만 사용할 애 만들고 this로 전달해주는거
		
		// 전달받은 로그인 회원 정보를 필드에 저장
		this.loginMember = loginMember;
		
		do { 
			
		} while ()

		
	}

}
