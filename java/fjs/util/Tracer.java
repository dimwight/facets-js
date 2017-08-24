package fjs.util;
import java.util.Collection;
/**
Utility superclass that can issue trace messages.
 */
public abstract class Tracer implements Identified{
	public static final class TracerTopped extends Tracer{
		private final boolean live;
		private final String top;
		public TracerTopped(boolean live,String top){
			super(top);
			this.live=live;
			this.top=top;
		}
		@Override
		protected void traceOutput(String msg){
			if(live)Util.printOut(top+msg);
		}
	}
	private final String top;
	private static int ids;
	private Integer id=++ids;
	@Override
	public Object identity(){
		return id;
	}
	public static Tracer newTopped(final String top,final boolean live){
		return new TracerTopped(live,top);
	}
	public Tracer(String top){
		this.top=top;
	}
	public Tracer(){
		this.top=Debug.info(this);
	}
	/**
	Outputs complete trace messages to console or elsewhere. 
	<p>Default prepends helpful classname to message.  
	@param msg passed from one of the <code>public</code> methods
	 */
	protected void traceOutput(String msg){
		traceOutputWithId(msg);
	}
	final public void traceOutputWithId(String msg){
		Util.printOut(top+" #"+id+" "+msg);
	}
	final public void trace(String msg){
		traceOutput(msg);
	}
	final public void trace(String msg,Throwable t,boolean stack){
		if(stack&=t!=null){
			traceOutput(msg);		
			t.printStackTrace();
		}
		else traceOutput(msg+traceObjectText(t));		
	}
	final public void trace(String msg,Object o){
		traceOutput(msg+traceObjectText(o));		
	}
	final public void trace(String msg,Collection c){
		traceOutput(msg+traceArrayText(c.toArray()));		
	}
	final public void trace(String msg,Object[]array){
		traceOutput(msg+traceArrayText(array));		
	}
	final public void traceDebug(String msg,Object o){
		traceOutput(msg+Debug.info(o));
	}
	final public void traceDebug(String msg,Object[]array){
		traceOutput(msg+(false?Debug.info(array):Debug.arrayInfo(array)));		
	}
	private String traceObjectText(Object o){
		return Debug.info(o);
	}
	private String traceArrayText(Object[]array){
		return Util.arrayPrintString(array);
	}
}