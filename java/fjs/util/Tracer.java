package fjs.util;
import java.util.Collection;
/**
Utility superclass that can issue trace messages.
 */
public abstract class Tracer implements Identified{
	public static int ids;
	public static class TracerTopped extends Tracer{
		private final String top;
		public TracerTopped(String top){
			super(top);
			this.top=top;
		}
		@Override
		protected void traceOutput(String msg){
			if(doTrace())super.traceOutput(msg);
		}
		protected boolean doTrace(){
			return true;
		}
	}
	private final String top;
	private Integer id=++ids;
	@Override
	public Object identity(){
		return id;
	}
	public static Tracer newTopped(final String top,final boolean live){
		return new TracerTopped(top);
	}
	public Tracer(String top){
		this.top=top;
	}
	public Tracer(){
		top=null;
	}
	final public void trace(String msg,Object o){
		traceOutput(msg+Debug.info(o));		
	}
	/**
	Outputs complete trace messages to console or elsewhere. 
	<p>Default prepends helpful classname to message.  
	@param msg passed from one of the <code>public</code> methods
	 */
	protected void traceOutput(String msg){
		traceOutputWithId(""+msg);
	}
	final public void traceOutputWithId(String msg){
		Util.printOut((top!=null?(top+" #"+id):Debug.info(this))+" "+msg);
	}
	final public void trace(String msg){
		traceOutput(msg);
	}
	final public void trace(String msg,Throwable t,boolean stack){
		if(stack&=t!=null){
			traceOutput(msg);		
			t.printStackTrace();
		}
		else traceOutput(msg+Debug.info(t));		
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
	private String traceArrayText(Object[]array){
		return Util.arrayPrintString(array);
	}
}