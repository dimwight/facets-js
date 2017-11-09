package fjs.swing;
import static fjs.SelectableType.*;
import static fjs.SurfaceCore.TargetTest.*;
import static javax.swing.BorderFactory.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import fjs.ContentingSurface;
import fjs.SelectingSurface;
import fjs.SimpleSurface;
import fjs.SurfaceCore;
import fjs.SurfaceCore.TargetTest;
import fjs.globals.Facets.IndexingState;
import fjs.globals.Globals;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.globals.Facets.Times;
public class FacetsApplet extends JApplet{
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
		TargetTest[]simples=simpleValues(),tests=false?simples
				:new TargetTest[]{false?Selecting:Contenting};
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
					new SimpleLayout(pane,test,this).build();
				}
			}
			:test==Contenting?new ContentingSurface(){
				private SelectingLayout layout=new ContentingLayout(pane,test,this);
				@Override
				protected void onRetargeted(String activeTitle){
					super.onRetargeted(activeTitle);
					layout.adjustCards(activeTitle);
				}
				@Override
				protected void buildLayout(){
					layout.build();
				}
			}:new SelectingSurface(Globals.newInstance(false),TargetTest.Selecting){
				private SelectingLayout layout=new SelectingLayout(pane,test,this);
				@Override
				protected void onRetargeted(String activeTitle){
					super.onRetargeted(activeTitle);
					layout.adjustCards(activeTitle);
				}
				@Override
				protected void buildLayout(){
					layout.build();
				}
			};
			surface.buildSurface();
		}
	}
}