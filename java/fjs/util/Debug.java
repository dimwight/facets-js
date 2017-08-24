package fjs.util;
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
		if(o instanceof String){
			String text=(String)o;
			int length=text.length();
			return text.substring(0,Math.min(length,60))
					+(": "+("length="+length));
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
	public static void traceEvent(String string){
		Util.printOut(">"+string);
	}
	public static boolean trace;
	static Class getMemberType(Object[]array){
		String fullName=array.getClass().getName(),
			trimSemiColon=fullName.substring(0,fullName.length()-1),
			trimDimensions=trimSemiColon.substring(fullName.indexOf("L")+1,trimSemiColon.length());
		Class useClass=null;
		try{
			useClass=Class.forName(trimDimensions);
		}
		catch(ClassNotFoundException e){
			throw new RuntimeException(e+" for "+info(trimDimensions));
		}
		return useClass;
	}
	public static String toStringWithHeader(Object[]array){
		return info(array)+" ["+array.length+"] " +
				Objects.toLines(array);
	}
}
