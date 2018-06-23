package kosta.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kosta.mvc.model.dto.AuthorityDTO;
import kosta.mvc.model.dto.TempKeyDTO;
import kosta.mvc.model.dto.UserDTO;
import kosta.mvc.model.user.service.UserServiceImpl;
import kosta.mvc.model.util.Coolsms;

@Controller
@RequestMapping("/sign")
public class SignController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private TempKeyDTO tempKeyDTO;
	
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
	
	/**
	 * 	SMS �̿��� ���� �����ϱ�
	 * */
	@ResponseBody
	@RequestMapping(value = "/sendSMS", method = RequestMethod.POST)
	public String sendSMS(String userId2, String hp) throws Exception { // �޴��� ���ں�����

		String userId = userId2;
		System.out.println("����� ���̵� : " +userId2);
		
		String api_key = "NCSI2JO596AKT3A4";
		String api_secret = "YFGJY3FTUDVWJMXDKU3OFHRONSO0SGFD";
		Coolsms coolsms = new Coolsms(api_key, api_secret); // �޽��������� ��ü ����
		String key = new TempKeyDTO().getNumKeys(6); // ����Ű ����
		
		userService.insertAuthCode(userId,hp, key); // �޴��� ���� ���� ����
		/*
		 * Parameters �������� : http://www.coolsms.co.kr/SDK_Java_API_Reference_ko#toc-0
		 */
		
		HashMap<String, String> set = new HashMap<String, String>();
		set.put("to", hp); // ���Ź�ȣ
		set.put("from", "01048524897"); // �߽Ź�ȣ
		set.put("text", "�ȳ��ϼ��� ���繮 �Դϴ�. ������ȣ�� [" + key + "] �Դϴ�."); // ���ڳ���
		set.put("type", "sms"); // ���� Ÿ��

		JSONObject result = coolsms.send(set); // ������&���۰���ޱ�
		if ((boolean) result.get("status") == true) {
			// �޽��� ������ ���� �� ���۰�� ���
			System.out.println("����");
			System.out.println(result.get("group_id")); // �׷���̵�
			System.out.println(result.get("result_code")); // ����ڵ�
			System.out.println(result.get("result_message")); // ��� �޽���
			System.out.println(result.get("success_count")); // �޽������̵�
			System.out.println(result.get("error_count")); // ������ ������ ������ �޽��� ��
			
			tempKeyDTO.setUserId(userId);
			tempKeyDTO.setHp(hp);
			tempKeyDTO.setKey(key);
			
			return "success";
		} else {
			// �޽��� ������ ����
			System.out.println("����");
			System.out.println(result.get("code")); // REST API �����ڵ�
			System.out.println(result.get("message")); // �����޽���
			return "fail";
		}
	}
	
	
	/**
	 * 	�ڵ��� ��ȣ�� �Է¹޴� â���� �̵���Ű��
	 * */
	@RequestMapping("/inputHp")
		public String phoneNum() {
			return "sign/smsAuth";
	}
	
	/**
	 * 	�Է� ���� �ڵ��� ��ȣ ���۽�Ű��
	 * */
	@RequestMapping("/sendHp")
	public String sendingHp() {
		return "sign/loginForm";
	}
	
	/**
	 * 	������ȣ �����ϱ�
	 * */
	@RequestMapping("/authCheck")
	public String authChecking(String authKey) {
		System.out.println("���� Ű : " +authKey);
		userService.updateAuth(authKey);
		
		return "main";
	}
}
