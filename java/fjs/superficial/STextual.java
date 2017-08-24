package fjs.superficial;
import fjs.util.Debug;
/**
{@link STarget} representing a textual value. 
<p> {@link STextual} represents a text value to be 
  exposed to user view and control in the surface; application-specific 
  mechanism can be defined in a {@link fjs.superficial.TextualCoupler}. 
 */
final public class STextual extends TargetCore{
	public final TextualCoupler coupler;
	private String text;
	/**
	Core constructor. 
	@param title passed to superclass
	@param coupler can supply application-specific mechanism and policy;
	must be non-<code>null</code>
	 */
	public STextual(String title,TextualCoupler coupler){
		super(title);
		if((this.coupler=coupler)==null)throw new IllegalArgumentException(
				"Null coupler in "+Debug.info(this));
	}
	@Override
	protected boolean notifiesTargeter(){
		return true;
	}
	/**
	 Sets the text value to be exposed. 
	 <p>Also calls {@link TextualCoupler#textSet(STextual)} if not initialising. 
	 @param text must be non-<code>null</code>; and non-blank 
	 unless {@link TextualCoupler#isValidText(STextual, String)} 
	 returns <code>true</code>.
	 */
	public void setText(String text){
		if(text!=null&&!coupler.isValidText(this,text))
				throw new IllegalArgumentException("Invalid text in "+Debug.info(this));
		boolean firstSet=this.text==null;
		this.text=text;
		if(!firstSet)coupler.textSet(this);
	}
	/**
	The text value represented. 
	 */
	public String text(){
		if(this.text!=null)return this.text;
		String text=coupler.getText(title());
		if(text==null&&!coupler.isValidText(this,text))
			throw new IllegalStateException("Null or invalid text in "+Debug.info(this));
		return text;
	}
	public String toString(){return super.toString()+(true?"":" "+text);}
}
