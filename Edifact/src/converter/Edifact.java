/**
 * TODO: add a configuration option to decide to process segments as a stream, or
 * separated by newline characters.
 *
 * TODO: add a way to specify the output file for the XML
 *
 */
package converter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import nu.xom.*;
import ml.options.OptionSet;

/**
 * @author qman
 *
 */
public class Edifact
{

    private EdifactStates currentState = ProcessingStates.EDIFACT;
    private EdifactStates nextState = null;

    // Create the root of the XML document to construct
    //
    private Element head = currentState.getRootElem();
    private Document doc = new Document( head );
    private DocType doctype = new DocType( "edifact", "EDIFACT.dtd" );

    //
    // This is not going to be as easy as I thought: it's not a simple
    // matter of just parsing each segment and creating the associated
    // elements.  You forgot that you also have to supply the necessary
    // attributes!!  
    //
    // You'll have to come up with some data structures to map each
    // code to all of its attributes.
    //
    public void parseSegment( String segment )
    {
        // Now give everything to the state machine
        //
        while( true )
        {
            nextState = currentState.actionRoutine( segment );

            if( currentState != nextState )
            {
                // I hate C-style casts, but this is, unfortunately,
                // the only way to get this done, given my design.
                // TODO: see if there's a better way to get this done.
                //
                if( (( ProcessingStates ) nextState).ordinal()
                        > (( ProcessingStates ) currentState).ordinal()
                        && (( ProcessingStates ) nextState) != ProcessingStates.TERMINAL )
                {
                    currentState.getRootElem().appendChild( nextState.getRootElem() );
                }
                currentState = nextState;
            }
            else
            {
                break;
            }
        }
    }

    public void dump( OptionSet set ) throws IOException
    {
        if( set.isSet( "o" ) )
        {
            format( new BufferedOutputStream( 
                    new FileOutputStream( set.getOption( "o" ).getResultValue( 0 ) ) ), 
                    doc );
        }
        else
        {
            format( System.out, doc );
        }
    }

    private void format( OutputStream os, Document doc ) throws IOException
    {
        Serializer serialize = new Serializer( os, "UTF-8" );
        serialize.setIndent( 4 );
        serialize.setMaxLength( 72 );
        serialize.write( doc );
        serialize.flush();
    }

    public Edifact()
    {
        // Configure state transitions -- 
        // MUST be done here, after all ProcessingStates enumerators are loaded! 
        //
        for( ProcessingStates e : ProcessingStates.values() )
        {
            e.configure();
        }
        
        doc.insertChild( doctype, 0 );
    }

}
