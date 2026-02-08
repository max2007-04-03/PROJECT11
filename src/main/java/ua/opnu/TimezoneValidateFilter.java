package ua.opnu;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String timezone = req.getParameter("timezone");
        if (timezone != null && !timezone.isEmpty()) {
            try {
                ZoneId.of(timezone.replace(" ", "+"));
            } catch (Exception e) {
                res.setStatus(400);
                res.getWriter().write("Invalid timezone");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}