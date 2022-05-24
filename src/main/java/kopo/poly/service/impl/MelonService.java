package kopo.poly.service.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.MelonDTO;
import kopo.poly.persistance.mongodb.IMelonMapper;
import kopo.poly.persistance.redis.IMelonCacheMapper;
import kopo.poly.service.IMelonService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.cert.CollectionCertStoreParameters;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

@Slf4j
@Service("MelonService")
public class MelonService implements IMelonService {

    @Resource(name = "MelonMapper")
    private IMelonMapper melonMapper; //MongoDB에 저장할 Mapper

    @Resource(name = "MelonCacheMapper")
    private IMelonCacheMapper melonCacheMapper; //redisDB에 저장할 Mapper

    @Override
    public int collectMelonSong() throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".collectMelonRank Start!");

        int res = 0;

        List<MelonDTO> pList = new LinkedList<>();

        // 멜론 Top100 중 50위까지 정보 가져오는 페이지
        String url = "https://www.melon.com/chart/index.htm";

        // JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML 소스 저장할 변수
        Document doc = Jsoup.connect(url).get();

        // <div class="service_list_song"> 이 태그 내에서 있는 HTML 소스만 element 에 저장됨
        Elements element = doc.select("div.service_list_song");

        // 멜론 100위까지 차트
        for (Element songInfo : element.select("div.wrap_song_info")) {

            // 크롤링을 통해 데이터 저장하기
            String song = CmmUtil.nvl(songInfo.select("div.ellipsis.rank01 a").text()); // 노래
            String singer = CmmUtil.nvl(songInfo.select("div.ellipsis.rank02 a").eq(0).text()); // 가수

            log.info("song : " + song);
            log.info("singer : " + singer);

            // 가수와 노래 정보가 모두 수집되었다면, 저장함
            if ((song.length() > 0) && (singer.length() > 0)) {

                MelonDTO pDTO = new MelonDTO();
                pDTO.setCollectTime(DateUtil.getDateTime("yyyyMMddhhmmss"));
                pDTO.setSong(song);
                pDTO.setSinger(singer);

                // 한번에 여러개의 데이터를 MongoDB에 저장할 List 형태의 데이터 저장하기
                pList.add(pDTO);

            }
        }
        // 생성할 컬렉션명
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // MongoDB에 데이터저장하기
        res = melonMapper.insertSong(pList, colNm);

        // RedisDB에 데이터저장하기
        res = melonCacheMapper.insertSong(pList, colNm);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".collectMelonSong End!");

        return res;
    }

    @Override
    public List<MelonDTO> getSongList() throws Exception {

        log.info(this.getClass().getName() + ".getSongList Start!");

        // MongoDB에 저장된 컬렉션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = null;


        if (melonCacheMapper.getExistKey(colNm)) {
            rList = melonCacheMapper.getSongList(colNm);
        } else {
            rList = melonMapper.getSongList(colNm);
        }

        if (rList == null) {
            rList = new LinkedList<>();
        }

        log.info(this.getClass().getName() + ".getSongList End!");

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSongCnt() throws Exception {

        log.info(this.getClass().getName() + ".getSingerSongList Start!");

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = melonMapper.getSingerSongCnt(colNm);

        if (rList == null) {
            rList = new LinkedList<>();
        }

        log.info(this.getClass().getName() + ".getSingerSongList End!");

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSong() throws Exception{
        log.info(this.getClass().getName() + ".getSingerSong Start!");

        // MongoDB에 저장된 컬렉션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 수집된 데이터로부터 검색할 가수명
       String singer = "방탄소년단";

        // 결과값
        List<MelonDTO> rList = null;

        // Melon 노래 수집하기
        if (this.collectMelonSong()==1) {

            rList = melonMapper.getSingerSong(colNm, singer);

            if (rList == null) {
                rList = new LinkedList<>();
            }
        }else{
            rList = new LinkedList<>();
        }

        log.info(this.getClass().getName() + ".getSingerSong Start!");

        return rList;
    }

    @Override
    public int collectMelonSongMany() throws Exception {

        log.info(this.getClass().getName() + ".collectMelonSongMany Start!");

        int res = 0;

        List<MelonDTO> pList = new LinkedList<>();

        String url = "https://www.melon.com/chart/index.htm";

        // JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML 소스 저장할 변수
        Document doc = Jsoup.connect(url).get();

        // <div class="service_list_cong"> 이 태그 내에서 있는 HTML 소스만 element에 저장됨
        Elements element = doc.select("div.service_list_song");

        // Iterator을 사용하여 멜론차트 정보를 가져오기
        // 멜론 100위까지 체크
        for (Element songInfo : element.select("div.wrap_song_info")) {

            String song = CmmUtil.nvl(songInfo.select("div.ellipsis.rank01 a").text());
            String singer = CmmUtil.nvl(songInfo.select("div.ellipsis.rank02 a").text());

            log.info("song : " + song);
            log.info("singer : " + singer);

            if ((song.length() > 0) && (singer.length() > 0)) {

                MelonDTO pDTO = new MelonDTO();
                pDTO.setCollectTime(DateUtil.getDateTime("yyyyMMddhhmmss"));
                pDTO.setSong(song);
                pDTO.setSinger(singer);

                // 한번에 여러개의 데이터를 MongoDB에 저장할 List 형태의 데이터 저장하기
                pList.add(pDTO);
            }
        }

        // 생성할 컬렉션명
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // MongoDB에 데이터저장하기
        res = melonMapper.insertSongMany(pList, colNm);

        res = melonCacheMapper.insertSong(pList, colNm);

        log.info(this.getClass().getName() + ".collectMelonSongMany End!");

        return res;
    }

    @Override
    public int updateBTSName() throws Exception {

        log.info(this.getClass().getName() + ".updateBTSName Start!");

        int res = 0;

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론 Top100 수집한 컬렉션 삭제
        melonMapper.dropMelonCollection(colNm);

        if (this.collectMelonSong() == 1) {

            // 수집된 데이터로부터 변경을 위해 찾을 가수명
            String singer = "방탄소년단";

            // 수집된 데이터로부터 변경할 가수명
            String updateSinger = "BTS";

            res = melonMapper.updateSong(colNm, singer, updateSinger);
        }

        log.info(this.getClass().getName() + ".updateBTSName End!");

        return res;
    }

    @Override
    public int updateAddBTSNickname() throws Exception {

        log.info(this.getClass().getName() + ".updateAddBTSNickname Start!");

        int res = 0;

        // 수집할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론 Top100 수집한 컬렉션 삭제
        melonMapper.dropMelonCollection(colNm);

        if (this.collectMelonSong() == 1) {

            // 수집된 데이터로부터 변경을 위해 찾을 가수명
            String singer = "방탄소년단";

            // 수집된 데이터로부터 변경할 가수명
            String nickname = "BTS";

            res = melonMapper.updateSongAddField(colNm, singer, nickname);
        }

        log.info(this.getClass().getName() + ".updateAddBTSNickname End!");

        return res;
    }

    @Override
    public int updateAddBTSMember() throws Exception {

        log.info(this.getClass().getName() + ".updateAddBTSMember Start!");

        int res = 0;

        // 수집할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론 Top100 수집한 컬렉션 삭제
        melonMapper.dropMelonCollection(colNm);

        if (this.collectMelonSong() == 1) {

            // 수집된 데이터로부터 변경을 위해 찾을 가수명
            String singer = "방탄소년단";

            // 수집된 데이터로부터 변경할 가수명
            String[] member = {"정국","뷔","지민","슈가","진","제이홉","RM"};

            // MongoDB에 데이터 저장하기
            // Arrays.asList(member) => List<String> 타입으로 member 변경하기
            res = melonMapper.updateSongAddListField(colNm, singer, Arrays.asList(member));
        }

        log.info(this.getClass().getName() + ".updateAddBTSNickname End!");

        return res;
    }

    @Override
    public int updateManySong() throws Exception{
        // 로그 찍기
        log.info(this.getClass().getName() + ".updateManySong Start!");

        int res = 0;

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론 top100 컬렉션 삭제
        melonMapper.dropMelonCollection(colNm);

        // 멜론top100 수집하기
        if (this.collectMelonSong() == 1) {
            String singer = "방탄소년단";
            String updateSinger = "BTS";
            String updateSong = "BTS-SONG";

            res = melonMapper.updateManySong(colNm, singer, updateSinger, updateSong);

        }

        log.info(this.getClass().getName() + ".updateManySong End!");

        return res;
    }

    @Override
    public int deleteSong() throws Exception{
        // 로그 찍기
        log.info(this.getClass().getName() + ".deleteSong Start!");

        int res = 0;

        // 삭제할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");


        // 수집된 데이터로부터 삭제할 가수명
        String singer = "BTS";

        // MongoDB에 데이터저장하기
        res = melonMapper.deleteSong(colNm, singer);

        log.info(this.getClass().getName() + ".deleteSong End!");

        return res;
    }
}
