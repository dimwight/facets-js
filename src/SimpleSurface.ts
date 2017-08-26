import Superficial from './Superficial';
const TITLE_FIRST : string = "First";
const TITLE_SECOND : string = "Second";
function buildSurface() {
console.info(" > Building surface");
    Superficial.buildTargeterTree(newTargetTree());
    console.info(" > Built targets, created targeters");
  buildLayout();
  console.info(" > Attached and laid out facets");
  Superficial.retargetFacets();
  console.info(" > Surface built");
  Superficial.updateTarget(TITLE_FIRST, "Some updated text");
}
function newTargetTree() : any {
  let text : string = "This text passed by ";
  console.info(".newTargetTree: text=", text);
  let coupler : any = {text: text};
  let first : any = Superficial.newTextual(TITLE_FIRST, coupler);
  let second : any = Superficial.newTextual(TITLE_SECOND, coupler);
  return Superficial.newTargetGroup("Textuals", first, second);
}
function buildLayout() {
  const fn=function(update):void{
    console.log(update)
  }
  Superficial.attachFacet(TITLE_FIRST,fn);
}
buildSurface();