package fjs.globals;
import java.util.HashMap;
import fjs.CouplerLike;
import fjs.UpdateFunction;
import fjs.core.Notifiable;
import fjs.core.SFacet;
import fjs.core.STarget;
import fjs.core.STargeter;
import fjs.core.STextual;
import fjs.core.TargetCore;
import fjs.core.TextualCoupler;
import fjs.util.Debug;
import fjs.util.Tracer;
import fjs.util.Tracer.TracerTopped;
public final class Globals{
	public static boolean trace=true;
	public static final String KEY_TEXTUAL_TEXT="text";
	private final static HashMap<String,STargeter>titleTargeters=new HashMap();
	private final static TracerTopped t=new Tracer.TracerTopped("Facets"){
		protected boolean doTrace(){
			return trace;
		}
	};
	private static STargeter targeterTree;
	public static Object newTextual(String title,Object couplerLike){
		TextualCoupler coupler=new TextualCoupler(){
				protected String getText(String title){
					return ((CouplerLike)couplerLike).text;
				}
			};
		Object textual=new STextual(title,coupler);
		t.trace(" > Created textual ",textual);
		return textual;
	}
	public static Object newTargetGroup(String title,Object...members){
		TargetCore group=new TargetCore(title,STarget.newTargets(members));
		t.trace(" > Created group "+Debug.info(group)+" ",members);
		return group;
	}
	public static void buildTargeterTree(Object targets){
		targeterTree=((TargetCore)targets).newTargeter();
		targeterTree.setNotifiable(new Notifiable(){
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
		});
		targeterTree.retarget((STarget)targets);
		for(STargeter targeter:targeterTree.elements()){
			titleTargeters.put(targeter.title(),targeter);
			t.trace(" > Created targeter ",targeter);
		}
	}
	final private static class AttachFacet implements SFacet{
		private Object updateFn;
		AttachFacet(Object updateFn){
			this.updateFn=updateFn;
		}
		@Override
		public void retarget(STarget target){
			((UpdateFunction)updateFn).apply(target.state());
		}
	}
	public static void attachFacet(String title,Object updateFn){
		if(updateFn==null)throw new IllegalArgumentException(
				"Null updateFn for "+title);
		else if(title==null||title.equals(""))throw new IllegalArgumentException(
				"Null or empty title for "+updateFn);
		STargeter targeter=titleTargeters.get(title);
		if(targeter==null)throw new IllegalArgumentException(
				"Null targeter for "+title);
		t.trace(" > Attaching facet to ",targeter);
		AttachFacet facet=new AttachFacet(updateFn);
		targeter.attachFacet(facet);
	}
	public static void retargetFacets(){
		targeterTree.retargetFacets();
		t.trace(" > Retargeted facets on ",targeterTree);
	}
	public static void updateTarget(String title,Object update){
		STarget target=titleTargeters.get(title).target();
		target.updateState(update);
		target.notifyParent();
	}
}
