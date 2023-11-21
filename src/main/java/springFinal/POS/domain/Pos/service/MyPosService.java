package springFinal.POS.domain.Pos.service;

import springFinal.POS.domain.Pos.MyPos;

public interface MyPosService {
    MyPos startPos();
    MyPos getPos();
    void updateTurnover(Integer summary);
    void addStockDay(String day);
}
