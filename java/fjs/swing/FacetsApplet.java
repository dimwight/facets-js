package fjs.swing;
import static fjs.ContentType.*;
import static fjs.SurfaceCore.TargetTest.*;
import static javax.swing.BorderFactory.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import fjs.ContentType;
import fjs.SelectingSurface;
import fjs.SelectingTitles;
import fjs.SimpleSurface;
import fjs.SimpleTitles;
import fjs.SurfaceCore;
import fjs.SurfaceCore.TargetTest;
import fjs.globals.Facets;
import fjs.globals.Facets.IndexingState;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.Times;
public class FacetsApplet extends JApplet{
	private final class SimpleLayout extends PaneLayout implements SimpleTitles{
		private SimpleLayout(Container pane,TargetTest test,Facets facets){
			super(pane,test,facets);
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
	@Override
	public void init(){
		String type=getParameter("type");
		JPanel content=(JPanel)getContentPane();
		content.addComponentListener(new ComponentListener(){
			@Override
			public void componentResized(ComponentEvent e){
				if(false)System.out.println("componentResized: "+content.getSize());
			}
			@Override
			public void componentShown(ComponentEvent e){}
			@Override
			public void componentMoved(ComponentEvent e){}
			@Override
			public void componentHidden(ComponentEvent e){}
		});
		if(type==null||type.equalsIgnoreCase("html")){
			new HtmlSurface(content).buildSurface();
			return;
		}
		TargetTest[]simples=simpleValues(),tests=false?simples:selectingValues();
		content.setLayout(new GridLayout(tests==simples?3:2,1));
		Color color=Color.CYAN;
		for(TargetTest test:tests){
			if(false&&(tests==simples&&test!=TogglingLive))continue;
			JPanel pane=new JPanel();
			if(false)pane.setBackground(color=color.darker());
			pane.setBorder(createCompoundBorder(createEmptyBorder(10,10,10,10),
					createEtchedBorder(EtchedBorder.LOWERED)));
			content.add(pane);
			SurfaceCore surface=test.isSimple()?new SimpleSurface(test.name(),test){
				@Override
				protected void buildLayout(){
					facets.times.setResetWait(50);
					facets.times.doTime=false;
					if(test==TogglingLive)
						facets.setTargetLive(TITLE_TOGGLED,
								(boolean)facets.getTargetState(TITLE_TOGGLING));
					new SimpleLayout(pane,test,facets).build();
				};
			}
			:new SelectingSurface(){
				private SelectingLayout layout=new SelectingLayout(pane,test,this);
				@Override
				protected void onRetargeted(){
					super.onRetargeted();
					layout.adjustCards();
				}
				@Override
				protected void buildLayout(){
					layout.build();
				};
			};
			surface.buildSurface();
		}
	}
	private final class SelectingLayout extends PaneLayout implements SelectingTitles{
		private final IndexingFramePolicy frame;
		final CardLayout cards=new CardLayout();
		final JComponent cardsParent=new JPanel(cards);
		private SelectingLayout(Container pane,TargetTest test,SelectingSurface surface){
			super(pane,test,surface.facets);
			frame=surface.frame;
		}
		private String cardsKey(ContentType type){
			return type==ShowChars?"longer":"shorter";
		}
		void adjustCards(){
			cards.show(cardsParent,cardsKey(getIndexedType(facets)));
		}
		@Override
		public void build(){
			pane.setLayout(new GridLayout(2,1));
			String title=frame.indexingTitle;
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
				cards.addLayoutComponent(inner,cardsKey(longer?ShowChars:Standard));
				if(false)inner.add(newLabelFacet(TITLE_INDEXED).mount);
				inner.add(newTextFieldFacet(TITLE_EDIT+tail,20,false).mount);
				if(false)inner.add(newLabelFacet(TITLE_EDIT+tail).mount);
				if(longer)inner.add(newLabelFacet(TITLE_CHARS+tail).mount);
				inner.add(newCheckBoxFacet(TITLE_LIVE).mount);
				adjustCards();
			}
		}
	}
}