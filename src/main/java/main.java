import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class main
{
    public static void main (String args [])
    {
        Access_Polynomial ap = new Access_Polynomial() ;

        String Keys [] = new String[] { "5" , "7" , "9" } ;
        //String Keys [] = new String[] { "1" , "2" , "3" , "4" , "5" } ;
        ap.Add_Key( Keys );

        try {
            ArrayList<String> x = ap.Evaluate_Coefficients();

            FileWriter fileWriter = new FileWriter("a.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println( Integer.toString( ap.Get_R() ) );
            //printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);

            for ( String temp : x )
            {
                //System.out.println(temp);
                printWriter.println( temp  );
            }

            printWriter.close();

            String KC = ap.Evaluate_Decrypt_Key("5");

            System.out.println(KC);

            String KC2 = ap.Evaluate_Decrypt_Key("7");

            System.out.println(KC2);

            String KC3 = ap.Evaluate_Decrypt_Key("9");

            System.out.println(KC3);




        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }





}
