package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableCaching
@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class SkyApplication {
    public static void main(String[] args) {

        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }
}
/**
 *                          _ooOoo_
 *                         o8888888o
 *                         88" . "88
 *                         (| ^_^ |)
 *                         O\  =  /O
 *                      ____/`---'\____
 *                    .'  \\|     |//  `.
 *                   /  \\|||  :  |||//  \
 *                  /  _||||| -:- |||||-  \
 *                 |   | \\\  -  /// |    |
 *                 | \_|  ''\---/''  |    |
 *                 \  .-\__  `-`  ___/-. /
 *               ___`. .'  /--.--\  `. . ___
 *            ."" '<  `.___\_<|>_/___.'  >'"".
 *           | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *           \  \ `-.   \_ __\ /__ _/   .-` /  /
 *     ========`-.____`-.___\_____/___.-`____.-'========
 *                          `=---='
 *     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *            佛祖保佑       永无BUG     永不修改
 */