/**
 *
 */
package converter;

import nu.xom.Element;

/**
 * @author qman
 *
 */
interface EdifactStates
{

    public EdifactStates actionRoutine( String s );
    public Element getRootElem();
}
