// import Facets from 'Facets';
import * as Facets from 'Facets.js';
// import * as Facets from '../in/fjs/globals/Facets';
const TITLE_FIRST : string = 'First';
const TITLE_SECOND : string = 'Second';
function buildSurface() {
console.info(' > Building surface');
    Facets.buildTargeterTree(newTargetTree());
    console.info(' > Built targets, created targeters');
  buildLayout();
  console.info(' > Attached and laid out facets');
  Facets.retargetFacets();
  console.info(' > Surface built');
  Facets.updateTarget(TITLE_FIRST, 'Some updated text');
}
function newTargetTree() : any {
  let text : string = 'This text passed using "import * as Facets from \'Facets.js\'" ';
  console.info('.newTargetTree: text=', text);
  let coupler : any = {text: text};
  let first : any = Facets.newTextual(TITLE_FIRST, coupler);
  let second : any = Facets.newTextual(TITLE_SECOND, coupler);
  return Facets.newTargetGroup('Textuals', first, second);
}
function buildLayout() {
  const accept=function(update):void{
    console.log(update)
  }
  Facets.attachFacet(TITLE_FIRST,accept);
}
buildSurface();