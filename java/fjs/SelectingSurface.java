package fjs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import fjs.SurfaceCore.TargetTest;
import fjs.core.STarget;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Facets.TogglingCoupler;
import fjs.globals.Facets;
import fjs.globals.Globals;
import fjs.util.Util;
import jsweet.lang.Interface;
import jsweet.lang.Optional;
public class SelectingSurface extends SurfaceCore implements SelectingTitles{
	public static class TextContent{
		public String text;
		public TextContent(String text){
			this.text=text;
		}
		public String toString(){
			return text;
		}
		@Override
		public boolean equals(Object o){
			return o!=null&&text.equals(((TextContent)o).text);
		}
	}
	@Override
	protected void traceOutput(String msg){
		if(false||facets.doTrace)super.traceOutput(msg);
	}
	private final List<TextContent>list=new ArrayList(Arrays.asList(new Object[]{
			new TextContent("Hello world!"),
			new TextContent("Hello Dolly!"),
			new TextContent("Hello, good evening and welcome!")
		}));
	protected SelectingSurface(Facets facets,TargetTest test){
		super(facets,test);
		facets.onRetargeted=arg->onRetargeted();
	}
	@Override
	protected STarget newTargetTree(){
		String appTitle=TargetTest.Selecting.name();
		return facets.newIndexingFrame(new IndexingFramePolicy(){{
			frameTitle=appTitle;
			indexingTitle=TITLE_SELECT;
			getIndexables=()->list.toArray();
			newUiSelectable=indexable->((TextContent)indexable).text;
			newFrameTargets=()->new STarget[]{
					facets.newTextualTarget(SimpleTitles.TITLE_INDEXED,new TextualCoupler(){{
						getText=titley->{
							Integer index=(Integer)facets.getTargetState(TITLE_SELECT);
							return false&&index==null?"No target yet":
									list.get(index).text;
						};
					}}),
					facets.newTogglingTarget(TITLE_LIVE,new TogglingCoupler(){{
						passSet=true;
					}})
			};		
			newIndexedTitle=indexed->{
				return appTitle+(((TextContent)indexed).text.length()>20?TAIL_SHOW_CHARS:"");
			};
			newIndexedTargets=(indexed,indexedTitle)->!indexedTitle.endsWith(TAIL_SHOW_CHARS)?
					new STarget[]{newEditTarget(indexed,"")}
				:new STarget[]{
					newEditTarget(indexed,TAIL_SHOW_CHARS),
					newCharsTarget()
				};
		}});
	}
	@Override
	public void buildSurface(){
		super.buildSurface();
		if(true)return;
		Consumer add=arg->{
			list.add(new TextContent("Hello sailor!"));
			trace(" > Simulating input: update=",list.get(list.size()-1).text);
			facets.notifyTargetUpdated(TITLE_SELECT);
		},
		edit=arg->{
			Object update="Hello !";
			trace(" > Simulating input: update=",update);
			String title=TITLE_EDIT;
			facets.updateTargetWithNotify(title,update);
		},
		select=arg->{
			Object update=2;
			trace(" > Simulating input: update=",update);
			String title=TITLE_SELECT;
			facets.updateTargetWithNotify(title,update);
		};
		for(Consumer update:new Consumer[]{
				add,
				edit,
				select
			}
		)update.accept("");
	}
	protected void onRetargeted(){
		boolean live=(boolean)facets.getTargetState(TITLE_LIVE);
		SelectingTargetType type=getIndexedType();
		String tail=type.titleTail();
		facets.setTargetLive(TITLE_EDIT+tail,live);
		if(type==SelectingTargetType.ShowChars)facets.setTargetLive(TITLE_CHARS+tail,live);
	}
	@Override
	protected void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT);
	}
	private STarget newEditTarget(Object indexed,String longer){
		return facets.newTextualTarget(TITLE_EDIT+longer,new TextualCoupler(){{
			passText=((TextContent)indexed).text;
			targetStateUpdated=(state,title)->((TextContent)indexed).text=(String)state;
		}});
	}
	private STarget newCharsTarget(){
		return facets.newTextualTarget(TITLE_CHARS+TAIL_SHOW_CHARS,new TextualCoupler(){{
			getText=(title)->""+((String)facets.getTargetState(TITLE_EDIT+TAIL_SHOW_CHARS)).length();
		}});
	}
	public SelectingTargetType getIndexedType(){
		return ((TextContent)facets.getIndexingState(test.indexingTitle()).indexed
				).text.length()>20?SelectingTargetType.ShowChars:SelectingTargetType.Standard;
	}
	public static void main(String[]args){
		(false?new SelectingSurface(Globals.newInstance(true),TargetTest.Selecting)
				:new ContentingSurface()).buildSurface();
	}
}
