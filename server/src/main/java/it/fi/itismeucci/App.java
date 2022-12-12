package it.fi.itismeucci;

public class App 
{
    public static void main( String[] args )
    {
        Server s = new Server();
        try {
            s.connetti();
        } catch (Exception e) {}
        
    }
}
