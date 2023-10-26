package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(value = "/time")

public class TimeZoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String[] timezones = req.getParameterValues("timezone");
        if (timezones == null) {
            chain.doFilter(req, res);
            return;
        }
        boolean isValidTimezone = isValidTimeZone(timezones[0]);

        if (isValidTimezone) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("Invalid timezone");
        }
    }

    public boolean isValidTimeZone(String timezone) {
        try {
            int offset = Integer.parseInt(timezone
                    .replace("UTC", "")
                    .replace("UT", "")
                    .replace("GMT", "")
                    .replace("+", "")
                    .trim());
            return offset <=18 && offset >= -18;
        } catch (Exception e) {
            return false;
        }
    }
}
