package converter;

import static converter.DataStates.*;
import static converter.Syntax.*;

import nu.xom.Attribute;
import nu.xom.Element;

enum CodeStates implements EdifactStates
{
	// ------------------ Advice, Headers, Trailers -------------------
	//
	UNA
	{
		public EdifactStates actionRoutine( String s )
		{
			// Create service_string_advice subtree
			//
			Element advice = new Element( "service_string_advice" );
			Element dataElemSep = new Element( "data_element_separator" );
			Element compDataElemSep = new Element( "component_data_element_separator" );
			Element decMark = new Element( "decimal_mark" );
			Element escChar = new Element( "escape_character" );
			Element segTerm = new Element( "segment_terminator" );
			
			// Establish the relationships
			//
			advice.appendChild(compDataElemSep);
			advice.appendChild(dataElemSep);
			advice.appendChild(decMark);
			advice.appendChild(escChar);
			advice.appendChild(segTerm);
			
			String elements = s.substring( 3 );
			
			COMP_DATA_ELEM_SEPARATOR.setSyntax( elements.charAt( COMP_DATA_ELEM_SEPARATOR.ordinal() ));
			DATA_ELEM_SEPARATOR.setSyntax( elements.charAt( DATA_ELEM_SEPARATOR.ordinal() ) );
			DECIMAL_MARK.setSyntax( elements.charAt( DECIMAL_MARK.ordinal() ) );
			ESCAPE_CHAR.setSyntax( elements.charAt( ESCAPE_CHAR.ordinal() )); 
			SEGMENT_TERMINATOR.setSyntax( elements.charAt( SEGMENT_TERMINATOR.ordinal() ) + "\n" );
			
			
			compDataElemSep.appendChild( COMP_DATA_ELEM_SEPARATOR.toString() );
			dataElemSep.appendChild( DATA_ELEM_SEPARATOR.toString() );
			decMark.appendChild( DECIMAL_MARK.toString() );
			escChar.appendChild( ESCAPE_CHAR.toString() ); 
			segTerm.appendChild( SEGMENT_TERMINATOR.toString() );
			
			this.rootElem = advice;
			
			return this;
		}
	},
	
