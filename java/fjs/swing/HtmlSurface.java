package fjs.swing;
import facets.util.HtmlFormBuilder.FormTag;
import java.awt.Container;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.swing.JScrollPane;
import fjs.SimpleSurface;
import fjs.core.STarget;
import fjs.swing.html.HtmlInputPaneMaster;
import fjs.swing.html.HtmlLayoutBuilder;
final class HtmlSurface extends SimpleSurface{
	private final HashMap<String,Consumer>updaters=new HashMap();
	private final HashMap<String,String>titles=new HashMap();
	private final HashMap<Consumer,Object>values=new HashMap();
	private final Container pane;
	HtmlSurface(Container pane){
		super(FacetsApplet.class.getSimpleName(),TargetTest.Textual);
		this.pane=pane;
	}
	@Override
	final protected void buildLayout(){
		if(test==TargetTest.TogglingLive)
			facets.setTargetLive(TITLE_TOGGLED,(boolean)facets.getTargetState(TITLE_TOGGLING));
		final String _LABEL="_";
		for(String id:new String[]{
			TITLE_TEXTUAL_FIRST,
			TITLE_TEXTUAL_FIRST+_LABEL,
			TITLE_TEXTUAL_SECOND,
			TITLE_TEXTUAL_SECOND+_LABEL
		}){
			Consumer updater=new Consumer(){
				@Override
				public void accept(Object value){
					values.put(this,value);
				}
			};
			String title=id.replace(_LABEL,"");
			facets.attachFacet(title,updater);
			updaters.put(id,updater);
			titles.put(id,title);
		}
		HtmlLayoutBuilder builder=new HtmlLayoutBuilder(new String[]{
			TITLE_TEXTUAL_FIRST,
			TITLE_TEXTUAL_FIRST+_LABEL,
			TITLE_TEXTUAL_SECOND,
			TITLE_TEXTUAL_SECOND+_LABEL
		}){
			@Override
			protected String getTitle(String id) {
				return titles.get(id);
			};
			@Override
			protected boolean useInputField(String name){
				return !name.endsWith(_LABEL);
			}
			@Override
			protected Object getValue(String id){
				return values.get(updaters.get(id));
			}
			@Override
			protected String newFieldCode(String id,Object value){
				return FormTag.newInputText(titles.get(id),
						inputCols(id),value.toString());
			}
			@Override
			protected int inputCols(String id){
				return 20;
			}
		};
		HtmlInputPaneMaster paneMaster=new HtmlInputPaneMaster(){
			@Override
			protected void valueEdited(String id,String value){
				facets.updateTargetState(titles.get(id),value);
				setCode(builder.buildForm());
}
		};
		pane.add(new JScrollPane(paneMaster.getPane()));
		paneMaster.setCode(builder.buildForm());
	}
	@Override
	protected STarget newTargetTree(){
		return super.newTargetTree();
	}

}