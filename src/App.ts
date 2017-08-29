import * as Facets from 'Facets.js';

const TITLE_FIRST = 'First', TITLE_SECOND = 'Second';

function newTargetTree(){
  const text='Some text';
  trace('.newTargetTree: text='+text);
  const coupler ={
    text:text,
    stateUpdated : (title) => trace("coupler.stateUpdated: title=" + title)
  };
  const first:any=Facets.newTextual(TITLE_FIRST,coupler),
    second:any=Facets.newTextual(TITLE_SECOND,coupler);
  return Facets.newTargetGroup('Textuals',first,second);
}
function buildLayout(){
  trace('.buildLayout')
  Facets.attachFacet(TITLE_FIRST,
      update=>trace('Facet updating with '+update));
}
function trace(text){
  console.info('App > ' +text);
}
trace('Building surface');
Facets.buildTargeterTree(newTargetTree());
trace('Built targets, created targeters');
buildLayout();
trace('Attached and laid out facets');
Facets.retargetFacets();
trace('Surface built');
Facets.updateTarget(TITLE_FIRST,'Some updated text');
if(typeof document!=='undefined')
  document.getElementById('pageTitle').innerText=document.title;

