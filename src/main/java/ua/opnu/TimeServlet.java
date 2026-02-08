package ua.opnu;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(getServletContext());
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        this.engine = new TemplateEngine();
        this.engine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timezoneParam = req.getParameter("timezone");
        String lastTimezone;

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            lastTimezone = timezoneParam.replace(" ", "+");
            Cookie cookie = new Cookie("lastTimezone", lastTimezone);
            cookie.setMaxAge(86400);
            resp.addCookie(cookie);
        } else {
            Cookie[] cookies = req.getCookies();
            lastTimezone = (cookies != null) ? Arrays.stream(cookies)
                    .filter(c -> "lastTimezone".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("UTC") : "UTC";
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(lastTimezone));
        String formattedTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + lastTimezone;

        WebContext context = new WebContext(req, resp, getServletContext());
        context.setVariable("formattedTime", formattedTime);

        resp.setContentType("text/html; charset=utf-8");
        engine.process("time", context, resp.getWriter());
    }
}
