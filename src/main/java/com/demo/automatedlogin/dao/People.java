package com.demo.automatedlogin.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Builder
public class People {

    @Nullable
    private Integer id;
    private String name;
    private int age;

}
