package fjs;
import fjs.core.FacetUpdatable;
import fjs.core.TextualCoupler;
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
		Superficial.buildTargeterTree(newTargetTree());
		trace(" > Built targets, created targeters");
		buildLayout();
		trace(" > Attached and laid out facets");
		Superficial.retargetFacets();
		trace(" > Surface built");
	}
	protected Object newTargetTree(){
		boolean js=Superficial.onlyJs;
		String text="This text passed by "+ (js?"jsObject":"TextualCoupler")+ " in "+title;
		Object coupler=
//				js?jsweet.util.Lang.$map(/*Superficial.KEY_TEXTUAL_TEXT*/"text", text):
			new TextualCoupler(){
				@Override
				protected String getText(String title){
					return text;
				}
			},
			first=Superficial.newTextual(TITLE_FIRST,coupler),
			second=Superficial.newTextual(TITLE_SECOND,coupler);
		return Superficial.newTargetGroup("Textuals",first,second);
	}
	protected abstract void buildLayout();
	public static void main(String[]args){
		new SimpleSurface(SimpleSurface.class.getSimpleName()){
			@Override
			protected void buildLayout(){
				FacetUpdatable updateFn=true?update -> trace(".main: update=",update)
						:new FacetUpdatable(){
					@Override
					public void updateFromFacet(Object update){
						trace(".main: update=",update);
					}
				};
				Superficial.attachFacet(TITLE_FIRST,updateFn);
			}
			@Override
			public void buildSurface(){
				super.buildSurface();
				Superficial.updateTarget(TITLE_FIRST,"Some updated text");
			}
		}.buildSurface();
	}
}