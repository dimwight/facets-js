package fjs.core.select;

import java.util.ArrayList;
import java.util.Arrays;
import fjs.core.SFrameTarget;
import fjs.core.SIndexing;
import fjs.core.STarget;
import fjs.core.STargeter;
import fjs.core.TargeterCore;
import fjs.util.Debug;

/**
Targeter for an {@link IndexingFrame}.   
<p>The {@link #indexing()} method provides a reference to the 
{@link SIndexing} wrapped by the target. 
 */
public class IndexingFrameTargeter extends TargeterCore{
	private STargeter indexing,selection;
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
  	SFrameTarget selectionFrame=frame.selectionFrame();
  	if(false)traceDebug(".retarget: selectionFrame=",selectionFrame);
    super.retarget(frame);
    if(indexing==null){
    	ArrayList<STargeter>list=new ArrayList<STargeter>();
    	for(STargeter e:elements)list.add(e);
    	indexing=ix.newTargeter();
			list.add(indexing);
			selection=selectionFrame.newTargeter();
			list.add(selection);
	  	indexing.setNotifiable(this);
	  	selection.setNotifiable(this);
	  	elements=list.toArray(new STargeter[]{});
	  	if(false)trace(".IndexingFrameTargeter: elements=",elements.length);
    }
    indexing.retarget(ix);
    selection.retarget(selectionFrame);
  }
  /**
  Overrides superclass method. 
   */
  public void retargetFacets(){
    super.retargetFacets();
    indexing().retargetFacets();
  }
}