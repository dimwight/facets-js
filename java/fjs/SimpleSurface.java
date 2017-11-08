package fjs;
import fjs.core.STarget;
import fjs.core.TargetCoupler;
import fjs.globals.Facets;
import fjs.globals.Facets.NumericCoupler;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Facets.TogglingCoupler;
import fjs.globals.Facets.Times;
import fjs.globals.Globals;
public class SimpleSurface extends SurfaceCore implements SimpleTitles{
	public SimpleSurface(String title,TargetTest test){
		super(Globals.newInstance(true),test);
	}
	@Override
	final protected void doTraceMsg(String msg){
		if(true&&facets.doTrace)super.doTraceMsg(msg);
	}
	final protected STarget newTrigger(String title){
		return facets.newTriggerTarget(title,new Facets.TargetCoupler(){{
			targetStateUpdated=(state,title)->{
				trace(" > Trigger fired: title="+title);
				String got=(String)facets.getTargetState(TITLE_TRIGGERINGS);
				if(got!=null)facets.updateTargetState(TITLE_TRIGGERINGS,
						String.valueOf(Integer.valueOf(got)+1));
			};
		}});
	}
	final protected STarget newTextual(String title){
		TextualCoupler coupler=newTextualCouplerCore(title);
		String passText=coupler.passText;
		trace(" > Generating textual target state=",
				passText!=null?passText:coupler.getText.apply(title));
		return facets.newTextualTarget(title,coupler);
	}
	final protected STarget newNumeric(String title){
		NumericCoupler coupler=new NumericCoupler(){{
			passValue=NUMERIC_START;
			min=5d;
			max=25d;
		}};
		trace(" > Generating numeric target state=",coupler.passValue);
		return facets.newNumericTarget(title,coupler);
	}
	final protected STarget newToggling(String title,boolean state){
		trace(" > Generating toggling target state=",state);
		TogglingCoupler coupler=new TogglingCoupler(){{
			passSet=state;
			targetStateUpdated=(state,title)->{
				trace(" > Toggling state updated: title="+title+" state=",state);
				facets.setTargetLive(TITLE_TOGGLED,(boolean)state);			
				facets.updateTargetState(TITLE_TOGGLED,"State is "+state);
			};
		}};
		return facets.newTogglingTarget(title,coupler);
	}
	final protected STarget newIndexing(String title,String[]indexables,int indexStart){
		trace(" > Generating indexing target state=",indexStart);
		Facets.IndexingCoupler coupler=new Facets.IndexingCoupler(){{
			getIndexables=title->indexables;
			newUiSelectable=indexable->(String)indexable;
			passIndex=indexStart;
		}};

		return facets.newIndexingTarget(title,coupler);
	}
	final protected TextualCoupler newTextualCouplerCore(String title){
		String textTextual=title+" text in "+this.title();
		return title.equals(TITLE_NUMERIC_LABEL)?new TextualCoupler(){{
			getText=title->{
				Object state=facets.getTargetState(TITLE_NUMERIC_FIELD);
				return ("Number is "+(state!=null?Math.rint((double)state):" not yet set")
						).replaceAll("\\.\\d+","");
			};
		}}
		:title.equals(TITLE_TOGGLED)?new TextualCoupler(){{
			getText=title->"State is "+facets.getTargetState(TITLE_TOGGLING);
		}}
		:title.equals(TITLE_INDEXED)?new TextualCoupler(){{
			getText=title->facets.getTargetState(TITLE_INDEXING)==null?("No data yet for "+TITLE_INDEXING)
			:(String)facets.getIndexingState(TITLE_INDEXING).indexed;
		}}
		:title.equals(TITLE_INDEX)?new TextualCoupler(){{
			getText=title->{
				Object state=facets.getTargetState(TITLE_INDEXING);
				return state==null?("No data yet for "+TITLE_INDEXING):String.valueOf(state);
			};
		}}
		:title.equals(TITLE_TEXTUAL_FIRST)?new TextualCoupler(){{
			getText=title->textTextual;
			targetStateUpdated=(state,title)->{
				trace(" > Textual state updated: title="+title+" state=",state);
				facets.updateTargetState(TITLE_TEXTUAL_SECOND,
						TITLE_TEXTUAL_FIRST+" has changed to: "+state);					
			};
		}}
		:title.equals(TITLE_TRIGGERINGS)?new TextualCoupler(){{
			passText="0";
			targetStateUpdated=(state,title)->{
				if(Integer.valueOf((String)state)>4)facets.setTargetLive(TITLE_TRIGGER,false);
			};
		}}
		:new TextualCoupler(){{
			passText=textTextual;
		}};
	}
	@Override
	protected STarget newTargetTree(){
		trace(" > Generating targets");
		String treeTitle=test.name()+" Test";
		STarget[]members=test==TargetTest.Textual?
				new STarget[]{newTextual(
						TITLE_TEXTUAL_FIRST),
						newTextual(TITLE_TEXTUAL_SECOND)
				}
			:test==TargetTest.TogglingLive?new STarget[]{
					newToggling(TITLE_TOGGLING,TOGGLE_START),newTextual(TITLE_TOGGLED)
				}
			:test==TargetTest.Indexing?new STarget[]{
					newIndexing(TITLE_INDEXING,new String[]{TITLE_TEXTUAL_FIRST,TITLE_TEXTUAL_SECOND},
							INDEX_START),
					newTextual(TITLE_INDEX),newTextual(TITLE_INDEXED)
				}
			:test==TargetTest.Numeric?new STarget[]{
					newNumeric(
						TITLE_NUMERIC_FIELD),
						newTextual(TITLE_NUMERIC_LABEL)
				}
			:new STarget[]{
					newTrigger(TITLE_TRIGGER),
					newTextual(TITLE_TRIGGERINGS)
				};
		return facets.newTargetGroup(treeTitle,members);
	}
	@Override
	protected void buildLayout(){
		if(test==TargetTest.Textual)generateFacets(TITLE_TEXTUAL_FIRST);
		else if(test==TargetTest.TogglingLive)generateFacets(TITLE_TOGGLING,TITLE_TOGGLED);
		else if(test==TargetTest.Numeric)generateFacets(TITLE_NUMERIC_FIELD,TITLE_NUMERIC_LABEL);
		else if(test==TargetTest.Trigger)generateFacets(TITLE_TRIGGER,TITLE_TRIGGERINGS);
		else generateFacets(TITLE_INDEXING,TITLE_INDEX,TITLE_INDEXED);
	}
	@Override
	public void buildSurface(){
		super.buildSurface();
		if(false)return;
		Object update=test==TargetTest.TogglingLive?!TOGGLE_START
				:test==TargetTest.Indexing?(INDEX_START+1)%2
				:test==TargetTest.Numeric?NUMERIC_START*2
				:"Some updated text";
		trace(" > Simulating input: update=",update);
		facets.updateTargetState(test==TargetTest.Indexing?TITLE_INDEXING:
			test==TargetTest.TogglingLive?TITLE_TOGGLING
					:test==TargetTest.Numeric?TITLE_NUMERIC_FIELD
					:test==TargetTest.Trigger?TITLE_TRIGGER
					:TITLE_TEXTUAL_FIRST,update);
	}
	public static void main(String[]args){
		new SimpleSurface("SimpleSurface",TargetTest.Trigger).buildSurface();
	}
}