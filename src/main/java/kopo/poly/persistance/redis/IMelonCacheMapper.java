package kopo.poly.persistance.redis;

import kopo.poly.dto.MelonDTO;

import java.util.List;

public interface IMelonCacheMapper {

    int insertSong(List<MelonDTO> pList, String rediskey) throws Exception;

    boolean getExistKey(String rediskey) throws Exception;

    List<MelonDTO> getSongList(String rediskey) throws Exception;
}
