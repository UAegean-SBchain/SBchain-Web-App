package com.example.sbchainssioicdoauth2.config;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Controller
public class ExceptionHandlerController implements ErrorController{

    private static final String ERROR_MESSAGE_ACCESS_DENIED = "Access Denied";
    private static final String NOT_FOUND = "Not Found";
    private static final String ERROR_MESSAGE_DEFAULT = "An unexpected error occurred";
    private static final String ERROR_TITLE_DEFAULT = "error";
    private static final String ERROR_RESOURCE = "Resource or Claims Not Found";
    private static final String ERROR_PAGE = "errorPage";

    @ExceptionHandler(Exception.class)
    public ModelAndView doExceptionHandling(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String errorTitle = ERROR_TITLE_DEFAULT;
        String errorMessage = ERROR_MESSAGE_DEFAULT;

        log.info("The Exception Handler runs. Controlled application flow after catching exception '{}'.", exception.toString());
        log.error("Caught Exception in the Front Error Handler.", exception);

        if (exception instanceof HttpClientErrorException ) {

            HttpClientErrorException clientException = (HttpClientErrorException) exception;

            if (clientException.getStatusCode() == HttpStatus.NOT_FOUND) {
                errorMessage = NOT_FOUND;
            } else if (clientException.getStatusCode() == HttpStatus.FORBIDDEN) {
                return handleError(request, response, ERROR_TITLE_DEFAULT, ERROR_MESSAGE_ACCESS_DENIED);
            } else {
                return handleError(request, response, errorTitle, errorMessage);

            }
        } else if (exception instanceof AccessDeniedException) {
            return handleError(request, response, ERROR_TITLE_DEFAULT, ERROR_MESSAGE_ACCESS_DENIED);

        } else if(exception instanceof MyResourceNotFoundException){
            return handleError(request, response, ERROR_TITLE_DEFAULT, ERROR_RESOURCE);

        } else {
            return handleError(request, response, errorTitle, errorMessage);
        }
        return handleError(request, response, errorTitle, errorMessage);
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response, String errorTitle, String errorMessage) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView mav = new ModelAndView();
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.FORBIDDEN.value()) {
                mav.getModel().put("title", HttpStatus.FORBIDDEN.value());
                mav.getModel().put("message", ERROR_MESSAGE_ACCESS_DENIED);
            }
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                mav.getModel().put("title", HttpStatus.NOT_FOUND.value());
                mav.getModel().put("message", NOT_FOUND);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                mav.getModel().put("title", HttpStatus.INTERNAL_SERVER_ERROR.value());
                mav.getModel().put("message", ERROR_MESSAGE_DEFAULT);
            } else {
                mav.getModel().put("title", errorTitle);
                mav.getModel().put("message", errorMessage);
            }
        } else {
            mav.getModel().put("title", errorTitle);
            mav.getModel().put("message", errorMessage);
        }

        mav.setViewName(ERROR_PAGE);
        return mav;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}