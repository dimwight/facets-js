package fjs.swing;
import static facets.facet.FacetFactory.dragNotifyInterim;
import static fjs.SurfaceCore.TargetTest.Indexing;
import facets.core.superficial.STextual.Update;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import fjs.SelectingSurface;
import fjs.SimpleTitles;
import fjs.SurfaceCore;
import fjs.SurfaceCore.TargetTest;
import fjs.globals.Facets;
import fjs.globals.Facets.IndexingState;
import fjs.globals.Facets.IndexingFramePolicy;
import fjs.util.Tracer;
abstract class PaneLayout extends Tracer{
	protected final Container pane;
	protected final TargetTest test;
	protected final SurfaceCore surface;
	protected final Facets facets;
	PaneLayout(Container pane,TargetTest test,SurfaceCore surface){
		this.pane=pane;
		this.test=test;
		this.surface=surface;
		this.facets=surface.facets;
	}
	public abstract void build();
	protected SwingFacet newButtonFacet(String title){
		JButton button=new JButton(title);
		return new SwingFacet<JButton,String>(button,title,facets){
			@Override
			protected void addFieldListener(){
				button.addActionListener(this);
			}
			@Override
			protected String getFieldState(){
				return "Fired";
			}
			@Override
			protected void updateField(String update){}
		};
	}
	protected final SwingFacet newTextFieldFacet(String title,int cols,boolean interim){
		JTextField field=new JTextField();
		field.setColumns(cols);
		SwingFacet<JTextField,String>facet=new SwingFacet<JTextField,String>(field,title,facets){	
			@Override
			protected void updateField(String update){
				field.setText(update);
			}
			@Override
			protected String getFieldState(){
				return field.getText();
			}
			@Override
			protected void addFieldListener(){
				field.addActionListener(this);
			}
		};
		if(interim)field.getDocument().addDocumentListener(new DocumentListener(){
			private String then;
			@Override
			public void removeUpdate(DocumentEvent e){
				changedUpdate(e);
			}
			@Override
			public void insertUpdate(DocumentEvent e){
				changedUpdate(e);
			}
			@Override
			public void changedUpdate(DocumentEvent doc){
				String now=field.getText();
				ActionEvent action=new ActionEvent(field,now.length(),"changedUpdate");
				if(field.hasFocus()&&!now.equals("")&&!now.equals(then))
					SwingUtilities.invokeLater(()->{
						facet.actionPerformed(action);
						then=now;
					});
			}
		});
		return facet;
	}
	protected final SwingFacet newLabelFacet(String title){
		return new SwingFacet<JLabel,String>(new JLabel(){
			@Override
			public Font getFont(){
				Font superFont=super.getFont();
				return superFont==null?null:superFont.deriveFont(Font.PLAIN);
			}
		},title,facets){
			@Override
			protected void updateField(String update){
				field.setText(update);
			}
			@Override
			protected String getFieldState(){
				return field.getText();
			}
			@Override
			protected void addFieldListener(){}
		};
	}
	protected final SwingFacet newCheckBoxFacet(String title){
		return new SwingFacet<JCheckBox,Boolean>(new JCheckBox(),title,facets){
			@Override
			protected void updateField(Boolean update){
				field.setSelected(update);
			}
			@Override
			protected Boolean getFieldState(){
				return field.isSelected();
			}
			@Override
			protected void addFieldListener(){
				field.addActionListener(this);
			}
		};
	}
	protected final SwingFacet newNumberFieldFacet(String title,int cols){
		JFormattedTextField field=new JFormattedTextField();
		field.setHorizontalAlignment(JFormattedTextField.RIGHT);
		field.setColumns(cols);
		final NumberFormat formatter=DecimalFormat.getInstance();
	  formatter.setMaximumFractionDigits(0);
		formatter.setMinimumFractionDigits(0);
		return new SwingFacet<JFormattedTextField,Double>(field,title,facets){	
			@Override
			protected void updateField(Double update){
				field.setText(formatter.format(update));
			}
			@Override
			protected Double getFieldState(){
				return Double.valueOf(field.getText());
			}
			@Override
			protected void addFieldListener(){
				field.addActionListener(this);
			}
		};
	}
	protected final SwingFacet newComboBoxFacet(String title){
		return new SwingFacet<JComboBox,Integer>(
				new JComboBox(facets.getIndexingState(title).uiSelectables),
				title,facets){
			@Override
			public void actionPerformed(ActionEvent e){
				if(!facets.getTargetState(title).equals(field.getSelectedIndex()))
					super.actionPerformed(e);
			}
			@Override
			protected void updateField(Integer update){
				field.setSelectedIndex(update);
			}
			@Override
			protected Integer getFieldState(){
				return field.getSelectedIndex();
			}
			@Override
			protected void addFieldListener(){
				field.addActionListener(this);
			}
		};
	}
	protected final SwingFacet newListFacet(String title){
		SwingFacet<JList,Integer>facet=new SwingFacet<JList,Integer>(
				new JList(),title,facets){
			@Override
			public void actionPerformed(ActionEvent e){
				if(field.getSelectedIndex()<0)return;
				if(!facets.getTargetState(title).equals(field.getSelectedIndex()))
					super.actionPerformed(e);
			}
			@Override
			protected void updateField(Integer update){
				String[]selectables=facets.getIndexingState(title).uiSelectables;
				field.setModel(new AbstractListModel(){
					@Override
					public Object getElementAt(int index){
						return selectables[index];
					}
					@Override
					public int getSize(){
						return selectables.length;
					}
				});
				field.setSelectedIndex(update);
				field.repaint();
			}
			@Override
			protected Integer getFieldState(){
				return field.getSelectedIndex();
			}
			@Override
			protected void addFieldListener(){
				field.addListSelectionListener(new ListSelectionListener(){
					@Override
					public void valueChanged(ListSelectionEvent e){
						actionPerformed(new ActionEvent(e.getSource(),e.hashCode(),e.toString()));
					}
				});
			}
		};
		facet.field.setBorder(BorderFactory.createLoweredBevelBorder());
		return facet;
	}
}