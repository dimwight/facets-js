package fjs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import fjs.core.STarget;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Globals;
public class ContentingSurface extends SelectingSurface{
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
		if(false||facets.doTrace)super.traceOutput(msg);
	}
	private final List<TextContent>list=new ArrayList(Arrays.asList(new Object[]{
			new TextContent("Hello world!"),
			new TextContent("Hello Dolly!"),
			new TextContent("Hello, good evening and welcome!")
		}));
	private final List<STarget>contentTrees=new ArrayList();
	private STarget chooser=facets.newIndexingFrame(new IndexingFramePolicy(){{
		frameTitle=TITLE_CHOOSER;
		indexingTitle=TITLE_SELECT;
		getIndexables=()->list.toArray();
		newUiSelectable=indexable->((TextContent)indexable).text;
		newFrameTargets=()->new STarget[]{
			facets.newTriggerTarget(SelectingTitles.TITLE_EDIT,new TextualCoupler(){{
				targetStateUpdated=(state,title)->{
					setContentTrees((TextContent)facets.getIndexingState(TITLE_SELECT).indexed);
				};
			}})
		};		
	}});
	private final String appTitle=TargetTest.Contenting.name();
	protected ContentingSurface(){
		super(Globals.newInstance(true),TargetTest.Contenting);
		facets.onRetargeted=arg->onRetargeted();
		setContentTrees(list.get(0),list.get(2));
	}
	private void setContentTrees(TextContent...contents){
		contentTrees.clear();
		contentTrees.add((chooser));
		for(TextContent content:contents){
			String title=appTitle+(content.text.length()>20?TAIL_SHOW_CHARS:"");
			STarget tree=facets.newTargetGroup(title,!title.endsWith(TAIL_SHOW_CHARS)?
				new STarget[]{newEditTarget(content,"")}
				:new STarget[]{
					newEditTarget(content,TAIL_SHOW_CHARS),
					newCharsTarget()
				});
			contentTrees.add(tree);
		}
	}
	@Override
	protected STarget newTargetTree(){
		return facets.newIndexingFrame(new IndexingFramePolicy(){{
			frameTitle=appTitle;
			indexingTitle=TITLE_SWITCH;
			getIndexables=()->contentTrees.toArray();
			newUiSelectable=indexable->((TextContent)indexable).text;
		}});
	}
	protected void onRetargeted(){
		setContentTrees(list.get(0));
	}
	private STarget newEditTarget(Object indexed,String longer){
		return facets.newTextualTarget(TITLE_EDIT+longer,new TextualCoupler(){{
			passText=((TextContent)indexed).text;
			targetStateUpdated=(state,title)->((TextContent)indexed).text=(String)state;
		}});
	}
	@Override
	public void buildSurface(){
		super.buildSurface();
		if(true)return;
	}
	@Override
	protected void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT,TITLE_EDIT+TAIL_SHOW_CHARS,
				TITLE_CHARS+TAIL_SHOW_CHARS);
	}
	private STarget newCharsTarget(){
		return facets.newTextualTarget(TITLE_CHARS+TAIL_SHOW_CHARS,new TextualCoupler(){{
			getText=(title)->""+((String)facets.getTargetState(TITLE_EDIT+TAIL_SHOW_CHARS)).length();
		}});
	}
	public SelectingTargetType getIndexedType(){
		STarget tree=(STarget)facets.getIndexingState(TITLE_SWITCH).indexed;
		return tree.title().equals(TITLE_CHOOSER)?SelectingTargetType.Chooser
				:super.getIndexedType(); 
	}
}
