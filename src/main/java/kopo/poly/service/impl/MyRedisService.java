package kopo.poly.service.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.redis.IMyRedisMapper;
import kopo.poly.service.IMyRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@Service("MyRedisService")
public class MyRedisService implements IMyRedisService {

    @Resource(name = "MyRedisMapper")
    private IMyRedisMapper myRedisMapper;

    @Override
    public int saveRedisString() throws Exception {

        log.info(this.getClass().getName() + ".saveRedisString Start!");

        String redisKey = "MyRedis_String";

        RedisDTO pDTO = new RedisDTO();
        pDTO.setTest_text("난 String타입으로 저장할 일반 문자열이다");

        int res = myRedisMapper.saveRedisString(redisKey, pDTO);

        log.info(this.getClass().getName() + ".saveRedisString End!");

        return res;
    }

    @Override
    public RedisDTO getRedisString() throws Exception {

        log.info(this.getClass().getName() + ".getRedisString Start!");

        String redisKey = "MyRedis_String";

        RedisDTO rDTO = myRedisMapper.getRedisString(redisKey);

        if (rDTO == null) {
            rDTO = new RedisDTO();
        }

        log.info(this.getClass().getName() + ".getRedisString End!");

        return rDTO;
    }

    @Override
    public int saveRedisStringJSON() throws Exception {

        log.info(this.getClass().getName() + ".saveRedisStringJSON Start!");

        String redisKey = "MyRedis_String_JSON";

        RedisDTO pDTO = new RedisDTO();
        pDTO.setTest_text("난 String타입에 JSON 구조로 저장할 일반 문자열이다");
        pDTO.setName("장석원");
        pDTO.setAddr("일산");
        pDTO.setEmail("maoru23@gmail.com");

        int res = myRedisMapper.saveRedisStringJSON(redisKey, pDTO);

        log.info(this.getClass().getName() + ".saveRedisStringJSON End!");

        return res;
    }

    @Override
    public int saveRedisList() throws Exception {

        log.info(this.getClass().getName() + ".saveRedisList Start!");

        String redisKey = "MyRedis_List";

        List<RedisDTO> pList = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            RedisDTO pDTO = new RedisDTO();
            pDTO.setTest_text(i + "번째 데이터 입니다.");

            pList.add(pDTO);
            pDTO = null;
        }

        int res = myRedisMapper.saveRedisList(redisKey, pList);

        log.info(this.getClass().getName() + ".saveRedisList End!");

        return res;
    }

    @Override
    public List<String> getRedisList() throws Exception {

        log.info(this.getClass().getName() + ".getRedisList Start!");

        String redisKey = "MyRedis_List";

        List<String> rList = myRedisMapper.getRedisList(redisKey);

        if (rList == null) {
            rList = new LinkedList<>();
        }

        log.info(this.getClass().getName() + ".getRedisList End!");

        return rList;
    }

    @Override
    public int saveRedisListJSON() throws Exception{
        log.info(this.getClass().getName() + ".saveRedisListJson start!");

        String redisKey = "MyRedis_List_JSON";

        List<RedisDTO> pList = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            RedisDTO pDTO = new RedisDTO();
            pDTO.setTest_text(i + "번째 데이터 입니다.");
            pDTO.setName("장석원[" + i + "]");
            pDTO.setAddr("서울");
            pDTO.setEmail("maoru23@gmail.com");

            pList.add(pDTO);
            pDTO = null;
        }

        int res = myRedisMapper.saveRedisListJSON(redisKey, pList);

        log.info(this.getClass().getName() + ".saveRedisListJson End!");

        return res;
    }

    @Override
    public List<RedisDTO> getRedisListJSON() throws Exception {

        log.info(this.getClass().getName() + ".getRedisListJSON Start!");

        String redisKey = "MyRedis_List_JSON";

        List<RedisDTO> rList = myRedisMapper.getRedisListJSON(redisKey);

        if (rList == null) {
            rList = new LinkedList<>();
        }

        log.info(this.getClass().getName() + ".getRedisListJSON End!");

        return rList;
    }

    @Override
    public int saveRedisListJSONRamda() throws Exception{
        log.info(this.getClass().getName() + ".saveRedisListJsonRamda start!");

        String redisKey = "myRedis_List_JSON_Ramda";

        List<RedisDTO> pList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            RedisDTO pDTO = new RedisDTO();
            pDTO.setTest_text(i + "번째 데이터 입니다.");
            pDTO.setName("장석원[" + i + "]");
            pDTO.setAddr("서울");
            pDTO.setEmail("maoru23@gmail.com");

            pList.add(pDTO);
            pDTO = null;
        }

        int res = myRedisMapper.saveRedisListJSONRamda(redisKey, pList);

        log.info(this.getClass().getName() + ".saveRedisListJsonRamda End!");

        return res;
    }

    @Override
    public List<RedisDTO> getRedisListJSONRamda() throws Exception {

        log.info(this.getClass().getName() + ".getRedisListJSONRamda Start!");

        String redisKey = "myRedis_List_JSON_Ramda";

        List<RedisDTO> rList = myRedisMapper.getRedisListJSON(redisKey);

        if (rList == null) {
            rList = new LinkedList<>();
        }

        log.info(this.getClass().getName() + ".getRedisListJSONRamda End!");

        return rList;
    }
}
