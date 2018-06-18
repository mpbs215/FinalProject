package kosta.mvc.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kosta.mvc.model.dto.UserDTO;
import kosta.mvc.model.user.service.UserServiceImpl;

@Controller
@RequestMapping("/sign")
public class SignController {

	@Autowired
	private UserServiceImpl userService;

	/**
	 * ���̵� ã�� ������ �̵�
	 * */
	@RequestMapping("/findId")
	public String findIdForm() {
		return "sign/findIdForm";
	}
	
	@RequestMapping(value="/idFind", method = RequestMethod.POST)
    public String find_pass(UserDTO userDTO, RedirectAttributes redirectattr, Errors errors) {
		
		
		System.out.println("��Ʈ�ѷ����� userDTO�� �̸����� : " +userDTO.getEmail());
        new FindPassValidator().validate(userDTO, errors);
        
        if(errors.hasErrors()) {
        	return "sign/findIdForm";
        } else {
        
        try {
            UserDTO resultDTO = userService.execute(userDTO.getEmail());
            System.out.println("service���� resultDTO�� : "+resultDTO);
            
            redirectattr.addFlashAttribute("resultDTO",resultDTO); 

            return "redirect:/sendpass";
            
        } catch(Exception e) {
            e.printStackTrace();
            
           return "sign/findIdForm";
        	}
        }
    }

	/**
	 * 	���̵� �ߺ� üũ �ϱ�
	 * */
	@RequestMapping(value= "/idCheck",  produces="text/html; charset=utf-8")
	@ResponseBody
	public String idCheck(String userId) {
		String message = userService.idcheck(userId); 
		System.out.println(message);
		return message;
	}

	/**
	 * ��й�ȣ ã�� ������ �̵�
	 * */
	@RequestMapping("/findPassword")
	public String findPasswordForm() {
		return "sign/findPwdForm";
	}

	/**
	 *  �α��� �� ����
	 * */
	@RequestMapping("/loginForm")
	public String loginForm() {
		return "sign/loginForm";
	}

	/**
	 * Request : ID, PWD
	 */
	@RequestMapping("/login")
	public ModelAndView login(String userId, String password) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/");
		return mv;
	}

	/**
	 * 	ȸ������ �� ����
	 * */
	@RequestMapping("/signUpForm")
	public String signUpForm() {
		return "sign/signUpForm";
	}

	/**
	 * 	ȸ�� ���� �ϱ�
	 * */
	@RequestMapping("/signUp")
	public String signUp(UserDTO userDTO) {
		
		System.out.println("�̸�" +userDTO.getUserName());
		
		userService.signUp(userDTO);	
		
		return "redirect:/";
	}
	

	/**
	 * 	�α��� üũ �ϱ�
	 * */
	@RequestMapping("/loginCheck")
	public ModelAndView loginCheck(UserDTO userDTO, HttpSession session) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		UserDTO dto = userService.loginCheck(userDTO, session);
		System.out.println("session�� = " +session);
		
		if (session != null) {
			session.setAttribute("userName", userDTO.getUserName());
			session.setAttribute("userId", userDTO.getUserId());
		}

		mv.addObject("resultDTO", dto);
		mv.setViewName("redirect:/");
		
		return mv;
	}
}
