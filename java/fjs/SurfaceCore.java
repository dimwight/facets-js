package fjs;
import fjs.core.STarget;
import fjs.globals.Facets;
import fjs.globals.Globals;
import fjs.globals.Facets.FacetsApp;
import fjs.util.Titled;
import fjs.util.Tracer;
public abstract class SurfaceCore extends Tracer implements Titled,FacetsApp{
	public enum TargetTest{Textual,TogglingLive,Indexing,Numeric,Trigger,Selecting,Contenting;
		public static TargetTest[]simpleValues(){
			return new TargetTest[]{Textual,TogglingLive,Indexing,Numeric,Trigger};
		}
		public boolean isSimple(){
			return this.ordinal()<Selecting.ordinal();
		}
		public String indexingTitle(){
			if(isSimple())throw new RuntimeException("Not implemented in "+this);
			return this==Selecting?SelectingTitles.TITLE_SELECT:SelectingTitles.TITLE_SWITCH;
		}
	}
	public final Facets facets;
	protected final TargetTest test;
	public SurfaceCore(Facets facets,TargetTest test){
		super(test.name());
		this.facets=facets;
		this.test=true||test==TargetTest.Selecting?test:TargetTest.Indexing;
		facets.times.doTime=false||facets.doTrace;
	}
	public void buildSurface(){
		facets.buildApp(this);
	}
	protected final void generateFacets(String...titles){
		for(String title:titles){
			trace(" > Generating facet for title=",title);
			facets.attachFacet(title,value -> trace(" > Facet for "+title
					+ " updated: value=",value));
		}
	}
	@Override
	public String title(){
		return test.name();
	}
}