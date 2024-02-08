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
        String lastPage = request.getSession().getAttribute("lastPage").toString();
        if (lastPage != null && !lastPage.isEmpty()) {
            clearAuthenticationAttributes(request);
            getRedirectStrategy().sendRedirect(request, response, lastPage);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
