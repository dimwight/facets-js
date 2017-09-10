import FacetsJs from 'facets-js';

function trace(text){
  console.info('App > ' +text);
}
const TITLE_FIRST = 'First', TITLE_SECOND = 'Second';
const app : FacetsJs.Facets = FacetsJs.Facets.newInstance(true);

function newTargetTree():FacetsJs.Target{
  const text='Some text';
  trace('.newTargetTree: text='+text);
  const coupler:FacetsJs.TextualCoupler={
    passText:text,
    targetStateUpdated : (title) => trace("coupler.stateUpdated: title=" + title)
  };
  const first:FacetsJs.Target=app.newTextualTarget(TITLE_FIRST,coupler),
    second:FacetsJs.Target=app.newTextualTarget(TITLE_SECOND,coupler);
  return app.newTargetsGroup('Textuals',first,second);
}
function buildLayout(){
  trace('.buildLayout');
  app.attachFacet(TITLE_FIRST,update=>trace('Facet updating with '+update));
}
trace('Building surface');
app.buildTargeterTree(newTargetTree());
trace('Built targets, created targeters');
buildLayout();
trace('Attached and laid out facets');
trace('Surface built');
app.updateTargetState(TITLE_FIRST,'Some updated text');
if(typeof document!=='undefined')
  document.getElementById('pageTitle').innerText=document.title;

