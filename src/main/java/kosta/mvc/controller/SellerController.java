package kosta.mvc.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.dto.CarTypeDTO;
import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkImgDTO;
import kosta.mvc.model.dto.ParkRegiDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.QNADTO;
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
	//public String sellerParkList(Model model) {
	public ModelAndView sellerParkList(HttpServletRequest request) {	
		
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = userDTO.getUserId();
		
		/*
		 * ���������̼�
		 */
		int numPerPage = 10; // �� �������� �� ����
		int cPage; // Ŭ���� ������ ��ȣ
		try {
			cPage = Integer.parseInt(request.getParameter("cPage"));
		} catch (NumberFormatException e) {
			cPage = 1;
		}

		// 2.3 ����¡�� �����
		int totalQNA = service.parkCnt(userId, cPage, numPerPage); // �� �Խù� ����

		int totalPage = (int) Math.ceil((double) totalQNA / numPerPage); // �Խù� ���� ��� ������ �� ���ϱ�
		String pageBar = "";
		int pageBarSize = 5; // �ѹ��� ���̴� ������ ����
		
		// ���������� no - ������ư ������ �� ������ �� �� ������ ����
		int pageNo;
		pageNo = (int) (Math.ceil(((double) cPage / pageBarSize) - 1) * pageBarSize) + 1;

		// ���������� no - ������ư �� ������ ����
		int pageEnd = pageNo + pageBarSize - 1;

		pageBar += "<ul class='pagination justify-content-center pagination-sm'>";

		// [����]
		if (pageNo == 1) {
			// ������ư �ʿ����
			pageBar += "<li class='page-item disabled'>";
			pageBar += "<a class='page-link' href='#' tabindex='-1'>����</a>";
			pageBar += "</li>";
		} else {
			pageBar += "<li class='page-item'>";
			pageBar += "<a class='page-link' href=" + request.getContextPath() + "/seller/sellerParkList?cPage=" + (pageNo - 1)
					+ "><span>[����]</span></a>";
			pageBar += "</li>";
		}

		// [pageNo] - ������ �ټ����� ���� ��ũ(������ �̵�)
		while (pageNo <= pageEnd && pageNo <= totalPage) {
			if (pageNo == cPage) {
				pageBar += "<li class='page-item active'>";
				pageBar += "<a class='page-link'>" + pageNo + "</a>";
				pageBar += "</li>";
			} else {
				pageBar += "<li class='page-item'>";
				pageBar += "<a class='page-link' href =" + request.getContextPath() + "/seller/sellerParkList?cPage=" + pageNo + "> <span>" + pageNo + "</span></a>";
				pageBar += "</li>";
			}

			pageNo++;
		}
		// [����]

		if (pageNo > totalPage) {
			pageBar += "<li class='page-item disabled'>";
	         pageBar += "<a class='page-link' href='#'>����</a>";
	         pageBar += "</li>";
		} else {
			pageBar += "<li class='page-item'>";
			pageBar += "<a class='page-link' href=" + request.getContextPath() + "/seller/sellerParkList?cPage=" + (pageNo)
					+ "><span>[����]</span></a>";
			pageBar += "</li>";
		}

		pageBar += "</ul>";
		
		ModelAndView mv = new ModelAndView();
		List<ParkDTO> list = service.sellerParkList(userId, cPage, numPerPage);	
		mv.setViewName("seller/sellerParkList");
		mv.addObject("sellerParkList", list);
		mv.addObject("pageBar", pageBar);

		return mv;
	}

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
	public int sellerParksDelete(@RequestParam(value="pNos[]") List<String> parkNos) {
		
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
	 * @throws Exception 
	 */
	@RequestMapping(value="/sellerParkRegist", method=RequestMethod.POST)
	public ModelAndView sellerParkRegist(HttpSession session,ParkDTO parkDto, CarTypeDTO carTypeDto, ParkImgDTO parkImgDto, ParkRegiDTO parkRegiDto,MultipartHttpServletRequest req) throws Exception{
		System.out.println("�������� ��Ʈ�ѷ� ����");
		UserDTO userDTO=(UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		ModelAndView mv = new ModelAndView();
		System.out.println(userDTO.getUserId());
		parkDto.setUserId(userDTO.getUserId());

		//��Ƽ ���� ���ε�
		List<MultipartFile> mf = req.getFiles("files");		
		imgPath = session.getServletContext().getRealPath("/resources/images/park");// ���� ���� ���� ���

		service.sellerParkRegist(parkDto, carTypeDto, parkImgDto, parkRegiDto, imgPath, mf);
	
		mv.setViewName("redirect:/seller/sellerParkList");
		return mv;
	}
	
	/**
	 * request : ���� id
	 * 
	 * @return ���� ����Ʈ
	 */
	@RequestMapping("/sellerReserveList")
	public ModelAndView sellerReserveList(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = userDTO.getUserId();
		
		/*
		 * �ٰ����� ���� ����Ʈ ���������̼�
		 */
		int numPerPage = 10; // �� �������� �� ����
		int cPage; // Ŭ���� ������ ��ȣ
		try {
			cPage = Integer.parseInt(request.getParameter("cPage"));
		} catch (NumberFormatException e) {
			cPage = 1;
		}

		// 2.3 ����¡�� �����
		int totalQNA = service.sellerReserveListLoadCnt(userId, cPage, numPerPage); // �� �Խù� ����

		int totalPage = (int) Math.ceil((double) totalQNA / numPerPage); // �Խù� ���� ��� ������ �� ���ϱ�
		String pageBar = "";
		int pageBarSize = 5; // �ѹ��� ���̴� ������ ����
		
		// ���������� no - ������ư ������ �� ������ �� �� ������ ����
		int pageNo;
		pageNo = (int) (Math.ceil(((double) cPage / pageBarSize) - 1) * pageBarSize) + 1;

		// ���������� no - ������ư �� ������ ����
		int pageEnd = pageNo + pageBarSize - 1;

		pageBar += "<ul class='pagination justify-content-center pagination-sm'>";

		// [����]
		if (pageNo == 1) {
			// ������ư �ʿ����
			pageBar += "<li class='page-item disabled'>";
			pageBar += "<a class='page-link' href='#' tabindex='-1'>����</a>";
			pageBar += "</li>";
		} else {
			pageBar += "<li class='page-item'>";
			pageBar += "<a class='page-link' href=" + request.getContextPath() + "/seller/sellerReserveList?cPage=" + (pageNo - 1)
					+ "><span>[����]</span></a>";
			pageBar += "</li>";
		}

		// [pageNo] - ������ �ټ����� ���� ��ũ(������ �̵�)
		while (pageNo <= pageEnd && pageNo <= totalPage) {
			if (pageNo == cPage) {
				pageBar += "<li class='page-item active'>";
				pageBar += "<a class='page-link'>" + pageNo + "</a>";
				pageBar += "</li>";
			} else {
				pageBar += "<li class='page-item'>";
				pageBar += "<a class='page-link' href =" + request.getContextPath() + "/seller/sellerReserveList?cPage=" + pageNo + "> <span>" + pageNo + "</span></a>";
				pageBar += "</li>";
			}

			pageNo++;
		}
		// [����]

		if (pageNo > totalPage) {
			pageBar += "<li class='page-item disabled'>";
	         pageBar += "<a class='page-link' href='#'>����</a>";
	         pageBar += "</li>";
		} else {
			pageBar += "<li class='page-item'>";
			pageBar += "<a class='page-link' href=" + request.getContextPath() + "/seller/sellerReserveList?cPage=" + (pageNo)
					+ "><span>[����]</span></a>";
			pageBar += "</li>";
		}

		pageBar += "</ul>";
				
		List<ParkReserveDTO> listLoad = service.sellerReserveListLoad(userId, cPage, numPerPage);			
		mv.addObject("reserveListLoad", listLoad);
		mv.addObject("pageBar", pageBar);

		
		
		/*
		 * ���� ���� ����Ʈ ���������̼�
		 */
		int numPerPage2 = 10; // �� �������� �� ����
		int cPage2; // Ŭ���� ������ ��ȣ
		try {
			cPage2 = Integer.parseInt(request.getParameter("cPage2"));
		} catch (NumberFormatException e) {
			cPage2 = 1;
		}

		// 2.3 ����¡�� �����
		int totalQNA2 = service.sellerReserveListLoadCnt(userId, cPage2, numPerPage2); // �� �Խù� ����

		int totalPage2 = (int) Math.ceil((double) totalQNA2 / numPerPage2); // �Խù� ���� ��� ������ �� ���ϱ�
		String pageBar2 = "";
		int pageBarSize2 = 5; // �ѹ��� ���̴� ������ ����
		
		// ���������� no - ������ư ������ �� ������ �� �� ������ ����
		int pageNo2;
		pageNo2 = (int) (Math.ceil(((double) cPage2 / pageBarSize2) - 1) * pageBarSize2) + 1;

		// ���������� no - ������ư �� ������ ����
		int pageEnd2 = pageNo2 + pageBarSize2 - 1;

		pageBar2 += "<ul class='pagination justify-content-center pagination-sm'>";

		// [����]
		if (pageNo2 == 1) {
			// ������ư �ʿ����
			pageBar2 += "<li class='page-item disabled'>";
			pageBar2 += "<a class='page-link' href='#' tabindex='-1'>����</a>";
			pageBar2 += "</li>";
		} else {
			pageBar2 += "<li class='page-item'>";
			pageBar2 += "<a class='page-link' href=" + request.getContextPath() + "/seller/sellerReserveList?cPage2=" + (pageNo2 - 1)
					+ "><span>[����]</span></a>";
			pageBar2 += "</li>";
		}

		// [pageNo] - ������ �ټ����� ���� ��ũ(������ �̵�)
		while (pageNo2 <= pageEnd2 && pageNo2 <= totalPage2) {
			if (pageNo2 == cPage2) {
				pageBar2 += "<li class='page-item active'>";
				pageBar2 += "<a class='page-link'>" + pageNo2 + "</a>";
				pageBar2 += "</li>";
			} else {
				pageBar2 += "<li class='page-item'>";
				pageBar2 += "<a class='page-link' href =" + request.getContextPath() + "/seller/sellerReserveList?cPage2=" + pageNo2 + "> <span>" + pageNo2 + "</span></a>";
				pageBar2 += "</li>";
			}

			pageNo2++;
		}
		// [����]

		if (pageNo2 > totalPage2) {
			pageBar2 += "<li class='page-item disabled'>";
	         pageBar2 += "<a class='page-link' href='#'>����</a>";
	         pageBar2 += "</li>";
		} else {
			pageBar2 += "<li class='page-item'>";
			pageBar2 += "<a class='page-link' href=" + request.getContextPath() + "/seller/sellerReserveList?cPage2=" + (pageNo2)
					+ "><span>[����]</span></a>";
			pageBar2 += "</li>";
		}

		pageBar2 += "</ul>";
				
		List<ParkReserveDTO> list = service.sellerReserveList(userId, cPage2, numPerPage2);			
		mv.addObject("reserveList", list);
		mv.addObject("pageBar2", pageBar2);
		

		mv.setViewName("seller/sellerReserveList");
		return mv;
		
		
		
		
		/*
		ModelAndView mv = new ModelAndView();
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// ���� ����
		List<ParkReserveDTO> list = service.sellerReserveList(userDTO.getUserId());
		mv.addObject("reserveList", list);
		// ���� ���� ��Ȳ
		List<ParkReserveDTO> listLoad = service.sellerReserveListLoad(userDTO.getUserId());
		mv.addObject("reserveListLoad", listLoad);
		return mv;*/
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
	
	@RequestMapping("/sellerStats")
	public void sellerStats() {
		
	}
	
	@ResponseBody
	@RequestMapping("/callStats")
	public List<ParkDTO> callStats(String startDate,String endDate) {
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return service.callStats(startDate,endDate,userDTO.getUserId());
	}
}