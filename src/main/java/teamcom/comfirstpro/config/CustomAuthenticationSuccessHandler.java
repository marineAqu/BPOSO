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
            //이전 페이지가 null인 경우
            getRedirectStrategy().sendRedirect(request, response, "showError");
        }
    }
}
