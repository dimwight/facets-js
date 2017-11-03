package fjs.swing.html;
import static fjs.util.Debug.info;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import facets.util.HtmlFormBuilder.FormInput;
import facets.util.HtmlFormBuilder.FormTag;
import facets.util.Tracer;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.Option;
public abstract class HtmlInputPaneMaster extends Tracer{
	private JEditorPane pane;
	final public void setCode(String code){
		((JEditorPane)getPane()).setText(code);
		for(SmartView view:views.values())view.refresh(code);
	}
	public JComponent getPane(){
		pane=pane!=null?pane:new JEditorPane();
		pane.setEditable(false);
		pane.setEditorKit(newEditorKit());
		return pane;
	}
	private final Map<String,SmartView>views=new HashMap();
	private final Map<String,String>values=new HashMap();
	protected HTMLEditorKit newEditorKit(){
		return new HTMLEditorKit(){
			@Override
			public ViewFactory getViewFactory(){
				return new HTMLFactory(){
					@Override
					public View create(Element elem){
						View view=super.create(elem);
						return view instanceof FormView?new SmartView(elem):view;
					}
				};
			}
		};
	}
	private final class SmartView extends FormView {
		@Override
		protected void submitData(String data){}
		private final Tracer t=Tracer.newTopped(info(this),true);
		private final String name;
		private final FormTag tag;
		private TextSmarts ts;
		SmartView(Element elem){
			super(elem);
			AttributeSet atts=elem.getAttributes();
			name=(String)atts.getAttribute(Attribute.NAME);
			String tagName=atts.getAttribute(StyleConstants.NameAttribute).toString(),
				inputType=(String)atts.getAttribute(Attribute.TYPE);
			if(name==null)throw new IllegalStateException("Null name in "+info(this));
			else if(tagName==null)throw new IllegalStateException("Null tagName for "+name);
			else if(tagName.equals("input")&&inputType==null)throw new IllegalStateException(
					"Null inputType for "+name);
			else tag=FormTag.getTag(tagName.toLowerCase().trim(),
					inputType==null?null:inputType.toLowerCase().trim());
			views.put(name,this);
		}
		@Override
		protected Component createComponent(){
			Component c=super.createComponent();
			if(c instanceof JTextComponent){
				final JTextComponent field=(JTextComponent)c;
				ts=new TextSmarts(field);
				field.addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent e){
						int keyCode=e.getKeyCode();
						switch(keyCode){
						case VK_ESCAPE:
							field.setText(value());
							field.selectAll();
							ts.resetUndo();
							break;
						case VK_ENTER:
							checkValue(field.getText());
							ts.resetUndo();
							break;
						default:
						}
					}
				});
			}
			else if(c instanceof JComboBox){
				final JComboBox box=(JComboBox)c;
				box.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						Object item=box.getSelectedItem();
						if(item instanceof Option)checkValue(((Option)item).getValue());
					}
				});
			}
			else if(c instanceof JCheckBox){
				final JCheckBox box=(JCheckBox)c;
				box.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						checkValue(Boolean.valueOf(box.isSelected()).toString());
					}
				});
			}
			else throw new RuntimeException("Not implemented for c="+info(c));
			return c;
		}
		void refresh(String code){
			String value=tag.findValue(code,name);
			values.put(name,value);
			Component c=getComponent();
			if(c instanceof JTextComponent){
				JTextComponent field=(JTextComponent)c;
				field.setText(value);
				field.setCaretPosition(0);
				if(false&&field.hasFocus())field.selectAll();
				ts.resetUndo();
			}
			else if(c instanceof JComboBox){
				JComboBox box=(JComboBox)c;
				int itemCount=box.getItemCount(),at=0;
				for(;at<itemCount;at++){
					Object item=box.getItemAt(at);
					if(item instanceof Option&&((Option)item).getValue().equals(value)
							||item.equals(value)){
						box.setSelectedIndex(at);
						break;
					}
				}
				if(at==itemCount)box.insertItemAt(value,0);
			}
			else if(c instanceof JCheckBox)((JCheckBox)c).setSelected(value.equals("true"));
		}
		private String value(){
			return values.get(name);
		}
		private void checkValue(String check){
			String put=values.put(name,check);
			trace(".checkValue: put="+put+" check="+check);
			if(check.equals(put))return;
			valueEdited(name,check);
		}
	}
	protected abstract void valueEdited(String name,String check);

}
