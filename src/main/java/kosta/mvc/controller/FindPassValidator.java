package kosta.mvc.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kosta.mvc.model.dto.UserDTO;

public class FindPassValidator implements Validator { //��й�ȣ ã�� ȭ�鿡�� �̸��ϰ� ���̵� ����� �Է� ����� �����ϴ� Ŭ����
	 
	
    private Pattern pattern;
    private Matcher matcher;
	
    String regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"; //�̸��� ���� ǥ����
    
    public FindPassValidator() {
    	pattern = pattern.compile(regexp);
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }
 
	@Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO)target;    
        System.out.println("validate" +"���� ����ǳ�" +target);
        System.out.println("userDTO�� �̸���" +userDTO.getEmail());
        //�̸����� �ùٸ� �������� �˻�
        matcher = pattern.matcher(userDTO.getEmail());
        
        if(userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            errors.reject("NoEmail");
        }
        else if(!matcher.matches())  { //����ڰ� �Է��� �̸����� ����ǥ���Ŀ� ��ġ �����ʴ´ٸ�
            errors.reject("NotEmailPattern");
        }
    }
}