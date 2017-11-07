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
		@Override
		public String toString(){
			return text;
		}
		@Override
		public boolean equals(Object o){
			return o!=null&&text.equals(((TextContent)o).text);
		}
		@Override
		public TextContent clone(){
			return new TextContent(text);
		}
		public void copyClone(TextContent clone){
			this.text=clone.text;
		}
	}
	@Override
	protected void traceOutput(String msg){
		if(true||facets.doTrace)super.traceOutput(msg);
	}
	protected final List<TextContent>list=new ArrayList(Arrays.asList(new Object[]{
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
			indexingFrameTitle=appTitle;
			indexingTitle=TITLE_SELECT;
			getIndexables=()->list.toArray();
			newUiSelectable=indexable->((TextContent)indexable).text;
			newFrameTargets=()->
				new STarget[]{
					facets.newTextualTarget(SimpleTitles.TITLE_INDEXED,new TextualCoupler(){{
						getText=titley->list.get((Integer)facets.getTargetState(TITLE_SELECT)).text;
					}}),
					facets.newTogglingTarget(TITLE_LIVE,new TogglingCoupler(){{
						passSet=true;
					}})
				};		
			newIndexedTargetsTitle=indexed->
			indexingFrameTitle+IndexableType.getContentType((TextContent)indexed).titleTail();
			newIndexedTargets=(indexed,indexedTargetsTitle)->{
				IndexableType type=IndexableType.getContentType((TextContent)indexed);
				return facets.newTargetGroup(indexedTargetsTitle,type==IndexableType.Standard?
						new STarget[]{newEditTarget(indexed,type)}
					:new STarget[]{
						newEditTarget(indexed,type),
						newCharsTarget()
					});
			};
		}});
	}
	@Override
	public void buildSurface(){
		super.buildSurface();
		if(false)return;
		Consumer add=arg->{
			list.add(new TextContent("Hello sailor!"));
			trace(" > Simulating input: update=",list.get(list.size()-1).text);
			facets.notifyTargetUpdated(TITLE_SELECT);
		},
		edit=arg->{
			Object update="Hello !";
			trace(" > Simulating input: update=",update);
			String title=TITLE_EDIT_TEXT;
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
		IndexableType type=getIndexedType();
		String tail=type.titleTail();
		facets.setTargetLive(TITLE_EDIT_TEXT+tail,live);
		if(type==IndexableType.ShowChars)facets.setTargetLive(TITLE_CHARS+tail,live);
	}
	@Override
	protected void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT_TEXT);
	}
	protected final STarget newEditTarget(Object indexed,IndexableType type){
		String tail=type==IndexableType.Standard?"":TAIL_SHOW_CHARS;
		return facets.newTextualTarget(TITLE_EDIT_TEXT+tail,new TextualCoupler(){{
			passText=((TextContent)indexed).text;
			targetStateUpdated=(state,title)->((TextContent)indexed).text=(String)state;
		}});
	}
	protected final STarget newCharsTarget(){
		return facets.newTextualTarget(TITLE_CHARS+TAIL_SHOW_CHARS,new TextualCoupler(){{
			getText=(title)->""+((String)facets.getTargetState(TITLE_EDIT_TEXT+TAIL_SHOW_CHARS)).length();
		}});
	}
	public IndexableType getIndexedType(){
		TextContent content=(TextContent)facets.getIndexingState(
				SelectingTitles.TITLE_SELECT).indexed;
		return IndexableType.getContentType(content);
	}
	public static void main(String[]args){
		(
			new ContentingSurface()
//			new SelectingSurface(Globals.newInstance(true),TargetTest.Selecting)
			).buildSurface();
	}
}
