import * as Facets from 'Facets.js';
const TITLE_FIRST : string = 'First';
const TITLE_SECOND : string = 'Second';
function newTargetTree(){
  let text:string='This text passed using "import * as Facets from \'Facets.js\'" ';
  console.info('.newTargetTree: text=',text);
  let coupler:any={
    text:text,
    stateUpdated : (title) => console.info("stateUpdated: title=" + title)

  };
  let first:any=Facets.newTextual(TITLE_FIRST,coupler);
  let second:any=Facets.newTextual(TITLE_SECOND,coupler);
  return Facets.newTargetGroup('Textuals',first,second);
}
function buildLayout(){
  const accept=function(update):void{
    console.log(update)
  };
  Facets.attachFacet(TITLE_FIRST,accept);
}
function buildSurface(){
  console.info(' > Building surface');
  Facets.buildTargeterTree(newTargetTree());
  console.info(' > Built targets, created targeters');
  buildLayout();
  console.info(' > Attached and laid out facets');
  Facets.retargetFacets();
  console.info(' > Surface built');
  Facets.updateTarget(TITLE_FIRST,'Some updated text');
  if(typeof document=='undefined') return;
  document.getElementById('pageTitle').innerText=document.title;
}
buildSurface();

