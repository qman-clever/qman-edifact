package converter;

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * @author qman
 *
 */
enum ComponentDataStates implements EdifactStates
{
	// *************** Associated with UNB ***************
	//
	IDENTIFIER, VERSION_NUMBER,
	SENDER_IDENTIFICATION, PARTNER_IDENTIFICATION, REVERSE_ROUTING_ADDRESS,
	RECIPIENT_IDENTIFICATION, ROUTING_ADDRESS,
	DATE, TIME,
	PASSWORD, PASSWORD_QUALIFIER,
	
	// *************** Associated with UNH ***************
	//
	MESSAGE_TYPE, MESSAGE_VERSION_NUMBER, MESSAGE_RELEASE_NUMBER, CONTROLLING_AGENCY, ASSOCIATION_ASSIGNED,
	SEQUENCE_OF_TRANSFERS, FIRST_AND_LAST_TRANSFER,
	
	// *************** Associated with BGM ***************
	//
	MESSAGE_NAME_CODED, CODE_LIST_QUALIFIER /* Also NAD, LIN, LOC */, 
	CODE_LIST_RESPONSIBLE_AGENCY_CODED /* Also NAD, LIN, LOC */, MESSAGE_NAME,
	MESSAGE_NUMBER, VERSION, REVISION_NUMBER,
	
	// *************** Associated with DTM ***************
	//
	DATE_TIME_PERIOD_QUALIFIER, DATE_TIME_PERIOD, DATE_TIME_PERIOD_FORMAT_QUALIFIER,

	
	// *************** Associated with NAD ***************
	//
	PARTY_ID_IDENTIFICATION, NAME_AND_ADDRESS_LINE, PARTY_NAME, PARTY_NAME_FORMAT_CODED, 
	STREET_AND_NUMBER,
	
	// *************** Associated with LIN ***************
	//
	ITEM_NUMBER, ITEM_NUMBER_TYPE_CODED, 
	SUB_LINE_INDICATOR_CODED, LINE_ITEM_NUMBER,
	
	// *************** Associated with QTY ***************
	//
	QUANTITY_QUALIFIER, QUANTITY, MEASURE_UNIT_QUALIFIER /* Also PRI */,
	
	// *************** Associated with LOC ***************
	//
	PLACE_LOCATION_IDENTIFICATION, /* CODE_LIST_QUALIFIER, CODE_LIST_RESPONSIBLE_AGENCY_CODED */ PLACE_LOCATION,
	RELATED_PLACE_LOCATION_ONE_IDENTIFICATION, /* CODE_LIST_QUALIFIER, CODE_LIST_RESPONSIBLE_AGENCY_CODED */ RELATED_PLACE_LOCATION_ONE,
	RELATED_PLACE_LOCATION_TWO_IDENTIFICATION, /* CODE_LIST_QUALIFIER, CODE_LIST_RESPONSIBLE_AGENCY_CODED */ RELATED_PLACE_LOCATION_TWO,
	
	// *************** Associated with PRI ***************
	//
	PRICE_QUALIFIER, PRICE, PRICE_TYPE_CODED, PRICE_TYPE_QUALIFIER, UNIT_PRICE_BASIS /* MEASURE_UNIT_QUALIFIER */
	
	;
	@Override
	public EdifactStates actionRoutine( String s )
	{
		// First create data element
		//
		rootElem = new Element( "component_data_element" );
		rootElem.addAttribute( new Attribute( "name", 
									this.toString().toLowerCase()) );
		
		
		if( s.isEmpty() )
		{
			System.err.println( "Warning -- building empty component data" 
					+ " element for state " + this );
		}
		else
		{
			rootElem.appendChild( s );
		}
		
		return this;
	}

	@Override
	public Element getRootElem() 
	{
		return rootElem;
	}
	
	private Element rootElem = null;
}