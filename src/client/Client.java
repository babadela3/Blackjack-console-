package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private String IP;
    private int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Scanner scanner;

    public Client(String IP, int port){

        this.IP = IP;
        this.port = port;
        scanner = new Scanner(System.in);

    }

    private void connectToServer(){
        try {
            socket = new Socket(IP,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpStreams(){
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            output.writeObject(message);
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close(){
        try {
            socket.close();
            scanner.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start(){
        connectToServer();
        setUpStreams();

        String message = null;




        while(true){
            try {

                message = (String) input.readObject();
                System.out.println(message );
                if(message.equals("HIT or STAND:")){

                    String option = scanner.nextLine();
                    sendMessage(option);
                }
                if(message.equals("Do you want to play again?\nYES or NO:")){

                    String option = scanner.nextLine();
                    sendMessage(option);
                }
                if(message.equals("Thank you for playing!")){

                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        close();
    }
}
