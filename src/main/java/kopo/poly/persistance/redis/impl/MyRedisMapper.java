package kopo.poly.persistance.redis.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.redis.IMyRedisMapper;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("MyRedisMapper")
public class MyRedisMapper implements IMyRedisMapper {

    public final RedisTemplate<String, Object> redisDB;

    public MyRedisMapper(RedisTemplate<String, Object> redisDB) {
        this.redisDB = redisDB;
    }

    @Override
    public int saveRedisString(String redisKey, RedisDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".saveRedisString Start!");

        int res = 0;

        String saveData = CmmUtil.nvl(pDTO.getTest_text());

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        if (!redisDB.hasKey(redisKey)) {

            redisDB.opsForValue().set(redisKey, saveData);

            redisDB.expire(redisKey, 2, TimeUnit.DAYS);

            log.info("Save Data!!");

            res = 1;
        }

        log.info(this.getClass().getName() + ".saveRedisString Start!");

        return res;
    }

    @Override
    public RedisDTO getRedisString(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".saveRedisString Start!");

        log.info("String redisKey : " + redisKey);
        RedisDTO rDTO = new RedisDTO();

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        if (redisDB.hasKey(redisKey)) {

            String res = (String) redisDB.opsForValue().get(redisKey);

            log.info("res : " + res);

            rDTO.setTest_text(res);
        }

        log.info(this.getClass().getName() + ".saveRedisString End!");

        return rDTO;
    }

    @Override
    public int saveRedisStringJSON(String redisKey, RedisDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".saveRedisStringJSON Start!");

        int res = 0;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        if (!redisDB.hasKey(redisKey)) {

            redisDB.opsForValue().set(redisKey, pDTO);

            redisDB.expire(redisKey, 2, TimeUnit.DAYS);

            log.info("Save Data!!");

            res = 1;
        }

        log.info(this.getClass().getName() + ".saveRedisStringJSON End!");

        return res;
    }

    @Override
    public int saveRedisList(String redisKey, List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveRedisList Start!");

        int res = 0;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        for (RedisDTO dto : pList) {

            // 오름차순 저장
            redisDB.opsForList().rightPush(redisKey, CmmUtil.nvl(dto.getTest_text()));

            // 내림차순 저장
            // redisDB.opsForList().leftPush(redisKey, CmmUtil.nvl(dto.getTest_text()));

        }

        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + ".saveRedisList End!");

        return res;

    }

    @Override
    public List<String> getRedisList(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getRedisList Start!");

        List<String> rList = null;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        if (redisDB.hasKey(redisKey)) {
            rList = (List) redisDB.opsForList().range(redisKey, 0, -1);
        }

        log.info(this.getClass().getName() + ".getRedisList End!");

        return rList;
    }

    @Override
    public int saveRedisListJSON(String redisKey, List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveRedisListJSON Start!");

        int res = 0;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        for (RedisDTO dto : pList) {

            // 오름차순 저장
            redisDB.opsForList().rightPush(redisKey, dto);

            // 내림차순 저장
            // redisDB.opsForList().leftPush(redisKey, dto);

        }

        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + ".saveRedisListJSON End!");

        return res;

    }

    @Override
    public List<RedisDTO> getRedisListJSON(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getRedisListJSON Start!");

        List<RedisDTO> rList = null;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        if (redisDB.hasKey(redisKey)) {
            rList = (List) redisDB.opsForList().range(redisKey, 0, -1);
        }

        log.info(this.getClass().getName() + ".getRedisListJSON End!");

        return rList;
    }

    @Override
    public int saveRedisListJSONRamda(String redisKey, List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveRedisListJSONRamda Start!");

        int res = 0;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        pList.forEach(dto -> redisDB.opsForList().rightPush(redisKey, dto));

        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + ".saveRedisListJSONRamda End!");

        return res;

    }

    @Override
    public int saveRedisHash(String redisKey, RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveRedisHash Start!");

        int res = 0;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        redisDB.opsForHash().put(redisKey, "name", CmmUtil.nvl(pDTO.getName()));
        redisDB.opsForHash().put(redisKey, "email", CmmUtil.nvl(pDTO.getEmail()));
        redisDB.opsForHash().put(redisKey, "addr", CmmUtil.nvl(pDTO.getAddr()));

        redisDB.expire(redisKey, 100, TimeUnit.MINUTES);

        res = 1;

        log.info(this.getClass().getName() + ".saveRedisHash End!");

        return res;

    }

    @Override
    public RedisDTO getRedisHash(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getRedisHash Start!");

        RedisDTO rDTO = new RedisDTO();

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        if (redisDB.hasKey(redisKey)) {
            String name = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "name"));
            String email = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "email"));
            String addr = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "addr"));

            log.info("name : " + name);
            log.info("email : " + email);
            log.info("addr : " + addr);

            rDTO.setName(name);
            rDTO.setEmail(email);
            rDTO.setAddr(addr);
        }

        log.info(this.getClass().getName() + ".getRedisHash End!");

        return rDTO;

    }

    @Override
    public int saveRedisSetJSONRamda(String redisKey, Set<RedisDTO> pSet) throws Exception {

        log.info(this.getClass().getName() + ".saveRedisSetJSONRamda Start!");

        int res = 0;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        pSet.forEach(dto -> redisDB.opsForSet().add(redisKey, dto));

        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + ".saveRedisSetJSONRamda End!");

        return res;

    }

    @Override
    public Set<RedisDTO> getRedisSetJSONRamda(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getRedisSetJSONRamda Start!");

        Set<RedisDTO> rSet = null;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        if (redisDB.hasKey(redisKey)) {
            rSet = (Set) redisDB.opsForSet().members(redisKey);
        }

        log.info(this.getClass().getName() + ".getRedisSetJSONRamda End!");

        return rSet;
    }

    @Override
    public int saveRedisZSetJSON(String redisKey, List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveRedisZSetJSON Start!");

        int res = 0;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        int idx = 0;

        for (RedisDTO dto : pList) {
            redisDB.opsForZSet().add(redisKey, dto, ++idx);
        }

        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + ".saveRedisZSetJSON End!");

        return res;

    }

    @Override
    public Set<RedisDTO> getRedisZSetJSON(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getRedisZSetJSON Start!");

        Set<RedisDTO> rSet = null;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        if (redisDB.hasKey(redisKey)) {
            long cnt = redisDB.opsForZSet().size(redisKey);
            rSet = (Set) redisDB.opsForZSet().range(redisKey, 0, cnt);
        }

        log.info(this.getClass().getName() + ".getRedisZSetJSON End!");

        return rSet;
    }

    @Override
    public boolean deleteDataJSON(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".deleteDataJSON Start!");

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        boolean res = false;

        if (redisDB.hasKey(redisKey)) {
            redisDB.delete(redisKey);

            res = true;
        }

        log.info(this.getClass().getName() + ".deleteDataJSON End!");

        return res;
    }

    @Override
    public boolean deleteDataString(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".deleteDataString Start!");

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        boolean res = false;

        if (redisDB.hasKey(redisKey)) {
            redisDB.delete(redisKey);

            res = true;
        }

        log.info(this.getClass().getName() + ".deleteDataString End!");

        return res;
    }
}
