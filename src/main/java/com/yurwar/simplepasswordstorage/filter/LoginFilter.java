package com.yurwar.simplepasswordstorage.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class LoginFilter extends OncePerRequestFilter {

    private static final int LOWER_BOUND = 500;
    private static final int UPPER_BOUND = 1000;
    private static final Random RANDOM = new Random();
    private static final String LOGIN_URI = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (httpServletRequest.getRequestURI().contains(LOGIN_URI)) {
            Thread waitingTask = createWaitingTask();

            waitingTask.start();

            filterChain.doFilter(httpServletRequest, httpServletResponse);

            try {
                waitingTask.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

//    @Override
//    public void doFilter( servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        Thread waitingTask = createWaitingTask();
//
//        waitingTask.start();
//
//        filterChain.doFilter(servletRequest, servletResponse);
//
//        try {
//            waitingTask.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private Thread createWaitingTask() {
        return new Thread(() -> {
            try {
                Thread.sleep(RANDOM.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
