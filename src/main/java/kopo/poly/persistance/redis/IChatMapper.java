package kopo.poly.persistance.redis;

import kopo.poly.dto.ChatDTO;
import kopo.poly.dto.MelonDTO;

import java.util.List;
import java.util.Set;

public interface IChatMapper {

    String roomInfoKey = "myRoomKey";

    Set<String> getRoomList() throws Exception;

    int insertChat(ChatDTO pDTO) throws Exception;

    List<ChatDTO> getChat(ChatDTO pDTO) throws Exception;

    boolean setTimeOutHour(String roomKey, int hours) throws Exception;

    boolean setTimeOutMinute(String roomKey, int minutes) throws Exception;
}
