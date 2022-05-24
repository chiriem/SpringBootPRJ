package kopo.poly.persistance.redis.impl;

import kopo.poly.dto.ChatDTO;
import kopo.poly.persistance.redis.IChatMapper;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("ChatMapper")
public class ChatMapper implements IChatMapper {

    public final RedisTemplate<String, Object> redisDB;

    public ChatMapper(RedisTemplate<String, Object> redisDB) {
        this.redisDB = redisDB;
    }

    @Override
    public Set<String> getRoomList() throws Exception {

        log.info(this.getClass().getName() + ".getRoomList Start!");

        Set<String> rSet = (Set) redisDB.keys("Chat*");

        log.info(this.getClass().getName() + ".getRoomList End!");

        return rSet;
    }

    @Override
    public int insertChat(ChatDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertChat Start!");

        int res = 0;

        String roomKey = CmmUtil.nvl(pDTO.getRoomKey());

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDTO.class));

        redisDB.opsForList().leftPush(roomKey, pDTO);

        res = 1;

        log.info(this.getClass().getName() + ".insertChat Start!");

        return res;
    }

    @Override
    public List<ChatDTO> getChat(ChatDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getChat Start!");

        List<ChatDTO> rList = null;

        String roomKey = CmmUtil.nvl(pDTO.getRoomKey());

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDTO.class));

        if (redisDB.hasKey(roomKey)) {
            rList = (List) redisDB.opsForList().range(roomKey, 0, -1);
        }

        log.info(this.getClass().getName() + ".getChat End!");

        return rList;
    }

    @Override
    public boolean setTimeOutHour(String roomKey, int hours) throws Exception {
        log.info(this.getClass().getName() + ".setTimeOutHour Start!");

        return redisDB.expire(roomKey, hours, TimeUnit.HOURS);
    }

    @Override
    public boolean setTimeOutMinute(String roomKey, int minutes) throws Exception{
        log.info(this.getClass().getName() + ".setTimeOutMinute Start!");

        return redisDB.expire(roomKey, minutes, TimeUnit.HOURS);

    }
}