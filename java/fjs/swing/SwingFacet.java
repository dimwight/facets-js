package fjs.swing;
import facets.util.Tracer;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import fjs.globals.Facets;
abstract class SwingFacet<C extends JComponent,T> extends Tracer 
		implements Consumer<T>,ActionListener{
	final JPanel mount;
	final C field;
	private final String title;
	private final JLabel label;
	private final Facets facets;
	SwingFacet(C field,String title,Facets facets){
		this.field=field;
		this.title=title;
		mount=new JPanel(new FlowLayout(FlowLayout.LEFT));
		label=new JLabel(title.replaceAll("\\|.*",""));
		if(!(field instanceof JButton))mount.add(label);
		mount.add(field);
		addFieldListener();
		(this.facets=facets).attachFacet(title,this);
	}
	@Override
	final public void accept(T update){
		updateField(update);
		boolean live=facets.isTargetLive(title);
		field.setEnabled(live);
		label.setEnabled(live);
	}
	@Override
	public void actionPerformed(ActionEvent e){
		facets.updateTargetWithNotify(title,getFieldState());
	}
	protected abstract void addFieldListener();
	protected abstract T getFieldState();
	protected abstract void updateField(T update);
}