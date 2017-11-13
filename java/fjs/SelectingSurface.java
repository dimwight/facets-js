package fjs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import fjs.SelectingSurface.TextContent;
import fjs.core.STarget;
import fjs.globals.Facets;
import fjs.globals.Facets.FacetsApp;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Facets.TogglingCoupler;
import fjs.globals.Globals;
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
	protected void doTraceMsg(String msg){
		if(true||facets.doTrace)super.doTraceMsg(msg);
	}
	protected final List<TextContent>list=new ArrayList(Arrays.asList(new Object[]{
			new TextContent("Hello world!"),
			new TextContent("Hello Dolly!"),
			new TextContent("Hello, good evening and welcome!")
		}));
	protected SelectingSurface(Facets facets,TargetTest test){
		super(facets,test);
	}
	@Override
	public Object getContentTrees(){
		String appTitle=TargetTest.Selecting.name();
		return facets.newIndexingFrame(new IndexingFramePolicy(){{
			frameTitle=appTitle;
			indexingTitle=TITLE_SELECT;
			getIndexables=()->list.toArray();
			newUiSelectable=indexable->((TextContent)indexable).text;
			newFrameTargets=()->
				new STarget[]{
					facets.newTextualTarget(SimpleTitles.TITLE_INDEXED,new TextualCoupler(){{
						getText=title->{
							TextContent indexed=(TextContent)facets.getIndexingState(TITLE_SELECT).indexed;
							return SelectableType.getContentType(indexed).title();
						};
					}}),
					facets.newTogglingTarget(TITLE_LIVE,new TogglingCoupler(){{
						passSet=true;
					}})
				};	
			newIndexedTreeTitle=indexed->
				appTitle+SelectableType.getContentType((TextContent)indexed).titleTail();
			newIndexedTree=(indexed,indexedTreeTitle)->{
				TextContent content=(TextContent)indexed;
				SelectableType type=SelectableType.getContentType(content);
				String tail=type.titleTail();
				return facets.newTargetGroup(indexedTreeTitle,type==SelectableType.Standard?
						new STarget[]{newEditTarget(content,tail)}
					:new STarget[]{
						newEditTarget(content,tail),
						newCharsTarget(tail)
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
	public void onRetargeted(String activeTitle){
		boolean live=(boolean)facets.getTargetState(TITLE_LIVE);
		SelectableType type=getIndexedType();
		String tail=type.titleTail();
		facets.setTargetLive(TITLE_EDIT_TEXT+tail,live);
		if(type==SelectableType.ShowChars)facets.setTargetLive(TITLE_CHARS+tail,live);
	}
	@Override
	public void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT_TEXT);
	}
	protected final STarget newEditTarget(TextContent indexed,String tail){
		return facets.newTextualTarget(TITLE_EDIT_TEXT+tail,new TextualCoupler(){{
			passText=indexed.text;
			targetStateUpdated=(state,title)->indexed.text=(String)state;
		}});
	}
	protected final STarget newCharsTarget(String tail){
		return facets.newTextualTarget(TITLE_CHARS+tail,new TextualCoupler(){{
			getText=(title)->""+((String)facets.getTargetState(TITLE_EDIT_TEXT+SELECTABLE_SHOW_CHARS)).length();
		}});
	}
	public SelectableType getIndexedType(){
		TextContent content=(TextContent)facets.getIndexingState(
				SelectingTitles.TITLE_SELECT).indexed;
		return SelectableType.getContentType(content);
	}
	public static void main(String[]args){
		(
//			new ContentingSurface()
			new SelectingSurface(Globals.newInstance(true),TargetTest.Selecting)
			).buildSurface();
	}
}
