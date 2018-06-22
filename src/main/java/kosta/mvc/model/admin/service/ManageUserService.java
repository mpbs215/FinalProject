package kosta.mvc.model.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosta.mvc.model.dao.UserDAO;
import kosta.mvc.model.dto.UserDTO;

@Service
public class ManageUserService {

	@Autowired
	private UserDAO userDAO;
	
	/**
	 * ȸ�� ��� ǥ��
	 * @return
	 */
	public List<UserDTO> manageUserList() {
		
		return userDAO.manageUserList();
	}
	
	/**
	 * ȸ�� ����
	 */
	
	public void deleteUser(String userId) {
		userDAO.deleteUser(userId);
	}
}
