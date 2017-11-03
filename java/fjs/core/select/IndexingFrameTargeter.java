package fjs.core.select;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import fjs.core.SFrameTarget;
import fjs.core.SIndexing;
import fjs.core.STarget;
import fjs.core.STargeter;
import fjs.core.TargetCore;
import fjs.core.TargeterCore;
import fjs.util.Debug;
/**
Targeter for an {@link IndexingFrame}.   
<p>The {@link #indexing()} method provides a reference to the 
{@link SIndexing} wrapped by the target. 
 */
public class IndexingFrameTargeter extends TargeterCore{
  final private Map<String,STargeter>cache=new HashMap();
	private STargeter indexing;
	/**
	Unique constructor. 
	@param indexed will be the first target of this instance
	 */
	public IndexingFrameTargeter(IndexingFrame indexed){
		super(indexed.getClass());
	}
  /**
  Retargeted to the {@link SIndexing} wrapped by the current target. 
   */
  public final STargeter indexing(){
    if(indexing==null)throw new IllegalStateException(
    		"No indexingLink in "+Debug.info(this));
    else return indexing;
  }
	/**
	Overrides superclass method. 
	 */
	public void retarget(STarget target){
		IndexingFrame frame=(IndexingFrame)target;
		SIndexing ix=frame.indexing();
		SFrameTarget indexedFrame=frame.indexedFrame();
		String indexedTitle=indexedFrame.title();
	  super.retarget(frame);
		if(false)trace(".retarget: indexedFrame=",indexedTitle);
		if(indexing==null){
	  	indexing=ix.newTargeter();
	  	indexing.setNotifiable(this);
	  }
		STargeter indexed=cache.get(indexedTitle);
		if(indexed==null){
	  	indexed=indexedFrame.newTargeter();
	  	cache.put(indexedTitle,indexed);
	  	indexed.setNotifiable(this);
		}
	  indexing.retarget(ix);
	  indexed.retarget(indexedFrame);
	}
  /**
  Overrides superclass method. 
   */
  public void retargetFacets(){
    super.retargetFacets();
    indexing.retargetFacets();
    for(STargeter t:cache.values())t.retargetFacets();
  }
	public STargeter[]titleElements(){
  	ArrayList<STargeter>list=new ArrayList<STargeter>(Arrays.asList(elements));
		list.add(indexing);
		for(STargeter t:cache.values())list.add(t);
		return list.toArray(new STargeter[]{});
	}
}