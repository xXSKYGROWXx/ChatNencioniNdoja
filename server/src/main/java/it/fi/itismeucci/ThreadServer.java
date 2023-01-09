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

        while(true){
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
                        //a tutti tranne il mittente
                        
                        if(msg.getDestinatario().equals("BROADCAST")){ 
                            
                            if(Server.utenti.size() == 1){
                                msg.setDestinatario(msg.getMittente());
                                msg.setMittente("SERVER");
                                msg.setContenuto("sei solo ;(");
                                Server.utenti.get(msg.getDestinatario()).send(msg);
                                break;
                            }
                            
                            for (String key : Server.utenti.keySet()) {
                                if(!key.equals(msg.mittente))
                                Server.utenti.get(key).send(msg);
                            }
                            /*Messaggio msgRisMit = new Messaggio("messaggio inviato",msg.getMittente(),"SERVERONE",msg.getCod());
                            ThreadServer rispostaMittente = Server.utenti.get(msgRisMit.getDestinatario()); 
                            rispostaMittente.send(msgRisMit);*/
                        }
                        // a uno
                        else if(Server.utenti.containsKey(msg.getDestinatario())) //controlla se il dst è nell'hashmap
                        { 
                            System.out.println(msg.getDestinatario());
                            ThreadServer destinatario = Server.utenti.get(msg.getDestinatario()); 
                            destinatario.send(msg);
                            /*Messaggio msgRisMit = new Messaggio("messaggio inviato",msg.getMittente(),"SERVERONE",msg.getCod());
                            ThreadServer rispostaMittente = Server.utenti.get(msgRisMit.getDestinatario()); 
                            rispostaMittente.send(msgRisMit); */
                        }
                        else{
                            msg.setDestinatario(msg.getMittente());
                            msg.setMittente("SERVER");
                            msg.setContenuto("utente non trovato");
                            Server.utenti.get(msg.getDestinatario()).send(msg);
                        }
                        
                    break;
                case 2: //invio lista
                    ArrayList<String> listaNomi = new ArrayList<>();
                    for( String key : Server.utenti.keySet()) {
                        listaNomi.add(key);
                    } // crea un arraylist e mette i nomi
                    msg.setNomiUtenti(listaNomi);
                    msg.setDestinatario(msg.getMittente()); //mette il vecchio mittente in destinatario
                    msg.setMittente("SERVER");
                    send(msg);
                    break;
                case 3:
                    send(msg);
                    Server.utenti.remove(msg.getMittente());
                    client.close();
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


