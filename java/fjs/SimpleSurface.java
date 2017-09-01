package fjs;
import fjs.globals.Globals;
import fjs.util.Titled;
import fjs.util.Tracer;
public abstract class SimpleSurface extends Tracer implements Titled{
	public static final String TITLE_FIRST="First",TITLE_SECOND="Second";
	protected final String title;
	protected SimpleSurface(String title){
		super(title);
		this.title=title;
	}
	@Override
	public String title(){
		return title;
	}
	public void buildSurface(){
		Object targets=newTargetTree();
		trace(" > Generating targeters...");
		Globals.buildTargeterTree(targets);
		trace(" > Building layout...");
		buildLayout();
		trace(" > Surface built!");
	}
	protected Object newTargetTree(){
		trace(" > Generating targets...");
		return Globals.newTargetsGroup("Textuals",
				newTextual(TITLE_FIRST),newTextual(TITLE_SECOND));
	}
	private Object newTextual(String title){
		String text=title+" text in "+SimpleSurface.this.title;
		trace(" > Generating textual target text=",text);
		return Globals.newTextualTarget(title,new Globals.TextualCoupler(){{
			passText=true?null:text;
			getText=title->text;
			targetStateUpdated=title->targetStateUpdated(title);
		}});
	}
	protected abstract void buildLayout();
	protected void targetStateUpdated(String title){
		trace(" > Target state updated: title="+title+" update=",
				Globals.getTargetState(title));
	}
	private static final class LocalSurface extends SimpleSurface{
		private LocalSurface(String title){
			super(title);
		}
		@Override
		protected void buildLayout(){
			trace(" > Generating facet  for ",TITLE_FIRST);
			Globals.attachFacet(TITLE_FIRST,value -> trace(" > Facet updated: value=",value));
		}
		@Override
		public void buildSurface(){
			super.buildSurface();
			String update="Some updated text";
			trace(" > Simulating input: update=",update);
			Globals.updateTargetState(TITLE_FIRST,update);
		}
	}
	public static void main(String[]args){
		new LocalSurface("SimpleSurface").buildSurface();
	}
}