package fjs;
import fjs.globals.Facets;
import fjs.globals.Globals;
import fjs.util.Titled;
import fjs.util.Tracer;
public abstract class SimpleSurface extends Tracer implements Titled{
	public enum FacetTest{Textual,Indexing}
	protected final FacetTest test;
	public static final int INDEX_START=1;
	public static final String TITLE_FIRST="First",TITLE_SECOND="Second",
		TITLE_INDEXING=TITLE_FIRST+" or "+TITLE_SECOND,
		TITLE_INDEXED="Indexed",TITLE_INDEX="Index",
		INDEXABLES[]={TITLE_FIRST,TITLE_SECOND};
	protected final Facets app=Globals.newInstance(true);
	private final String title;
	protected SimpleSurface(String title,FacetTest test){
		super(title);
		this.title=title;
		this.test=test;
	}
	protected void traceOutput(String msg) {
		if(true&&app.trace)super.traceOutput(msg);
	}
	@Override
	public String title(){
		return title;
	}
	public void buildSurface(){
		Object targets=newTargetTree();
		trace(" > Generating targeters...");
		app.buildTargeterTree(targets);
		trace(" > Building layout...");
		buildLayout();
		trace(" > Surface built!");
	}
	protected Object newTargetTree(){
		trace(" > Generating targets");
		return test==FacetTest.Textual?app.newTargetsGroup("Textuals",
				newTextual(TITLE_FIRST),newTextual(TITLE_SECOND))
			:app.newTargetsGroup("Indexing+Textual",
					newIndexing(TITLE_INDEXING),newTextual(TITLE_INDEX),newTextual(TITLE_INDEXED));
	}
	private Object newIndexing(String title){
		String text=title+" text in "+SimpleSurface.this.title;
		trace(" > Generating indexing target text=",text);
		return app.newIndexingTarget(title,new Facets.IndexingCoupler(){{
			passIndexables=INDEXABLES;
			passIndex=INDEX_START;
			targetStateUpdated=(title,state)->targetStateUpdated(title,state);
		}});
	}
	private Object newTextual(String title){
		String text=title+" text in "+SimpleSurface.this.title;
		trace(" > Generating textual target text=",text);
		return app.newTextualTarget(title,new Facets.TextualCoupler(){{
			passText=title.equals(TITLE_INDEXING)?INDEXABLES[INDEX_START]
					:true?null:text;
			getText=title->!title.equals(TITLE_INDEX)?text
					:String.valueOf(app.getTargetState(TITLE_INDEXING));
			targetStateUpdated=(title,state)->targetStateUpdated(title,state);
		}});
	}
	protected abstract void buildLayout();
	protected void targetStateUpdated(String title,Object state){
		trace(" > Target state updated: title="+title+" state=",state);
		if(title.equals(TITLE_FIRST))
			app.updateTargetState(TITLE_SECOND,TITLE_FIRST+" has changed to: "+state);
		else if(title.equals(TITLE_INDEXING)){
			app.updateTargetState(TITLE_INDEXED,INDEXABLES[(int)state]);
			app.updateTargetState(TITLE_INDEX,String.valueOf(state));
		}
	}
	private static final class LocalSurface extends SimpleSurface{
		private LocalSurface(String title){
			super(title,FacetTest.Indexing);
		}
		@Override
		protected void buildLayout(){
			if(test==FacetTest.Textual)generateFacet(TITLE_FIRST);
			else generateFacet(TITLE_INDEXING,TITLE_INDEXED);
		}
		private void generateFacet(String...titles){
			for(String title:titles){
				trace(" > Generating facet for",title);
				app.attachFacet(title,value -> trace(" > Facet updated: value=",value));
			}
		}
		@Override
		public void buildSurface(){
			super.buildSurface();
			Object update=test==FacetTest.Indexing?(INDEX_START+1)%2:"Some updated text";
			trace(" > Simulating input: update=",update);
			app.updateTargetState(test==FacetTest.Indexing?TITLE_INDEXING:TITLE_FIRST,update);
		}
	}
	public static void main(String[]args){
		new LocalSurface("SimpleSurface").buildSurface();
	}
}