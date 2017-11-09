package fjs.swing;
import static fjs.SelectableType.Chooser;
import static fjs.SelectableType.ShowChars;
import static fjs.SelectableType.Standard;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JPanel;
import fjs.ContentingSurface;
import fjs.SelectableType;
import fjs.SurfaceCore.TargetTest;
import fjs.core.STarget;
import fjs.globals.Facets;
class ContentingLayout extends SelectingLayout{
	ContentingLayout(Container pane,TargetTest test,ContentingSurface surface){
		super(pane,test,surface);
	}
	@Override
	public void build(){
		pane.setLayout(new GridLayout(1,1));
		pane.add(cardsParent);
		int at=0;
		for(JPanel card:new JPanel[]{
				new JPanel(new GridLayout(8,1)),
				new JPanel(new GridLayout(8,1)),
				new JPanel(new GridLayout(4,1))}){
			cardsParent.add(card);
			SelectableType type=SelectableType.values[at++];
			String typeTitle=type.title();
			cards.addLayoutComponent(card,typeTitle);
			if(type==SelectableType.Chooser){
				card.add(newListFacet(TITLE_SELECT).mount);
				card.add(newLabelFacet(TITLE_INDEXED).mount);
				card.add(newButtonFacet(TITLE_OPEN).mount);
			}
			else{
				String tail=type.titleTail();
				card.add(newTextFieldFacet(TITLE_EDIT_TEXT+tail,20,false).mount);
				if(type==SelectableType.ShowChars)
					card.add(newLabelFacet(TITLE_CHARS+tail).mount);
				card.add(newButtonFacet(TITLE_SAVE+tail).mount);
				card.add(newButtonFacet(TITLE_CANCEL+tail).mount);
			}
			adjustCards(typeTitle);
		}
	}
}