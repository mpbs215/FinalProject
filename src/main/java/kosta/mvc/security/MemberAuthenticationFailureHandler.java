package kosta.mvc.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * ��������ť������ �α��� ���н� ȣ��Ǵ� EventHandler��.
 * 
 * springBean������������ <security:form-login�±׿� authentication-failure-handler-ref
 * �߰��ϸ� �α��� ���н� onAuthenticationFailure �޼ҵ尡 �ڵ� ȣ��ȴ�.
 */
@Component // id="memberAuthenticationFailureHandler"
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException auth)
			throws IOException, ServletException {
		request.setAttribute("errorMessage", auth.getMessage());
		request.getRequestDispatcher("/WEB-INF/views/sign/sellerAuth.jsp").forward(request, response);
	}
}
