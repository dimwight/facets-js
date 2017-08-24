package fjs.core;
import java.util.Arrays;
import java.util.HashSet;
import fjs.util.Debug;
import fjs.util.Titled;
/**
{@link STarget} representing one or more indices into a list of items. 
<p>{@link SIndexing} represents a list of items to be exposed 
  to user view and control in the surface, together with an index/indices 
  into that list; application-specific mechanism and policy can be defined in 
	a {@link fjs.core.SIndexing.Coupler}. 
 */
final public class SIndexing extends TargetCore{
	/**Allows an indexing to be functionally empty. */
	public static final Object[]NO_INDEXABLES={"Not indexable"};
	private static final String NOT_INDEXED="Not indexed";
	/**
	Connects an {@link SIndexing} to the application. 
	<p>A {@link Coupler} supplies client-specific mechanism and 
	  policy for an {@link SIndexing}. 
	 */
	public static class Coupler implements TargetCoupler{
		/**
		Returns strings to represent the indexables.  
		<p>These will appear in facet lists, or as labels for menu items or 
		radio buttons. 
		<p>The default returns titles based on the type of the {@link SIndexing}'s 
		<code>indexables</code>:
		<ul>
		<li>if they are {@link String}s, returns them</li> 
		<li>if they are {@link fjs.util.Titled}s, returns their <code>title</code>  properties</li> 
		<li>otherwise, returns their <code>toString</code> properties</li> 
		<li>if they the {@link SIndexing#NO_INDEXABLES} constant, returns an 
		empty {@link String}[].
		</ul> 		 
		 */
		public String[]newIndexableTitles(SIndexing i){
		  Object[]indexables=i.indexables();
			if(indexables==NO_INDEXABLES)return new String[]{};
			else if(indexables instanceof String[])return(String[])indexables;
		  String[]titles=new String[indexables.length];
		  boolean titled=indexables instanceof Titled[];
		  for(int t=0;t<titles.length;t++)
		    titles[t]=titled?((Titled)indexables[t]).title():
		      indexables[t].toString();
		  return titles;
		}
		/**
		Called whenever an index changes. 
		 */
		public void indexSet(SIndexing i){}
		/**
		Can return dynamic indexables. 
		<p>Called by {@link SIndexing#indexables()} if none set during construction. 
		@return a non-empty array
		 */
		public Object[]getIndexables(){
			throw new RuntimeException("Not implemented in "+Debug.info(this));
		}
	}
	/**
	The <code>Indexing.Coupler</code> passed to the core constructor. 
	 */
	public final Coupler coupler;	
	private int[]indices=null;
	private transient Object[]indexables,indexings;  
	/**
	Core constructor. 
	<p>Declared visibly for documentation purposes only. 
	 @param title passed to superclass
	 @param indexables the objects to be indexed; may not be empty but 
	 may be <code>null</code> in which case they are 
	 read dynamically from the coupler
	 @param coupler supplies application-specific mechanism and policy
	 */
	protected SIndexing(String title,Object[]indexables,Coupler coupler){
		super(title);this.coupler=coupler;
		if(indexables!=null&&indexables.length==0)
			throw new IllegalArgumentException("Null or empty indexables in "+Debug.info(this));
		this.indexables=indexables;
	}
	/**
	Convenience constructor setting initial index. 
	 @param index the initial index
	 */
	public SIndexing(String title,Object[]indexables,int index,Coupler coupler){
		this(title,indexables,coupler);
		setIndex(index);
	}
	/**
	The first index into the <code>indexables</code>. 
		 */
	public int index(){
		if(indices==null||indices.length==0)throw new IllegalStateException(
				"Null or empty indices in "+Debug.info(this));
		return indices[0];
	}
  /**
	The indices last set into the <code>indexables</code>. 
		 */
	public int[]indices(){
		if(indices==null)throw new IllegalStateException(
				"Null indices in "+Debug.info(this));
		return indices;
	}
	/**
	The items exposed to indexing. 
	<p>These will either have been set during construction or be read 
	dynamically from the coupler. 
		 */
	public Object[]indexables(){
		Object[]indexables=this.indexables!=null?this.indexables
				:coupler.getIndexables();
	  if(indexables==null)throw new IllegalStateException(
	  		"Null indexables in "+this);
	  if(indexables.length==0)return NO_INDEXABLES;
	  if(new HashSet(Arrays.asList(indexables)).size()!=indexables.length)
	  	throw new IllegalStateException("Duplicate indexables in "+Debug.info(this));
	  else return indexables;
	}
	/**
	Sets a single index into the <code>indexables</code>. 
		 */
	public void setIndex(int index){
		setIndices(new int[]{index});
	}
	/**
	Sets indices into the <code>indexables</code>. 
	@param indices may be empty but may not be <code>null</code>
		 */
	public void setIndices(int[]indices){
		if(indices==null)throw new IllegalArgumentException(
				"Null indices in "+Debug.info(this));
	  boolean first=this.indices==null;
	  for(int i=0;indices.length>0&&i<indices.length;i++)
	  	if(indices[i]<0)throw new IllegalArgumentException(
	  			"Bad index in "+Debug.info(this));
	  this.indices=indices;
	  if(indexings!=null){
	    Object[]indexables=indexables(),old=indexings;
	    indexings=new Object[old.length];indexings[0]=indexables[indices[0]];
	    for(int i=0,indexion=1;i<old.length;i++)
	      if(old[i]!=indexings[0])indexings[indexion++]=old[i];
	  }
	  if(!first)coupler.indexSet(this);
	}
	/**
	The item denoted by the current <code>index</code>. 
		 */
	public Object indexed(){
	  if(indices==null)throw new IllegalStateException("No index in "+Debug.info(this));
	  return indices.length==0?NOT_INDEXED:indexables()[index()];
	}
	/**
	The <code>indexables</code> ordered by most recently indexed. 
		 */
	public Object[]indexings(){
	  if(indexings==null)throw new IllegalStateException("Null indexings in "+this);
	  return indexings;
	}
	public String toString(){
		return super.toString()+" "+indices().length;
	}
}