package converter;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map.Entry;

import nu.xom.Element;

import static converter.CodeStates.*;

/**
 * @author qman
 *
 */
enum ProcessingStates implements EdifactStates
{
    EDIFACT
    {
        protected HashMap<CodeStates, Entry<ProcessingStates, Boolean>> configureTransitions()
        {
            HashMap<CodeStates, Entry<ProcessingStates, Boolean>> map = new HashMap<>();

            // ---- Code ----------------------------------------- Next State --- Process Code? ---
            //
            map.put( UNA, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( UNB, new SimpleEntry<ProcessingStates, Boolean>( INTERCHANGE, false ) );

            return map;
        }
    },
    INTERCHANGE
    {
        protected HashMap<CodeStates, Entry<ProcessingStates, Boolean>> configureTransitions()
        {
            HashMap<CodeStates, Entry<ProcessingStates, Boolean>> map = new HashMap<>();

            // ---- Code ---------------------------------------------- Next State --- Process Code? ---
            //
            map.put( UNB, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( UNE, new SimpleEntry<ProcessingStates, Boolean>( this, false ) );
            map.put( UNG, new SimpleEntry<ProcessingStates, Boolean>( FUNCTIONAL_GROUP, false ) );
            map.put( UNH, new SimpleEntry<ProcessingStates, Boolean>( MESSAGE, false ) );
            map.put( UNZ, new SimpleEntry<ProcessingStates, Boolean>( TERMINAL, true ) );

            return map;
        }

    },
    FUNCTIONAL_GROUP
    {
        protected HashMap<CodeStates, Entry<ProcessingStates, Boolean>> configureTransitions()
        {
            HashMap<CodeStates, Entry<ProcessingStates, Boolean>> map = new HashMap<>();

            // ---- Code --------------------------------------------- Next State --- Process Code? ---
            //
            map.put( UNE, new SimpleEntry<ProcessingStates, Boolean>( INTERCHANGE, true ) );
            map.put( UNG, new SimpleEntry<ProcessingStates, Boolean>( MESSAGE, true ) );

            return map;
        }
    },
    MESSAGE
    {
        protected HashMap<CodeStates, Entry<ProcessingStates, Boolean>> configureTransitions()
        {
            HashMap<CodeStates, Entry<ProcessingStates, Boolean>> map = new HashMap<>();

            // ---- Code ------------------------------------------------- Next State ----- Process Code? ---
            //
            map.put( UNE, new SimpleEntry<ProcessingStates, Boolean>( this, false ) );
            map.put( UNG, new SimpleEntry<ProcessingStates, Boolean>( this, false ) );
            map.put( UNH, new SimpleEntry<ProcessingStates, Boolean>( MESSAGE_DATA_SEGMENTS, true ) );
            map.put( UNT, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( UNZ, new SimpleEntry<ProcessingStates, Boolean>( INTERCHANGE, false ) );

            return map;
        }
    },
    MESSAGE_DATA_SEGMENTS
    {
        protected HashMap<CodeStates, Entry<ProcessingStates, Boolean>> configureTransitions()
        {
            HashMap<CodeStates, Entry<ProcessingStates, Boolean>> map = new HashMap<>();

            // ---- Code ------------------------------------------------- Next State ----- Process Code? ---
            //
            map.put( UNH, new SimpleEntry<ProcessingStates, Boolean>( this, false ) );
            map.put( UNT, new SimpleEntry<ProcessingStates, Boolean>( MESSAGE, false ) );
            map.put( BGM, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( DTM, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( NAD, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( LIN, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( QTY, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( LOC, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );
            map.put( PRI, new SimpleEntry<ProcessingStates, Boolean>( this, true ) );

            return map;
        }
    },
    TERMINAL // End state
    {
        protected HashMap<CodeStates, Entry<ProcessingStates, Boolean>> configureTransitions()
        {
            HashMap<CodeStates, Entry<ProcessingStates, Boolean>> map = new HashMap<>();

            // ---- Code ------------------------------------------------- Next State ----- Process Code? ---
            //
            map.put( UNZ, new SimpleEntry<ProcessingStates, Boolean>( this, false ) );

            return map;
        }
    };

    @Override
    public EdifactStates actionRoutine( String seg )
    {
        getCodeState( seg );
        Entry<ProcessingStates, Boolean> pair = getCodeData();

        // Should this state process this segment?
        //
        if( pair.getValue() )
        {
            currentCode.actionRoutine( seg );
            rootElem.appendChild( currentCode.getRootElem() );
        }

        // Reset the currentCode handle for the next segment, 
        // and return the next state
        //
        currentCode = null;
        return pair.getKey();
    }

    public Element getRootElem()
    {
        return rootElem;
    }

    public void configure()
    {
        transitionMap = configureTransitions();
    }

    private Entry<ProcessingStates, Boolean> getCodeData()
    {
        if( transitionMap.containsKey( currentCode ) )
        {
            return transitionMap.get( currentCode );
        }
        else
        {
            // Should throw an exception
            //
            throw new RuntimeException( this + ".getCodeData(): no transition "
                    + "for segment code " + currentCode + ", state remains at " + this );
        }
    }

    protected CodeStates getCodeState( String seg )
    {
        if( currentCode != null )
        {
            return currentCode;
        }

        //
        // Else suck out the code and see if there's a corresponding enumerator
        // Throw an exception if not
        //
        String segCode = seg.substring( 0, 3 );
        currentCode = CodeStates.valueOf( segCode );

        if( currentCode == null )
        {
            throw new RuntimeException( "No CodeStates enumerator for segment code "
                    + segCode );
        }

        return currentCode;
    }

    private ProcessingStates()
    {
        rootElem = new Element( this.toString().toLowerCase() );
        System.out.println( this + " constructed" );
    }

    abstract protected HashMap< CodeStates, Entry<ProcessingStates, Boolean>> configureTransitions();

    private HashMap< CodeStates, Entry<ProcessingStates, Boolean>> transitionMap = null;
    private CodeStates currentCode = null;
    protected Element rootElem = null;

}
