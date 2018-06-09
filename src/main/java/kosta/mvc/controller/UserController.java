package kosta.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.dto.ReviewDTO;
import kosta.mvc.model.dto.SearchFilterDTO;
import kosta.mvc.model.user.service.UserServiceImpl;

@RequestMapping("/user")
@Controller
public class UserController {
	
	@Autowired
	private UserServiceImpl service;
	
	@RequestMapping("/userReserve")
	public void userReserve() {}
	
	/*
	 * �˻� ����(����, ��, ��, ��¥, �ð�, ����, ����, �븦 Ŭ����) �˻�
	 * 			 
	 */
	@RequestMapping("/userClickSearchPark")
	public ModelAndView userClickSearchPark(SearchFilterDTO searchFilterDTO) {
		
		return null;
	}
	
	/*
	 *  Ű����� Ű�ʵ�(�ּ�, ����, ��¥)
	 */
	@RequestMapping("/userStringSearchPark")
	public ModelAndView userStringSearchPark(String keyWord, String keyField) {
		return null;
	}
	
	/*
	 * ������ �����ϱ� ������ �̵�
	 * return : parkDTO , List<reviewDTO>
	 */
	@RequestMapping("/userReserveForm")
	public ModelAndView userReserveForm(int parkNo) {
		return null;
	}
	
	/*
	 * ���� ���� ���� Ŭ���� AJAX�� ���� ����Ʈ ����
	 */
	@RequestMapping("/userClickReviewStar")
	@ResponseBody
	public List<ReviewDTO> userClickReviewStar(int parkNo, int starNo){
		
		return null;
	}
	
	//////////////
	
}