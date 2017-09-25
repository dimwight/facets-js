export {Facets};

/**
* For passing simple state in and out of a {Facets}.
*/
export type SimpleState=string|boolean|number
/**
Marker interface for Facets implementation of Superficial target.
*/
export interface Target{}
interface TargetCoupler {
/**
 * Called on update of the target constructed with the coupler.
 * @param {string} title identifies the target
 * @param state the updated state
 */
targetStateUpdated?: (title: string, state: SimpleState) => void;
}
/**
Connects a textual target with client code.
*/
export interface TextualCoupler extends TargetCoupler{
  /**
  Sets initial state of the textual.
  */
  passText?:string;
  /**
  * Supply state for the target.
  * Must be implemented if no passText.
  * @param {string} title identifies the target
  * @returns {string} the state
  */
  getText?:(title:string)=>string;
  /**
  * Allows validation of changed target state
  * @param {string} title identifies the target
  * @param {string} text to validate
  * @returns {boolean} true if valid
  */
  isValidText?:(title:string,text:string)=>boolean;
}
interface TogglingCoupler extends TargetCoupler {
  passSet: boolean;
}
interface IndexingCoupler extends TargetCoupler{
  passIndexables:any[];
  passIndex:number;
  getFacetIndexables: (title: string) => string[];
}
interface IndexingState {
  indexables: string[];
  indexed: any;
}
interface IndexingFrameProxy {
  title: string;
  indexingTitle: string;
  content: any[];
  newFacetIndexables: (indexables: any[]) => string[];
  newEditElements: (indexed: any) => any[];
  frame?: Target;
  getIndexedContent?: any;
}
interface NumericCoupler extends TargetCoupler {
  passValue?: number;
  min: number;
  max: number;
}
/**
* Constructs a new Superficial application core.
* @param {boolean} trace
* @returns {Facets.Facets}
*/
export function newInstance(trace:boolean):Facets;
/**
* A Superficial application core.
*/
interface Facets{
  updatedTarget(target:any,coupler:TargetCoupler):void;
  /**
   *
   * @param {string} title identifies the target or its targeter
   * @param {Facets.TextualCoupler} coupler connects the target to client code
   * @returns textual {Facets.Target}
   */
  newTextualTarget(title:string,coupler:TextualCoupler):Target;
  newTogglingTarget(title: string, c: TogglingCoupler): Target;
  /**
   *
   * @param {string} title for the target
   * @param {Facets.Target} members of the group
   * @returns group of {Facets.Target}s
   */
  newIndexingTarget(title:string,coupler:IndexingCoupler):Target;
  getIndexingState(title: string): IndexingState;
  buildIndexingFrame(proxy: IndexingFrameProxy): void;
  newNumericTarget(title: string, coupler: NumericCoupler): Target;
  newTargetGroup(title:string,...members:Target[]):Target;
  /**
   * Constructs a tree of targeters using the initial target tree.
   * @param {Facets.Target} targets the root of the target tree
   */
  buildTargeterTree(targets:Target):void;
  /**
   * Attach an internal facet to the targeter with the target title passed.
   * @param {string} title identifies the targeter
   * @param {(state) => void} facetUpdated callback to update the UI with the target state
   */
  attachFacet(title:string,facetUpdated:(state:SimpleState)=>void):void;
  /**
   * Update the state of the target identified.
   * @param {string} title identifies the target
   * @param {SimpleState} update to update the target
   */
  updateTargetState(title:string,update:SimpleState):void;
  /**
   * Obtain the the state of the target identified.
   * @param {string} title identifies the target
   * @returns {SimpleState} the state
   */
  getTargetState(title:string):SimpleState;
  setTargetLive(title: string, live: boolean): void;
  isTargetLive(title: string): boolean;
}