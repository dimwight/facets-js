package fjs.swing;
import static fjs.SurfaceCore.TargetTest.Indexing;
import static fjs.SurfaceCore.TargetTest.Numeric;
import static fjs.SurfaceCore.TargetTest.TogglingLive;
import static fjs.SurfaceCore.TargetTest.Trigger;
import java.awt.Container;
import java.awt.GridLayout;
import fjs.SelectingSurface;
import fjs.SimpleTitles;
import fjs.SurfaceCore;
import fjs.SurfaceCore.TargetTest;
import fjs.globals.Facets;
final class SimpleLayout extends PaneLayout implements SimpleTitles{
	SimpleLayout(Container pane,TargetTest test,SurfaceCore surface){
		super(pane,test,surface);
	}
	public void build(){
		pane.setLayout(new GridLayout(4,1,5,5));
		for(SwingFacet facet:test==Indexing?new SwingFacet[]{
				newComboBoxFacet(TITLE_INDEXING),
				newLabelFacet(TITLE_INDEX),
				newLabelFacet(TITLE_INDEXED),
		}:test==TogglingLive?new SwingFacet[]{
				newCheckBoxFacet(TITLE_TOGGLING),
				newLabelFacet(TITLE_TOGGLED),
		}:test==Numeric?new SwingFacet[]{
				newNumberFieldFacet(TITLE_NUMERIC_FIELD,5),
				newLabelFacet(TITLE_NUMERIC_LABEL),
		}:test==Trigger?new SwingFacet[]{
				newButtonFacet(TITLE_TRIGGER),
				newLabelFacet(TITLE_TRIGGERINGS),
		}:new SwingFacet[]{
				newTextFieldFacet(TITLE_TEXTUAL_FIRST,20,false),
				newTextFieldFacet(TITLE_TEXTUAL_SECOND,20,false),
				newLabelFacet(TITLE_TEXTUAL_FIRST),
				newLabelFacet(TITLE_TEXTUAL_SECOND),
		}) {
			pane.add(facet.mount);
		}
	}
}