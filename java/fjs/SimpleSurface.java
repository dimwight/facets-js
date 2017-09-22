package fjs;
import fjs.globals.Facets;
import fjs.globals.Facets.NumericCoupler;
import fjs.globals.Facets.TextualCoupler;
import fjs.globals.Facets.TogglingCoupler;
import fjs.util.Times;
import fjs.globals.Globals;
public class SimpleSurface extends SurfaceCore implements SimpleTitles{
	public SimpleSurface(String title,TargetTest test){
		super(title,Globals.newInstance(true),test);
		Times.times=true;
	}
	@Override
	final protected void traceOutput(String msg){
		if(true&&facets.trace)super.traceOutput(msg);
	}
	final protected Object newTextual(String title){
		TextualCoupler coupler=newTextualCouplerCore(title);
		String passText=coupler.passText;
		trace(" > Generating textual target state=",
				passText!=null?passText:coupler.getText.apply(title));
		return facets.newTextualTarget(title,coupler);
	}
	final protected Object newNumeric(String title){
		NumericCoupler coupler=new NumericCoupler(){{
			passValue=NUMERIC_START;
			min=5d;
			max=25d;
		}};
		trace(" > Generating numeric target state=",coupler.passValue);
		return facets.newNumericTarget(title,coupler);
	}
	final protected Object newToggling(String title,boolean state){
		trace(" > Generating toggling target state=",state);
		TogglingCoupler coupler=new TogglingCoupler(){{
			passSet=state;
			targetStateUpdated=(title,state)->{
				trace(" > Toggling state updated: title="+title+" state=",state);
				facets.setTargetLive(TITLE_TOGGLED,(boolean)state);			
				facets.updateTargetState(TITLE_TOGGLED,"State is "+state);
			};
		}};
		return facets.newTogglingTarget(title,coupler);
	}
	final protected Object newIndexing(String title,String[]indexables,int indexStart){
		trace(" > Generating indexing target state=",indexStart);
		Facets.IndexingCoupler coupler=new Facets.IndexingCoupler(){{
			passIndexables=indexables;
			passIndex=indexStart;
			targetStateUpdated=(title,state)->{
				trace(" > Indexing state updated: title="+title+" state=",state);
				facets.updateTargetState(TITLE_INDEXED,facets.getIndexingValues(title).indexed.get());
				facets.updateTargetState(TITLE_INDEX,"Index is "+state);
			};
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
			passText=INDEXABLES[INDEX_START];
		}}
		:title.equals(TITLE_TEXTUAL_FIRST)?new TextualCoupler(){{
			getText=title->textTextual;
			targetStateUpdated=(title,state)->{
				trace(" > Textual state updated: title="+title+" state=",state);
				facets.updateTargetState(TITLE_TEXTUAL_SECOND,
						TITLE_TEXTUAL_FIRST+" has changed to: "+state);					
			};
		}}
		:new TextualCoupler(){{
			passText=textTextual;
		}};
	}
	@Override
	protected Object newTargetTree(){
		trace(" > Generating targets");
		String treeTitle=test.toString();
		return test==TargetTest.Textual?facets.newTargetsGroup(treeTitle,
				newTextual(TITLE_TEXTUAL_FIRST),newTextual(TITLE_TEXTUAL_SECOND))
			:test==TargetTest.TogglingLive?facets.newTargetsGroup(treeTitle,
				newToggling(TITLE_TOGGLING,TOGGLE_START),newTextual(TITLE_TOGGLED))
			:test==TargetTest.Indexing?facets.newTargetsGroup(treeTitle,
					newIndexing(TITLE_INDEXING,INDEXABLES,INDEX_START),
					newTextual(TITLE_INDEX),newTextual(TITLE_INDEXED))
			:facets.newTargetsGroup(treeTitle,
					newNumeric(TITLE_NUMERIC_FIELD),newTextual(TITLE_NUMERIC_LABEL));
	}
	@Override
	protected void buildLayout(){
		if(test==TargetTest.Textual)generateFacets(TITLE_TEXTUAL_FIRST);
		else if(test==TargetTest.TogglingLive)generateFacets(TITLE_TOGGLING,TITLE_TOGGLED);
		else if(test==TargetTest.Numeric)generateFacets(TITLE_NUMERIC_FIELD,TITLE_NUMERIC_LABEL);
		else generateFacets(TITLE_INDEXING,TITLE_INDEX,TITLE_INDEXED);
	}
	@Override
	public void buildSurface(){
		super.buildSurface();
		if(false||!test.isSimple())return;
		Object update=test==TargetTest.TogglingLive?!TOGGLE_START
				:test==TargetTest.Indexing?(INDEX_START+1)%2
				:test==TargetTest.Numeric?NUMERIC_START*2
				:"Some updated text";
		trace(" > Simulating input: update=",update);
		facets.updateTargetState(test==TargetTest.Indexing?TITLE_INDEXING:
			test==TargetTest.TogglingLive?TITLE_TOGGLING
					:test==TargetTest.Numeric?TITLE_NUMERIC_FIELD
					:TITLE_TEXTUAL_FIRST,update);
	}
	public static void main(String[]args){
		new SimpleSurface("SimpleSurface",TargetTest.Numeric).buildSurface();
	}
}