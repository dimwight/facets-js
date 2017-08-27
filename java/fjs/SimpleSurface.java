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
		trace(" > Building surface");
		Globals.buildTargeterTree(newTargetTree());
		trace(" > Built targets, created targeters");
		buildLayout();
		trace(" > Attached and laid out facets");
		Globals.retargetFacets();
		trace(" > Surface built");
	}
	protected Object newTargetTree(){
		String passText="This text passed [by CouplerLike] in "+title;
		trace(".newTargetTree: text=",passText);
		Object coupler=new CouplerLike(){{this.text=passText;}},
			first=Globals.newTextual(TITLE_FIRST,coupler),
			second=Globals.newTextual(TITLE_SECOND,coupler);
		return Globals.newTargetGroup("Textuals",first,second);
	}
	protected abstract void buildLayout();
	public static void main(String[]args){
		new SimpleSurface(SimpleSurface.class.getSimpleName()){
			@Override
			protected void buildLayout(){
				UpdateFunction updateJs=new UpdateFunction() {
					@Override
					public Object apply(Object value){
						trace(".UpdateFunction: value=",value);		
						return null;
					}};
				trace(".buildLayout: attaching facet [with UpdateFunction] in ",title);
				Globals.attachFacet(TITLE_FIRST,updateJs);
			}
			@Override
			public void buildSurface(){
				super.buildSurface();
				Globals.updateTarget(TITLE_FIRST,"Some updated text");
			}
		}.buildSurface();
	}
}