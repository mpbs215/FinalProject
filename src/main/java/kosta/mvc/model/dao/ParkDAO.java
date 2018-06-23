package kosta.mvc.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.SearchFilterDTO;

@Repository
public class ParkDAO {
	@Autowired
	private SqlSession session;

	public int selectParkNo() {
		return session.selectOne("sellerMapper.selectParkNo");
	}
	
	public int insertPark(ParkDTO parkDTO) {
		return session.insert("sellerMapper.insertPark",parkDTO);
	}
	
	/**
	 * ����� ������ ����Ʈ
	 */	
	public List<ParkDTO> selectParkList(String userId){
		return session.selectList("sellerMapper.sellerParkList", userId);
	}
		
	/**
	 * ������ ����
	 */
	public int deletePark(int parkNo) {
		return session.delete("sellerMapper.deletePark", parkNo);
	}

	/**
	 * ����� �����忡 ���� ���� ����Ʈ - ����
	 */
	public List<ParkReserveDTO> sellerReserveList(String userId){
		return session.selectList("sellerMapper.selectReserveById", userId);
	}
	
	/**
	 * ����� �����忡 ���� ���� ����Ʈ - �̷�
	 */	
	public List<ParkReserveDTO> sellerReserveListLoad(String userId){
		return session.selectList("sellerMapper.selectReserveLoadById", userId);
	}

	/**
	 * ������ �˻� ���� ��� ����Ʈ
	 */
	public List<ParkDTO> renewParkList(SearchFilterDTO dto) {
		return session.selectList("searchMapper.selectPark", dto);
	}
	
	public ParkDTO selectOnePark(int parkNo) {
		return session.selectOne("userMapper.selectOnePark",parkNo);
	}
	
	
/*	public int sellerParkDelete(int parkNo) {
		return session.delete("sellerMapper.deletePark", parkNo);
	}



	public List<ParkDTO> renewParkList(SearchFilterDTO dto) {
		return session.selectList("searchMapper.selectPark", dto);
	}*/

}