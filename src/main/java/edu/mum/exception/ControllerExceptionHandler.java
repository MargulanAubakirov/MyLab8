package edu.mum.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UploadImageException.class)
    public ModelAndView handlerException(HttpServletRequest request, UploadImageException exception){
        ModelAndView result = new ModelAndView();
        result.addObject("exception", exception);
        String prms = request.getQueryString();
        result.addObject("url", request.getRequestURL() + (isNullOrEmpty(prms)?"":"?" + prms));
        result.setViewName("imageUploadError");
        return  result;
    }
    private boolean isNullOrEmpty(String str){
        return str == null || "".equalsIgnoreCase(str);
    }
}
