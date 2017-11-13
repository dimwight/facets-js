package fjs.globals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import fjs.core.IndexingFrame;
import fjs.core.Notifiable;
import fjs.core.SFacet;
import fjs.core.SFrameTarget;
import fjs.core.SIndexing;
import fjs.core.SNumeric;
import fjs.core.STarget;
import fjs.core.STargeter;
import fjs.core.STextual;
import fjs.core.SToggling;
import fjs.core.STrigger;
import fjs.core.TargetCore;
import fjs.core.TargeterCore;
import fjs.util.Debug;
import fjs.util.NumberPolicy;
import fjs.util.Tracer;
import fjs.util.Util;
import jsweet.lang.Interface;
import jsweet.lang.Optional;
public final class Facets extends Tracer{
	public final Times times=new Times();
	public final boolean doTrace;
	private final HashMap<String,STargeter>titleTargeters=new HashMap();
	private final HashMap<String,STarget>titleTrees=new HashMap();
	private final IndexingFrame root;
	private final Notifiable notifiable=new Notifiable(){
		@Override
		public void notify(Object notice){
			String msg="> Surface for "+Debug.info(rootTargeter)+" notified by "+notice;
			if(times.doTime)times.traceElapsed(msg);
			else trace(msg);
			STarget target=rootTargeter.target();
			rootTargeter.retarget(target);
		  msg="> Targeters retargeted on "+Debug.info(target);
			if(times.doTime)times.traceElapsed(msg);
			else trace(msg);
		  if(false)putTitleTargeters(rootTargeter);
		  callOnRetargeted();
			rootTargeter.retargetFacets();
			msg="> Facets retargeted in "+Debug.info(rootTargeter);
			if(times.doTime)times.traceElapsed(msg);
			else trace(msg);
		}
		@Override
		public String title(){
			throw new RuntimeException("Not implemented in "+this);
		}
	};
	private STargeter rootTargeter;
	@Override
	protected void doTraceMsg(String msg){
		if(doTrace||(Debug.trace&&msg.startsWith(">>")))super.doTraceMsg(msg);
	}
  public interface FacetsApp{
		Object getContentTrees();
		void onRetargeted(String activeTitle);
		void buildLayout();
	}
	Facets(String top,boolean trace){
		super(top);
		this.doTrace=trace;
		SIndexing indexing=new SIndexing("RootIndexing",new SIndexing.Coupler(){
			private Object[]thenIndexables;
			@Override
			protected Object[]getIndexables(SIndexing i){
				Object[]trees=titleTrees.values().toArray();
				boolean equal=Util.arraysEqual(trees,thenIndexables);
				if(!equal)trace("> New trees: ",trees);
				thenIndexables=trees;
				return trees;
			}
		});
		root=new IndexingFrame("RootFrame",indexing);
		if(false)trace(" > Created trees root ",root);
	}
	public void buildApp(FacetsApp app){
		this.onRetargeted=title->{
			app.onRetargeted(title);
		};
	  trace("Building surface...");
	  Object trees=app.getContentTrees();
	  if(trees instanceof Object[])
	  	for(Object each:(Object[])trees)addContentTree((STarget)each);
	  else addContentTree((STarget)trees);
		trace(" > Building targeter tree for root=",root);
		if(rootTargeter==null)rootTargeter=((TargetCore)root).newTargeter();
		rootTargeter.setNotifiable(notifiable);
		rootTargeter.retarget(root);
		putTitleTargeters(rootTargeter);
		trace(" > Created targeters="+titleTargeters.values().size());
		callOnRetargeted();
	  this.trace("Built targets, created targeters");
	  app.buildLayout();
	  this.trace("Attached and laid out facets");
	  this.trace("Surface built.");
	}
	public void activateContentTree(String title){
		trace(" > Activating content title="+title);
		STarget tree=titleTrees.get(title);
		if(tree==null)throw new IllegalStateException("Null tree in "+this);
		root.indexing().setIndexed(tree);
		notifiable.notify(root);
	}
	public void addContentTree(STarget add){
		String title=add.title();
		trace(" > Adding content title="+title);
		titleTrees.put(title,add);
		root.indexing().setIndexed(add);
	}
	private void putTitleTargeters(STargeter t){
		String title=t.title();
		STargeter then=titleTargeters.get(title);
		titleTargeters.put(title,t);
		STargeter[]elements=((TargeterCore)t).titleElements();
		if(false&&then==null)trace("> Added targeter: title="+title+
				(false?(": elements="+elements.length)
				:(": titleTargeters="+titleTargeters.values().size())));
		for(STargeter e:elements)putTitleTargeters(e);
	}
	private Consumer<String>onRetargeted;
	private void callOnRetargeted(){
		String title=root.indexedTarget().title();
	  trace(" > Calling onRetargeted with active="+title);
		onRetargeted.accept(title);
	}
	@Interface
	public static class TargetCoupler{
		@Optional
		public BiConsumer<Object,String>targetStateUpdated;
	}
	@Interface
	public static class TextualCoupler extends TargetCoupler{
		@Optional
		public String passText;
		@Optional
		public Function<String,String>getText;
		@Optional
		public BiPredicate<String,String>isValidText;
	}
	@Interface
	public static class TogglingCoupler extends TargetCoupler{
		public Boolean passSet;
	}
	@Interface
	public static class NumericCoupler extends TargetCoupler{
		public Double passValue;
		public Double min;
		public Double max;
	}
	public STarget newTextualTarget(String title,TextualCoupler c){
		STextual textual=new STextual(title,new STextual.Coupler(){
			@Override
			public void textSet(STextual target){
				updatedTarget(target,c);
			}
			@Override
			protected String getText(STextual t){
				Function<String,String>getText=c.getText;
				String title=t.title();
				if(getText==null)throw new IllegalStateException("Null getText for "+title);
				else return getText.apply(title);
			}
			@Override
			public boolean isValidText(STextual t,String text){
				BiPredicate<String,String>valid=c.isValidText;
				return valid==null?true:valid.test(t.title(),text);
			}
		});
		String passText=c.passText;
		if(passText!=null)textual.setText(passText);
		trace(" > Created textual ",textual);
		return textual;
	}
	public STarget newTogglingTarget(String title,TogglingCoupler c){
		Boolean passSet=c.passSet;
		if(passSet==null)throw new IllegalStateException(
				"Null passSet for "+title);
		SToggling toggling=new SToggling(title,passSet,new SToggling.Coupler(){
			@Override
			public void stateSet(SToggling target){
				updatedTarget(target,c);
			}
		});
		trace(" > Created toggling ",toggling);
		return toggling;
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
	public STarget newTriggerTarget(String title,TargetCoupler c){
		STrigger trigger=new STrigger(title,new STrigger.Coupler(){
			@Override
			public void fired(STrigger t){
				updatedTarget(t,c);
			}
		});
		trace(" > Created trigger ",trigger);
		return trigger;
	}
	public STarget newTargetGroup(String title,STarget[]members){
		TargetCore group=new TargetCore(title,members);
		trace(" > Created target group "+Debug.info(group)+" ",members);
		return group;
	}
	private void updatedTarget(STarget target,TargetCoupler c){
		String title=target.title();
		trace(" > Updated target ",target);
		if(c.targetStateUpdated!=null){
			Object state=target.state();
			c.targetStateUpdated.accept(state,title);
		}
	}
	@Interface
	public static class IndexingCoupler extends TargetCoupler{
		public int passIndex;
		public Function<String,Object[]>getIndexables;
		public Function<Object,String>newUiSelectable;
	}
	public STarget newIndexingTarget(String title,IndexingCoupler c){
		SIndexing indexing=new SIndexing(title,new SIndexing.Coupler(){
			@Override
			protected Object[]getIndexables(SIndexing i){
				return c.getIndexables.apply(i.title());
				}
			@Override
			public void indexSet(SIndexing target){
				updatedTarget(target,c);
			}
			@Override
			protected String[]getFacetSelectables(SIndexing i){
				Function<Object,String>getter=c.newUiSelectable;
				if(getter==null)return super.getFacetSelectables(i);
				List<String>selectables=new ArrayList();
				int at=0;
				for(Object each:i.indexables())selectables.add(getter.apply(each));
				 return selectables.toArray(new String[]{});
			}
		});
		Integer passIndex=c.passIndex;
		if(passIndex==null)throw new IllegalStateException("Null passIndex for "+title);
		else indexing.setIndex(passIndex);
		trace(" > Created indexing ",indexing);
		return indexing;
	}
	@Interface
	public static class IndexingState{
		public String[]uiSelectables;
		public Object indexed;
	}
	public IndexingState getIndexingState(String title){
		STarget titleTarget=titleTarget(title);
		if(titleTarget==null)throw new IllegalStateException("Null target for "+title);
		SIndexing indexing=(SIndexing)titleTarget;
		return new IndexingState(){{
			uiSelectables=indexing.facetSelectables();
			indexed=indexing.indexed();
		}};
	}
	private int indexingFrames;
	@Interface
	public static class IndexingFramePolicy{
		public String indexingTitle;
		public Supplier<Object[]>getIndexables;
		@Optional
		public String frameTitle;
		@Optional
		public Function<Object,String>newUiSelectable;
		@Optional
		public Supplier<STarget[]>newFrameTargets;
		@Optional
		public Function<Object,String>newIndexedTreeTitle;
		@Optional
		public BiFunction<Object,String,STarget>newIndexedTree;
	}
	private static final class LocalIndexingFrame extends IndexingFrame{
		private final IndexingFramePolicy p;
		private LocalIndexingFrame(String title,SIndexing indexing,IndexingFramePolicy p){
			super(title,indexing);
			this.p=p;
		}
		protected STarget[]lazyElements(){
			Supplier<STarget[]>getter=p.newFrameTargets;
			STarget[]got=getter!=null?getter.get():new STarget[]{};
			if(false)trace(".lazyElements: ",got);
			return got==null?new STarget[]{}:STarget.newTargets(got);
		}
		@Override
		protected STarget newIndexedTargets(Object indexed){
			if(false)trace(".newIndexedTargets: indexed="+(indexed!=null));
			Function<Object,String>titler=p.newIndexedTreeTitle;
			String indexedTargetsTitle=titler==null?title()+"|indexed"
					:titler.apply(indexed);
			return p.newIndexedTree==null?new TargetCore(indexedTargetsTitle)
					:p.newIndexedTree.apply(indexed,indexedTargetsTitle);
		}
	}
	public STarget newIndexingFrame(IndexingFramePolicy p){
		String frameTitle=p.frameTitle!=null?p.frameTitle
				:"IndexingFrame"+indexingFrames++,
			indexingTitle=p.indexingTitle!=null?p.indexingTitle
					:frameTitle+".Indexing";
		SIndexing indexing=new SIndexing(indexingTitle,new SIndexing.Coupler(){
			private Object[]thenIndexables,thenSelectables;
			@Override
			protected Object[]getIndexables(SIndexing i){
				Object[]got=p.getIndexables.get();
				if(got==null)throw new IllegalStateException("Null getIndexables for "+i.title());
				boolean equal=Util.arraysEqual(got,thenIndexables);
				if(!equal)trace("> Got new indexables in "+Debug.info(i)+": ",got);
				thenIndexables=got;
				return got;
			}
			@Override
			protected String[]getFacetSelectables(SIndexing i){
				Function<Object,String>getter=p.newUiSelectable;
				if(getter==null)return super.getFacetSelectables(i);
				List<String>selectables=new ArrayList();
				int at=0;
				for(Object each:i.indexables())selectables.add(getter.apply(each));
				String[]got=selectables.toArray(new String[]{});
				boolean equal=Util.arraysEqual(got,thenSelectables);
				if(!equal)trace("> Got new selectables in "+Debug.info(i)+": ",got);
				thenSelectables=got;
				return got;
			}
		});
		indexing.setIndex(0);
		IndexingFrame frame=new LocalIndexingFrame(frameTitle,indexing,p);
		trace(" > Created indexing frame ",frame);
		return frame;
	}
	private Object getTargetFramed(String title){
		SFrameTarget frame=(SFrameTarget)titleTarget(title);
		if(frame==null)throw new IllegalStateException("Null frame for "+title);
		return frame.framed;
	}
	private STarget titleTarget(String title){
		STargeter targeter=titleTargeters.get(title);
		return targeter==null?null:targeter.target();
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
			@Override
			public void retarget(STarget target){
				Object state=target.state();
				String title=target.title();
				trace(" > Updating UI for "+title+ " with state=",state);
				facetUpdated.accept(state);
			}
			@Override
			public String toString(){
				return "#"+id;
			}
		};
		trace(" > Attaching facet "+facet+ " to",targeter);
		targeter.attachFacet(facet);
	}
	public void updateTargetState(String title,Object update){
		trace(" > Updating target state for title="+title+" update=",update);
		STarget target=titleTarget(title);
		if(target==null)throw new IllegalStateException("Null target for "+title);
		target.updateState(update);
	}
	public void notifyTargetUpdated(String title){
		STarget target=titleTarget(title);
		if(target==null)throw new IllegalStateException("Null target for "+title);
		target.notifyParent();
	}
	public void updateTargetWithNotify(String title,Object update){
		updateTargetState(title,update);
		notifyTargetUpdated(title);
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
		if(target==null)throw new IllegalStateException("Null target for "+title);
		else target.setLive(live);
	}
	public boolean isTargetLive(String title){
		STarget target=titleTarget(title);
		if(target==null)throw new IllegalStateException("Null target for "+title);
		else return target.isLive();
	}
	public final class Times{
		public boolean doTime=false;
		private long resetWait=1000;
	  private long then,start;
		private boolean restarted;
	  private final boolean debug=false;
	  public void setResetWait(int millis){
	  	if(debug)Util.printOut("Times.setResetWait: wait=",millis);
	  	start=newMillis();
			doTime=true;
			resetWait=millis;
		}
		/**
		The time since the last auto-reset. 
		<p>Interval for reset set by {@link #resetWait}. 
		 */
	  public long elapsed(){
	  	long now=newMillis();
	  	if(now-then>resetWait){
	  		start=now;
	  		restarted=true;
	  		if(debug)Util.printOut("Times: reset resetWait="+resetWait);
	  	}
	  	else restarted=false;
			return(then=now)-start;
	  }
	  /**
		Print {@link #elapsed()} followed by the message. 
		 */
		void traceElapsed(String msg){
	    if(!doTime){
	    	if(!Debug.trace){
	    		start=then=newMillis();
	    		if(debug)Util.printOut("Times.printElapsed: times=",doTime);
	    	}
	    	return;
	    }
			long elapsed=elapsed();
	    String elapsedText=true&&elapsed>5*1000?(Util.fxs(elapsed/1000d))
	    		:(""+elapsed), 
	    	toPrint=(restarted?"\n":"")+elapsedText+(msg!=null?":\t"+msg:"");
			Util.printOut(toPrint);
	  }
		private long newMillis(){
			return System.currentTimeMillis();
		}
	}
	private STarget _newFrameTarget(String title,Object toFrame,Object[]editTargets){
		STarget[]asTargets=STarget.newTargets(editTargets);
		return false?new _LocalFrameTarget(title,toFrame,asTargets)
				:new SFrameTarget(title,toFrame){
			@Override
			protected STarget[]lazyElements(){
				return asTargets;
			}
		};
	}
	private static final class _LocalFrameTarget extends SFrameTarget{
		private final STarget[]newIndexedTargets;
		private _LocalFrameTarget(String title,Object toFrame,
				STarget[]newIndexedTargets){
			super(title,toFrame);
			this.newIndexedTargets=newIndexedTargets;
		}
		@Override
		protected STarget[]lazyElements(){
			return newIndexedTargets;
		}
	}
}
