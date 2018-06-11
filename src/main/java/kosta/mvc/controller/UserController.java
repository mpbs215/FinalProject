package kosta.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		ParkDTO parkDTO = service.selectOnePark(parkNo);
		ParkRegiDTO parkRegiDTO = service.selectOneParkRegi(parkNo);
		List<ParkReserveDTO> parkReserveList = service.selectparkReserve(parkNo);
		List<ReviewDTO> reviewList = service.selectReview(parkNo);
		return null;
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
	@RequestMapping("/userModifyUserForm")
	public ModelAndView userModifyUserForm(String userId) {
		return null;
	}

	/**
	 * ���� ȸ������ ���� ���������� ������ư Ŭ���� �۾�
	 * 
	 * @param userDto
	 * @return userModifyUserFormȣ��
	 */
	@RequestMapping("/userModifyUser")
	public ModelAndView userModifyUser(UserDTO userDTO) {

		return null;
	}

}