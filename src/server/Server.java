package server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Player dealer;
    private Player player;
    private int playerTurn;
    private int dealerTurn;
    private Deck deck;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final int portNumber = 9999;

    public Server(){
        dealer = new Player("Dealer");
        deck = new Deck();
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerTurn = 0;
        dealerTurn = 0;
    }

    public void start(){
        try {
            System.out.println("Waiting for connection...");
            clientSocket = serverSocket.accept();
            System.out.println("Player connected");

            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());


            player = new Player("Player");
            deck.shuffle();
            splitTwoCards();


            boolean check = checkBlackjack();


            if(!check) {
                showCards();
                output.writeObject("HIT or STAND:");
                output.flush();

                String message = null;

                while (true) {


                    try {
                        message = (String) input.readObject();
                        if (message.equals("HIT")) {
                            Card card = deck.drawCard();
                            player.addCard(card);
                            if (player.getTotal() <= 21) {
                                playerTurn++;
                                output.writeObject("\nPlayer Turn(" + playerTurn + ")\n" + player.toString() + '\n');
                                output.flush();
                                output.writeObject("HIT or STAND:");
                                output.flush();
                            } else {
                                playerTurn++;
                                output.writeObject("\nPlayer Turn(" + playerTurn + ")\n" + player.toString());
                                output.flush();
                                output.writeObject("\nPlayer BUSTS!");
                                output.flush();
                                output.writeObject("Player LOSES!");
                                output.flush();
                                break;
                            }

                        } else {
                            if (message.equals("STAND")) {
                                break;
                            } else {
                                output.writeObject("Try again!");
                                output.flush();
                                output.writeObject("HIT or STAND:");
                                output.flush();
                            }
                        }


                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (message.equals("STAND")) {
                    while (true) {
                        dealerTurn++;
                        output.writeObject("\nDealer Turn(" + dealerTurn + ")\n" + dealer.toString());
                        output.flush();
                        if (dealer.getTotal() < 17) {
                            output.writeObject("Dealer draws another card.");
                            output.flush();
                            dealer.addCard(deck.drawCard());
                        } else {
                            if (dealer.getTotal() > 21) {
                                output.writeObject("\nDealer BUSTS!");
                                output.flush();
                                output.writeObject("Player WINS!");
                                output.flush();
                                break;
                            } else {
                                getResult();
                                break;
                            }

                        }


                    }


                }
            }

            close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void splitTwoCards(){

        Card card;


        card = deck.drawCard();
        player.addCard(card);
        card = deck.drawCard();
        player.addCard(card);


        card = deck.drawCard();
        dealer.addCard(card);
        card = deck.drawCard();
        dealer.addCard(card);
    }

    private void showCards(){

        try {
            output.writeObject("Dealer has: \n" + dealer.getCards().get(0).toString() + "\nHidden\n");
            output.flush();
            playerTurn++;
            output.writeObject("Player Turn(" + playerTurn + ")\n" + player.toString() + "\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private boolean checkBlackjack(){

            try {   if(player.getTotal() == 21 || dealer.getTotal() == 21){

                        output.writeObject("\n" + player.toString());
                        output.flush();
                        output.writeObject("\n" +dealer.toString());
                        output.flush();

                        if(player.getTotal() == 21 && dealer.getTotal() == 21){

                            output.writeObject("\nPlayer and Dealer have BLACKJACK!");
                            output.flush();
                            output.writeObject("TIE!");
                            output.flush();
                        }
                        else {
                            if(player.getTotal() == 21){

                                output.writeObject("\nYou have BLACKJACK!");
                                output.flush();
                                output.writeObject("Player WINS!");
                                output.flush();
                            }
                            else{

                                output.writeObject("\nDealer has BLACKJACK!");
                                output.flush();
                                output.writeObject("Player LOSES!");
                                output.flush();
                            }
                        }
                        return true;
                    }

                } catch (IOException e) {
                e.printStackTrace();
            }


        return false;
    }


    private void getResult(){

            try {
                output.writeObject("\nPlayer Hand values " + player.getTotal() + ", Dealer Hand values " + dealer.getTotal());
                output.flush();

                if(player.getTotal() > dealer.getTotal()) {
                    output.writeObject("Player WINS!");
                    output.flush();
                }
                else{
                    if(player.getTotal() < dealer.getTotal()){
                        output.writeObject("Player LOSES!");
                        output.flush();
                    }
                    else{
                        output.writeObject("TIE!");
                        output.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void close(){
        try {
            clientSocket.close();
            serverSocket.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
