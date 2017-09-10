export default Facets;

declare namespace Facets{
  /**
   * For passing target state in and out.
   */
  type TargetState=string|boolean|number
  /**
   Marker interface
   */
  interface Target{}
  interface TargetCoupler {
    /**
     * Called on update of the target constructed with the coupler. 
     * @param {string} title identifies the target updated
     * @param state the updated state
     */
    targetStateUpdated: (title: string, state: TargetState) => void;
  }
  /**
  Connects a textual target with client code.
   */
  interface TextualCoupler extends TargetCoupler{
    /**
     Sets initial state of the textual.
     */
    passText:string;
    /**
     * Allows validation of the changed target state
     * @param {string} title identifies the target
     * @param {string} text to validate
     * @returns {boolean} true if valid
     */
    isValidText?:(title:string,text:string)=>boolean;
    /**
     * Supply state for the target.
     * Must be implemented if no passText.
     * @param {string} title identifies the target
     * @returns {string} the state
     */
    getText?:(title:string)=>string;
    /**
     * Specify flash retargeting during text entry
     * @param {string} title identifies the target
     * @returns {boolean} true if required
     */
    updateInterim?:(title:string)=>boolean;
  }
  interface IndexingCoupler extends TargetCoupler{
    passIndexables:any[];
    passIndex:number;
  }
  function newInstance(trace:boolean):Facets;
  interface Facets{
    updatedTarget(target:any,coupler:TargetCoupler):void;
    newIndexingTarget(title:string,coupler:IndexingCoupler):Target;
    /**
     *
     * @param {string} title identifies the target or its targeter
     * @param {Facets.TextualCoupler} coupler connects the target to client code
     * @returns textual {Facets.Target}
     */
    newTextualTarget(title:string,coupler:TextualCoupler):Target;
    /**
     *
     * @param {string} title for the target
     * @param {Facets.Target} members of the group
     * @returns group of {Facets.Target}s
     */
    newTargetsGroup(title:string,...members:Target[]):Target;
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
    attachFacet(title:string,facetUpdated:(state:TargetState)=>void):void;
    /**
     * Update the state of the target identified. 
     * @param {string} title identifies the target
     * @param {TargetState} update to update the target
     */
    updateTargetState(title:string,update:TargetState):void;
    /**
     * Obtain the the state of the target identified.
     * @param {string} title identifies the target
     * @returns {TargetState} the state
     */
    getTargetState(title:string):TargetState;
  }
}