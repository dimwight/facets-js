package fjs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import fjs.core.STarget;
import fjs.globals.Facets.TargetCoupler;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Facets.TogglingCoupler;
import fjs.globals.Globals;
import fjs.util.Debug;
import fjs.util.Titled;
import fjs.SurfaceCore;
public class ContentingSurface extends SelectingSurface{
	private final static int switcherChooserAt=0,switcherEditAt=1;
	private final List<STarget>switchTrees=new ArrayList();
	private TextContent active,edit;
	private final STarget chooser=facets.newIndexingFrame(new IndexingFramePolicy(){{
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
					updateSwitchTrees(edit=active.clone());
					setSwitcher(switcherEditAt);
				};
			}}),
		};		
	}});
	public ContentingSurface(){
		super(Globals.newInstance(true),TargetTest.Contenting);
		facets.onRetargeted=arg->onRetargeted();
	}
	@Override
	protected STarget newTargetTree(){
		updateSwitchTrees(list.get(0),list.get(2));
		return facets.newIndexingFrame(new IndexingFramePolicy(){{
			indexingTitle=TITLE_SWITCH;
			getIndexables=()->switchTrees.toArray();
		}});
	}
	private void updateSwitchTrees(TextContent...contents){
		switchTrees.clear();
		switchTrees.add(chooser);
		for(TextContent content:contents){
			IndexableType type=IndexableType.getContentType(content);
			String frameTitle=type.title(),
				tail=type==IndexableType.Standard?"":TAIL_SHOW_CHARS;
			trace(".updateSwitchTrees: tail=",tail);
			List<STarget>elements=new ArrayList<>();
			elements.add(newEditTarget(content,tail));
			if(type==IndexableType.ShowChars)elements.add(newCharsTarget(tail));
			elements.add(facets.newTriggerTarget(TITLE_SAVE+tail,new TargetCoupler(){{
				targetStateUpdated=(state,title)->{
					active.copyClone(edit);
					setSwitcher(switcherChooserAt);
				};
			}}));
			elements.add(facets.newTriggerTarget(TITLE_CANCEL+tail,new TargetCoupler(){{
				targetStateUpdated=(state,title)->setSwitcher(switcherChooserAt);
			}}));
			switchTrees.add(facets.newTargetGroup(frameTitle,elements.toArray(new STarget[]{})));
		}
	}
	private void setSwitcher(int at){
		facets.updateTargetState(TITLE_SWITCH,at);
	}
	@Override
	protected void onRetargeted(){
		if(switchTrees.size()>2)updateSwitchTrees(list.get(0));
	}
	@Override
	public void buildSurface(){
		super.buildSurface();
		if(true)return;
	}
	@Override
	protected void buildLayout(){
		generateFacets(TITLE_SELECT,TITLE_EDIT_TEXT,TITLE_EDIT_TEXT+TAIL_SHOW_CHARS,
				TITLE_CHARS+TAIL_SHOW_CHARS);
	}
	@Override
	public IndexableType getIndexedType(){
		STarget tree=(STarget)facets.getIndexingState(SelectingTitles.TITLE_SWITCH).indexed;
		for(IndexableType type:IndexableType.values)
			if(type.title().equals(tree.title()))return type;
		throw new IllegalStateException("No type for tree="+tree);
	}
}
