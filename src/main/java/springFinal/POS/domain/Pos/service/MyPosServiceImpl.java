package springFinal.POS.domain.Pos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springFinal.POS.domain.Pos.MyPos;
import springFinal.POS.domain.Pos.repository.MyPosRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPosServiceImpl implements MyPosService {
    private final MyPosRepository myPosRepository;
    @Override
    public MyPos startPos() {
        return myPosRepository.save(MyPos.builder().balance(200000).build());
    }
    @Override
    public void updateTurnover(Integer summary) {
        MyPos myPos = myPosRepository.findAll().get(0);

        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");

        String day = LocalDateTime.now().format(dayFormat);
        String month = LocalDateTime.now().format(monthFormat);

        String[] dateArray = day.split("-");

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(dateArray[0]), (Integer.parseInt(dateArray[1]) -1), Integer.parseInt(dateArray[2])-1);

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();

        cal.add(Calendar.DAY_OF_MONTH, - dayOfWeek);
        //해당 주차의 첫 일자
        String stDt = formatter.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 6);
        //해당 주차의 마지막일자
        String edDt = formatter.format(cal.getTime());

        String week = stDt + " ~ " + edDt;

        myPos.updateTurnover(summary, day, week, month);
    }

    @Override
    public MyPos getPos() {
        return myPosRepository.findAll().get(0);
    }
    @Override
    public void addStockDay(String day) {
        MyPos pos = getPos();
        if( ! (pos.stockDay.contains(day))) pos.addStockDay(day);
    }
}
