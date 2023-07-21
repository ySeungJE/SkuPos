package springFinal.POS.domain.Pos.service;

import springFinal.POS.domain.Pos.MyPos;

public interface MyPosService {
    MyPos startPos();
    void updateTurnover(Integer summary);
    MyPos getPos();
    void addStockDay(String day);
}
