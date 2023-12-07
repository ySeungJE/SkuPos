package springFinal.POS.domain.payment.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springFinal.POS.domain.Item.Item;
import springFinal.POS.domain.Item.repository.ItemRepository;
import springFinal.POS.domain.Pos.MyPos;
import springFinal.POS.domain.Pos.repository.MyPosRepository;
import springFinal.POS.domain.order.Order;
import springFinal.POS.domain.order.repository.OrderRepository;
import springFinal.POS.domain.payment.repository.PaymentRepository;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import static springFinal.POS.domain.payment.PaymentStatus.*;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class RefundService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MyPosRepository myPosRepository;
    public String refundRequest(String access_token, Long orderId, String reason) throws IOException {

        Order order = orderRepository.findById(orderId).orElse(null);

        URL url = new URL("https://api.iamport.kr/payments/cancel");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // 요청 방식을 POST로 설정
        conn.setRequestMethod("POST");

        // 요청의 Content-Type, Accept, Authorization 헤더 설정
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", access_token);

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // JSON 객체에 해당 API가 필요로하는 데이터 추가.
        JsonObject json = new JsonObject();
        json.addProperty("merchant_uid", order.getOrderUid());
        json.addProperty("reason", reason);
 
        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();
 
        // 입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        br.close();
        conn.disconnect();

        order.getPayment().updateStatus(CANCEL);
        String saleDay = order.getSaleDate().get("day");
        String saleWeek = order.getSaleDate().get("week");
        String saleMonth = order.getSaleDate().get("month");

        for (Map.Entry<String, Integer> entry : order.getItemData().entrySet()) {
            Item item = itemRepository.findByName(entry.getKey()).orElse(null);

            item.getDaySales().put(saleDay, item.getDaySales().get(saleDay) -  entry.getValue());
            item.getWeekSales().put(saleWeek, item.getWeekSales().get(saleWeek) -  entry.getValue());
            item.getMonthSales().put(saleMonth, item.getMonthSales().get(saleMonth) -  entry.getValue());
        }

        MyPos myPos = myPosRepository.findAll().get(0);

        myPos.updateTurnover(Integer.parseInt("-"+order.getPrice()),saleDay,saleWeek,saleMonth);

        return "결제 취소 완료 : 주문 번호" + order.getOrderUid();
    }

    public String getToken(String apiKey, String secretKey) throws IOException {
        URL url = new URL("https://api.iamport.kr/users/getToken");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
 
        // 요청 방식을 POST로 설정
        conn.setRequestMethod("POST");
 
        // 요청의 Content-Type과 Accept 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
 
        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);
 
        // JSON 객체에 해당 API가 필요로하는 데이터 추가.
        JsonObject json = new JsonObject();
        json.addProperty("imp_key", apiKey);
        json.addProperty("imp_secret", secretKey);
 
        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString()); // json 객체를 문자열 형태로 HTTP 요청 본문에 추가
        bw.flush(); // BufferedWriter 비우기
        bw.close(); // BufferedWriter 종료
 
        // 입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        Gson gson = new Gson(); // 응답 데이터를 자바 객체로 변환
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
        String accessToken = gson.fromJson(response, Map.class).get("access_token").toString();
        br.close(); // BufferedReader 종료
 
        conn.disconnect(); // 연결 종료
 
        log.info("Iamport 엑세스 토큰 발급 성공 : {}", accessToken);
        return accessToken;
    }
}