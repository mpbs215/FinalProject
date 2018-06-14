package kosta.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkRegiDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.ReviewDTO;
import kosta.mvc.model.dto.SearchFilterDTO;
import kosta.mvc.model.dto.UserDTO;
import kosta.mvc.model.user.service.UserServiceImpl;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private UserServiceImpl service;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	/**
	 * ������ ���� �ʱ�������
	 */
	@RequestMapping("/userReserve")
	public ModelAndView userReserve() {
		ModelAndView mv = new ModelAndView();
		List<ParkDTO> list=service.userReserve();
		return mv;
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
		mv.setViewName("user/userReserveForm");
		return mv;
	}
	
	@RequestMapping("/insert")
	public String insertReview(ReviewDTO dto) {
		service.insertReview(dto);
		return "redirect:/user/userReserveForm";
	}

	/*
	 * ���� ���� ���� Ŭ���� AJAX�� ���� ����Ʈ ����
	 */
	@RequestMapping("/userClickReviewStar")
	@ResponseBody
	public List<ReviewDTO> userClickReviewStar(int parkNo, int starNo) {
		return service.userClickReviewStar(parkNo, starNo);
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
		String id = request.getParameter("userId");
		UserDTO userDTO = service.selectUserInfo(id);
		return new ModelAndView("User/userModifyUserForm", "userDTO", userDTO);
	}

	/**
	 * ���� ȸ������ ���� ���������� ������ư Ŭ���� �۾�
	 * 
	 * @param userDto
	 * @return userModifyUserFormȣ��
	 */
	@RequestMapping("/userModifyUser")
	public ModelAndView userModifyUser(HttpServletRequest request, UserDTO userDTO) {
		// System.out.println("1. UserDTO :: "+userDTO);
		// ȸ������ �������� Spring Security ���� ȸ�������� ��ȯ�޴´�
		UserDTO uDTO = (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// System.out.println("2. Spring Security ���� ���� �� ȸ������:" + uDTO);

		// ������ ��й�ȣ�� ��ȣȭ�Ѵ�
		String encodePassword = passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodePassword);
		service.updateUserInfo(userDTO);

		// ������ư Ŭ�� ó��
		
		// ������ ȸ�������� Spring Security ���� ȸ�������� ������Ʈ�Ѵ�
		uDTO.setPassword(encodePassword);
		uDTO.setUserName(userDTO.getUserName());
		uDTO.setAddress(userDTO.getAddress());
		//System.out.println("3. Spring Security ���� ���� �� ȸ������:" + pvo);

		return new ModelAndView("User/userModifyUserForm");
	}

}