package it.fi.itismeucci;

import java.io.*;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadServer extends Thread{
    Socket client;
    DataOutputStream out; // stream output
    BufferedReader in; // stream input

    public ThreadServer(Socket client){
    this.client = client;
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
                case 0: //login
                    if(Server.utenti.containsKey(msg.getContenuto()))
                    {
                        msg.setContenuto("nosenatore");
                    }
                    else{
                        Server.utenti.put(msg.getContenuto(), this);
                        msg.setContenuto("sisenatore");
                    }
                    send(msg);
                    break;
                case 1:
                //manda messaggio
                    if(Server.utenti.containsKey(msg.getDestinatario()))
                    {
                        if(msg.getDestinatario().equals("BROADCAST")){ 
                            //a tutti
                            for( Map.Entry<String, ThreadServer> entry  : Server.utenti.entrySet()) {
                                String key = entry.getKey();
                                entry.getValue().send(msg);
                            }
                        }
                        else{
                            // a uno
                            System.out.println(msg.getDestinatario());
                            ThreadServer destinatario = Server.utenti.get(msg.getDestinatario()); 
                            destinatario.send(msg);
                            
                        }
                    }
                    break;
                case 2:
                    ArrayList<String> listaNomi = new ArrayList<>();
                    for( Map.Entry<String, ThreadServer> entry  : Server.utenti.entrySet()) {
                        String key = entry.getKey();
                        listaNomi.add(key);
                    }
                    msg.setNomiUtenti(listaNomi);
                    //mette il vecchio mittente in destinatario
                    msg.setDestinatario(msg.getMittente()); 
                    msg.setMittente("SERVER");
                    send(msg);
                    break;

                default:
                    break;
            }
        }
    }
    //Serializzazione in un oggetto JSon e invio del messaggio
    private void send(Messaggio msg) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        String msgStringa = objectMapper.writeValueAsString(msg);
        out.writeBytes(msgStringa + "\n");
    }

    //Deserializzazione dell'oggetto JSon ricevuto
    private Messaggio ricevi() throws IOException{
        String ricevuto = in.readLine();
        ObjectMapper objectMapper = new ObjectMapper();
        Messaggio msg = objectMapper.readValue(ricevuto, Messaggio.class);
        return msg;
    }
}


