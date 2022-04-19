package kopo.poly.controller;

import kopo.poly.dto.RedisDTO;
import kopo.poly.service.IMyRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class RedisController {

    @Resource(name = "MyRedisService")
    private IMyRedisService myRedisService;

    /**
     * Redis 문자열 저장 실습
     */
    @GetMapping(value = "redis/saveRedisString")
    public String saveRedisString() throws Exception {

        log.info(this.getClass().getName() + ".saveRedisString Start!");

        // 수집 결과 출력
        String msg;

        int res = myRedisService.saveRedisString();

        if (res == 1) {
            msg = "success";

        } else {
            msg = "fail";
        }

        log.info(this.getClass().getName() + ".saveRedisString End!");

        return msg;
    }

    @GetMapping(value = "redis/getRedisString")
    public RedisDTO getRedisString() throws Exception {

        log.info(this.getClass().getName() + ".getRedisString Start!");

        RedisDTO rDTO = myRedisService.getRedisString();

        log.info(this.getClass().getName() + ".getRedisString End!");

        return rDTO;
    }

    @GetMapping(value = "redis/saveRedisStringJSON")
    public String saveRedisStringJSON() throws Exception {

        log.info(this.getClass().getName() + ".saveRedisStringJSON Start!");

        // 수집 결과 출력
        String msg;

        int res = myRedisService.saveRedisStringJSON();

        if (res == 1) {
            msg = "success";

        } else {
            msg = "fail";
        }

        log.info(this.getClass().getName() + ".saveRedisStringJSON End!");

        return msg;
    }
}