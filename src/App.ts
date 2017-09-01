// import * as Facets from 'Facets'; //libInclude
// import * as Facets from 'Facets.js'; //libExclude
import * as Facets from './lib/Facets';

const TITLE_FIRST = 'First', TITLE_SECOND = 'Second';

function newTargetTree(){
  const text='Some text';
  trace('.newTargetTree: text='+text);
  const coupler:Facets.TextualCoupler ={
    passText:text,
    targetStateUpdated : (title) => trace("coupler.stateUpdated: title=" + title)
  };
  const first:any=Facets.newTextualTarget(TITLE_FIRST,coupler),
    second:any=Facets.newTextualTarget(TITLE_SECOND,coupler);
  return Facets.newTargetsGroup('Textuals',first,second);
}
function buildLayout(){
  trace('.buildLayout');
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
trace('Surface built');
Facets.updateTargetState(TITLE_FIRST,'Some updated text');
if(typeof document!=='undefined')
  document.getElementById('pageTitle').innerText=document.title;

