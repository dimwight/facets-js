package fjs.globals;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import fjs.core.Notifiable;
import fjs.core.SFacet;
import fjs.core.SIndexing;
import fjs.core.STarget;
import fjs.core.STargeter;
import fjs.core.STextual;
import fjs.core.TargetCore;
import fjs.util.Debug;
import fjs.util.Tracer;
import jsweet.lang.Interface;
import jsweet.lang.Optional;
public final class Facets extends Tracer{
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
	public static class IndexingCoupler extends TargetCoupler{
		public Object[]passIndexables;
		public int passIndex;
	}
	public final boolean trace;
	private final HashMap<String,STargeter>titleTargeters=new HashMap();
	private STargeter targeterTree;
	private final Notifiable notifiable=new Notifiable(){
		@Override
		public void notify(Object notice){
			trace(" > Surface for "+Debug.info(targeterTree)+" notified by "+notice);
			STarget targets=targeterTree.target();
			targeterTree.retarget(targets);
		  trace(" > Targeters retargeted on ",targets);
			targeterTree.retargetFacets();
		  trace(" > Facets retargeted in ",targeterTree);
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
	protected void traceOutput(String msg) {
		if(trace)super.traceOutput(msg);
	}
	void updatedTarget(STarget target,TargetCoupler c){
		String title=target.title();
		Object state=target.state();
		trace(" > Updated target "+target.title()+" with ",state);
		c.targetStateUpdated.accept(title,state);
	}
	public Object newIndexingTarget(String title,IndexingCoupler c){
		SIndexing indexing=new SIndexing(title,c.passIndexables,new SIndexing.Coupler(){
			@Override
			public void indexSet(SIndexing target){
				updatedTarget(target,c);
			}
		});
		Integer passIndex=c.passIndex;
		if(passIndex!=null)indexing.setIndex(passIndex);
		trace(" > Created indexing ",indexing);
		return indexing;
	}
	public Object newTextualTarget(String title,TextualCoupler c){
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
	public Object newTargetsGroup(String title,Object...members){
		TargetCore group=new TargetCore(title,STarget.newTargets(members));
		trace(" > Created target group "+Debug.info(group)+" ",members);
		return group;
	}
	public void buildTargeterTree(Object targets){
		trace(" > Creating targeters for ",targets);
		targeterTree=((TargetCore)targets).newTargeter();
		targeterTree.setNotifiable(notifiable);
		targeterTree.retarget((STarget)targets);
		for(STargeter targeter:targeterTree.elements()){
			titleTargeters.put(targeter.title(),targeter);
			trace(" > Created targeter ",targeter);
		}
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
				if(false)trace(".attachFacet #"+id+ ".retarget: state="+state+" stateThen="+stateThen);
				if(!state.equals(stateThen)){
					trace(" > Updating UI with state=",state);
					stateThen=state;
					facetUpdated.accept(state);
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
	public void updateTargetState(String title,Object update){
		trace(" > Updating target state for title="+title+" update=",update);
		STarget target=titleTarget(title);
		target.updateState(update);
		target.notifyParent();
	}
	public Object getTargetState(String title){
		Object state=titleTarget(title).state();
		trace(" > Getting target state for title="+title+" state=",state);
		return state;
	}
	private STarget titleTarget(String title){
		STargeter targeter=titleTargeters.get(title);
		if(targeter==null)throw new IllegalStateException(
				"Null target for "+title);
		STarget target=targeter.target();
		return target;
	}
}
