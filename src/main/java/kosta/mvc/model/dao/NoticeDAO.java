package kosta.mvc.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kosta.mvc.model.dto.NoticeDTO;

@Repository
public class NoticeDAO {
	@Autowired
	private SqlSession session;

	public List<NoticeDTO> selectNotice() {
		return session.selectList("commonMapper.selectNotice");
	}

	public NoticeDTO selectOneNotice(int noticeNo) {
		return session.selectOne("commonMapper.selectNotice",noticeNo);
	}
	
	
}