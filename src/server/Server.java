package server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final int portNumber = 9999;

    public Server(){

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start(){
        try {
            System.out.println("Waiting for connection...");
            clientSocket = serverSocket.accept();
            System.out.println("Player connected");

            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());

            String message = null;
            Player player;
            Player dealer;




            while(true){

                output.writeObject("Start game!\n");
                output.flush();
                int playerTurn = 0;
                int dealerTurn = 0;
                player = new Player("Player");
                dealer = new Player("Dealer");
                Deck deck = new Deck();
                deck.shuffle();
                splitTwoCards(player, dealer, deck);


                boolean check = checkBlackjack(player, dealer);


                if(!check) {
                    showCards(player, dealer);
                    output.writeObject("HIT or STAND:");
                    output.flush();



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
                                    output.writeObject("Player LOSES!\n");
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
                                    output.writeObject("Player WINS!\n");
                                    output.flush();
                                    break;
                                } else {
                                    getResult(player, dealer);
                                    break;
                                }

                            }


                        }


                    }
                }


                try {
                    output.writeObject("Do you want to play again?\nYES or NO:");
                    output.flush();
                    while (true) {
                        message = (String) input.readObject();
                        if (message.equals("NO") || message.equals("YES")) {
                            break;
                        } else {
                            output.writeObject("Try again!");
                            output.flush();
                            output.writeObject("Do you want to play again?\nYES or NO:");
                            output.flush();
                        }
                    }
                    if (message.equals("NO")){
                        output.writeObject("Thank you for playing!");
                        output.flush();
                        break;
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }

            close();
            System.out.print("Player disconnected");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void splitTwoCards(Player player, Player dealer,Deck deck){

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

    private void showCards(Player player, Player dealer){

        try {
            output.writeObject("Dealer has: \n" + dealer.getCards().get(0).toString() + "\nHidden\n");
            output.flush();
            output.writeObject("Player Turn(1)\n" + player.toString() + "\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private boolean checkBlackjack(Player player, Player dealer){

            try {   if(player.getTotal() == 21 || dealer.getTotal() == 21){

                        output.writeObject("\n" + player.toString());
                        output.flush();
                        output.writeObject("\n" +dealer.toString());
                        output.flush();

                        if(player.getTotal() == 21 && dealer.getTotal() == 21){

                            output.writeObject("\nPlayer and Dealer have BLACKJACK!");
                            output.flush();
                            output.writeObject("TIE!\n");
                            output.flush();
                        }
                        else {
                            if(player.getTotal() == 21){

                                output.writeObject("\nYou have BLACKJACK!");
                                output.flush();
                                output.writeObject("Player WINS!\n");
                                output.flush();
                            }
                            else{

                                output.writeObject("\nDealer has BLACKJACK!");
                                output.flush();
                                output.writeObject("Player LOSES!\n");
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


    private void getResult(Player player, Player dealer){

            try {
                output.writeObject("\nPlayer Hand values " + player.getTotal() + ", Dealer Hand values " + dealer.getTotal());
                output.flush();

                if(player.getTotal() > dealer.getTotal()) {
                    output.writeObject("Player WINS!\n");
                    output.flush();
                }
                else{
                    if(player.getTotal() < dealer.getTotal()){
                        output.writeObject("Player LOSES!\n");
                        output.flush();
                    }
                    else{
                        output.writeObject("TIE!\n");
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
