package fjs.swing;
import static fjs.SelectingTargetType.Chooser;
import static fjs.SelectingTargetType.ShowChars;
import static fjs.SelectingTargetType.Standard;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JPanel;
import fjs.ContentingSurface;
import fjs.SelectingTargetType;
import fjs.SurfaceCore.TargetTest;
import fjs.core.STarget;
import fjs.globals.Facets;
final class ContentingLayout extends SelectingLayout{
	ContentingLayout(Container pane,TargetTest test,ContentingSurface surface){
		super(pane,test,surface);
	}
	@Override
	public void build(){
		pane.setLayout(new GridLayout(2,1));
		String title=TITLE_SELECT;
		SwingFacet list=newListFacet(title);
		pane.add(list.mount);
		pane.add(cardsParent);
		int at=0;
		for(JPanel inner:new JPanel[]{
				new JPanel(new GridLayout(5,1)),
				new JPanel(new GridLayout(5,1))}){
			cardsParent.add(inner);
			boolean longer=at++==1;
			String tail=longer?TAIL_SHOW_CHARS:"";
			cards.addLayoutComponent(inner,(longer?ShowChars:Standard).title);
			inner.add(newTextFieldFacet(TITLE_EDIT+tail,20,false).mount);
			if(longer)inner.add(newLabelFacet(TITLE_CHARS+tail).mount);
			inner.add(newCheckBoxFacet(TITLE_LIVE).mount);
			adjustCards();
		}
	}
}