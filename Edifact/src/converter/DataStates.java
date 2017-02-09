/**
 * Models state sequence for constructing a UNB segment
 */
package converter;

import static converter.Syntax.COMP_DATA_ELEM_SEPARATOR;

import nu.xom.Attribute;
import nu.xom.Element;


/**
 * @author qman
 *
 */
enum DataStates implements EdifactStates 
{
	// *************** Associated with UNB ***************
	//
	SYNTAX_IDENTIFIER
	{
		@Override
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach = 
			{
				ComponentDataStates.IDENTIFIER, 
			  	ComponentDataStates.VERSION_NUMBER 				  
			};
			
			return mach;
		}
	}, 
	
	INTERCHANGE_SENDER
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.SENDER_IDENTIFICATION, 
					ComponentDataStates.PARTNER_IDENTIFICATION,
					ComponentDataStates.REVERSE_ROUTING_ADDRESS
				};
			return mach;
		}
	}, 
	
	INTERCHANGE_RECIPIENT
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
				 	ComponentDataStates.RECIPIENT_IDENTIFICATION, 
					ComponentDataStates.PARTNER_IDENTIFICATION,
					ComponentDataStates.ROUTING_ADDRESS
				};
			return mach;
		}
	},
	
	DATE_TIME_OF_PREPARATION // Also in UNG
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.DATE, 
					ComponentDataStates.TIME 
				};
			
			return mach;
		}
	}, 
	INTERCHANGE_CONTROL_REFERENCE, // Also in UNZ
	RECIPIENTS_REFERENCE_PASSWORD
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.PASSWORD, 
					ComponentDataStates.PASSWORD_QUALIFIER 
				};
			
			return mach;
		}
	}, 
	
	APPLICATION_REFERENCE, 
	PROCESSING_PRIORITY_CODE, ACKNOWLEDGEMENT_REQUEST, 
	COMMUNICATIONS_AGREEMENT, TEST_INDICATOR,
	
	// *************** Associated with UNG ***************
	//
	FUNCTIONAL_GROUP_IDENTIFICATION, 
	APPLICATION_SENDERS_IDENTIFICATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					// The following two are also in UNB, but 
					// I have a feeling they may be different,
					// here in UNG.  We'll just have to test
					// it and see.
					//
					ComponentDataStates.SENDER_IDENTIFICATION,
					ComponentDataStates.PARTNER_IDENTIFICATION
				};
			
			return mach;
		}
	},
	
	APPLICATION_RECIPIENTS_IDENTIFICATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					// Same as above
					//
				 	ComponentDataStates.RECIPIENT_IDENTIFICATION, 
					ComponentDataStates.PARTNER_IDENTIFICATION,
				};
			
			return mach;
		}
	},
	/* DATE_TIME_OF_PREPARATION */
	FUNCTIONAL_GROUP_REFERENCE_NUMBER,
	CONTROLLING_AGENCY, // NOTE: also exists as component of UNH's MESSAGE_IDENTIFIER
	MESSAGE_VERSION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.MESSAGE_VERSION_NUMBER, 
					ComponentDataStates.MESSAGE_RELEASE_NUMBER, 
					ComponentDataStates.ASSOCIATION_ASSIGNED
				};
			
			return mach;
		}
	},
	APPLICATION_PASSWORD,

	// *************** Associated with UNH ***************
	//
	MESSAGE_REFERENCE_NUMBER, // Also in UNT
	MESSAGE_IDENTIFIER
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.MESSAGE_TYPE, 
					ComponentDataStates.MESSAGE_VERSION_NUMBER, 
					ComponentDataStates.MESSAGE_RELEASE_NUMBER, 
					ComponentDataStates.CONTROLLING_AGENCY, 
					ComponentDataStates.ASSOCIATION_ASSIGNED
				};
			
			return mach;
		}
	},
	COMMON_ACCESS_REFERENCE,
	STATUS_OF_THE_TRANSFER
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.SEQUENCE_OF_TRANSFERS, 
					ComponentDataStates.FIRST_AND_LAST_TRANSFER
				};
			
			return mach;
		}
	},

	// *************** Associated with UNT ***************
	//
	NUMBER_OF_SEGMENTS,
	/* MESSAGE_REFERENCE_NUMBER */

	// *************** Associated with UNE ***************
	//
	NUMBER_OF_MESSAGES,
	/* FUNCTIONAL_GROUP_REFERENCE_NUMBER */

	// *************** Associated with UNZ ***************
	//
	INTERCHANGE_CONTROL_COUNT,
	/* INTERCHANGE_CONTROL_REFERENCE */

	// *************** Associated with BGM ***************
	//
	MESSAGE_NAME
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.MESSAGE_NAME_CODED, 
					ComponentDataStates.CODE_LIST_QUALIFIER,
					ComponentDataStates.CODE_LIST_RESPONSIBLE_AGENCY_CODED,
					ComponentDataStates.MESSAGE_NAME
				};
			
			return mach;
		}
	},
	
	MESSAGE_IDENTIFICATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.MESSAGE_NUMBER, 
					ComponentDataStates.VERSION,
					ComponentDataStates.REVISION_NUMBER
				};
		
			return mach;
		}
	},
	
	MESSAGE_FUNCTION, RESPONSE_TYPE,
	
	// *************** Associated with DTM ***************
	//
	DATE_TIME_PERIOD
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.DATE_TIME_PERIOD_QUALIFIER, 
					ComponentDataStates.DATE_TIME_PERIOD,
					ComponentDataStates.DATE_TIME_PERIOD_FORMAT_QUALIFIER
				};
		
			return mach;
		}
	},
	
	
	// *************** Associated with NAD ***************
	//
	PARTY_QUALIFIER,
	
	PARTY_IDENTIFICATION_DETAILS
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.PARTY_ID_IDENTIFICATION, 
					ComponentDataStates.CODE_LIST_QUALIFIER,
					ComponentDataStates.CODE_LIST_RESPONSIBLE_AGENCY_CODED
				};
		
			return mach;
		}
	},

	NAME_AND_ADDRESS
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.NAME_AND_ADDRESS_LINE, // Up to five
					ComponentDataStates.NAME_AND_ADDRESS_LINE, 
					ComponentDataStates.NAME_AND_ADDRESS_LINE, 
					ComponentDataStates.NAME_AND_ADDRESS_LINE, 
					ComponentDataStates.NAME_AND_ADDRESS_LINE				};
		
			return mach;
		}
	},
 
	PARTY_NAME
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.PARTY_NAME, // Up to five
					ComponentDataStates.PARTY_NAME, 
					ComponentDataStates.PARTY_NAME, 
					ComponentDataStates.PARTY_NAME, 
					ComponentDataStates.PARTY_NAME,
					ComponentDataStates.PARTY_NAME_FORMAT_CODED
				};
		
			return mach;
		}
	},
 
	STREET
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.STREET_AND_NUMBER, // Up to four
					ComponentDataStates.STREET_AND_NUMBER, 
					ComponentDataStates.STREET_AND_NUMBER, 
					ComponentDataStates.STREET_AND_NUMBER
				};
		
			return mach;
		}
	},
 
	CITY_NAME, COUNTRY_SUB_ENTITY_IDENTIFICATION, POSTCODE_IDENTIFICATION, COUNTRY_CODED,
	
	// *************** Associated with LIN ***************
	//
	LINE_ITEM_NUMBER, ACTION_REQUEST_NOTIFICATION_CODED, 
	
	ITEM_NUMBER_IDENTIFICATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.ITEM_NUMBER, 
					ComponentDataStates.ITEM_NUMBER_TYPE_CODED
				};
		
			return mach;
		}
	},
 
	SUB_LINE_INFORMATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.SUB_LINE_INDICATOR_CODED, 
					ComponentDataStates.LINE_ITEM_NUMBER
				};
		
			return mach;
		}
	},
	
	CONFIGURATION_LEVEL, CONFIGURATION_CODED,
	
	// *************** Associated with QTY ***************
	//
	QUANTITY_DETAILS
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.QUANTITY, 
					ComponentDataStates.QUANTITY_QUALIFIER, 
					ComponentDataStates.MEASURE_UNIT_QUALIFIER
				};
		
			return mach;
		}
	},

	// *************** Associated with LOC ***************
	//
	PLACE_LOCATION_QUALIFIER, 
	LOCATION_IDENTIFICATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.PLACE_LOCATION_IDENTIFICATION, 
					ComponentDataStates.CODE_LIST_QUALIFIER, 
					ComponentDataStates.CODE_LIST_RESPONSIBLE_AGENCY_CODED,
					ComponentDataStates.PLACE_LOCATION
				};
		
			return mach;
		}
	},

	RELATED_LOCATION_ONE_IDENTIFICATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.RELATED_PLACE_LOCATION_ONE_IDENTIFICATION, 
					ComponentDataStates.CODE_LIST_QUALIFIER, 
					ComponentDataStates.CODE_LIST_RESPONSIBLE_AGENCY_CODED,
					ComponentDataStates.RELATED_PLACE_LOCATION_ONE
				};
		
			return mach;
		}
	},

	RELATED_LOCATION_TWO_IDENTIFICATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.RELATED_PLACE_LOCATION_TWO_IDENTIFICATION, 
					ComponentDataStates.CODE_LIST_QUALIFIER, 
					ComponentDataStates.CODE_LIST_RESPONSIBLE_AGENCY_CODED,
					ComponentDataStates.RELATED_PLACE_LOCATION_TWO
				};
		
			return mach;
		}
	},

	RELATION_CODED,

	// *************** Associated with PRI ***************
	//
	PRICE_INFORMATION
	{
		EdifactStates[] configureMachine()
		{
			EdifactStates[] mach =
				{
					ComponentDataStates.PRICE_QUALIFIER, 
					ComponentDataStates.PRICE, 
					ComponentDataStates.PRICE_TYPE_CODED,
					ComponentDataStates.UNIT_PRICE_BASIS,
					ComponentDataStates.MEASURE_UNIT_QUALIFIER
				};
		
			return mach;
		}
	},

	SUBLINE_PRICE_CHANGE_CODED
	
	;

	EdifactStates[] configureMachine()
	{
		return null;
	}

	private EdifactStates[] compDataElemStateMachine = null;
	
	private DataStates()
	{
		compDataElemStateMachine = configureMachine();
	}
	
	@Override
	public EdifactStates actionRoutine(String s) 
	{
		// First create data element
		//
		rootElem = new Element( "data_element" );
		rootElem.addAttribute( new Attribute( "name", 
									this.toString().toLowerCase()) );
		
		// If no data to parse, then just return empty element
		//
		if( s.isEmpty() )
		{
			System.err.println( "Warning -- " + this + ": " + "no data" );
			return this;
		}
		
		// We have data.  Now see if it has components to parse
		//
		if( compDataElemStateMachine == null )
		{
			System.err.println( "Warning -- No component data element states for data element " + 
								s + " and state " + this );
			rootElem.appendChild(s);
			return this;
		}
		
		
		int begPos = 0, endPos = -1, index = 0;
		try
		{
			while( true )
			{
				endPos = s.indexOf( COMP_DATA_ELEM_SEPARATOR.toString(), begPos );
				compDataElemStateMachine[ index ].
						actionRoutine( s.substring( begPos, endPos == -1 ? s.length() : endPos ));
				rootElem.appendChild( compDataElemStateMachine[ index++ ].getRootElem() );
				
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
			System.err.println( this + ": Ran out of states at index = " + --index );
			return this;
		}

		return this;
	}

	@Override
	public Element getRootElem() 
	{
		return rootElem;
	}
	
	protected Element rootElem = null;

}


