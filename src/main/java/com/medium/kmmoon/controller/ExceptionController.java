package com.medium.kmmoon.controller;

import com.medium.kmmoon.repository.ExceptionRepository;
import com.medium.kmmoon.vo.ExceptionResolverVO;
import com.medium.kmmoon.vo.TestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/test")
@RestController
public class ExceptionController {

    @Autowired
    private ExceptionRepository exceptionRepository;

    /**
     * 생성된 에러를 조회합니다.
     **/
    @GetMapping("/read/{no}")
    public ExceptionResolverVO readOne(@PathVariable("no") int no) {
        return exceptionRepository.findByNo(no);
    }

    /**
     * 생성된 에러를 모두 조회합니다.
     **/
    @GetMapping("/read/all")
    public List readAll() {
        return exceptionRepository.findAll();
    }

    /**
     * Get으로 접근 시 에러를 생성합니다.
     **/
    @GetMapping("/get/error")
    public Map create() {
        int a = 0;
        int b = 0;

        //java.lang.ArithmeticException: / by zero를 발생시킵니다.
        log.info("a+b*c={}",a/b);


        Map<String, Object> result = null;

        return result;
    }

    /**
     * Post로 접근 시 에러를 생성합니다.
     **/
    @PostMapping("/post/error")
    public Map postCreate(@RequestBody TestVO vo) {
        int a = 0;
        int b = 0;

        log.info("body : {}",vo);
        //java.lang.ArithmeticException: / by zero를 발생시킵니다.
        log.info("a+b*c={}",a/b);


        Map<String, Object> result = null;

        return result;
    }
}