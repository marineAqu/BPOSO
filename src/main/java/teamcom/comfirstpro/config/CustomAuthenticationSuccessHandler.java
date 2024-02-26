package teamcom.comfirstpro.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (request.getSession().getAttribute("lastPage") != null) {
            String lastPage = request.getSession().getAttribute("lastPage").toString();
            clearAuthenticationAttributes(request);
            getRedirectStrategy().sendRedirect(request, response, lastPage);
        } else {
            //회원가입 후 로그인에 들어오는 경우와 같이 이전 페이지가 null이 되는 경우 main으로 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, "main");
        }
    }
}
