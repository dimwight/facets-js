package fjs.globals;
import java.util.HashMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import fjs.core.Notifiable;
import fjs.core.SFacet;
import fjs.core.STarget;
import fjs.core.STargeter;
import fjs.core.STextual;
import fjs.core.TargetCore;
import fjs.util.Debug;
import fjs.util.Tracer;
import fjs.util.Tracer.TracerTopped;
import jsweet.lang.Interface;
import jsweet.lang.Optional;
public final class Globals{
	private static class TargetCoupler{
		public Consumer<String>targetStateUpdated;
	}
	public static boolean trace=true;
	private final static TracerTopped t=new Tracer.TracerTopped("Facets"){
		protected boolean doTrace(){
			return trace;
		}
	};
	private final static HashMap<String,STargeter>titleTargeters=new HashMap();
	private static STargeter targeterTree;
	private static final Notifiable notifiable=new Notifiable(){
		@Override
		public void notify(Object notice){
			t.trace(" > Surface for "+Debug.info(targeterTree)+" notified by "+notice);
			STarget targets=targeterTree.target();
			targeterTree.retarget(targets);
		  t.trace(" > Targeters retargeted on ",targets);
			targeterTree.retargetFacets();
		  t.trace(" > Facets retargeted in ",targeterTree);
		}
		@Override
		public String title(){
			throw new RuntimeException("Not implemented in "+this);
		}
	};
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
	public static Object newTextualTarget(String title,TextualCoupler c){
		STextual textual=new STextual(title,new STextual.Coupler(){
			@Override
			public void textSet(STextual textual){
				String title=textual.title();
				t.trace(" > Updated textual ",textual);
				c.targetStateUpdated.accept(title);
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
		t.trace(" > Created textual ",textual);
		return textual;
	}
	public static Object newTargetsGroup(String title,Object...members){
		TargetCore group=new TargetCore(title,STarget.newTargets(members));
		t.trace(" > Created target group "+Debug.info(group)+" ",members);
		return group;
	}
	public static void buildTargeterTree(Object targets){
		t.trace(" > Creating targeters for ",targets);
		targeterTree=((TargetCore)targets).newTargeter();
		targeterTree.setNotifiable(notifiable);
		targeterTree.retarget((STarget)targets);
		for(STargeter targeter:targeterTree.elements()){
			titleTargeters.put(targeter.title(),targeter);
			t.trace(" > Created targeter ",targeter);
		}
	}
	public static void attachFacet(String title,Consumer facetUpdated){
		if(facetUpdated==null)throw new IllegalArgumentException(
				"Null facetUpdated for "+title);
		else if(title==null||title.equals(""))throw new IllegalArgumentException(
				"Null or empty title for "+facetUpdated);
		STargeter targeter=titleTargeters.get(title);
		if(targeter==null)throw new IllegalArgumentException("Null targeter for "+title);
		t.trace(" > Attaching facet to",targeter);
		targeter.attachFacet(new SFacet(){
			@Override
			public void retarget(STarget target){
				Object state=target.state();
				facetUpdated.accept(state);
			}
		});
	}
	public static void updateTargetState(String title,Object update){
		t.trace(" > Updating target state for title="+title+" update=",update);
		STarget target=titleTarget(title);
		target.updateState(update);
		target.notifyParent();
	}
	public static Object getTargetState(String title){
		Object state=titleTarget(title).state();
		t.trace(" > Getting target state for title="+title+" state=",state);
		return state;
	}
	private static STarget titleTarget(String title){
		STarget target=titleTargeters.get(title).target();
		return target;
	}
}
