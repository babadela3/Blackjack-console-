package server;


import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;

    public Deck(){
        deck = new ArrayList<Card>();
        for(int i=2;i<=14;i++)
        {
            deck.add(new Card(i,"Diamonds"));
            deck.add(new Card(i,"Clubs"));
            deck.add(new Card(i,"Hearts"));
            deck.add(new Card(i,"Spades"));
        }

    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public Card drawCard()
    {
        Card temp;
        temp = deck.get(deck.size()-1);
        deck.remove(deck.size()-1);
        return temp;
    }
}
