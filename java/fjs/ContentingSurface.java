package fjs;
import java.util.ArrayList;
import java.util.List;
import fjs.core.STarget;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.TargetCoupler;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Globals;
public class ContentingSurface extends SelectingSurface{
	private TextContent active,edit;
	public ContentingSurface(){
		super(Globals.newInstance(true),TargetTest.Contenting);
		facets.callOnRetargeted=title->callOnRetargeted(title);
	}
	@Override
	protected void buildContentTrees(){
		facets.addContentTree(newContentTree(list.get(0)));
		facets.addContentTree(newContentTree(list.get(2)));
		facets.addContentTree(facets.newIndexingFrame(new IndexingFramePolicy(){{
			frameTitle=TITLE_CHOOSER;
			indexingTitle=TITLE_SELECT;
			getIndexables=()->list.toArray();
			newUiSelectable=indexable->((TextContent)indexable).text;
			newFrameTargets=()->new STarget[]{
				facets.newTextualTarget(SimpleTitles.TITLE_INDEXED,new TextualCoupler(){{
					getText=title->(active=(TextContent)facets.getIndexingState(TITLE_SELECT
							).indexed).text;
				}}),
				facets.newTriggerTarget(SelectingTitles.TITLE_OPEN,new TextualCoupler(){{
					targetStateUpdated=(state,title)->{
						facets.addContentTree(newContentTree(edit=active.clone()));
					};
				}}),
			};		
		}}));
		facets.buildTargeterTree();
	}
	private STarget newContentTree(TextContent content){
		SelectableType type=SelectableType.getContentType(content);
		String tail=type==SelectableType.Standard?"":TAIL_SHOW_CHARS;
		List<STarget>members=new ArrayList();
		members.add(newEditTarget(content,tail));
		if(type==SelectableType.ShowChars)members.add(newCharsTarget(tail));
		members.add(facets.newTriggerTarget(TITLE_SAVE+tail,new TargetCoupler(){{
			targetStateUpdated=(state,title)->{
				active.copyClone(edit);
				activateChooser();
			};
		}}));
		members.add(facets.newTriggerTarget(TITLE_CANCEL+tail,new TargetCoupler(){{
			targetStateUpdated=(state,title)->activateChooser();
		}}));
		return facets.newTargetGroup(type.title(),members.toArray(new STarget[]{}));
	}
	private void activateChooser(){
		facets.activateContentTree(TITLE_CHOOSER);
	}
	@Override
	protected void callOnRetargeted(String activeTitle){}
	@Override
	protected void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT_TEXT,TITLE_EDIT_TEXT+TAIL_SHOW_CHARS,
				TITLE_CHARS+TAIL_SHOW_CHARS);
	}
}
