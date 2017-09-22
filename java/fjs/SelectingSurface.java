package fjs;
import fjs.globals.Facets.SelectingFrameProxy;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Globals;
import fjs.util.Times;
import jsweet.lang.Interface;
import jsweet.lang.Optional;
public abstract class SelectingSurface extends SurfaceCore implements SelectingTitles{
	@Interface
	public class TextContent{
		@Optional
		public String text;
		public String toString(){
			return text;
		}
	}
	public final SelectingFrameProxy frame;
	protected SelectingSurface(){
		super(TargetTest.Selecting.name(),Globals.newInstance(true),TargetTest.Selecting);
		Times.times=true;
		frame=new SelectingFrameProxy(){{
			title=TITLE_FRAME;
			indexingTitle=TITLE_SELECT;
			content=new Object[]{
				new TextContent(){{text="Hello world!";}},
				new TextContent(){{text="Hello Dolly!";}},
				new TextContent(){{text="Hello, good evening and welcome!";}}
			};
			newEditElements=framed->{
				TextContent text=(TextContent)framed;
				if(false)trace(".newEditElements: text=",text.text);	
				return new Object[]{
					facets.newTextualTarget(TITLE_EDIT,new TextualCoupler(){{
						passText=text.text;
						targetStateUpdated=(title,state)->{
							text.text=(String)state;
						};
					}}),
					facets.newTextualTarget(TITLE_CHARS,new TextualCoupler(){{
						getText=(title)->""+((String)facets.getTargetState(TITLE_EDIT)).length();
					}})
				};
			};
		}};
	}	
	@Override
	final protected void traceOutput(String msg){
		if(true&&facets.trace)super.traceOutput(msg);
	}
	@Override
	protected Object newTargetTree(){
		facets.buildSelectingFrame(frame);
		return frame.frame;
	}
	@Override
	protected void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT,TITLE_CHARS);
	}
	@Override
	public void buildSurface(){
		super.buildSurface();
		if(false)return;
		Object update=1;
		trace(" > Simulating input: update=",update);
		facets.updateTargetState(frame.indexingTitle,update);
		trace(" > selection=",((TextContent)frame.selectedContent.get()).text);
		update="Hi there!";
		trace(" > Simulating input: update=",update);
		facets.updateTargetState(TITLE_EDIT,update);
	}
	private static final class LocalSurface extends SelectingSurface{}
	public static void main(String[]args){
		new LocalSurface().buildSurface();
	}
}
