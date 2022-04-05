package kopo.poly.persistance.mongodb;

import kopo.poly.dto.MelonDTO;

import java.util.List;
import java.util.Map;

public interface IMelonMapper {

    int insertSong(List<MelonDTO> pList, String colNm) throws Exception;

    List<MelonDTO> getSongList(String colNm) throws Exception;

    List<MelonDTO> getSingerSongCnt(String colNm) throws Exception;

    List<MelonDTO> getSingerSong(String colNm, String singer) throws Exception;

    int insertSongMany(List<MelonDTO> pList, String colNm) throws Exception;

    int dropMelonCollection(String colNm) throws Exception;

    /**
     * 가수의 이름 수정하기
     * 방탄소년단을 BTS로 변경
     *
     * @param colNm 저장할 컬렉션 이름
     * @param singer 수정할 가수 이름
     * @param updateSinger 수정될 가수 이름
     * @return 저장 결과과     * @throws Exception
     */
    int updateSong(String colNm, String singer, String updateSinger) throws Exception;

    /**
     * singer 필드의 값인 방탄소년단을 BTS로 변경하기
     */
    int updateBTSName() throws Exception;
}
