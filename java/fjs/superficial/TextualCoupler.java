package fjs.superficial;
import fjs.util.Debug;
import fjs.util.Tracer;
/**
Connects a {@link STextual} to the application. 
<p>A {@link TextualCoupler} is required to supply a {@link STextual}
with client-specific mechanism.
 */
public class TextualCoupler extends Tracer implements TargetCoupler{
	/**
	Called when <code>setText</code> is called on <code>t</code>. 
	 */
	public void textSet(STextual t){}
	/**
	Is this text valid for the {@link STextual}? 
	<p>Default returns <code>true</code> for non-blank text.
	 */
	public boolean isValidText(STextual t,String text){
		return true||!text.trim().equals("");
	}
	protected String getText(String title){
		throw new RuntimeException("Not implemented in "+Debug.info(this));
	}
}