package it.fi.itismeucci;

import java.io.*;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadInput extends Thread {
    Messaggio msgRicevuto;
    BufferedReader in;
    Socket socket;

    public ThreadInput(BufferedReader in,Socket socket){
        this.in = in;
        this.socket = socket;
    }

    public void run(){
        try {
            input();
        } catch (Exception e) {
        }
    }

    public void input() throws IOException{
        while(true){
           msgRicevuto = ricevi();
           switch (msgRicevuto.getCod()) {
            case 1:
                System.out.println("\n"+msgRicevuto.getContenuto()); //ricevi messaggio
                break;
            case 2:
                //riceve la lista e la stampa 
                if (msgRicevuto.getNomiUtenti().size() == 1) {
                    System.out.println("nessuno ti vuole");
                }else{
                    System.out.println("sono online:");
                    for ( String nomi : msgRicevuto.getNomiUtenti()) {
                    System.out.println(nomi);
                    }
                }
                break;
            case 3:
                //si disconnette
                socket.close();
                System.exit(0);
                break;
            default:
                break;
           }
        
        }
    }

    private Messaggio ricevi() throws IOException{
        String ricevuto = in.readLine();
        ObjectMapper objectMapper = new ObjectMapper();
        Messaggio msg = objectMapper.readValue(ricevuto, Messaggio.class);
        return msg;
    }
}
