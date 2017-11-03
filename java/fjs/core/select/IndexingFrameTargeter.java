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
public class IndexingFrameTargeter extends TargeterCore{
  final private Map<String,STargeter>titleTargeters=new HashMap();
	private STargeter indexing,indexed;
	private SIndexing ix;
	private SFrameTarget indexedFrame;
	private String indexedTitle,frameTitle;
	public void retarget(STarget target){
		super.retarget(target);
		updateToTarget();
		if(indexing==null){
	  	indexing=ix.newTargeter();
	  	indexing.setNotifiable(this);
	  }
		indexing.retarget(ix);
		if(titleTargeters.isEmpty()){
			int atThen=ix.index();
			for(int at=0;at<ix.indexables().length;at++){
				ix.setIndex(at);
				updateToTarget();
				indexed=indexedFrame.newTargeter();
				indexed.setNotifiable(this);
				indexed.retarget(indexedFrame);
				titleTargeters.put(indexedTitle,indexed);
			}
			ix.setIndex(atThen);
			updateToTarget();
		}
		indexed=titleTargeters.get(indexedTitle);
		if(indexed==null)throw new IllegalStateException("Null indexed in "+this);
	  indexed.retarget(indexedFrame);
	}
	private void updateToTarget(){
		IndexingFrame frame=(IndexingFrame)target();
		String checkTitle=frame.title();
		if(frameTitle!=null&&!checkTitle.equals(frameTitle))
			throw new IllegalStateException("Bad frame title="+checkTitle);
		frameTitle=checkTitle;
		ix=frame.indexing();
		indexedFrame=frame.indexedFrame();
		indexedTitle=indexedFrame.title();
	}
  public void retargetFacets(){
    super.retargetFacets();
    indexing.retargetFacets();
    for(STargeter t:titleTargeters.values())t.retargetFacets();
  }
	public STargeter[]titleElements(){
  	ArrayList<STargeter>list=new ArrayList<STargeter>(Arrays.asList(elements));
		list.add(indexing);
		for(STargeter t:titleTargeters.values())list.add(t);
		return list.toArray(new STargeter[]{});
	}
}