package kosta.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.dto.UserDTO;

@Controller
@RequestMapping("/sign")
public class SignController {

	/**
	 * ���ε�IDForm�������� �̵�
	 * @return 
	 */
	@RequestMapping("/findIdForm")
	public void findIdForm() {}
	
	/**
	 * request: �̸��� �̸���
	 * @return ���̵�
	 */
	@RequestMapping("/findId")
	public ModelAndView findId() {
		
		return null;
	}
	
	/**
	 * ���ε�Pwd�������� �̵�
	 * @return 
	 */
	@RequestMapping("/findPwdForm")
	public void findPwdForm() {}
	
	/**
	 * request: �̸� ���̵� ��й�ȣ
	 * @return ��й�ȣ
	 */
	@RequestMapping("/findPwd")
	public ModelAndView findPwd() {
		
		return null;
	}
	
	/**
	 * �α��� �������� �̵�
	 */
	@RequestMapping("/loginForm")
	public void loginForm() {}
	
	/**
	 * Request : ID, PWD
	 */
	@RequestMapping("/login")
	public ModelAndView login(String userId,String password) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/");
		return mv;
	}
	
	/**
	 * ȸ������ ������ �̵�
	 */
	@RequestMapping("/signUpForm")
	public void signUpForm() {}
	
	/**
	 * request : UserDTO
	 * @return
	 */
	@RequestMapping("/signUp")
	public ModelAndView signUp(UserDTO dto) {
		return null;
	}
}
