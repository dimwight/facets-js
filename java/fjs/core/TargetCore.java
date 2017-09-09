package fjs.core;
import fjs.util.Debug;
/**
 Implements {@link STarget}. 
 <p>{@link TargetCore} is the core implementation of {@link STarget}, 
 extended both in this and other packages. It also serves as 
 a means of grouping other simple targets. 
 */
public class TargetCore extends NotifyingCore implements STarget{
	public static int targets;
	private final String title;
	private STarget[]elements;
	private boolean live=true;
	/**
	 Convenience constructor that sets no child elements. 
	 */
	public TargetCore(String title){
		this(title,(STarget[])null);
	}
	/**
	 Core constructor. 
	 @param title should be suitable for return as the (immutable)
	 <code>title</code> property
	 @param elements may be <code>null</code> (in which case 
	 suitable elements may be created using <code>lazyElements</code>); otherwise
	 passed to {@link #setElements(STarget[])}
	 */
	public TargetCore(String title,STarget...elements){
		this.title=title;
		if(elements!=null)setElements(elements);
		if(title==null||title.equals(""))
			throw new IllegalArgumentException("Null or empty title in "+Debug.info(this));
		if(Debug.trace)Debug.traceEvent("Created "+Debug.info(this));
		targets++;
	}
	/**
	 Sets the {@link STarget} children of the {@link TargetCore}. 
	 <p>Intended for use in specialised subclass construction; 
	 elements set are thereafter immutable. 
	 @param elements (which may not be <code>null</code> nor contain <code>null</code>
	 members) will be returned as the <code>elements</code> property. 
	 */
	final protected void setElements(STarget[]elements){
		if(this.elements!=null)throw new RuntimeException("Immutable elements in "
				+Debug.info(this));
		this.elements=elements;
		if((this.elements=elements)==null)
			throw new IllegalArgumentException("Null elements in "+Debug.info(this));
		else for(int i=0;i<elements.length;i++)
			if(elements[i]==null)
				throw new IllegalArgumentException("Null element "+i+" in "+Debug.info(this));
	}
	/**
	 Implements interface method. 
	 <p>If no elements have been set, attempts to create them with 
	 <code>lazyElements</code>. 
	 <p>Each call to this method also sets the {@link TargetCore} 
	 as notification monitor of any element that is not a {@link 
	 fjs.core.SFrameTarget}. 
	  
	 */
	final public STarget[]elements(){
		if(elements==null)setElements(lazyElements());
		if(elements==null)throw new IllegalStateException("No elements in "+Debug.info(this));
		for(int i=0;i<elements.length;i++)
			if(!(elements[i].type()==NotifyingType.Frame))elements[i].setNotifiable(this);
		return elements;
	}
	/**
	 Lazily creates <code>element</code>s for this target.  
	 <p>Called at most once from {@link #elements()}. 
	 <p>Though defined in {@link TargetCore} this method is primarily for use by
	 {@link SFrameTarget}s, which always create their elements dynamically 
	 by reimplementing this method.
	 Default implementation returns an empty {@link STarget}[]. 
	 */
	protected STarget[]lazyElements(){
		return new STarget[]{};
	}
	/**
	 Create and return a targeter suitable for retargeting to 
	 this target. 
	 <p>This is the key method used by Facets to implement dynamic 
	 creation of a surface targeter tree. During initial retargeting 
	 each {@link TargeterCore} queries its <code>target</code> 
	 for any child elements, and calls this method on each child 
	 to obtain suitable {@link STargeter} instances which 
	 it then adds to its elements. 
	 <p>This method may be also called on subsequent retargetings 
	 where the specific type of a target is subject 
	 to change (for instance when it represents a selection). 
	 Either the {@link STargeter} returned can be matched 
	 to an existing one to which facet have already been attached, 
	 or such facet can be attached and the surface layout adjusted 
	 accordingly. 
	 
	 */
	public STargeter newTargeter(){
		return new TargeterCore(getClass());
	}
	public boolean isLive(){
		Notifiable n=notifiable();
		boolean notifiesTarget=n!=null&&n instanceof STarget;
		return !notifiesTarget?live:live&&((STarget)n).isLive();
	}
	public void setLive(boolean live){
		this.live=live;
	}
	public String title(){
		return title;
	}
	public String toString(){
		return Debug.info(this);
	}
	/**
	Used to construct the notification tree. 
	<p><b>NOTE</b> This method must NOT be overridden in application code. 
	 */
	protected boolean notifiesTargeter(){
		return true&&elements!=null;
	}
	@Override
	public NotifyingType type(){
		return NotifyingType.Target;
	}
	@Override
	public Object state(){
		throw new RuntimeException("Not implemented in "+this);
	}
	@Override
	public void updateState(Object update){
		throw new RuntimeException("Not implemented in "+this);
	}
}
