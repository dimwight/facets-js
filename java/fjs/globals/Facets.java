package fjs.globals;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import fjs.SelectingSurface.TextContent;
import fjs.core.Notifiable;
import fjs.core.SFacet;
import fjs.core.SFrameTarget;
import fjs.core.SIndexing;
import fjs.core.SNumeric;
import fjs.core.STarget;
import fjs.core.STargeter;
import fjs.core.STextual;
import fjs.core.SToggling;
import fjs.core.TargetCore;
import fjs.core.select.IndexingFrame;
import fjs.util.Debug;
import fjs.util.NumberPolicy;
import fjs.util.Times;
import fjs.util.Tracer;
import jsweet.lang.Interface;
import jsweet.lang.Optional;
public final class Facets extends Tracer{
	@Interface
	public static class IndexingValues{
		public Supplier indexed;
	}
	@Interface
	public static class SelectingFrameProxy{
		public String title;
		public String indexingTitle;
		public Object[]content;
		public Function<Object,Object[]>newEditElements;
		@Optional
		public Object frame;
		@Optional
		public Supplier selectedContent;
		@Optional
		public Consumer<Integer>setIndex;
	}
	public static class TargetCoupler{
		public BiConsumer<String,Object>targetStateUpdated;
	}
	@Interface
	public static class TextualCoupler extends TargetCoupler{
		@Optional
		public String passText;
		@Optional
		public BiPredicate<String,String>isValidText;
		@Optional
		public Function<String,String>getText;
		@Optional
		public Predicate<String>updateInterim;
	}
	@Interface
	public static class TogglingCoupler extends TargetCoupler{
		public Boolean passSet;
	}
	@Interface
	public static class IndexingCoupler extends TargetCoupler{
		public Object[]passIndexables;
		public int passIndex;
	}
	@Interface
	public static class NumericCoupler extends TargetCoupler{
		@Optional
		public Double passValue;
		public Double min;
		public Double max;
	}
	public final boolean trace;
	private final HashMap<String,STargeter>titleTargeters=new HashMap();
	private STargeter targeterTree;
	private final Notifiable notifiable=new Notifiable(){
		@Override
		public void notify(Object notice){
			String msg="> Surface for "+Debug.info(targeterTree)+" notified by "+notice;
			if(Times.times)Times.printElapsed(msg);
			else trace(msg);
			STarget targets=targeterTree.target();
			targeterTree.retarget(targets);
		  trace("> Targeters retargeted on ",targets);
			targeterTree.retargetFacets();
			msg="> Facets retargeted in "+Debug.info(targeterTree);
			if(Times.times)Times.printElapsed(msg);
			else trace(msg);
		}
		@Override
		public String title(){
			throw new RuntimeException("Not implemented in "+this);
		}
	};
	Facets(String top,boolean trace){
		super(top);
		this.trace=trace;
	}
	@Override
	protected void traceOutput(String msg){
		if(trace||(Debug.trace&&msg.startsWith(">>")))super.traceOutput(msg);
	}
	public STarget newNumericTarget(String title,NumericCoupler c){
		SNumeric numeric=new SNumeric(title,c.passValue,new SNumeric.Coupler(){
			@Override
			public void valueSet(SNumeric n){
				updatedTarget(n,c);
			}
			@Override
			public NumberPolicy policy(SNumeric n){
				double min=c.min!=null?c.min:Integer.MIN_VALUE,
						max=c.max!=null?c.max:Integer.MAX_VALUE;
				return new NumberPolicy(min,c.max);
			}
		});
		trace(" > Created numeric ",numeric);
		return numeric;
	}
	public STarget newTextualTarget(String title,TextualCoupler c){
		STextual textual=new STextual(title,new STextual.Coupler(){
			@Override
			public void textSet(STextual target){
				updatedTarget(target,c);
			}
			@Override
			public boolean isValidText(STextual t,String text){
				BiPredicate<String,String>isValidText=c.isValidText;
				return isValidText==null?true:isValidText.test(t.title(),text);
			}
			@Override
			public boolean updateInterim(STextual t){
				Predicate<String>updateInterim=c.updateInterim;
				return updateInterim==null?false:updateInterim.test(t.title());
			}
			@Override
			protected String getText(STextual t){
				Function<String,String>getText=c.getText;
				if(getText==null)throw new IllegalStateException("Null getText in "+this);
				else return getText.apply(t.title());
			}
		});
		String passText=c.passText;
		if(passText!=null)textual.setText(passText);
		trace(" > Created textual ",textual);
		return textual;
	}
	public Object newTogglingTarget(String title,TogglingCoupler c){
		Boolean passSet=c.passSet;
		if(passSet==null)throw new IllegalStateException(
				"Null passSet in "+this);
		SToggling toggling=new SToggling(title,passSet,new SToggling.Coupler(){
			@Override
			public void stateSet(SToggling target){
				updatedTarget(target,c);
			}
		});
		if(false)toggling.set(passSet);
		trace(" > Created toggling ",toggling);
		return toggling;
	}
	public Object newIndexingTarget(String title,IndexingCoupler c){
		SIndexing indexing=new SIndexing(title,c.passIndexables,new SIndexing.Coupler(){
			@Override
			public void indexSet(SIndexing target){
				updatedTarget(target,c);
			}
		});
		Integer passIndex=c.passIndex;
		if(passIndex==null)throw new IllegalStateException("Null passIndex in "+this);
		else indexing.setIndex(passIndex);
		trace(" > Created indexing ",indexing);
		return indexing;
	}
	public IndexingValues getIndexingValues(String title){
		STarget titleTarget=titleTarget(title);
		if(titleTarget==null)throw new IllegalStateException("Null target in "+this);
		return new IndexingValues(){{
			indexed=()->((SIndexing)titleTarget).indexed();
		}};
	}
	private void updatedTarget(STarget target,TargetCoupler c){
		String title=target.title();
		trace(" > Updated target ",target);
		if(c.targetStateUpdated!=null){
			Object state=target.state();
			c.targetStateUpdated.accept(title,state);
		}
	}
	public Object newTargetsGroup(String title,Object...members){
		TargetCore group=new TargetCore(title,STarget.newTargets(members));
		trace(" > Created target group "+Debug.info(group)+" ",members);
		return group;
	}
	private final static class LocalSelectingFrame extends IndexingFrame{
		private final SelectingFrameProxy frame;
		private LocalSelectingFrame(SelectingFrameProxy frame){
			super(frame.title,frame.content,newSelectingIndexing(frame));
			this.frame=frame;
			SIndexing indexing=indexing();
			frame.setIndex=index->indexing.setIndex(index);
			frame.selectedContent=()->indexing.indexed();
		}
		static SIndexing newSelectingIndexing(SelectingFrameProxy frame){
			SIndexing indexing=new SIndexing(frame.indexingTitle,frame.content,
					new SIndexing.Coupler(){
				@Override
				public void indexSet(SIndexing i){
					((IndexingFrame)frame.frame).defineSelection(i.indexed());
				}
			});
			return indexing;
		}
		@Override
		protected SFrameTarget newIndexedFrame(Object indexed){
		Function<Object,Object[]>editElements=frame.newEditElements;
			return new SFrameTarget(frame.title+":selected",indexed){
				@Override
				protected STarget[]lazyElements(){
					return STarget.newTargets(editElements.apply(indexed));
				}
			};
		}
	}
	public void buildSelectingFrame(SelectingFrameProxy frame){
		frame.frame=new LocalSelectingFrame(frame);
		trace(" > Created frame ",frame.frame);
	}
	public void buildTargeterTree(Object targetTree){
		trace(" > Initial retargeting on ",targetTree);
		targeterTree=((TargetCore)targetTree).newTargeter();
		targeterTree.setNotifiable(notifiable);
		targeterTree.retarget((STarget)targetTree);
		addTitleTargeters(targeterTree);
	}
	private void addTitleTargeters(STargeter t){
		String title=t.title();
		STargeter then=titleTargeters.put(title,t);
		STargeter[]elements=t.elements();
		trace("> Added targeter: title="+title+": elements="+elements.length);
		for(STargeter e:elements)addTitleTargeters(e);
	}
	public void attachFacet(String title,Consumer facetUpdated){
		if(facetUpdated==null)throw new IllegalArgumentException(
				"Null facetUpdated for "+title);
		else if(title==null||title.equals(""))throw new IllegalArgumentException(
				"Null or empty title for "+facetUpdated);
		STargeter targeter=titleTargeters.get(title);
		if(targeter==null)throw new IllegalArgumentException("Null targeter for "+title);
		SFacet facet=new SFacet(){
			private final int id=Tracer.ids++;
			private Object stateThen;
			@Override
			public void retarget(STarget target){
				Object state=target.state();
				String title=target.title();
				if(false)trace(".attachFacet #"+id+ ".retarget: state="+state+" stateThen="+stateThen);
				if(true||!state.equals(stateThen)){
					trace(" > Updating UI for '"+title+ "' with state=",state);
					facetUpdated.accept(stateThen=state);
				}
			}
			@Override
			public String toString(){
				return "#"+id;
			}
		};
		trace(" > Attaching facet "+facet+ " to",targeter);
		targeter.attachFacet(facet);
	}
	private STarget titleTarget(String title){
		STargeter targeter=titleTargeters.get(title);
		return targeter==null?null:targeter.target();
	}
	public void updateTargetState(String title,Object update){
		trace(" > Updating target state for title="+title+" update=",update);
		STarget target=titleTarget(title);
		if(target==null)throw new IllegalStateException("Null target for "+title);
		target.updateState(update);
		target.notifyParent();
	}
	public Object getTargetState(String title){
		STarget target=titleTarget(title);
		if(target==null)return null;
		Object state=target.state();
		trace(" > Getting target state for title="+title+" state=",state);
		return state;
	}
	public void setTargetLive(String title,boolean live){
		STarget target=titleTarget(title);
		if(target==null)throw new IllegalStateException("Null target in "+this);
		else target.setLive(live);
	}
	public boolean isTargetLive(String title){
		STarget target=titleTarget(title);
		if(target==null)throw new IllegalStateException("Null target in "+this);
		else return target.isLive();
	}
}
