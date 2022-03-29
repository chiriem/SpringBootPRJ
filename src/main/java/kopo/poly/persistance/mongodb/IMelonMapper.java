package kopo.poly.persistance.mongodb;

import kopo.poly.dto.MelonDTO;

import java.util.List;
import java.util.Map;

public interface IMelonMapper {

    int insertSong(List<MelonDTO> pList, String colNm) throws Exception;

    List<MelonDTO> getSongList(String colNm) throws Exception;

    List<MelonDTO> getSingerSongCnt(String colNm) throws  Exception;
}
