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
/**
 Connects a toggling (boolean) target with client code.
 */
interface TogglingCoupler extends TargetCoupler {
  /**
   Sets initial state of the toggling.
   */
  passSet: boolean;
}
/**
 Connects a numeric target with client code.
 */
interface NumericCoupler extends TargetCoupler {
  /**
   Sets initial state of the numeric.
   */
  passValue: number;
  /**
   Sets minimum state of the numeric.
   */
  min: number;
  /**
   Sets maximum state of the numeric.
   */
  max: number;
}
/**
 Connects an indexing (list-type) target with client code.
 */
interface IndexingCoupler extends TargetCoupler{
  /**
   Sets initial state of the indexing (the index into its contents).
   */
  passIndex:number;
  /**
   * Get the contents to be indexed
   * @param {string} title idt
   * @returns {any[]}
   */
  getIndexables: (title: string) => any[];
  /**
   * Get the strings to represent the contents in the UI
   * @param {string} title idt
   * @returns {string[]}
   */
  getFacetIndexables: (title: string) => string[];
}
/**
 * Current values exposed by the indexing
 */
interface IndexingState {
  /**
   * As last created by IndexingCoupler.getFacetIndexables
   */
  facetIndexables: string[];
  /**
   * The result of the current index into the content array.
   */
  indexed: any;
}
/**
 * Enables definition and communication with a Target that wraps
 * content selected with an indexing.
 */
interface IndexingFramePolicy {
  /**
   * Will be the title of the wrapping Target.
   */
  title: string;
  /**
   * Will be the title of the wrapped indexing.
   */
  indexingTitle: string;
  /**
   * Supply content to be selected.
   * @returns {any[]}
   */
  getContent: () => any[];
  /**
   * Supply strings to expose the content in the UI.
   * @param {any[]} content supplied by getContent
   * @returns {string[]}
   */
  newFacetIndexables: (content: any[]) => string[];
  /**
   * Create Targets exposing the selected content
   * @param indexed selected with the indexing
   * @returns {Target[]}
   */
  newIndexedElements: (indexed: any) => Target[];
  /**
   * Optionally supply Targets exposing the content array.
   * @returns {Target[]}
   */
  newFrameElements?: () => Target[];
  /**
   * The wrapping Target
   */
  frame?: Target;
  /**
   * Equivalent of IndexingState.indexed
   */
  getIndexedContent?: any;
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
  newNumericTarget(title: string, coupler: NumericCoupler): Target;
  newTriggerTarget(title: string, coupler: TargetCoupler): Target;
  newTargetGroup(title:string,...members:Target[]):Target;
  getIndexingState(title: string): IndexingState;
  buildIndexingFrame(policy: IndexingFramePolicy): void;
  /**
   * Constructs a tree of targeters using the initial target tree.
   * @param {Facets.Target} targets the root of the target tree
   */
  buildTargeterTree(targetTree:Target):void;
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