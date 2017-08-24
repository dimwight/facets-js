package fjs.superficial;
/**
  Allows a {@link Notifying} to refine its notification.
  <p>The members of {@link NotifyingImpact} can define the scope of the event 
  triggering a notification;   
  each member guarantees that its impact will be less than any member
  with higher {@link #ordinal()} 
<ul>
	<li>{@link #MINI} no state change; typically a status message  
	<li>{@link #ACTIVE} active state eg of viewers may have changed
	<li>{@link #SELECTION} content selection state may have changed
	<li>{@link #CONTENT} content state may have changed
	<li>{@link #DEFAULT} any application element or state may have changed
	</ul> 
	 */
  public enum NotifyingImpact{
  	MINI,
  	ACTIVE,
  	SELECTION,
  	CONTENT,
  	DEFAULT,
  	DISPOSE;
		public boolean exceeds(NotifyingImpact other){
			return ordinal()>other.ordinal();
		}
	}