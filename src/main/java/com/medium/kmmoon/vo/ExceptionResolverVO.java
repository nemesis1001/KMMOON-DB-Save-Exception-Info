package com.medium.kmmoon.vo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Exceiption_Resolver")
public class ExceptionResolverVO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GenericGenerator(name="note-uuid", strategy = "uuid")
    @Column(name = "no", nullable = false)
    private int no;

    @Column(name = "uri", nullable = false, length=350)
    private String uri;
    @Column(name = "header", nullable = false, length=2500)
    private String header;
    @Column(name = "body", length=1500)
    private String body;

    @Column(name = "message", nullable = false, length=12000)
    private String message;
    @Column(name = "regist_date", nullable = false)
    private LocalDateTime registDate;


}
