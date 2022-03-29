package kopo.poly.persistance.mongodb;

import kopo.poly.dto.MelonDTO;

import java.util.List;
import java.util.Map;

public interface IMelonMapper {

    int insertSong(List<MelonDTO> pList, String colNm) throws Exception;

    List<MelonDTO> getSongList(String colNm) throws Exception;

    List<MelonDTO> getSingerSongCnt(String colNm) throws  Exception;

    List<MelonDTO> getSingerSong(String colNm, String singer) throws  Exception;

    int insertSongMany(List<MelonDTO> pList, String colNm) throws  Exception;

}
