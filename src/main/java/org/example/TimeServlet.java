package org.example;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(value = "/time")

public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("C:\\apache-tomcat-10.1.15\\webapps\\resources\\");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int offset = 0;
        String timezone = "";
        String[] timezones = req.getParameterValues("timezone");
        if (timezones != null) {
            timezone = timezones[0];
        } else {
            timezone = getCookies(req).get("lastTimezone");
            if (timezone == null) timezone = "UTC+0";
        }
        offset = Integer.parseInt(timezone
                .replace("UTC", "")
                .replace("UT", "")
                .replace("GMT", "")
                .replace("+", "")
                .trim());

        String sOffset = "";
        if (offset < 0) sOffset = String.valueOf(offset);
        else if (offset > 0) sOffset = "+" + offset;
        OffsetDateTime utcDateTime = OffsetDateTime.now(ZoneOffset.ofHours(offset));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formattedDate = utcDateTime.format(formatter) + " UTC" + sOffset;

        resp.setHeader("Set-Cookie", "lastTimezone=" + timezone.replace(" ", "+"));

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("time", formattedDate);
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("queryParams", params)
        );
        engine.process("time", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }

    private Map<String, String> getCookies(HttpServletRequest req) {
        String cookies = req.getHeader("Cookie");

        if (cookies == null) {
            return Collections.emptyMap();
        }

        Map<String, String> result = new HashMap<>();

        String[] separateCookies = cookies.split(";");
        for (String pair : separateCookies) {
            String[] keyValue = pair.split("=");

            result.put(keyValue[0], keyValue[1]);
        }

        return result;
    }
}
