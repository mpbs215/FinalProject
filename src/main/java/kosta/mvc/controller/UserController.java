package kosta.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.dto.AuthorityDTO;
import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.ReviewDTO;
import kosta.mvc.model.dto.SearchFilterDTO;
import kosta.mvc.model.dto.TempKeyDTO;
import kosta.mvc.model.dto.UserDTO;
import kosta.mvc.model.user.service.SearchServiceImpl;
import kosta.mvc.model.user.service.UserServiceImpl;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private UserServiceImpl service;
	
	@Autowired
	private SearchServiceImpl searchService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * ������ ���� �ʱ�������
	 */
	@RequestMapping("/userReserve")
	public ModelAndView userReserve() {
		ModelAndView mv = new ModelAndView();
		List<String> sidoList=searchService.selectSido();
		mv.setViewName("user/userReserve");
		mv.addObject("sidoList",sidoList);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/renewParkList")
	public List<ParkDTO> renewParkList(SearchFilterDTO dto){
		return searchService.renewParkList(dto);
	}
	
	@ResponseBody
	@RequestMapping("/renewParkPager")
	public List<Object> renewParkPager(SearchFilterDTO dto,HttpServletRequest request){
		System.out.println(dto);
		return searchService.renewParkPager(dto, request);
	}

	/*
	 * �˻� ����(����, ��, ��, ��¥, �ð�, ����, ����, �븦 Ŭ����) �˻�
	 * 
	 */
	@RequestMapping("/userClickSearchPark")
	public ModelAndView userClickSearchPark(SearchFilterDTO searchFilterDTO) {

		return null;
	}

	/*
	 * Ű����� Ű�ʵ�(�ּ�, ����, ��¥)
	 */
	@RequestMapping("/userStringSearchPark")
	public ModelAndView userStringSearchPark(String keyWord, String keyField) {
		return null;
	}

	/*
	 * ������ �����ϱ� ������ �̵� return : parkDTO , List<reviewDTO>,List<parkRegiDTO>
	 */
	@RequestMapping("/userReserveForm")
	public ModelAndView userReserveForm(int parkNo) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> dataMap = service.userReserveForm(parkNo);
		mv.addObject("parkDTO",dataMap.get("parkDTO"));
		mv.addObject("parkRegiDTO",dataMap.get("parkRegiDTO"));
		mv.addObject("parkReserveList",dataMap.get("parkReserveList"));
		mv.addObject("reviewList",dataMap.get("reviewList"));
		mv.addObject("parkImageList", dataMap.get("parkImageList"));
		mv.addObject("carTypeList",dataMap.get("carTypeList"));
		mv.setViewName("user/userReserveForm");
		return mv;
	}
	
	@RequestMapping("/insertReview")
	public String insertReview(ReviewDTO dto) {
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		dto.setUserId(userDTO.getUserId());
		service.insertReview(dto);
		return "redirect:/user/userReserveForm?parkNo="+dto.getParkNo();
	}

	/*
	 * ���� ���� ���� Ŭ���� AJAX�� ���� ����Ʈ ����
	 */
	@RequestMapping("/userClickReviewStar")
	@ResponseBody
	public List<ReviewDTO> userClickReviewStar(int parkNo, int rating) {
		List<ReviewDTO> list=service.userClickReviewStar(parkNo, rating);
		for(ReviewDTO dto:list) {
			System.out.println(dto.getUserId());
		}
		return list;
	}
	

	@RequestMapping("/reservation")
	public String reservation(ParkReserveDTO dto) {
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		dto.setUserId(userDTO.getUserId());
		if(service.reserveCheck(dto).equals("OK")) {
			service.insertReserve(dto);
		}else {
			throw new RuntimeException("���࿡ �����Ͽ����ϴ�.");
		}
		return "redirect:/user/userMypageReserveList";
	}
	
	@ResponseBody
	@RequestMapping(value="/reserveCheck",produces="text/plain;charset=UTF-8")
	public String reserveCheck(ParkReserveDTO dto) {
		System.out.println("��Ʈ�ѷ� ȣ���");
		System.out.println(dto);
		UserDTO userDTO = (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		dto.setUserId(userDTO.getUserId());
		return service.reserveCheck(dto);
	}


	/**
	 * ���������� ȸ������ ���� ������ �̵������� ���� request:userId
	 * 
	 * @return ModelAndView�� �������̵� �ش��ϴ� DTO ��������
	 */

	/*
	 * UserDTO
	 * userDTO=(UserDTO)SecurityContextHolder.getContext().getAuthentication().
	 * getPrincipal(); �̰� ���ֽð� DTO���� id�� �������� �˴ϴ�.
	 */
	@RequestMapping("/userModifyUserForm")
	public ModelAndView userModifyUserForm(HttpServletRequest request) {
		UserDTO userDTO=(UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDTO dto = service.selectUserInfo(userDTO.getUserId());
		return new ModelAndView("mypage/userModifyUserForm", "dto", dto);
	}

	/**
	 * ���� ȸ������ ���� ���������� ������ư Ŭ���� �۾�
	 * 
	 * @param userDto
	 * @return userModifyUserFormȣ��
	 */
	@RequestMapping("/userModifyUser")
	public ModelAndView userModifyUser(UserDTO userDTO) {
		// ȸ������ �������� Spring Security ���� ȸ�������� ��ȯ�޴´�
		UserDTO uDTO = (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userDTO.setUserId(uDTO.getUserId());

		// ������ ��й�ȣ�� ��ȣȭ�Ѵ�
		String encodePassword = passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodePassword);
		

		// ������ư Ŭ�� ó��
		service.updateUserInfo(userDTO);

		return new ModelAndView("redirect:/user/userModifyUserForm");
	}
	
	/**
	 * ���������� ������
	 */
	@RequestMapping("/userMypageReserveList")
	public ModelAndView regiParkLoad(){
		ModelAndView mv = new ModelAndView();
		UserDTO userDTO = (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ParkDTO> list = service.userMypageReserveList(userDTO.getUserId());
		mv.setViewName("mypage/userMypageReserveList");
		mv.addObject("list",list);
		return mv;
	}
	
	@RequestMapping("/deleteReserve")
	public String deleteReserve(int reserveNo) {
		service.deleteReserve(reserveNo);
		return "redirect:/user/userMypageReserveList";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "redirect:/";
	}
	
	@ResponseBody
	@RequestMapping("/selectSido")
	public List<String> selectSido(){
		return searchService.selectSido();
	}
	
	@ResponseBody
	@RequestMapping("/selectGugun")
	public List<String> selectGugun(String sido){
		System.out.println(sido);
		return searchService.selectGugun(sido);
	}
	
	@ResponseBody
	@RequestMapping("/selectDong")
	public List<String> selectDong(String gugun){
		System.out.println(gugun);
		return searchService.selectDong(gugun);
	}
	
	@ResponseBody
	@RequestMapping("/selectRi")
	public List<String> selectRi(String dong){
		return searchService.selectRi(dong);
	}
	
	@RequestMapping(value="/unSign", method = RequestMethod.GET ) 
	public String unSign( HttpSession session,
						@RequestParam("password") String password,
						@RequestParam("userId") String userId) {
		
		String Depassword = service.selectPassword(userId);
		
		if (passwordEncoder.matches(password, Depassword)) {
			service.deleteUserInfo(password);
		}
		session.invalidate();
		
		return "redirect:/user/logout";
	}
}