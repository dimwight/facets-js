package fjs.core;
import fjs.util.Debug;
/**
{@link TargetCore} that enables editing of the contents of an {@link SIndexing}. 
 */
public abstract class IndexingFrame extends TargetCore{
	private final SIndexing indexing;
	/**
	Unique constructor. 
	@param title passed to superclass 
	@param indexing supplies content for {{@link #newIndexedFrame(Object)}
	 */
	protected IndexingFrame(String title,SIndexing indexing){
		super(title);
		if(indexing==null)throw new IllegalArgumentException(
				"Null indexing in "+Debug.info(this));
		this.indexing=indexing;
		indexing.setNotifiable(this);
	}
	/**
	<p>Returns the {@link SFrameTarget} created in {@link #newIndexedFrame(Object)}. 
	 */
	final public STarget indexedTarget(){
		Object indexed=indexing.indexed();
		return indexed instanceof STarget?(STarget)indexed:newIndexedFrame(indexed);
	}
	/**
	Create
	@param indexed the currently indexed member of {@link #indexing}
	 */
	protected abstract SFrameTarget newIndexedFrame(Object indexed);
	/** 
	Sets an {@link SIndexing} containing content to be exposed.
	 */
	/**
	The indexing passed to the constructor. 
	 */
	public final SIndexing indexing(){
	  return indexing;
	}
  /**
	Overrides superclass method. 
	<p>Returns an {@link IndexingFrameTargeter}. 
	 */
	final public STargeter newTargeter(){
		return new IndexingFrameTargeter();
	}
	/**
	Re-implementation returning <code>true</code>. 
	 */
	final protected boolean notifiesTargeter(){
		return true;
	}
}