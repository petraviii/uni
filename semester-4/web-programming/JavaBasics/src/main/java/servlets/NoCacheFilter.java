package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter that disables browser caching for all protected,
 * dynamically-generated pages (Student/Professor/Secretary actions).
 *
 * This ensures that after a user logs out, pressing the browser's
 * Back button cannot reveal previously-viewed authenticated content
 * from cache — satisfying the assignment's requirement (step 2.4)
 * that logout clears the application's cache, not just the session.
 *
 * @author Petros Travlos P21173
 */
@WebFilter(urlPatterns = {"/StudentServlet", "/ProfessorServlet", "/SecretaryServlet"})
public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        // Pass the request along to whichever servlet was actually targeted
        chain.doFilter(request, response);
    }
}