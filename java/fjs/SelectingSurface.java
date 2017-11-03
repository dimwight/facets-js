package fjs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import fjs.core.STarget;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Facets.TogglingCoupler;
import fjs.globals.Globals;
import fjs.util.Util;
import jsweet.lang.Interface;
import jsweet.lang.Optional;
public abstract class SelectingSurface extends SurfaceCore implements SelectingTitles{
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
	final protected void traceOutput(String msg){
		if(true||facets.doTrace)super.traceOutput(msg);
	}
	public final IndexingFramePolicy frame;
	private boolean firstPass=true;
	private final List<TextContent>list=new ArrayList(Arrays.asList(new Object[]{
			new TextContent("Hello world!"),
			new TextContent("Hello Dolly!"),
			new TextContent("Hello, good evening and welcome!")
		}));
	protected SelectingSurface(){
		super(TargetTest.Selecting.name(),Globals.newInstance(true),
				TargetTest.Selecting);
		if(false){
			Object[]content_=new Object[]{
				new TextContent("Hello world!"),
				new TextContent("Hello Dolly!"),
				new TextContent("Hello, good evening and welcome!")
			};
			Object[]content=list.toArray();
			trace(".SelectingSurface: content1=",content);
			((TextContent)content_[0]).text="Hello";
			trace("equal="+Util.longEquals(content,content_),content_);
		}
		frame=new IndexingFramePolicy(){{
			frameTitle=TITLE_FRAME;
			indexingTitle=TITLE_SELECT;
			getIndexables=()->list.toArray();
			getUiSelectables=()->{
				List<String>selectables=new ArrayList();
				for(TextContent c:list)selectables.add(c.text);
				return selectables.toArray(new String[]{});
			};
			newIndexingTargets=()->new STarget[]{
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
				return TITLE_FRAME+(((TextContent)indexed).text.length()>20?TAIL_SHOW_CHARS:"");
			};
			newIndexedTargets=(indexed,indexedTitle)->!indexedTitle.endsWith(TAIL_SHOW_CHARS)?
					new STarget[]{newEditTarget(indexed,"")}
				:new STarget[]{
					newEditTarget(indexed,TAIL_SHOW_CHARS),
					newCharsTarget()
				};
		}};
		facets.onRetargeted=arg->onRetargeted();
	}
	@Override
	protected STarget newTargetTree(){
		return facets.newIndexingFrame(frame);
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
				select}
		)update.accept("");
	}
	protected void onRetargeted(){
		if(firstPass){
			firstPass=false;
			int updateThen=(int)facets.getTargetState(TITLE_SELECT);
			ContentType.updateTargeterTree(list,facets,this);
			facets.updateTargetState(TITLE_SELECT,updateThen);
		}
		boolean live=(boolean)facets.getTargetState(TITLE_LIVE);
		ContentType type=ContentType.getIndexedType(facets);
		String tail=type.titleTail();
		facets.setTargetLive(TITLE_EDIT+tail,live);
		if(type==ContentType.ShowChars)facets.setTargetLive(TITLE_CHARS+tail,live);
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
	private static final class LocalSurface extends SelectingSurface{}
	public static void main(String[]args){
		new LocalSurface().buildSurface();
	}
}
