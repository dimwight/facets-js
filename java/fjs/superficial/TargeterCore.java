package fjs.superficial;
import java.util.ArrayList;
import java.util.List;
import fjs.util.Debug;
import fjs.util.Util;
/**
Implements {@link STargeter}. 
<p>{@link TargeterCore} is a public implementation of {@link STargeter} 
  to provide for extension in other packages; instances are generally 
  created by an implementation of {@link fjs.superficial.TargetCore#newTargeter()}. 
*/
public class TargeterCore extends NotifyingCore implements STargeter{
	public static List<STargeter>targeters=new ArrayList();
	private transient STargeter[]elements;
	private transient ArrayList<SFacet>facets=new ArrayList();
	private transient STarget target;  
  private final Class targetType;
  /**
	Construct a {@link TargeterCore} to match <code>target</code>. 
	@param targetType set as {@link #targetType}.
	   */
	public TargeterCore(Class targetType){
		this.targetType=targetType;;
	  targeters.add(this);
	  if(Debug.trace)Debug.traceEvent("Created " +//"targeter " +targeters+" "+
				this);
	}
	public void retarget(STarget target,NotifyingImpact impact){
	  if(target==null)throw new IllegalArgumentException(
	  		"Null target in "+Debug.info(this));
	  if(false)trace(".retarget: Retargeting "+Debug.info(this)+" on "+Debug.info(target));
	  this.target=target;
		STarget[]targets=target.elements();
		if(targets==null)throw new IllegalStateException("No targets in "+Debug.info(this));
		if(elements==null){
	    elements=new STargeter[targets.length];
			for(int i=0;i<elements.length;i++){
				elements[i]=((TargetCore)targets[i]).newTargeter();
				elements[i].setNotifiable(this);
			}
	  }
		if(targets.length==elements.length)
			for(int i=0;i<elements.length;i++)elements[i].retarget(targets[i],impact);			
		if(((TargetCore)target).notifiesTargeter())target.setNotifiable(this);
	}
	final public void attachFacet(SFacet facet){
		if(facet==null)throw new IllegalArgumentException("Null facet in "+Debug.info(this));
		if(!facets.contains(facet))facets.add(facet);
		if(Debug.trace)Debug.traceEvent("Attached facet "+Debug.info(facet)+" to "+Debug.info(this));
	}
	public void retargetFacets(NotifyingImpact impact){
		SFacet[]facets=this.facets.toArray(new SFacet[]{});
		for(int i=0;i<elements.length;i++)elements[i].retargetFacets(impact);
		if(facets==null)return;
	  for(int i=0;i<facets.length;i++){
			facets[i].retarget(target,impact);
			if(Debug.trace)Debug.traceEvent("Retargeted facet " +Debug.info(facets[i])+" in "+this);
		}
	}
	final public STargeter[]elements(){
	  if(elements==null)throw new IllegalStateException("No elements in "+Debug.info(this));
	  return elements;
	}
	final public STarget target(){
	  if(target==null)throw new IllegalStateException("No target in "+Debug.info(this));
	  return target;
	}
	final public String title(){
		return target==null?"Untargeted":target.title();
	}
	final public String toString(){
		String targetInfo=target==null?"":Debug.info(target);
		return Debug.info(this)+(true?"":" ["+targetInfo+"]");
	}
	@Override
	public NotifyingType type(){
		return NotifyingType.Targeter;
	}
}
