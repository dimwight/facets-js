package fjs.core;
import fjs.util.Debug;
import fjs.util.Tracer;
/**
{@link STarget} representing a textual value. 
<p> {@link STextual} represents a text value to be 
  exposed to user view and control in the surface; application-specific 
  mechanism can be defined in a {@link fjs.core.STextual.Coupler}. 
 */
final public class STextual extends TargetCore{
	/**Connects a {@link STextual} to the application. 
	<p>A {@link Coupler} is required to supply a {@link STextual}
	with client-specific mechanism.
	 */
	public static class Coupler extends Tracer implements TargetCoupler{
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
		/**
		Can the {@link STextual} accept interim updates? 
		<p>Default returns <code>false</code>.
		 */
		public boolean updateInterim(STextual t){
			return false;
		}
		protected String getText(STextual t){
			throw new RuntimeException("Not implemented in "+Debug.info(this));
		}
	}
	public final Coupler coupler;
	private String text;
	/**
	Core constructor. 
	@param title passed to superclass
	@param coupler can supply application-specific mechanism and policy;
	must be non-<code>null</code>
	 */
	public STextual(String title,Coupler coupler){
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
	 <p>Also calls {@link STextual.Coupler#textSet(STextual)} if not initialising. 
	 @param text must be non-<code>null</code>; and non-blank 
	 unless {@link STextual.Coupler#isValidText(STextual, String)} 
	 returns <code>true</code>.
	 */
	public void setText(String text){
		if(text==null||!coupler.isValidText(this,text))
				throw new IllegalArgumentException("Null or invalid text in "+Debug.info(this));
		boolean firstSet=this.text==null;
		this.text=text;
		if(!firstSet)coupler.textSet(this);
	}
	/**
	The text value represented. 
	 */
	public String text(){
		if(text!=null)return text;
		else text=coupler.getText(this);
		if(text==null||!coupler.isValidText(this,text))
			throw new IllegalStateException("Null or invalid text in "+Debug.info(this));
		else return text;
	}
	@Override
	public Object state(){
		return text();
	}
	@Override
	public void updateState(Object update){
		if(false)trace(" > Updating state: update=",update);
		setText((String)update);
	}
	public String toString(){
		return super.toString()+(true?"":" "+text);
	}
}
