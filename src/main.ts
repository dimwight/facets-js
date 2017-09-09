import * as Facets from 'Facets';

const TITLE_FIRST = 'First', TITLE_SECOND = 'Second';

const app : any = Facets.newInstance(true);

function newTargetTree(){
  const text='Some text';
  trace('.newTargetTree: text='+text);
  const coupler={
    passText:text,
    targetStateUpdated : (title) => trace("coupler.stateUpdated: title=" + title)
  };
  const first:any=app.newTextualTarget(TITLE_FIRST,coupler),
    second:any=app.newTextualTarget(TITLE_SECOND,coupler);
  return app.newTargetsGroup('Textuals',first,second);
}
function buildLayout(){
  trace('.buildLayout');
  app.attachFacet(TITLE_FIRST,
      update=>trace('Facet updating with '+update));
}
function trace(text){
  console.info('App > ' +text);
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

