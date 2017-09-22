package fjs;
import fjs.globals.Facets;
import fjs.globals.Globals;
import fjs.util.Titled;
import fjs.util.Tracer;
public abstract class SurfaceCore extends Tracer implements Titled{
	public enum TargetTest{Textual,TogglingLive,Indexing,Numeric,Selecting;
		public static TargetTest[]simpleValues(){
			return new TargetTest[]{Textual,TogglingLive,Numeric,Indexing};
		}
		public boolean isSimple(){
			for(TargetTest test:simpleValues())
				if(test==this)return true;
			return false;
		}
		public static TargetTest[]selectingValues(){
			return new TargetTest[]{Selecting};
		}
	}
	public final Facets facets;
	private final String title;
	protected final TargetTest test;
	public SurfaceCore(String title,Facets facets,TargetTest test){
		super(title);
		this.title=title;
		this.facets=facets;
		this.test=true||test==TargetTest.Selecting?test:TargetTest.Indexing;
	}
	public void buildSurface(){
		Object targets=newTargetTree();
		if(targets==null)throw new IllegalStateException("Null targets in "+this);
		trace(" > Generating targeters...");
		if(targets==null)throw new IllegalStateException("Null targets in "+this);
		facets.buildTargeterTree(targets);
		trace(" > Building layout...");
		buildLayout();
		trace(" > Surface built.");
	}
	protected abstract Object newTargetTree();
	protected abstract void buildLayout();
	@Override
	public String title(){
		return title;
	}
	protected final void generateFacets(String...titles){
		for(String title:titles){
			trace(" > Generating facet for ",title);
			facets.attachFacet(title,value -> trace(" > Facet '"+title
					+ "' updated: value=",value));
		}
	}
}