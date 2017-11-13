package fjs;
import java.util.ArrayList;
import java.util.List;
import fjs.core.STarget;
import fjs.globals.Facets.FacetsApp;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.TargetCoupler;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Globals;
import fjs.SurfaceCore;
public class ContentingSurface extends SelectingSurface{
	private TextContent active,edit;
	public ContentingSurface(){
		super(Globals.newInstance(false),TargetTest.Contenting);
	}
	@Override
	public Object getContentTrees(){
		return new STarget[]{
		newContentTree(list.get(0)),
		newContentTree(list.get(2)),
		facets.newIndexingFrame(new IndexingFramePolicy(){{
			frameTitle=TITLE_CHOOSER;
			indexingTitle=TITLE_SELECT;
			getIndexables=()->list.toArray();
			newUiSelectable=indexable->((TextContent)indexable).text;
			newFrameTargets=()->new STarget[]{
				facets.newTextualTarget(SimpleTitles.TITLE_INDEXED,new TextualCoupler(){{
					passText="Updated in onRetargeted";
				}}),
				facets.newTriggerTarget(SelectingTitles.TITLE_OPEN,new TextualCoupler(){{
					targetStateUpdated=(state,title)->{
						active=(TextContent)facets.getIndexingState(TITLE_SELECT
								).indexed;
						facets.addContentTree(newContentTree(edit=active.clone()));
					};
				}}),
			};		
		}})};
	}
	private STarget newContentTree(TextContent content){
		SelectableType type=SelectableType.getContentType(content);
		String tail=type.titleTail();
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
	public void onRetargeted(String activeTitle){
		facets.updateTargetState(TITLE_INDEXED,activeTitle);
	}
	@Override
	public void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT_TEXT,TITLE_EDIT_TEXT+SELECTABLE_SHOW_CHARS,
				TITLE_CHARS+SELECTABLE_SHOW_CHARS);
	}
}
