/**
 * Models the syntactical elements
 */
package converter;

/**
 * @author qman
 *
 */
enum Syntax 
{
	COMP_DATA_ELEM_SEPARATOR( ":" ),
	DATA_ELEM_SEPARATOR( "+" ), 
	DECIMAL_MARK( "," ),
	ESCAPE_CHAR( "?" ),
	DUMMY_SPACE( null ),
	SEGMENT_TERMINATOR( "'\n" );
	
	public String toString()
	{
		return syntacticalElement;
	}
	
	private Syntax( String def )
	{
		syntacticalElement = def;
	}
	
	void setSyntax( String elem )
	{
		syntacticalElement = elem;
	}
	
	void setSyntax( Character c )
	{
		syntacticalElement = c.toString();
	}
	
	private String syntacticalElement;
}
