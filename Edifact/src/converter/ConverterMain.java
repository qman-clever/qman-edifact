/**
 * This program will read an EDIFACT message and convert it into
 * a corresponding XML document, then validate it according
 * to EDIFACT.dtd.
 *
 * TODO: add command line argument to separate segments via an optional newline
 * TODO: add a new static method, call it parseSegments(), which will do the splitting
 */
package converter;

import java.io.*;
import java.util.Scanner;
import ml.options.*;

/**
 * @author qman
 *
 */
public class ConverterMain
{

    /**
     * @param args
     * @throws IOException
     */
    public static void main( String[] args ) throws IOException
    {
        // Open the EDIFACT document provided as a command line argument, or puke
        //
        if( args.length == 0 )
        {
            System.err.println( "USAGE: ConverterMain filename" );
            return;
        }

        Options ops = new Options( args );
        ops.getSet().addOption( OptionData.Type.VALUE, "i", Options.Multiplicity.ONCE );
        ops.getSet().addOption( OptionData.Type.VALUE, "o", Options.Multiplicity.ZERO_OR_ONCE );
        
        if( ops.check() == false )
        {
            System.err.println( "You fucked up!!" );
            return;
        }
        
        File file = new File( ops.getSet().getOption( "i" ).getResultValue( 0 ) );
        Scanner inputFile;

        try
        {
            inputFile = new Scanner( file );
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
            return;
        }

        // Create an object which will model the transformed EDIFACT document
        //
        Edifact ed = new Edifact();

        // 
        // Start reading
        //
        while( inputFile.hasNext() )
        {
            // TODO: chop off the segment terminator here, before feeding it to Edifact object
            //
            ed.parseSegment( inputFile.nextLine() );
        } // End of loop

        // Dump out the contents 
        //
        ed.dump( ops.getSet() );

        // Put your toys away when you're done playing with them
        //
        inputFile.close();
    }

}
