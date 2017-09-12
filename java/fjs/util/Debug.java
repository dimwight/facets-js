package Facets.util;
/**
Utilities for use during development. 
 */
public final class Debug{
	/**
		Returns basic Debug.information about an object's type and identity. 
		<p>This will be some combination of
	<ul>
		<li>the non-trivial simple class name
		<li>{@link Titled#title()} if available
		</ul>
			 */
	public static String info(Object o){
		if(o==null)return "null";
		if(o instanceof Integer)return String.valueOf(o);
		else if(o instanceof String){
			String text=(String)o;
			int length=text.length();
			return text.substring(0,Math.min(length,60))
					+(true?"":(": "+("length="+length)));
		}
		return (o.getClass().getSimpleName())+
				(o instanceof Identified?(" #"+((Identified)o).identity()):"")+
			(o instanceof Titled?(" "+((Titled)o).title()):"");
	}
	/**
	Returns an array of <code>Debug.info</code>s. 
	 */
	public static String arrayInfo(Object[]array){
		return "arrayInfo";
	}
	public static boolean trace=false;
	public static void traceEvent(String string){
		Util.printOut(">>"+string);
	}
	public static String toStringWithHeader(Object[]array){
		return info(array)+" ["+array.length+"] " +Objects.toLines(array);
	}
}
