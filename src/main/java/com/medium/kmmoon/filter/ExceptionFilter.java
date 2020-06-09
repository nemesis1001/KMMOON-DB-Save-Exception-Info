package com.medium.kmmoon.filter;

import com.medium.kmmoon.repository.ExceptionRepository;
import com.medium.kmmoon.vo.ExceptionResolverVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("exceptionFilter")
// Servlet Filter 이므로 가장 처음 동작합니다.
public class ExceptionFilter implements Filter {

	@Autowired
	private ExceptionRepository exceptionRepository;
	
	protected FilterConfig filterConfig;

    /**
     * init 함수는 컨텍스트 로드시 호출됩니다.
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        log.info("init call");

        this.filterConfig = config;
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());

    }

    /**
     * 필터 실행 부분 입니다.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    	
    	String encoding = "UTF-8";
    	
        // Controller 처리 전에 처리를 수행 하는곳 입니다.
        // 파라미터 값으로 POST 데이터의 인코딩을 지정합니다.
    	if(request != null) request.setCharacterEncoding(encoding);

        // 요청과 응답에 필요한 처리를 수행할 Wrapper를 생성합니다.

        // 다음 필터의 호출, 실제 자원의 처리를 합니다.
        ContentCachingRequestWrapper requestChainWrapper = new ContentCachingRequestWrapper((HttpServletRequest)request);
		ContentCachingResponseWrapper responsChainWrapper = new ContentCachingResponseWrapper((HttpServletResponse)response);

		// 숫자를 찍어둔건 어떤 순서로 컨트롤러에 도달하는지 사용자에게 알려주기 위해 찍어둔 로그입니다.
        // ExceptionFilter.init ->
        // ExceptionFilter.doFilter ->
        // ExceptionFilter.doFilter log.info("1") ->
        // chain.doFilter(requestChainWrapper,response); ->
        // Controller ->
        // CustomExceptionResolver ->
        // ExceptionFilter.doFilter log.info("2") ~ doFilter 종료 까지

		log.info("1");
		// request는 휘발성 데이터입니다.
		// request를 FilterChain에 저장하는 이유는 Controller에서 request를 호출 시
        // 데이터가 삭제되어 다시 Filter에서 호출해도 데이터는 남아있지 않습니다.
        // 고로 FilterChain에 따로 저장해둔 request 데이터는 Controller에서 이미 호출해도 남아있을 수 있습니다.
        chain.doFilter(requestChainWrapper,response);
        log.info("2");

        // request body 데이터를 가져옵니다.
        String body = new String(requestChainWrapper.getContentAsByteArray());

		log.info("3 : {}", body);

		// CustomExceptionResolver에서 세팅한 EXCEPTION 데이터를 가져와 실제 에러가 발생했는지 확인합니다.
        // 에러가 발생한 경우 데이터를 DB에 저장합니다.
		ExceptionResolverVO vo = (ExceptionResolverVO) request.getAttribute("EXCEPTION");

		if(vo instanceof ExceptionResolverVO) {

			vo.setBody((body.equals("") || body == null)? null : body);

			try {
                exceptionRepository.save(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info("exception filter");
		}
		
		
    }

    /**
     * destroy 는 컨텍스트가 종료될 때 호출됩니다.
     */
    @Override
    public void destroy() {
    	log.info("destroy call");
    }

   
    

}




