import * as Facets from 'facets-js';

function trace(text){
  console.info('App > ' +text);
}
const TITLE_FIRST = 'First', TITLE_SECOND = 'Second';
const core : Facets.Facets = Facets.newInstance(true);

function newTargetTree():Facets.Target{
  const text='Some text';
  trace('.newTargetTree: text='+text);
  const coupler:Facets.TextualCoupler={
    passText:text,
    targetStateUpdated : (title) => trace("coupler.stateUpdated: title=" + title),
  };
  const first:Facets.Target=core.newTextualTarget(TITLE_FIRST,coupler),
    second:Facets.Target=core.newTextualTarget(TITLE_SECOND,coupler);
  return core.newTargetGroup('Textuals',[first,second]);
}
function buildLayout(){
  trace('.buildLayout');
  core.attachFacet(TITLE_FIRST,update=>trace('Facet updating with '+update));
}
trace('Building surface');
core.buildTargeterTree(newTargetTree());
trace('Built targets, created targeters');
buildLayout();
trace('Attached and laid out facets');
trace('Surface built');
core.updateTargetState(TITLE_FIRST,'Some updated text');
if(typeof document!=='undefined')
  document.getElementById('pageTitle').innerText=document.title;

