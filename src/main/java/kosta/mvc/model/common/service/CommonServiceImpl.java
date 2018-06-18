package kosta.mvc.model.common.service;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kosta.mvc.model.dao.FAQDAO;
import kosta.mvc.model.dao.NoticeDAO;
import kosta.mvc.model.dao.QNADAO;
import kosta.mvc.model.dao.TermsDAO;
import kosta.mvc.model.dto.FAQDTO;
import kosta.mvc.model.dto.NoticeDTO;
import kosta.mvc.model.dto.QNADTO;
import kosta.mvc.model.dto.TermsDTO;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private FAQDAO faqDAO;

	@Autowired
	private TermsDAO termsDAO;

	@Autowired
	private NoticeDAO noticeDAO;

	@Autowired
	private QNADAO qnaDAO;

	public List<FAQDTO> selectFAQAll() {
		List<FAQDTO> list = faqDAO.selectFAQAll();
		return list;
	}

	public List<TermsDTO> selectTerms() {
		return termsDAO.selectTerms();
	}

	public List<NoticeDTO> selectNotice() {
		return noticeDAO.selectNotice();
	}

	public NoticeDTO selectOneNotice(int noticeNo) {
		return noticeDAO.selectOneNotice(noticeNo);
	}

	@Override
	public List<QNADTO> selectQNAList() {
		return qnaDAO.selectQNAList();
	}

	@Transactional
	@Override
	public QNADTO selectOneQNA(HttpServletRequest request, HttpServletResponse response, int QNANo) {
		QNADTO qnaDTO = qnaDAO.selectOneQNA(request, response, QNANo);

		Cookie[] cookies = request.getCookies();
		String boardCookieVal = "";
		boolean hasRead = false;

		if (cookies != null) {
			for (Cookie c : cookies) {
				String name = c.getName();
				String value = c.getValue();

				if ("boardCookie".equals(name)) {
					boardCookieVal = value;
					if (boardCookieVal.contains("|" + qnaDTO.getQNANo() + "|")) {
						hasRead = true;
						break;
					}
				}

			}
		}

		// �Խñ� ���� ���� ������
		if (!hasRead) {
			// ��ȸ�� ����
			qnaDTO.setQNAHit(qnaDTO.getQNAHit() + 1);
			qnaDAO.increaserQNA(qnaDTO);

			// ��Ű����
			Cookie boardCookie = new Cookie("boardCookie", boardCookieVal + "|" + qnaDTO.getQNANo() + "|");
			// boardCookie.setPath("/mvc/board"); �������� ������ ���� ��η� ����.
			// boardCookie.setMaxAge(60*60*24); //�ۼ� ���ϸ�, �������� ��������.
			response.addCookie(boardCookie);
		}

		return qnaDTO;
	}

}