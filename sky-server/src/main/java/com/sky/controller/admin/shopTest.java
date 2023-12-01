package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop/status")
@Slf4j
public class shopTest {

    @GetMapping
    public Result logout() {
        return new Result(0,"1",1);
    }




}

