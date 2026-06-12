package com.example.miniproyecto3.model.interfaces;
import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.Deck;
import com.example.miniproyecto3.model.TablePile;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;

public interface IPlayable {
    void playCard(Card card, TablePile table) throws InvalidCardPlayException;
    void drawCard(Deck deck) throws EmptyDeckException;
}
