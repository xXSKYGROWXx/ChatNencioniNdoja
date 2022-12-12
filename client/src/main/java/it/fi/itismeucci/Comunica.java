package it.fi.itismeucci;

import java.io.*;
import java.net.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Comunica {

    int portaServer = 8088; // porta x servizio data e ora
    Socket mioSocket;
    BufferedReader tastiera; // buffer per l'input da tastiera
    String StringaUtente; // stringa inserita da utente
    String StringaRicevutaDalServer; // stringa ricevuta dal server
    DataOutputStream outVersoServer; // stream output
    BufferedReader inDalServer; // stream input
    //boolean controllo = true;
    boolean cServer = true;
    Messaggio msgRicevuto;
    String nome;

    public Comunica(String nome) throws IOException {
        this.nome = nome;
        mioSocket = new Socket("127.0.0.1", portaServer);
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        outVersoServer = new DataOutputStream(mioSocket.getOutputStream());
        inDalServer = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));
    }

    public void output() throws IOException {
        
        
        //Scelta di un'opzione tramite codice
        while(true){
            //controllo = true;
            System.out.println("Cod = 1 messaggio");
            System.out.println("Cod = 2 richiedi lista");
            System.out.println("Cod = 3 disconessione");
            System.out.print("Inserire il codice: ");
            
            switch (Integer.parseInt(tastiera.readLine())) {
                //Invio di un messaggio ad un altro client
                case 1:
                    Messaggio msg1 = new Messaggio(null,null,nome,1);                  
                    System.out.println("Inserire il nome del destinatario");
                    msg1.setDestinatario(tastiera.readLine());
                    System.out.println("Inserire il contenuto del messaggio");
                    msg1.setContenuto(tastiera.readLine());
                    send(msg1);
                    System.out.println(mioSocket);
                    //while(controllo){} //controllo per verificare se il messaggio è arrivato
                    //System.out.println(msgRicevuto.getContenuto()); 
                    break;
                //Stampa della lista dei client connessi
                case 2:
                    Messaggio msg2 = new Messaggio(null,null,null,2);
                    send(msg2);
                    //while(controllo){}
                    //System.out.println(msgRicevuto.getContenuto()); //Nel server dobbiamo inserire la lista in contenuto
                    break;
                //Disconnessione del client dalla chat
                case 3:
                    Messaggio msg3 = new Messaggio(null,null,null,3);
                    send(msg3);
                    
                    break;
                //Messaggio di errore per codice errato
                default:
                System.out.println("Codice sbagliato");
                    break;
            }
        }
    }

    public void input() throws IOException {
        for(;/*kebabbata*/;){
           msgRicevuto = ricevi();
           switch (msgRicevuto.getCod()) {
            case 0:
                cServer = false;
                break;
           
            case 1:
                System.out.println(msgRicevuto.getContenuto()); //ricevi messaggio
                break;
            case 2:
                //riceve la lista e la stampa 
                break;
            case 3:
                //si disconnette
                mioSocket.close();
                System.exit(0);
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
        outVersoServer.writeBytes(msgStringa + "\n");
    }

    //Deserializzazione dell'oggetto JSon ricevuto
    private Messaggio ricevi() throws IOException{
        String ricevuto = inDalServer.readLine();
        ObjectMapper objectMapper = new ObjectMapper();
        Messaggio msg = objectMapper.readValue(ricevuto, Messaggio.class);
        return msg;
    }
}