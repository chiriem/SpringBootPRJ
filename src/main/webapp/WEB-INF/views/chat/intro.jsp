<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    String room_name = CmmUtil.nvl((String) session.getAttribute("SS_ROOM_NAME"));
%>
<!DOCTYPE html>
<html>
<head>
    <script src="/js/annyang.js"></script>
    <script src="/js/jquery-3.6.0.min.js"></script>
    <script>
        $(window).on('load', function () {
            getRoomList();

            setInterval("getRoomList()", 5000);

        })

        //전체 채팅방 리스트 가져오기
        function getRoomList() {

            $.ajax({
                url: "/chat/roomList",
                type: "post",
                dataType: "JSON",
                contentType: "application/json; charset=UTF-8",
                success: function (json) {

                    let roomList = "";

                    for (let i = 0; i < json.length; i++) {
                        room_list += (json[i] + "<br/>");
                    }

                    $("#room_list").html(roomList);

                    getAllMsg();
                }
            })
        }

        function getAllMsg() {

            $.ajax({
                url: "/chat/getMsg",
                type: "post",
                dataType: "JSON",
                data: $("form").serialize(),
                success: function (json) {
                    $("#view_json").html("getAllMsg : " + json);

                    doOutputMsg(json);
                }
            })
        }

        function doOutputMsg() {

            if (json != null) {

                let totalMsg = "";

                for (let i = 0; i < json.length; i++){
                    totalMsg += (json[i].msg + " | " + json[i].userNm + " | " + json[i].dateTime + "<br/>>");
                }

                $("#total_msg").html(totalMsg);
            }
        }

        //annyang 라이브러리 실행
        annyang.start({
            autoRestart: true,
            continuous: true,
        })

        //음성인식 값 받아오기 위한 객체 생성
        let recognition = annyang.getSpeechRecognizer();

        //말하는 동안에 인식되는 값 가져오기(비허용)
        recognition.interimResults = false;

        //음성 인식결과 가져오기
        recognition.onresult = function (event) {

            //최종 음성인식 결과값 저장 변수
            let final_transcript = "";

            for (let i = event.resultIndex; i < event.results.length; ++i) {
                if (event.results[i].isFinal) {
                    final_transcript += event.results[i][0].transcript;
                }
            }

            $("#view_msg").html(final_transcript);
            $("#send_msg").val(final_transcript);

            $.ajax({
                url: "/chat/msg",
                type: "post",
                dataType: "JSON",
                data: $("form").serialize(),
                success: function (json) {
                    $("#view_json").html("msg : " + json);

                    doOutputMsg(json);
                }
            })
        };
    </script>
    <title></title>
</head>
<body>
<h1>
    [<%=room_name%>] 채팅방 '음성대화' 전체 내용
</h1>
<hr/>
<div id="total_msg"></div>

<h1>내가 방금 말한 전송 메시지</h1>
<hr/>
<div id="view_msg"></div>
<br/>
<h1>채팅방 전체 리스트</h1>
<hr/>
<div id="room_list"></div>

<form name="form" method="post">
    <input type="hidden" name="send_msg" id="send_msg"/>
</form>

</body>
</html>