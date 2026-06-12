package com.example.miniproyecto3.model.interfaces;
import com.example.miniproyecto3.model.Card;
import java.util.List;
public interface IEliminable {
    boolean isEliminated();
    void eliminate();
    List<Card> getHand();


}

