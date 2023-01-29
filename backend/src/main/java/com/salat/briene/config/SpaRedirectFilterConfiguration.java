//package com.salat.briene.config;
//
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.regex.Pattern;
//
//@Log4j2
//@Component
//@RequiredArgsConstructor
//public class SpaRedirectFilterConfiguration extends OncePerRequestFilter {
//    private final String REGEX = "(?!/actuator|/api|/_nuxt|/static|/index\\.html|/200\\.html|/favicon\\.ico|/robots\\.txt|sw\\.js).*$";
//    private final Pattern pattern = Pattern.compile(REGEX);
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest req,
//                                    @NonNull HttpServletResponse res,
//                                    @NonNull FilterChain chain) throws ServletException, IOException {
//        if (pattern.matcher(req.getRequestURI()).matches() && !req.getRequestURI().equals("/")) {
//            // Delegate/Forward to `/` if `pattern` matches, and it is not `/`
//            // Required because of 'mode: history's usage in frontend routing
//            RequestDispatcher rd = req.getRequestDispatcher("/");
//            rd.forward(req, res);
//        } else {
//            chain.doFilter(req, res);
//        }
//    }
//}