	UNB( true )
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				SYNTAX_IDENTIFIER, INTERCHANGE_SENDER, INTERCHANGE_RECIPIENT,
				DATE_TIME_OF_PREPARATION, INTERCHANGE_CONTROL_REFERENCE,
				RECIPIENTS_REFERENCE_PASSWORD, APPLICATION_REFERENCE,
				PROCESSING_PRIORITY_CODE, ACKNOWLEDGEMENT_REQUEST,
				COMMUNICATIONS_AGREEMENT, TEST_INDICATOR		
			};
			
			return mach;
		}
		
		@Override
		public EdifactStates actionRoutine( String seg ) 
		{
			this.rootElem = new Element( "interchange_header" );
			return super.actionRoutine( seg );
		}	
	}, 
	
	UNG( true )
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				FUNCTIONAL_GROUP_IDENTIFICATION,
				APPLICATION_SENDERS_IDENTIFICATION,
				APPLICATION_RECIPIENTS_IDENTIFICATION,
				DATE_TIME_OF_PREPARATION, // Also in UNB
				FUNCTIONAL_GROUP_REFERENCE_NUMBER,
				CONTROLLING_AGENCY, // NOTE: also exists as component of UNH's MESSAGE_IDENTIFIER
				MESSAGE_VERSION,
				APPLICATION_PASSWORD
			};
			
			return mach;
		}
		
		@Override
		public EdifactStates actionRoutine( String seg ) 
		{
			this.rootElem = new Element( "functional_group_header" );
			return super.actionRoutine( seg );
		}	
	}, 
	 
	UNH( true )
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				MESSAGE_REFERENCE_NUMBER,
				MESSAGE_IDENTIFIER,
				COMMON_ACCESS_REFERENCE,
				STATUS_OF_THE_TRANSFER
			};
			
			return mach;
		}
		
		@Override
		public EdifactStates actionRoutine( String seg ) 
		{
			this.rootElem = new Element( "message_header" );
			return super.actionRoutine( seg );
		}	
	}, 
	
	UNT( true )
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				NUMBER_OF_SEGMENTS,
				MESSAGE_REFERENCE_NUMBER
			};
			
			return mach;
		}
		
		@Override
		public EdifactStates actionRoutine( String seg ) 
		{
			this.rootElem = new Element( "message_trailer" );
			return super.actionRoutine( seg );
		}	
	}, 
	
	UNE( true )
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				NUMBER_OF_MESSAGES,
				FUNCTIONAL_GROUP_REFERENCE_NUMBER
			};
			
			return mach;
		}
		
		@Override
		public EdifactStates actionRoutine( String seg ) 
		{
			this.rootElem = new Element( "functional_group_trailer" );
			return super.actionRoutine( seg );
		}	
	},


	UNZ( true )
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				INTERCHANGE_CONTROL_COUNT,
				INTERCHANGE_CONTROL_REFERENCE
			};
			
			return mach;
		}
		
		@Override
		public EdifactStates actionRoutine( String seg ) 
		{
			this.rootElem = new Element( "interchange_trailer" );
			return super.actionRoutine( seg );
		}	
	},
	
	// ------------------------- Message guts -------------------------
	//
	BGM
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				MESSAGE_REFERENCE_NUMBER,
				MESSAGE_IDENTIFIER,
				COMMON_ACCESS_REFERENCE,
				STATUS_OF_THE_TRANSFER
			};
			
			return mach;
		}
		
	},
	
	DTM
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				DATE_TIME_PERIOD
			};
			
			return mach;
		}
		
	},
 
	NAD
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				PARTY_QUALIFIER, 
				PARTY_IDENTIFICATION_DETAILS,
				NAME_AND_ADDRESS,
				PARTY_NAME,
				STREET,
				CITY_NAME, 
				COUNTRY_SUB_ENTITY_IDENTIFICATION, 
				POSTCODE_IDENTIFICATION, 
				COUNTRY_CODED
			};
			
			return mach;
		}
		
	},
 
	LIN
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				LINE_ITEM_NUMBER,
				ACTION_REQUEST_NOTIFICATION_CODED,
				ITEM_NUMBER_IDENTIFICATION,
				SUB_LINE_INFORMATION,
				CONFIGURATION_LEVEL,
				CONFIGURATION_CODED
			};
			
			return mach;
		}
		
	},
 
	QTY
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				QUANTITY_DETAILS
			};
			
			return mach;
		}
		
	},
 
	LOC
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				PLACE_LOCATION_QUALIFIER,
				LOCATION_IDENTIFICATION,	
				RELATED_LOCATION_ONE_IDENTIFICATION,
				RELATED_LOCATION_TWO_IDENTIFICATION,
				RELATION_CODED
			};
			
			return mach;
		}
		
	},
 
	PRI
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				PRICE_INFORMATION,
				SUBLINE_PRICE_CHANGE_CODED
			};
			
			return mach;
		}
		
	};

	EdifactStates[] configureMachine()
	{
		return null;
	}
	
	private EdifactStates[] dataElemStateMachine = null;
	
	private CodeStates( )
	{
		dataElemStateMachine = configureMachine();
	}
	
	private CodeStates( boolean headerOrTrailer )
	{
		this.headerOrTrailer = headerOrTrailer;
		dataElemStateMachine = configureMachine();
	}
	
	public EdifactStates actionRoutine( String seg ) 
	{
		// Not necessary to add a check to make sure that the segment code matches
		// the enum object name.  If we get to this point, my design assures
		// that there is a match.
		//
		Element elem = new Element( "segment" );
		elem.addAttribute( new Attribute( "code", seg.substring( 0, 3 ) ) );
		
		// Do data elements
		//
		if( dataElemStateMachine == null )
		{
			throw new RuntimeException( this + ".actionRoutine(): No data element states for segment "
										+ seg );
		}
		
		
		int begPos = 4, endPos = -1, index = 0;
		try
		{
			while( true )
			{
				endPos = seg.indexOf( DATA_ELEM_SEPARATOR.toString(), begPos );
				dataElemStateMachine[ index ].
						// TODO: change positive result of ternary operator to assume that 
						// segment terminating character has already been stripped at this point
						//
						actionRoutine( seg.substring(begPos, endPos == -1 ? seg.length() - 1 : endPos ));
				elem.appendChild(dataElemStateMachine[ index++ ].getRootElem() );
				
				if( endPos == -1 )
				{
					break;
				}
				else
				{
					begPos = ++endPos;
				}
			}
		}
		catch( ArrayIndexOutOfBoundsException a )
		{
			System.err.println( this + ".actionRoutine(): ran out of states at index = " + --index );
		}

		// Figure out if this particular element is enclosed within another.
		// This is only true, however, of segments which are headers or trailers.
		//
		if( this.rootElem != null && this.headerOrTrailer )
		{
			rootElem.appendChild( elem );
		}
		else
		{
			rootElem = elem;
		}
		
		return this;
	}

	@Override
	public Element getRootElem() 
	{
		return rootElem;
	}
	
	protected Element rootElem = null;
	private boolean headerOrTrailer = false;
}