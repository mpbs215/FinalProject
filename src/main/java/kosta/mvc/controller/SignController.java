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
	 * 아이디 찾기 폼으로 이동
	 * */
	@RequestMapping("/findId")
	public String findIdForm() {
		return "sign/findIdForm";
	}
	
	@RequestMapping(value="/idFind", method = RequestMethod.POST)
    public String find_pass(UserDTO userDTO, RedirectAttributes redirectattr, Errors errors) {
		
		
		System.out.println("컨트롤러에서 userDTO의 이메일은 : " +userDTO.getEmail());
        new FindPassValidator().validate(userDTO, errors);
        
        if(errors.hasErrors()) {
        	return "sign/findIdForm";
        } else {
        
        try {
            UserDTO resultDTO = userService.execute(userDTO.getEmail());
            System.out.println("service에서 resultDTO값 : "+resultDTO);
            
            redirectattr.addFlashAttribute("resultDTO",resultDTO); 

            return "redirect:/sendpass";
            
        } catch(Exception e) {
            e.printStackTrace();
            
           return "sign/findIdForm";
        	}
        }
    }

	/**
	 * 	아이디 중복 체크 하기
	 * */
	@RequestMapping(value= "/idCheck",  produces="text/html; charset=utf-8")
	@ResponseBody
	public String idCheck(String userId) {
		String message = userService.idcheck(userId); 
		System.out.println(message);
		return message;
	}

	/**
	 * 비밀번호 찾기 폼으로 이동
	 * */
	@RequestMapping("/findPassword")
	public String findPasswordForm() {
		return "sign/findPwdForm";
	}

	/**
	 *  로그인 폼 띄우기
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
	 * 	회원가입 폼 띄우기
	 * */
	@RequestMapping("/signUpForm")
	public String signUpForm() {
		return "sign/signUpForm";
	}

	/**
	 * 	회원 가입 하기
	 * */
	@RequestMapping("/signUp")
	public String signUp(UserDTO userDTO) {
		
		System.out.println("이름" +userDTO.getUserName());
		
		userService.signUp(userDTO);	
		
		return "redirect:/";
	}
	

	/**
	 * 	로그인 체크 하기
	 * */
	@RequestMapping("/loginCheck")
	public ModelAndView loginCheck(UserDTO userDTO){
		
		ModelAndView mv = new ModelAndView();
		UserDTO dto = userService.loginCheck(userDTO);

		mv.addObject("resultDTO", dto);
		mv.setViewName("redirect:/");
		
		return mv;
	}
	
	/**
	 * 	SMS 이용한 본인 인증하기
	 * */
	@ResponseBody
	@RequestMapping(value = "/sendSMS", method = RequestMethod.POST)
	public String sendSMS(String userId2, String hp) throws Exception { // 휴대폰 문자보내기

		String userId = userId2;
		System.out.println("사용자 아이디 : " +userId2);
		
		String api_key = "NCSI2JO596AKT3A4";
		String api_secret = "YFGJY3FTUDVWJMXDKU3OFHRONSO0SGFD";
		Coolsms coolsms = new Coolsms(api_key, api_secret); // 메시지보내기 객체 생성
		String key = new TempKeyDTO().getNumKeys(6); // 인증키 생성
		
		userService.insertAuthCode(userId,hp, key); // 휴대폰 인증 관련 서비스
		/*
		 * Parameters 관련정보 : http://www.coolsms.co.kr/SDK_Java_API_Reference_ko#toc-0
		 */
		
		HashMap<String, String> smsSet = new HashMap<String, String>();
		smsSet.put("to", hp); // 수신번호
		smsSet.put("from", "01048524897"); // 발신번호
		smsSet.put("text", "안녕하세요 이재문 입니다. 인증번호는 [" + key + "] 입니다."); // 문자내용
		smsSet.put("type", "sms"); // 문자 타입

		JSONObject result = coolsms.send(smsSet); // 보내기&전송결과받기
		if ((boolean) result.get("status") == true) {
			// 메시지 보내기 성공 및 전송결과 출력
			System.out.println("성공");
			System.out.println(result.get("group_id")); // 그룹아이디
			System.out.println(result.get("result_code")); // 결과코드
			System.out.println(result.get("result_message")); // 결과 메시지
			System.out.println(result.get("success_count")); // 메시지아이디
			System.out.println(result.get("error_count")); // 여러개 보낼시 오류난 메시지 수
			
			tempKeyDTO.setUserId(userId);
			tempKeyDTO.setHp(hp);
			tempKeyDTO.setKey(key);
			
			return "success";
		} else {
			// 메시지 보내기 실패
			System.out.println("실패");
			System.out.println(result.get("code")); // REST API 에러코드
			System.out.println(result.get("message")); // 에러메시지
			return "fail";
		}
	}
	
	
	/**
	 * 	핸드폰 번호를 입력받는 창으로 이동시키기
	 * */
	@RequestMapping("/inputHp")
		public String phoneNum() {
			return "sign/smsAuth";
	}
	
	/**
	 * 	입력 받은 핸드폰 번호 전송시키기
	 * */
	@RequestMapping("/sendHp")
	public String sendingHp() {
		return "sign/loginForm";
	}
	
	/**
	 * 	인증번호 인증하기
	 * */
	@RequestMapping("/authCheck")
	public String authChecking(String authKey) {
		System.out.println("인증 키 : " +authKey);
		userService.updateAuth(authKey);
		
		return "main";
	}
}
