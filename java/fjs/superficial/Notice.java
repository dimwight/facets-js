package fjs.superficial;
import java.util.ArrayList;
import java.util.List;
import fjs.util.Debug;
/**
Object to be passed by {@link Notifying} to {@link Notifiable}.
 */
public class Notice{
	public final NotifyingImpact impact;
	public final List<Notifying>sources=new ArrayList();
	public Notice(Notifying source,NotifyingImpact impact){
		this.impact=impact;
		addSource(source);
	}
	final public Notice addSource(Notifying source){
		if(source==null)throw new IllegalArgumentException(
				"Null source in "+Debug.info(this));
		sources.add(source);
		return this;
	}
	public String toString(){
		return Debug.info(this)+" "+impact.name();
	}
}
