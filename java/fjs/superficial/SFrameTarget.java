package fjs.superficial;
import fjs.util.Debug;
import fjs.util.Titled;
/**
Extends {@link TargetCore} by framing 
  application content to be exposed directly to facets. 
 */
public class SFrameTarget extends TargetCore{
	private static int frames;
	/**Immutable framed framed by the {@link SFrameTarget}.*/
	final public Object framed;
	/**
 	Core constructor. 
  <p>Note that this passes no child target elements to the superclass; 
    elements can only be set by subclassing and  
  <ul>
    <li>in named subclasses where the elements are known at 
      construction, calling {@link #setElements(STarget[])} 
      from the constructor 
    <li>in other cases (in practice the large majority), overriding 
      {@link #lazyElements()} from {@link TargetCore} 
  </ul>
  <p>This limitation ensures that the effective type of 
    a {@link SFrameTarget} with child elements can be distinguished 
    by reference to the compiled type. Care must therefore be 
    taken in applications not vary the effective type of the 
    elements created by a subclass. 
  @param title passed to the superclass 
  @param toFrame must not be <code>null</code>
	 */
	public SFrameTarget(String title,Object toFrame){
	  super(title);
		if((framed=toFrame)==null)throw new IllegalArgumentException(
				"Null framed in "+Debug.info(this));
	}
  protected final boolean notifiesTargeter(){
		return true;
	}
	public String title(){
		return framed==null||!(framed instanceof Titled)?super.title()
			:((Titled)framed).title();
	}
}