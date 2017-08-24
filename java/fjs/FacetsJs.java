package fjs;
import java.util.HashMap;
import fjs.superficial.FacetUpdatable;
import fjs.superficial.Notice;
import fjs.superficial.Notifiable;
import fjs.superficial.NotifyingImpact;
import fjs.superficial.SFacet;
import fjs.superficial.STarget;
import fjs.superficial.STargeter;
import fjs.superficial.STextual;
import fjs.superficial.TargetCore;
import fjs.superficial.TextualCoupler;
import fjs.util.Debug;
import fjs.util.Tracer;
import fjs.util.Tracer.TracerTopped;
public final class FacetsJs{
	public final static boolean onlyJs=true;
	public static final String KEY_TEXTUAL_TEXT="text";
	private final static NotifyingImpact IMPACT=NotifyingImpact.DEFAULT;
	private final static HashMap<String,STargeter>titleTargeters=new HashMap();
	private final static TracerTopped t=new Tracer.TracerTopped(true,"FacetsJs");
	private static STargeter targeterTree;
	public static Object newTextual(String title,Object coupler){
		TextualCoupler facets;
		if(coupler instanceof TextualCoupler)facets=(TextualCoupler)coupler;
		else{
			String jsText=((def.js.Object)coupler).$get(KEY_TEXTUAL_TEXT);
			facets=new TextualCoupler(){
				protected String getText(String title){
					return jsText;
				};
			};
		}
		Object textual=new STextual(title,facets);
		t.trace(" > Created textual ",textual);
		return textual;
	}
	public static Object newTargetGroup(String title,Object...members){
		TargetCore group=new TargetCore(title,STarget.newTargets(members));
		t.trace(" > Created group "+title+" ",members);
		return group;
	}
	public static void buildTargeterTree(Object targets){
		targeterTree=((TargetCore)targets).newTargeter();
		targeterTree.setNotifiable(new Notifiable(){
			@Override
			public void notify(Notice notice){
				t.trace(" > Surface for "+Debug.info(targeterTree)+" notified with "+notice);
				STarget targets=targeterTree.target();
				targeterTree.retarget(targets,notice.impact);
			  t.trace(" > Targeters retargeted on ",targets);
				targeterTree.retargetFacets(notice.impact);
			  t.trace(" > Facets retargeted in ",targeterTree);
			}
			@Override
			public String title(){
				throw new RuntimeException("Not implemented in "+this);
			}
		});
		targeterTree.retarget((STarget)targets,IMPACT);
		for(STargeter targeter:targeterTree.elements()){
			titleTargeters.put(targeter.title(),targeter);
			t.trace(" > Created targeter ",targeter);
		}
	}
	public static void attachFacet(String title,Object updateFn){
		STargeter targeter=titleTargeters.get(title);
		if(targeter==null)throw new IllegalArgumentException(
				"Null targeter for "+title);
		else if(!(targeter.target()instanceof STextual))
			throw new RuntimeException("Not implemented for "+title);
		targeter.attachFacet(new SFacet(){
			@Override
			public void retarget(STarget target,NotifyingImpact impact){
				((FacetUpdatable)updateFn).updateFromFacet(((STextual)target).text());
			}
		});
		t.trace(" > Attached facet to ",targeter);
	}
	public static void retargetFacets(){
		targeterTree.retargetFacets(IMPACT);
		t.trace(" > Retargeted facets on ",targeterTree);
	}
	public static void updateTarget(String title,Object update){
		STarget target=titleTargeters.get(title).target();
		if(update instanceof String)((STextual)target).setText((String)update);
		else throw new RuntimeException("Not implemented for "+title+" with update="+update);
		target.notifyParent(IMPACT);
	}
}
