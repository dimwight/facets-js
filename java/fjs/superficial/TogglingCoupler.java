package fjs.superficial;
/**
Connects a {@link SToggling} to the application. 
<p>A {@link TogglingCoupler} supplies application-specific mechanism 
for a {@link SToggling}.
 */
public class TogglingCoupler implements TargetCoupler{
	/**
	Called by the toggling whenever its state is set. 
 */
	public void stateSet(SToggling t){}
}