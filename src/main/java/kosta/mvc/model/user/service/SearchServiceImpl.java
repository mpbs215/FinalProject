package kosta.mvc.model.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosta.mvc.model.dao.ParkDAO;
import kosta.mvc.model.dao.SidogugundongriDAO;
import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.SearchFilterDTO;

@Service
public class SearchServiceImpl {
	@Autowired
	private SidogugundongriDAO sidogugundongriDAO;
	
	@Autowired
	private ParkDAO parkDAO;
	
	public List<String> selectSido(){
		return sidogugundongriDAO.selectSido();
	}
	
	public List<String> selectGugun(String sido){
		return sidogugundongriDAO.selectGugun(sido);
	}
	
	public List<String> selectDong(String gugun){
		return sidogugundongriDAO.selectDong(gugun);
	}
	
	public List<String> selectRi(String dong){
		return sidogugundongriDAO.selectRi(dong);
	}

	public List<ParkDTO> renewParkList(SearchFilterDTO dto) {
		System.out.println("ajax �۵�");
		System.out.println("��ȿ�� üũ");
		if(dto.getParkAddr()!=null && dto.getParkAddr().trim().equals("")) {
			dto.setParkAddr(null);
		}
		if(dto.getCarType()!=null && dto.getCarType().trim().equals("")) {
			dto.setCarType(null);
		}
		if(dto.getReserveDate()!=null && dto.getReserveDate().trim().equals("")) {
			dto.setReserveDate(null);
		}
		if(dto.getParkContent()!=null && dto.getParkContent().trim().equals("")) {
			dto.setParkContent(null);
		}
		if(dto.getDestination()!=null && dto.getDestination().trim().equals("")) {
			dto.setDestination(null);
		}
		if(dto.getParkName()!=null &&dto.getParkName().trim().equals("")) {
			dto.setParkName(null);
		}
		System.out.println("��ȿ�� üũ�Ϸ�");
		System.out.println(dto);
		return parkDAO.renewParkList(dto);
	}

	public List<Object> renewParkPager(SearchFilterDTO dto,HttpServletRequest request) {
		int cPage=dto.getcPage();
		int numPerPage = 4; //�� �������� ǥ�õ� �Խñ� ��
		if(cPage==0) {cPage=1;}
		
		int totalPark = parkDAO.CountParkList(dto);
		int totalPage = (int) Math.ceil((double) totalPark / numPerPage);
		int pageBarSize = 5;
		
		// ���������� no
		int pageNo;
		pageNo = (int) (Math.ceil(((double) cPage / pageBarSize) - 1) * pageBarSize) + 1;
		String pageBar = "";
		// ���������� no
		int pageEnd = pageNo + pageBarSize - 1;
		
		pageBar += "<ul class='pagination justify-content-center pagination-sm'>";

		// [����]
		if (pageNo == 1) {
			// ������ư �ʿ����
			pageBar += "<li class='page-item disabled'>";
			pageBar += "<buttton class='page-link' tabindex='-1'>����</buttton>";
			pageBar += "</li>";
		} else {
			pageBar += "<li class='page-item'>";
			pageBar += "<buttton class='page-link' id='"+(pageNo - 1)
					+ "'><span>[����]</span></buttton>";
			pageBar += "</li>";
		}

		// [pageNo]
		while (pageNo <= pageEnd && pageNo <= totalPage) {
			if (pageNo == cPage) {
				pageBar += "<li class='page-item active'>";
				pageBar += "<buttton class='page-link'>" + pageNo + "</buttton>";
				pageBar += "</li>";
			} else {
				pageBar += "<li class='page-item'>";
				pageBar += "<buttton class='page-link' id='"+pageNo+"'> <span>" + pageNo + "</span></buttton>";
				pageBar += "</li>";
			}

			pageNo++;
		}
		// [����]

		if (pageNo > totalPage) {
			pageBar += "<li class='page-item disabled'>";
	         pageBar += "<buttton class='page-link' >����</buttton>";
	         pageBar += "</li>";
		} else {
			pageBar += "<li class='page-item'>";
			pageBar += "<buttton class='page-link' id='"+pageNo+"'"
					+ "'><span>[����]</span></buttton>";
			pageBar += "</li>";
		}

		pageBar += "</ul>";
		List<ParkDTO> parkList = parkDAO.renewParkList(cPage, numPerPage,dto);
		
		List<Object> list = new ArrayList<>();
		
		list.add(parkList);
		list.add(pageBar);
		
		return list;
	}
}