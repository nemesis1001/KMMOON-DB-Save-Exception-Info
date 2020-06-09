package com.medium.kmmoon.filter;

import com.medium.kmmoon.vo.ExceptionResolverVO;
import com.medium.kmmoon.vo.Globals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object handler,
                                              Exception ex){
		log.info("CustomExceptionResolver");
        try {
        	Enumeration<String> headerArr = request.getHeaderNames();
    		String header = "";
    		while (headerArr.hasMoreElements()) {
    			String headerName = headerArr.nextElement();
    			String value = request.getHeader(headerName);
    			header += headerName + " : " + value + " \n";
    		}
    		log.info("header : {}", header);
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));

			LocalDateTime date = LocalDateTime.now();


			ExceptionResolverVO vo = new ExceptionResolverVO();
			vo.setUri(request.getRequestURI());
			vo.setHeader(header);
			vo.setMessage(errors.toString());
			vo.setRegistDate(date);

			request.setAttribute("EXCEPTION", vo);

		} catch (Exception e) {
			log.error("send back error status and message : " + ex.getMessage(), e);
		}

		response.reset();

        // 에러 발생 시 사용자에게 보여줄 JSON Body를 생성합니다.
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		response.setContentType("text/json");

		MappingJackson2JsonView view = new MappingJackson2JsonView();
        Map<String, Object> map = new HashMap<String, Object>();
		if(ex instanceof DuplicateKeyException){
	     	map.put(Globals.RESULT_LABEL,Globals.RESULT_FAIL_CODE_02);
	     	map.put(Globals.RESULT_LABEL_MSG,Globals.RESULT_FAIL_MESSAGE_02);
	    }else if(ex instanceof SQLException){
	     	map.put(Globals.RESULT_LABEL,Globals.RESULT_EXCEPTION_CODE);
	     	map.put(Globals.RESULT_LABEL_MSG,Globals.RESULT_EXCEPTION_MSG);
	    }else if(ex instanceof NullPointerException){
	     	map.put(Globals.RESULT_LABEL,Globals.RESULT_FAIL_CODE_03);
	     	map.put(Globals.RESULT_LABEL_MSG,Globals.RESULT_FAIL_MESSAGE_03);
	    } else {
	     	map.put(Globals.RESULT_LABEL,Globals.RESULT_EXCEPTION_CODE);
	     	map.put(Globals.RESULT_LABEL_MSG,Globals.RESULT_EXCEPTION_MSG);
	    }

		view.setAttributesMap(map);
        return new ModelAndView(view);


    }

    
}