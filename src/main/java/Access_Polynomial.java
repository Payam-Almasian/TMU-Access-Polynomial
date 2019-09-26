import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;


public class Access_Polynomial
{

    private int r ;

    private ArrayList <String> Keys ;

    private ArrayList <String> Co_efficients ;

    private String Prime ;

    private String KC ;

    public Access_Polynomial ()
    {
        this.r = 1 ;
        Keys = new ArrayList<String>();
        Co_efficients = new ArrayList<String>() ;
        Prime = "29"  ;
        KC="7" ;

    }


    public int Get_R ()
    {
        return this.r ;
    }

    public ArrayList<String> Get_Co_efficients ()
    {
        return this.Co_efficients ;
    }


    public boolean Add_Key ( String ... keys)
    {

        for ( String key : keys )
        {
            this.Keys.add(key);

        }

        return true ;

    }


    public boolean Remove_Key ( String ... keys )
    {
        for ( String key : keys )
        {
            this.Keys.removeIf( n -> ( n.equals(key) ) ) ;


        }


        return true ;
    }



    public ArrayList<String> Evaluate_Coefficients () throws NoSuchAlgorithmException {
        Random random = new Random() ;

        int temp = random.nextInt(10) % 10 + 1 ;

        this.r = this.r + temp ;


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest( ( Keys.get(0) + Integer.toString( this.r ) ).getBytes(StandardCharsets.UTF_8));
        //String sha256hex = new String(Hex.encode(hash));


        BigInteger a0 = new BigInteger( hash );
        //System.out.println( a0.toString() );
        a0= a0.multiply( new BigInteger("-1") );
        //System.out.println( a0.toString() );
        a0 = a0.mod( new BigInteger(Prime)) ;

        BigInteger a1 = new BigInteger( "1" );

        Co_efficients.add( a0.toString() ) ;
        Co_efficients.add( a1.toString() ) ;


        for ( int i = 2 ; i < 10 ; i++ )
        {
            Co_efficients.add( Integer.toString( 0 ) ) ;
        }


        for ( int i = 1 ; i <=  ( Keys.size() - 1 ) ; i++ )
        {

            for ( int j = i ;  j >= 1 ; j-- )
            {
                String aj_1 = Co_efficients.get( j - 1 ) ;

                String aj = Co_efficients.get( j  ) ;

                byte[] hash_temp = digest.digest( ( Keys.get(i) + Integer.toString( this.r ) ).getBytes(StandardCharsets.UTF_8));
                BigInteger a_temp = new BigInteger( hash_temp );


                //System.out.println( a_temp.toString() );
                a_temp= a_temp.multiply( new BigInteger( aj ) );


                BigInteger aj_1_big_integer = new BigInteger( aj_1 );

                a_temp = aj_1_big_integer.subtract( a_temp ) ;

                a_temp = a_temp.mod( new BigInteger(Prime)) ;

                Co_efficients.set( j , a_temp.toString() ) ;

                //System.out.println( a0.toString() );


            }

            BigInteger ai_plus_1 = new BigInteger( "1" );
            Co_efficients.set( i+1  , ai_plus_1.toString() ) ;



            byte[] hash_temp_2 = digest.digest( ( Keys.get(i) + Integer.toString( this.r ) ).getBytes(StandardCharsets.UTF_8));
            BigInteger a_temp_2 = new BigInteger( hash_temp_2 );

            String a0_again = Co_efficients.get(0) ;

            BigInteger a0_again_big_integer = new BigInteger( a0_again ) ;

            a_temp_2 = a_temp_2.multiply( a0_again_big_integer ).multiply( new BigInteger("-1")).mod( new BigInteger(Prime)) ;

            //a_temp_2 = a_temp_2.add( new BigInteger(KC)).mod( new BigInteger(Prime)) ;

            Co_efficients.set( 0 , a_temp_2.toString() ) ;




        }

        BigInteger a_temp_2 =  new BigInteger( Co_efficients.get( 0 ) )  ;

        a_temp_2 = a_temp_2.add( new BigInteger(KC)).mod( new BigInteger(Prime)) ;

        Co_efficients.set( 0 , a_temp_2.toString() ) ;



        return Co_efficients ;
    }




    public String Evaluate_Decrypt_Key ( String Key ) throws NoSuchAlgorithmException {


        BigInteger S = new BigInteger( Co_efficients.get(0) ) ;


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash_temp = digest.digest( ( Key + Integer.toString( this.r ) ).getBytes(StandardCharsets.UTF_8));
        BigInteger X = new BigInteger( hash_temp );

        BigInteger Mult = new BigInteger("1") ;

        for ( int i = 1 ; i <= ( this.Co_efficients.size() - 1 ) ; i++  )
        {
            Mult= Mult.multiply(X) ;

            String ai = Co_efficients.get(i) ;

            BigInteger ai_big_integer = new BigInteger(ai) ;

            S = ai_big_integer.multiply( Mult ).add(S).mod( new BigInteger( Prime ) );

        }




        return S.toString() ;
    }



}
