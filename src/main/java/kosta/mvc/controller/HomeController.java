package kosta.mvc.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String moveMain() {
		return "main";
	}
	
	/**
	 * �ּ� �˻�â ����
	 */
	@RequestMapping("/addrPopup")
	public void addrPopup() {}
	
	@RequestMapping("/real")
	public void real(HttpSession session) {
		System.out.println("Ȩ��Ʈ�ѷ�");
		System.out.println(session.getServletContext().getRealPath("/"));
	}
}
