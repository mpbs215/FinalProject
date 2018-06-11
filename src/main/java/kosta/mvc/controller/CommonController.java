package kosta.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.common.service.CommonServiceImpl;
import kosta.mvc.model.dto.FAQDTO;
import kosta.mvc.model.dto.NoticeDTO;
import kosta.mvc.model.dto.TermsDTO;
import kosta.mvc.model.dto.UserDTO;

@RequestMapping("/common")
@Controller
public class CommonController {
	
	@Autowired
	private CommonServiceImpl service;
	
	/**
	 * @return FAQ ���̺��� ����Ʈ
	 */
	@RequestMapping("/faq")
	public ModelAndView faq() {
		ModelAndView mv = new ModelAndView();
		List<FAQDTO> list=service.selectFAQ();
		mv.addObject("list",list);
		mv.setViewName("Common/faq");
		return mv;
	}
	
	
	/**
	 * ���񽺾ȳ� ������������ �̵�
	 */
	@RequestMapping("/introduce")
	public void introduce() {}
	
	/**
	 * �����̿�ȳ� �������� �̵�
	 */
	@RequestMapping("/serviceInfo")
	public void serviceInfo() {}
	
	/**
	 * �̿��� ������ �̵�
	 */
	@RequestMapping("/terms")
	public ModelAndView terms() {
		ModelAndView mv = new ModelAndView();
		List<TermsDTO> list = service.selectTerms();
		mv.addObject("list",list);
		mv.setViewName("/Common/terms");
		return mv;
	}
	
	/**
	 * �������� �������� �̵�
	 * @return �������� List
	 */
	@RequestMapping("/notice")
	public ModelAndView notice() {
		ModelAndView mv = new ModelAndView();
		List<NoticeDTO> list = service.selectNotice();
		mv.addObject("list",list);
		mv.setViewName("/Common/notice");
		return mv;
	}
	
	/**
	 * �������� �������� �Խù� Ŭ���� �̵�
	 * @param noticeNo
	 * @return noticeDTO
	 */
	@RequestMapping("/noticeDetail")
	public ModelAndView noticeDetail(int noticeNo) {
		ModelAndView mv = new ModelAndView();
		NoticeDTO noticeDTO = service.selectOneNotice(noticeNo);
		mv.setViewName("/Common/noticeDetail");
		mv.addObject("dto",noticeDTO);
		return mv;
	}
	
	
}