package fjs.core;
import fjs.util.Debug;
import fjs.util.Tracer;
/**
Core implementation of key interfaces. 
<p>{@link NotifyingCore} is the shared superclass of both the {@link STarget} and
{@link STargeter} class hierarchies. 
<p>Declared <code>public</code> for documentation purposes only; client code should 
use the concrete subclass hierarchies. 
 */
abstract class NotifyingCore extends Tracer implements Notifying{
	transient Notifiable notifiable;
	private static int identities;
	private final int identity=identities++;
	@Override
	public NotifyingImpact impact(){
		return NotifyingImpact.DEFAULT;
	}
	@Override
	protected void traceOutput(String msg){
		super.traceOutput(getClass().getSimpleName()+msg);
	}
	@Override
  final public Notifiable notifiable(){
  	if(false&&notifiable==null)throw new IllegalStateException("No monitor in "+Debug.info(this));
  	return notifiable;
  }
	@Override
	public void notify(Notice notice){
		if(Debug.trace)Debug.traceEvent("Notified in "+this+" with "+notice);
		if(notifiable==null)return;
		if(!blockNotification())notifiable.notify(notice.addSource(this));
		else if(Debug.trace)Debug.traceEvent("Notification blocked in "+this);
	}
	@Override
  final public void notifyParent(NotifyingImpact impact){
    if(notifiable==null)return;
    notifiable.notify(new Notice(this,impact));
  }
  /**
  Enables notification to be restricted to this member of the tree. 
  <p>Checked by {@link #notify(Notice)}; default returns <code>false</code>.
   */
  protected boolean blockNotification(){return false;}
	@Override
	public final void setNotifiable(Notifiable n){
  	if(false)trace(".setNotifiable: n=",n);
		this.notifiable=n;
	}
	@Override
  public String toString(){return Debug.info(this);}
}
