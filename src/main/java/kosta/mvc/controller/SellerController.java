package kosta.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.dto.CarTypeDTO;
import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkImgDTO;
import kosta.mvc.model.dto.ParkRegiDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.SearchFilterDTO;
import kosta.mvc.model.dto.UserDTO;
import kosta.mvc.model.seller.service.SellerServiceImpl;

@RequestMapping("/seller")
@Controller
public class SellerController {

	@Autowired
	private SellerServiceImpl service;
	

	private String imgPath;

	/**
	 * ����� ������ ���ڵ帮��Ʈ
	 */

	@RequestMapping("/sellerParkList")
	public String sellerParkList(Model model) {
		
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ParkDTO> parkList =  service.sellerParkList(userDTO.getUserId());
		
		model.addAttribute("sellerParkList", parkList);
			
		return "seller/sellerParkList";
	}

	/**
	 * ������ ����Ʈ���� ���� ��ư Ŭ�� �� ����Ʈ ���ε�
	 * 
	 * ���� �ʿ�!!!
	 */
/*	@ResponseBody
	@RequestMapping
	public List<ParkDTO> sellerParkListReload() {
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ParkDTO> list = service.sellerParkList(userDTO.getUserId());
		return list;
	}*/

	/**
	 * ������ �ϳ� ����
	 */
	@ResponseBody
	@RequestMapping("/sellerParkDelete")
	public int sellerParkDelete(String pNo) {
		int parkNo = Integer.parseInt(pNo);
		return service.sellerParkDelete(parkNo);
	}

	/**
	 * ������ ������ ����
	 */
	@ResponseBody
	@RequestMapping("/sellerParksDelete")
	public int sellerParksDelete(List<String> parkNos) {
		
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
	 */
	@RequestMapping("/sellerParkRegist")
	public ModelAndView sellerParkRegist(HttpSession session, ParkDTO parkDto, CarTypeDTO carTypeDto, ParkImgDTO parkImgDto, ParkRegiDTO parkRegiDto, MultipartHttpServletRequest req) throws Exception{
		
		UserDTO userDTO=(UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		ModelAndView mv = new ModelAndView();
		
		parkDto.setUserId(userDTO.getUserId());

		//��Ƽ ���� ���ε�
		List<MultipartFile> mf = req.getFiles("files");		
		imgPath = session.getServletContext().getRealPath("/resources/images/park");// ���� ���� ���� ���

		service.sellerParkRegist(parkDto, carTypeDto, parkImgDto, parkRegiDto, imgPath, mf);
	
		mv.setViewName("redirect:/seller/sellerParkList");
		return mv;
	}

	/**
	 * �ּ� �˻�â ����
	 */
	@RequestMapping("/addrPopup")
	public void addrPopup() {}
	
	/**
	 * request : ���� id
	 * 
	 * @return ���� ����Ʈ
	 */
	@RequestMapping("/sellerReserveList")
	public ModelAndView sellerReserveList() {
		ModelAndView mv = new ModelAndView();
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// ���� ����
		List<ParkReserveDTO> list = service.sellerReserveList(userDTO.getUserId());
		mv.addObject("reserveList", list);
		// ���� ���� ��Ȳ
		List<ParkReserveDTO> listLoad = service.sellerReserveListLoad(userDTO.getUserId());
		mv.addObject("reserveListLoad", listLoad);
		return mv;
	}

	/**
	 * �����Ȳ ���������� ��� ��ưŬ����
	 * request: parkNo result: 0-����, 1-���� 
	 */
	@ResponseBody
	@RequestMapping("/sellerReserveDelete")
	public void sellerReserveDelete(int reserveNo) {
		service.sellerReserveDelete(reserveNo);
	}
}