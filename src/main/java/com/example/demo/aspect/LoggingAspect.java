package com.example.demo.aspect;

import com.example.demo.model.Log;
import com.example.demo.repository.LogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
public class LoggingAspect {
    private final LogRepository logRepository;

    @Autowired
    public LoggingAspect(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Before("@annotation(Loggable)")
    public void logAction(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            String methodName = joinPoint.getSignature().getName();
            Log log = new Log();
            log.setLogDate(LocalDate.now());
            log.setActionTaken(methodName);
            log.setUserName(userName);

            logRepository.save(log);
        }
    }

}
