package com.medium.kmmoon.repository;

import com.medium.kmmoon.vo.ExceptionResolverVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExceptionRepository extends JpaRepository<ExceptionResolverVO,Long> {
    ExceptionResolverVO findByNo(int no);
}
