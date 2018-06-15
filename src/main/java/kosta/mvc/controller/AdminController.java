package kosta.mvc.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import kosta.mvc.model.admin.service.FaqService;
import kosta.mvc.model.admin.service.ManageUserService;
import kosta.mvc.model.admin.service.NoticeService;
import kosta.mvc.model.admin.service.QnaReviewService;
import kosta.mvc.model.admin.service.QnaService;
import kosta.mvc.model.admin.service.TermsService;
import kosta.mvc.model.dto.FAQDTO;
import kosta.mvc.model.dto.NoticeDTO;
import kosta.mvc.model.dto.TermsDTO;
import kosta.mvc.model.dto.UserDTO;

@RequestMapping("/admin")
@Controller
public class AdminController {

	String savePath="C:\\Users\\minso\\Desktop\\Edu\\SpringWorkSpace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\finalProject\\resources\\images\\notice";
	@Autowired
	private FaqService faqService;

	@Autowired
	private ManageUserService manageUserService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private QnaReviewService qnaReviewService;

	@Autowired
	private QnaService qnaService;

	@Autowired
	private TermsService termsService;

	@RequestMapping("/manageUser")
	public void manageUser() {
	}

	/**
	 * Ajax�� ��������Ʈ ȣ��
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/manageUserList")
	public List<UserDTO> manageUserList() {
		return manageUserService.manageUserList();
	}

	/**
	 * ���� �Ѹ� ���� request: ������ȣ
	 * 
	 * @return �������
	 */
	@ResponseBody
	@RequestMapping("/manageUserDelete")
	public int manageUserDelete(int userNo) {
		return 00;
	}

	/**
	 * ���� ������ ���� ����
	 * 
	 * @param userNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/manageUserMultiDelete")
	public int manageUserMultiDelete(int userNo[]) {
		return 0;
	}

	@ResponseBody
	@RequestMapping("/manageUserSearch")
	public List<UserDTO> manageUserSearch(String keyword) {
		return null;
	}

	/**
	 * @param userId
	 *            ���� ���̵�
	 * @return ȸ�� �Ѹ� DTO ����
	 */
	@RequestMapping("/manageUserDetail")
	public ModelAndView manageUserDetail(String userId) {
		// �̿볻������, �Ǹ��������� �߰�����
		return null;
	}

	/**
	 * updateForm���� �̵��Ͽ� �ش� Terms ����
	 */

	@RequestMapping("/updateTerms/{termsNo}")
	public ModelAndView termsUpdateForm(@PathVariable int termsNo) {
		ModelAndView mv = new ModelAndView();
		TermsDTO termsDTO = termsService.updateFormTerms(termsNo);
		mv.addObject("termsDTO", termsDTO);
		mv.setViewName("common/termsUpdateForm");
		return mv;
	}

	@RequestMapping("/updateTerms")
	public ModelAndView updateTerms(TermsDTO termsDTO) {
		ModelAndView mv = new ModelAndView();
		int result = termsService.updateTerms(termsDTO);
		mv.setViewName("redirect:/common/terms");
		return mv;
	}

	@RequestMapping("/insertTermsForm")
	public String insertTermsForm() {
		return "common/insertTermsForm";
	}

	@RequestMapping("/insertTerms")
	public ModelAndView insertTerms(TermsDTO termsDTO) {
		ModelAndView mv = new ModelAndView();
		int result = termsService.insertTerms(termsDTO);
		mv.setViewName("redirect:/common/terms");
		return mv;
	}

	@RequestMapping("/deleteTerms/{termsNo}")
	public ModelAndView deleteTerms(@PathVariable int termsNo) {
		ModelAndView mv = new ModelAndView();
		int result = termsService.deleteTerms(termsNo);
		mv.setViewName("redirect:/common/terms");
		return mv;
	}

	@RequestMapping("/insertFAQForm")
	public String insertFAQForm() {
		return "common/insertFAQForm";
	}

	@RequestMapping("/insertFAQ")
	public ModelAndView insertFAQ(FAQDTO faqDTO) {
		ModelAndView mv = new ModelAndView();
		int result = faqService.insertFAQ(faqDTO);
		mv.setViewName("redirect:/common/faq");
		return mv;
	}

	@RequestMapping("/updateFAQ/{FAQNo}")
	public ModelAndView updateFAQ(@PathVariable int FAQNo) {
		ModelAndView mv = new ModelAndView();
		FAQDTO faqDTO = faqService.updateFAQ(FAQNo);
		mv.addObject("faqDTO", faqDTO);
		mv.setViewName("common/faqUpdateForm");
		return mv;
	}

	@RequestMapping("/updateFAQ")
	public ModelAndView updateFAQ(FAQDTO faqDTO) {
		ModelAndView mv = new ModelAndView();
		int result = faqService.updateFAQ(faqDTO);
		mv.setViewName("redirect:/common/faq");
		return mv;
	}

	@RequestMapping("/deleteFAQ/{FAQNo}")
	public ModelAndView deleteFAQ(@PathVariable int FAQNo) {
		ModelAndView mv = new ModelAndView();
		int result = faqService.deleteFAQ(FAQNo);
		mv.setViewName("redirect:/common/faq");
		return mv;
	}

	@RequestMapping("/insertNoticeForm")
	public String insertNoticeForm() {
		return "common/insertNoticeForm";
	}

	@RequestMapping("/insertNotice")
	public String insert(HttpSession session, NoticeDTO noticeDTO) throws Exception {
		MultipartFile file = noticeDTO.getNoticeImage();
		// ����÷�ο���üũ(electronicsDTO.getFile()==null�� üũ�� �� ����. ��
		// electronicsDTO.getFile()�� null�� ������ �ʴ´�.)
		if (noticeDTO.getNoticeImage().getSize() > 0) {
			noticeDTO.setNoticeSub(file.getOriginalFilename());
			
			file.transferTo(new File(savePath + "/" + file.getOriginalFilename()));
		}
		// insert ȣ���ϱ�
		noticeService.insertNotice(noticeDTO);
		// redirect:list �̵�
		return "redirect:/common/notice";
	}
}