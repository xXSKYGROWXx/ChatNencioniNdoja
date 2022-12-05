package it.fi.itismeucci;

import java.io.*;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadServer extends Thread{
    Socket client;
    HashMap<String,Socket> utenti;
    DataOutputStream out; // stream output
    BufferedReader in; // stream input

    public ThreadServer(Socket client, HashMap<String,Socket> utenti){
    this.client = client;
    this.utenti = utenti;
    }

    public void run(){
        try {
            comunica();
        } catch (Exception e) {}
         
    }

    private void comunica() throws IOException{
        out = new DataOutputStream(client.getOutputStream());
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        for(;;){
            Messaggio msg = ricevi();
            switch (msg.getCod()) {
                case 0:
                    //controlla se il contenuto è un nome valido
                    if(utenti.containsKey(msg.getContenuto()))
                    {
                        msg.setContenuto("nome non valido");
                    }
                    else{
                        utenti.put(msg.getContenuto(), client);
                        msg.setContenuto("sisenatore");
                    }
                    send(msg,client);
                    break;
                case 1:
                //manda messaggio
                    if(!utenti.containsKey(msg.getDestinatario()))
                    {
                        if(msg.getDestinatario().equals("BROADCAST")){ 
                            //a tutti

                        }
                        else{
                            // a uno
                            Socket destinatario = utenti.get(msg.getDestinatario()); 
                            send(msg, destinatario);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
    //Serializzazione in un oggetto JSon e invio del messaggio
    private void send(Messaggio msg,Socket socket) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        String msgStringa = objectMapper.writeValueAsString(msg);
        (new DataOutputStream(socket.getOutputStream())).writeBytes(msgStringa + "\n");
    }

    //Deserializzazione dell'oggetto JSon ricevuto
    private Messaggio ricevi() throws IOException{
        String ricevuto = in.readLine();
        ObjectMapper objectMapper = new ObjectMapper();
        Messaggio msg = objectMapper.readValue(ricevuto, Messaggio.class);
        return msg;
    }
}


