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
		Facets.buildTargeterTree(newTargetTree());
		trace(" > Built targets, created targeters");
		buildLayout();
		trace(" > Attached and laid out facets");
		Facets.retargetFacets();
		trace(" > Surface built");
	}
	protected Object newTargetTree(){
		boolean js=Facets.onlyJs;
		String text="This text passed by "+ (js?"jsObject":"TextualCoupler")+ " in "+title;
		Object coupler=
//				js?jsweet.util.Lang.$map(/*Facets.KEY_TEXTUAL_TEXT*/"text", text):
			new TextualCoupler(){
				@Override
				protected String getText(String title){
					return text;
				}
			},
			first=Facets.newTextual(TITLE_FIRST,coupler),
			second=Facets.newTextual(TITLE_SECOND,coupler);
		return Facets.newTargetGroup("Textuals",first,second);
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
				Facets.attachFacet(TITLE_FIRST,updateFn);
			}
			@Override
			public void buildSurface(){
				super.buildSurface();
				Facets.updateTarget(TITLE_FIRST,"Some updated text");
			}
		}.buildSurface();
	}
}