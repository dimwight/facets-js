package fjs;
import fjs.core.STarget;
import fjs.globals.Facets;
import fjs.globals.Globals;
import fjs.util.Titled;
import fjs.util.Tracer;
public abstract class SurfaceCore extends Tracer implements Titled{
	public enum TargetTest{Textual,TogglingLive,Indexing,Numeric,Trigger,Selecting;
		public static TargetTest[]simpleValues(){
			return new TargetTest[]{Textual,TogglingLive,Indexing,Numeric,Trigger};
		}
		public boolean isSimple(){
			return this.ordinal()<Selecting.ordinal();
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
		facets.times.doTime=false||facets.doTrace;
	}
	public void buildSurface(){
		trace(" > Creating targets...");
		STarget targets=newTargetTree();
		if(targets==null)throw new IllegalStateException("Null targets in "+this);
		trace(" > Generating targeters...");
		if(targets==null)throw new IllegalStateException("Null targets in "+this);
		facets.buildTargeterTree(targets);
		trace(" > Building layout...");
		buildLayout();
		trace(" > Surface built.");
	}
	protected abstract STarget newTargetTree();
	protected abstract void buildLayout();
	@Override
	public String title(){
		return title;
	}
	protected final void generateFacets(String...titles){
		for(String title:titles){
			trace(" > Generating facet for title=",title);
			facets.attachFacet(title,value -> trace(" > Facet '"+title
					+ "' updated: value=",value));
		}
	}
}