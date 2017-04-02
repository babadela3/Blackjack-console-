package server;


import java.util.ArrayList;

public class Player {
    private ArrayList<Card> cards;
    private int total;
    private final String name;

    Player(String name){
        this.name = name;
        total = 0;
        cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getTotal() {
        return total;
    }

    public String getName() {
        return name;
    }

    public void setTotal() {
        total = 0;
        int nrAces = 0;
        for(int i = 0; i < cards.size(); i++){

            if(cards.get(i).getValue() >= 12){
                total = total + 10;
            }
            else {
                if(cards.get(i).getValue() !=  11){
                    total = total + cards.get(i).getValue();
                }
                else {
                    nrAces++;
                }
            }

        }
        for(int i = 0; i < nrAces; i++){
            if(total <= 10){
                total = total + 11;
            }
            else {
                total = total + 1;
            }
        }
    }

    public void addCard(Card card)
    {
        cards.add(card);
        setTotal();
    }

    public String toString() {
        if (cards.size() != 0) {
            String result = String.format("The cards of %s are: \n", name);
            for (int i = 0; i < cards.size(); i++)
                result = result + cards.get(i) + "\n";
            result = result + String.format("with a total score of %d", total);
            return result;
        }
        else return String.format("%s has no cards. \n", name);
    }
}

