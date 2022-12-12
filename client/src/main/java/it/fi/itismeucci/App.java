package it.fi.itismeucci;

public class App 
{
    public static void main( String[] args )
    {
        Client c = new Client();
        try {
            c.comunica();
        } catch (Exception e) {}
        
    }
}
