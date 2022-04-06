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
     * 가수의 NickName 필드 추가 및 값 저장
     *
     * @param colNm 저장할 컬렉션 이름
     * @param singer 추가를 위해 검색할 가수이름
     * @param nickname 추가할 서브 가수 이름
     * @return 저장결과
     */
    int updateSongAddField(String colNm, String singer, String nickname) throws Exception;

    /**
     * 가수의 Member 필드 추가 및 BTS 멤버 이름 List로 저장하기
     *
     * @param colNm 저장할 컬렉션 이름
     * @param singer 추가를 위해 검색할 가수이름
     * @param member BTS 멤버 이름들을 추가
     * @return 저장 결과
     */
    int updateSongAddListField(String colNm, String singer, List<String> member) throws Exception;
}
