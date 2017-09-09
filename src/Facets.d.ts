declare namespace Facets{
  interface Target{}
  interface TargetCoupler {
    targetStateUpdated: (p1: string, p2: any) => void;
    constructor();
  }
  interface TextualCoupler extends TargetCoupler{
    passText?:string;
    isValidText?:(p1:string,p2:string)=>boolean;
    getText?:(p1:string)=>string;
    updateInterim?:(p1:string)=>boolean;
  }
  interface IndexingCoupler extends TargetCoupler{
    passIndexables:any[];
    passIndex:number;
  }
  function newInstance(trace:boolean):any;
  function updatedTarget(target:any,c:TargetCoupler):void;
  function newIndexingTarget(title:string,c:IndexingCoupler):Target;
  function newTextualTarget(title:string,c:TextualCoupler):Target;
  function newTargetsGroup(title:string,...members:any[]):Target;
  function buildTargeterTree(targets:any):void;
  function attachFacet(title:string,facetUpdated:any):void;
  function updateTargetState(title:string,update:any):void;
  function getTargetState(title:string):any;
}