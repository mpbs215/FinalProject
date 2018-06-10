package kosta.mvc.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kosta.mvc.model.dto.ParkReserveDTO;

@Repository
public class ParkReserveDAO {
	
	@Autowired
	private SqlSession session;

	public int sellerReserveDelete(int reserveNo) {
		return session.delete("sellerMapper.deleteReserve",reserveNo);
	}
	
	/**
	 * @param id
	 * @return ���������Ȳ
	 */
	public List<ParkReserveDTO> sellerReserveList(String id) {
		return session.selectList("sellerMapper.selectReserveById",id);
	}
	
	/**
	 * 
	 * @param userId �Ǹ��� ���̵�
	 * @return ���翹���Ȳ
	 */
	public List<ParkReserveDTO> sellerReserveListLoad(String userId) {
		return session.selectList("sellerMapper.selectReserveLoadById",userId);
	}

	public List<ParkReserveDTO> selectparkReserve(int parkNo) {
		return session.selectList("",parkNo);
	}
	
	
}