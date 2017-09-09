import * as Facets from 'Facets';

function trace(text){
  console.info('App > ' +text);
}
const TITLE_FIRST = 'First', TITLE_SECOND = 'Second';
const app : Facets = Facets.newInstance(true);

function newTargetTree(){
  const text='Some text';
  trace('.newTargetTree: text='+text);
  const coupler:Facets={
    passText:text,
    targetStateUpdated : (title) => trace("coupler.stateUpdated: title=" + title)
  };
  const first:any=app.newTextualTarget(TITLE_FIRST,coupler as Facets.TextualCoupler),
    second:any=app.newTextualTarget(TITLE_SECOND,coupler as Facets.TextualCoupler);
  return app.newTargetsGroup('Textuals',first,second);
}
function buildLayout(){
  trace('.buildLayout');
  app.attachFacet(TITLE_FIRST,
      update=>trace('Facet updating with '+update));
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

