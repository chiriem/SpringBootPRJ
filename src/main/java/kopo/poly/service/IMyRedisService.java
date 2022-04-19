package kopo.poly.service;

import kopo.poly.config.RedisConfiguration;
import kopo.poly.dto.RedisDTO;

import java.util.List;
import java.util.Set;

public interface IMyRedisService {

    int saveRedisString() throws Exception;

    RedisDTO getRedisString() throws Exception;

    int saveRedisStringJSON() throws Exception;
}

