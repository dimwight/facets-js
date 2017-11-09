package fjs.swing;
import static fjs.SelectableType.ShowChars;
import static fjs.SelectableType.Standard;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import fjs.SelectingSurface;
import fjs.SelectableType;
import fjs.SelectingTitles;
import fjs.SelectingSurface.TextContent;
import fjs.SurfaceCore.TargetTest;
import fjs.core.STarget;
import fjs.globals.Facets;
class SelectingLayout extends PaneLayout implements SelectingTitles{
	final CardLayout cards=new CardLayout();
	final JComponent cardsParent=new JPanel(cards);
	SelectingLayout(Container pane,TargetTest test,SelectingSurface surface){
		super(pane,test,surface);
		pane.setLayout(new GridLayout(2,1));
	}
	protected void adjustCards(String activeTitle){
		cards.show(cardsParent,activeTitle);
	}
	@Override
	public void build(){
		pane.add(newListFacet(TITLE_SELECT).mount);
		pane.add(cardsParent);
		int at=0;
		for(JPanel card:new JPanel[]{
				new JPanel(new GridLayout(5,1)),
				new JPanel(new GridLayout(5,1))}){
			cardsParent.add(card);
			SelectableType type=SelectableType.values[at++];
			String activeTitle=type.title();
			cards.addLayoutComponent(card,activeTitle);
			String tail=type.titleTail();
			card.add(newTextFieldFacet(TITLE_EDIT_TEXT+tail,20,false).mount);
			if(type==SelectableType.ShowChars)
				card.add(newLabelFacet(TITLE_CHARS+tail).mount);
			card.add(newCheckBoxFacet(TITLE_LIVE).mount);
			adjustCards(activeTitle);
		}
	}
}