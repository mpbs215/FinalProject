package kosta.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.dto.CarTypeDTO;
import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkImgDTO;
import kosta.mvc.model.dto.ParkRegiDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.UserDTO;
import kosta.mvc.model.seller.service.SellerServiceImpl;

@RequestMapping("/seller")
@Controller
public class SellerController {

	@Autowired
	private SellerServiceImpl service;

	/**
	 * request : ���� ���̵�
	 * 
	 * @return �ڽ��� ������ ���ڵ帮��Ʈ
	 */
	@RequestMapping("/sellerParkList")
	public String sellerParkList() {
		return "Seller/sellerParkList";
	}

	@ResponseBody
	@RequestMapping
	public List<ParkDTO> sellerParkListLoad() {
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ParkDTO> list = service.sellerParkList(userDTO.getUserId());
		return list;
	}

	@ResponseBody
	@RequestMapping("/sellerParkDelete")
	public int sellerParkDelete(int parkNo) {
		return service.sellerParkDelete(parkNo);
	}

	@ResponseBody
	@RequestMapping("/sellerParksDelete")
	public int sellerParksDelete(int[] parkNos) {
		return service.sellerParksDelete(parkNos);
	}

	/**
	 * ������ ��� ������ �̵�
	 */
	@RequestMapping("/sellerParkRegistForm")
	public void sellerParkRegistForm() {
	}

	/**
	 * ������ ����ϱ�
	 * 
	 * @param parkDto
	 *            ����������
	 * @return
	 */
	@RequestMapping("/sellerParkRegist")
	public ModelAndView sellerParkRegist(ParkDTO parkDto, CarTypeDTO carTypeDto, ParkImgDTO parkImg,
			ParkRegiDTO parkRegi) {
		ModelAndView mv = new ModelAndView();

		service.sellerParkRegist(parkDto, carTypeDto, parkImg, parkRegi);

		mv.setViewName("redirect:/seller/sellerParkList");
		return mv;
	}

	/**
	 * request : ���� id
	 * 
	 * @return ���������Ȳ ����Ʈ
	 */
	@RequestMapping("/sellerReserveList")
	public ModelAndView sellerReserveList(String userId) {
		ModelAndView mv = new ModelAndView();
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ParkReserveDTO> list = service.sellerReserveList(userDTO.getUserId());
		mv.addObject("list", list);
		return mv;
	}

	/**
	 * �����Ȳ ���������� ���� �����Ȳ ���̺� ���ڵ� ȣ��
	 */
	@ResponseBody
	@RequestMapping("/sellerReserveListLoad")
	public List<ParkReserveDTO> sellerReserveListLoad() {
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ParkReserveDTO> list = service.sellerReserveListLoad(userDTO.getUserId());
		return list;
	}

	/**
	 * request: parkNo result: 0-����, 1-���� �����Ȳ ���������� ��� ��ưŬ����
	 */
	@ResponseBody
	@RequestMapping("/sellerReserveDelete")
	public void sellerReserveDelete(int reserveNo) {
		service.sellerReserveDelete(reserveNo);
	}
}