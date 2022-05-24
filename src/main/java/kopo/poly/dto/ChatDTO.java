package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

// 숫자가 있으면 NON_DEFAULT, 없으면 NON_NULL(모든 변수가 String일 때 사용)을 쓰면 된다
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class ChatDTO {

    private String roomKey = "";
    private String userNm = "";
    private String msg = "";
    private String dateTime = "";
}
