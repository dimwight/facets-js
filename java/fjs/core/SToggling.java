package fjs.core;

/**
{@link STarget} representing a Boolean value. 
<p>{@link SToggling} represents a Boolean value to 
  be exposed to user view and control in the surface; application-specific 
  mechanism can be defined in a {@link fjs.core.TogglingCoupler}. 
 */
final public class SToggling extends TargetCore{
	public final TogglingCoupler coupler;
	private boolean state;
	/**
	Unique constructor. 
	@param title passed to superclass
	@param state initial state of the toggling
	@param coupler can supply application-specific mechanism
	 */
	public SToggling(String title,boolean state,TogglingCoupler coupler){
		super(title);
		this.state=state;
		this.coupler=coupler;
	}
	/**
	The Boolean state of the toggling. 
	 <p>The value returned will that set using 
	 <code>setState</code> or during construction.    
		 */
	public boolean isSet(){
		return state;
	}
	/**
	Sets the Boolean state. 
	<p> Subsequently calls {@link fjs.core.TogglingCoupler#stateSet(SToggling)}.
	*/
	public void set(boolean state){
		this.state=state;
		coupler.stateSet(this);
	}
	public String toString(){
		return super.toString()+(false?"":" "+state);
	}
}