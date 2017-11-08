package fjs.swing;
import static fjs.IndexableType.Chooser;
import static fjs.IndexableType.ShowChars;
import static fjs.IndexableType.Standard;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JPanel;
import fjs.ContentingSurface;
import fjs.IndexableType;
import fjs.SurfaceCore.TargetTest;
import fjs.core.STarget;
import fjs.globals.Facets;
final class ContentingLayout extends SelectingLayout{
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
			IndexableType type=IndexableType.values[at++];
			cards.addLayoutComponent(card,type.title());
			if(type==IndexableType.Chooser){
				card.add(newListFacet(TITLE_SELECT).mount);
				card.add(newLabelFacet(TITLE_INDEXED).mount);
				card.add(newButtonFacet(TITLE_OPEN).mount);
			}
			else{
				String tail=type.titleTail();
				card.add(newTextFieldFacet(TITLE_EDIT_TEXT+tail,20,false).mount);
				if(type==IndexableType.ShowChars)
					card.add(newLabelFacet(TITLE_CHARS+tail).mount);
				card.add(newButtonFacet(TITLE_SAVE+tail).mount);
				card.add(newButtonFacet(TITLE_CANCEL+tail).mount);
			}
			adjustCards();
		}
	}
}