package server;



public class Card {

    private int value;
    private String suit;



    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;
    }



    public int getValue() {
        return value;
    }


    public String getSuit() {
        return suit;
    }


    public String toString()
    {
        String result = "";
        if(value == 11)
        {
            result = "Ace of " + suit;
        }
        if(value == 12)
        {
            result = "Jack of " + suit;
        }
        if(value == 13)
        {
            result = "Queen of " + suit;
        }
        if(value == 14)
        {
            result = "King of " + suit;
        }
        if(value <= 10)
        {
            result = value + " of " + suit;
        }
        return result;
    }
}
