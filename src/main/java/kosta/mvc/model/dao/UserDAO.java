package kosta.mvc.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kosta.mvc.model.dto.TempKeyDTO;
import kosta.mvc.model.dto.UserDTO;

@Repository
public class UserDAO {
	@Autowired
	private SqlSession session;

//	public List<UserDTO> manageUserList() {
	//	return session.selectList("");
	//}
	/**
	 * ���� �� user ��� ����ϴ� �޼ҵ� (admin�� ���)
	 */
	public List<UserDTO> manageUserList() {
		return session.selectList("manageUserMapper.regiUserList");
	}
	
	/**
	 * ȸ�� ���� ���� (admin�� ���)
	 */
	public void deleteUser(String userID) {
		session.delete("manageUserMapper.manageUser",userID);
	}
	
	/**
	 *������� ������ �����ִ� �޼ҵ�
	 */
//	public UserDTO viewUserInfo(String userId) {
	//	return session.selectOne("userMapper.selectUserInfo",userId);
	//}
	
	/**
	 * ����� ������ �����ϴ� �޼ҵ�
	 */
	public int updateUserInfo(UserDTO userDTO) {
		return session.update("userMapper.updateUserInfo", userDTO);
	}
	
		/**
	 * ȸ�� ����
	 * */
	public int signUp(UserDTO userDTO) {
	
		int result = session.insert("signMapper.insertMember", userDTO);
		System.out.println("��� : " +result);
		return result;
	}
	
	/**
	 *	 id �ߺ� üũ �ϱ�
	 * */
	public UserDTO idcheck(String userId) {
		UserDTO userDTO = session.selectOne("signMapper.idCheck",userId);
		return userDTO; 				
	}
	

	/**
	 * 	id�� pk������ �Ͽ� ������ ã�� �޼ҵ�
	 * */
	public UserDTO selectMemberById(String userId) {
		return session.selectOne("userMapper.selectUserInfo", userId);
	}
	
	/**
	 *������� ������ �����ִ� �޼ҵ�
	 */
	public UserDTO viewUserInfo(String userId) {
		return session.selectOne("userMapper.selectUserInfo",userId);
	}
	
	/**
	 * ����� ������ �����ϴ� �޼ҵ�
	 *//*
	public void updateUserInfo(UserDTO userDTO) {
		session.update("userMapper.updateUserInfo", userDTO);
	}*/
	
	/**
	 * 	�α��� üũ �ϱ� (�α��� ����, �α��� ���� üũ)
	 * */
	public UserDTO loginCheck(UserDTO userDTO) {
		
		UserDTO dto = session.selectOne("userMapper.loginCheck", userDTO);
		
		System.out.println("dto��" +dto);
		
		return dto;
	}
	
	/**
	 * ������ �̿��Ͽ� ������� ������ ã���ִ� �޼ҵ�
	 * */
	public UserDTO findByEmail(String email) {
		 
		UserDTO dto = session.selectOne("signMapper.findByEmail",email);
		System.out.println("DAO���� dto�� ���� " +dto);
		
		return dto;
	}
	
	/**
	 * 	SMS�� ������ȣ �ޱ�
	 * */
	public int SMSAuth(TempKeyDTO sms) {
		
		int result = session.insert("userMapper.SMS",sms);
		System.out.println("��� : " + result);
		
		return result; 
	}
	
	/**
	 * 	ȸ�� Ż�� �ϱ� (SMS ���̺�)
	 * */
	/*public int deleteSMS(String password, String hp) {
		
		return session.delete("userMapper.deleteSMS", hp);
	}*/
	
	/**
	 * 	ȸ�� Ż�� ���� ��ȣȭ�� ��й�ȣ ��������
	 * */
	public String selectPassword(String userId) {
		String password = session.selectOne("userMapper.selectPassword", userId);
		
		System.out.println("DAO���� ��й�ȣ : " +password);
		
		return password;
	}
	
	/**
	 * 	ȸ�� Ż�� �ϱ� (USER ���̺�)
	 * */
	public int deleteUserInfo(String encodePassword) {
		
		return session.delete("userMapper.deleteUser",encodePassword);
	}
	
}