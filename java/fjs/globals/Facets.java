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
import fjs.SelectingSurface.TextContent;
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
	public final Times times=new Times();
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
	public final boolean doTrace;
	public Consumer onRetargeted;
	private final HashMap<String,STargeter>titleTargeters=new HashMap();
	private STargeter targeterTree;
	private final Notifiable notifiable=new Notifiable(){
		@Override
		public void notify(Object notice){
			String msg="> Surface for "+Debug.info(targeterTree)+" notified by "+notice;
			if(times.doTime)times.traceElapsed(msg);
			else trace(msg);
			STarget targets=targeterTree.target();
			targeterTree.retarget(targets);
		  trace("> Targeters retargeted on ",targets);
		  putTitleTargeters(targeterTree);
		  onRetargeted();
			targeterTree.retargetFacets();
			msg="> Facets retargeted in "+Debug.info(targeterTree);
			if(times.doTime)times.traceElapsed(msg);
			else trace(msg);
		}
		@Override
		public String title(){
			throw new RuntimeException("Not implemented in "+this);
		}
	};
	private void onRetargeted(){
		if(onRetargeted!=null){
		  trace("> Calling onRetargeted...");
	  	onRetargeted.accept(null);
	  }
	}
	Facets(String top,boolean trace){
		super(top);
		this.doTrace=trace;
	}
	@Override
	protected void traceOutput(String msg){
		if(doTrace||(Debug.trace&&msg.startsWith(">>")))super.traceOutput(msg);
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
		if(false)toggling.set(passSet);
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
		return new STrigger(title,new STrigger.Coupler(){
			@Override
			public void fired(STrigger t){
				updatedTarget(t,c);
			}
		});
	}
	public STarget newTargetGroup(String title,STarget...members){
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
		public Function<String,String[]>getUiSelectables;
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
				return c.getUiSelectables.apply(i.title());
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
			uiSelectables=indexing.facetIndexables();
			indexed=indexing.indexed();
		}};
	}
	@Interface
	public static class IndexingFramePolicy{
		public String indexingTitle;
		public Supplier<Object[]>getIndexables;
		@Optional
		public String indexingFrameTitle;
		@Optional
		public Function<Object,String>newUiSelectable;
		@Optional
		public Supplier<STarget[]>newFrameTargets;
		@Optional
		public Function<Object,String>newIndexedTargetsTitle;
		@Optional
		public BiFunction<Object,String,STarget>newIndexedTargets;
	}
	private int indexingFrames;
	public STarget newIndexingFrame(IndexingFramePolicy p){
		SIndexing indexing=new SIndexing(p.indexingTitle,new SIndexing.Coupler(){
			private Object[]thenIndexables,thenSelectables;
			@Override
			protected Object[]getIndexables(SIndexing i){
				Object[]got=p.getIndexables.get();
				if(got==null)throw new IllegalStateException("Null getIndexables for "+i.title());
				boolean equal=Util.longEquals(got,thenIndexables);
				if(!equal)trace("> Got new indexables: ",got.length);
				thenIndexables=got;
				return got;
			}
			@Override
			protected String[]getFacetSelectables(SIndexing i){
				Function<Object,String>getter=p.newUiSelectable;
				List<String>selectables=new ArrayList();
				int at=0;
				for(Object each:i.indexables())selectables.add(
						getter!=null?getter.apply(each):"Selectable"+String.valueOf(at++)
					);
				String[]got=selectables.toArray(new String[]{});
				boolean equal=Util.longEquals(got,thenSelectables);
				if(!equal)trace("> Got new selectables: ",got.length);
				thenSelectables=got;
				return got;
			}
		});
		indexing.setIndex(0);
		trace(" > Created indexing ",indexing);
		String title=p.indexingFrameTitle;
		IndexingFrame frame=new IndexingFrame(title!=null?title:"IndexingFrame"+indexingFrames++,
				indexing){
			protected STarget[]lazyElements(){
				Supplier<STarget[]>getter=p.newFrameTargets;
				STarget[]got=getter!=null?getter.get():new STarget[]{};
				if(false&&doTrace)trace(".lazyElements: ",got);
				return got==null?new STarget[]{}:STarget.newTargets(got);
			};
			@Override
			protected STarget newIndexedTargets(Object indexed){
				if(false&&doTrace)trace(".newIndexedTargets: indexed="+(indexed!=null));
				Function<Object,String>titler=p.newIndexedTargetsTitle;
				String indexedTargetsTitle=titler==null?title+"|indexed"
						:titler.apply(indexed);
				return p.newIndexedTargets==null?new TargetCore(indexedTargetsTitle)
						:p.newIndexedTargets.apply(indexed,indexedTargetsTitle);
			}
		};
		trace(" > Created indexing frame ",frame);
		return frame;
	}
	private Object getTargetFramed(String title){
		SFrameTarget frame=(SFrameTarget)titleTarget(title);
		if(frame==null)throw new IllegalStateException("Null frame for "+title);
		return frame.framed;
	}
	private STarget newFrameTarget(String title,Object toFrame,Object[]editTargets){
		STarget[]asTargets=STarget.newTargets(editTargets);
		return false?new LocalFrameTarget(title,toFrame,asTargets)
				:new SFrameTarget(title,toFrame){
			@Override
			protected STarget[]lazyElements(){
				return asTargets;
			}
		};
	}
	private static final class LocalFrameTarget extends SFrameTarget{
		private final STarget[]newIndexedTargets;
		private LocalFrameTarget(String title,Object toFrame,
				STarget[]newIndexedTargets){
			super(title,toFrame);
			this.newIndexedTargets=newIndexedTargets;
		}
		@Override
		protected STarget[]lazyElements(){
			return newIndexedTargets;
		}
	}
	public void buildTargeterTree(STarget targetTree){
		trace(" > Retargeting on ",targetTree);
		if(targeterTree==null)targeterTree=((TargetCore)targetTree).newTargeter();
		targeterTree.setNotifiable(notifiable);
		targeterTree.retarget(targetTree);
		putTitleTargeters(targeterTree);
		onRetargeted();
	}
	private void putTitleTargeters(STargeter t){
		String title=t.title();
		STargeter then=titleTargeters.get(title);
		titleTargeters.put(title,t);
		STargeter[]elements=((TargeterCore)t).titleElements();
		if(then==null)trace("> Added targeter: title="+title+
				(false?(": elements="+elements.length)
				:(": titleTargeters="+titleTargeters.values().size())));
		for(STargeter e:elements)putTitleTargeters(e);
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
				trace(" > Updating UI for '"+title+ "' with state=",state);
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
}
