/**
 Marker interface for Facets implementation of Superficial target.
 */
export interface Target{}
/**
 * For passing state in and out of a simple {@link Target}.
 */
export type SimpleState=string|boolean|number
/** */
export interface TargetCoupler {
  /**
   * Called on update of the {@link Target} constructed with the coupler.
   * @param state the updated state
   * @param {string} title identifies the {@link Target}
   */
  targetStateUpdated?: (state: SimpleState, title: string) => void;
}
/**
 Connects a textual {@link Target} with client code.
 */
export interface TextualCoupler extends TargetCoupler {
  /**
   Sets initial state of the textual.
   * If not supplied, {@link getText} must be.
   */
  passText?: string;
  /**
   * Supply state for the {@link Target}.
   * Must be supplied in the absence of{@link passText}.
   * @param {string} title identifies the {@link Target}
   * @returns {string} the state
   */
  getText?: (title: string) => string;
  /**
   * Allows validation of changed text state
   * @param {string} text to validate
   * @param {string} title identifies the {@link Target}
   * @returns {boolean} true if valid
   */
  isValidText? (text: string, title: string) : boolean;
}
/**
 Connects a toggling (boolean) {@link Target} with client code.
 */
export interface TogglingCoupler extends TargetCoupler {
  /**
   Sets initial state of the toggling.
   */
  passSet: boolean;
}
/**
 Connects a numeric {@link Target} with client code.
 */
export interface NumericCoupler extends TargetCoupler {
  /**
   Sets initial state of the {@link Target}.
   */
  passValue: number;
  /**
   Minimum state of the {@link Target}.
   */
  min: number;
  /**
   Maximum state of the {@link Target}.
   */
  max: number;
}
/**
 Connects an indexing (list-type) {@link Target} with client code.
 */
export interface IndexingCoupler extends TargetCoupler {
  /**
   Sets initial state of the {@link Target} (the index into its contents).
   */
  passIndex: number;
  /**
   * Get the contents to be indexed
   * @param title identifies the {@link Target}
   * @returns {any[]} the contents
   */
  getIndexables (title: string) : any[];
  /**
   * Get string to represent indexable content in the UI
   * @param {string} title identifies the {@link Target}
   * @returns the string
   */
  newUiSelectable? (indexable: any) : string;
}
/**
 * Current values exposed by an indexing {@link Target}
 */
interface IndexingState {
  /**
   * As last created by {@link IndexingCoupler}
   */
  uiSelectables: string[];
  /**
   * The result of the current index into the indexables.
   */
  indexed: any;
}
/**
 * Defines a {@link Target} that wraps an indexing {@link Target}.
 */
export interface IndexingFramePolicy {
  /**
   * Title for the wrapping {@link Target}.
   */
  frameTitle?: string;
  /**
   * Title for the wrapped indexing {@link Target}.
   */
  indexingTitle: string;
  /**
   * Get items to be indexed.
   */
  getIndexables () : any[];
  /**
   * Supply  string to expose content item in the UI.
   * Analogue of {@link IndexingCoupler} function.
   * @param {any} content to stringify
   * @returns {string} the string
   */
  newUiSelectable? (indexable: any):string;
  /**
   * Create {@link Target}s to be attached to the framing {@link Target}
   * @returns {Target[]} the targets
   */
  newFrameTargets? () :Target[];
  /**
   * Provides for defining different indexable types.
   * Will be passed to {@link newIndexedTree}
   * @param {any} indexed selected with the framed indexing {@link Target}
   */
  newIndexedTreeTitle? (indexed: any): string;
  /**
   * Create {@link Target}s exposing the indexed content
   * @param {any} indexed selected with the framed indexing {@link Target}
   * @param title from {@link newIndexedTreeTitle} or created by framework
   * @returns {Target} root of tree
   */
  newIndexedTree? (indexed: any, indexedTreeTitle: string) : Target;
}
/** Manages time tracing by the {@link Facets} instance. */
export interface Times {
  /** Should trace messages include times? */
  doTime: boolean;
  /** Set the automatic reset timeout and reset {@link elapsed()}
   *  */
  setResetWait(millis: number): void;
  /** Time since the last (usually automatic) reset.
   */
  elapsed(): number;
  /**
   * Print {@link elapsed()} followed by the message.
   * @param {string} msg to append to time
   */
  traceElapsed(msg: string): void;
}
/**
 * A Superficial application core.
 */
export interface Facets {
  /** Built-in instance. */
  times: Times;
  /** Should the {@link Facets} instance issue trace messages? */
  doTrace: boolean;
  /** Creates a textual {@link Target}.
   *  @param {string} title identifies the {@link Target}
   * @param {TextualCoupler} coupler connects the {@link Target} to client code
   * @returns the {@link Target}
   */
  newTextualTarget(title: string, coupler: TextualCoupler): Target;
  /** Creates a boolean {@link Target}.
   *  @param {string} title identifies the {@link Target}
   * @param {TextualCoupler} coupler connects the {@link Target} to client code
   * @returns the {@link Target}
   */
  newTogglingTarget(title: string, c: TogglingCoupler): Target;
  /** Creates a numeric {@link Target}.
   *  @param {string} title identifies the {@link Target}
   * @param {TextualCoupler} coupler connects the {@link Target} to client code
   * @returns the {@link Target}
   */
  newNumericTarget(title: string, coupler: NumericCoupler): Target;
  /** Creates a stateless 'action' {@link Target}.
   *  @param {string} title identifies the {@link Target}
   * @param {TextualCoupler} coupler connects the {@link Target} to client code
   * @returns the {@link Target}
   */
  newTriggerTarget(title: string, coupler: TargetCoupler): Target;
  /** Creates a {@link Target} containing others.
   * @param {string} title for the {@link Target}
   * @param {{@link Target}} members of the group
   * @returns group of {Target}s
   */
  newTargetGroup(title: string, members: Target[]): Target;
  /** Creates a {@link Target} that indexes a list.
   *  @param {string} title identifies the {@link Target}
   * @param {TextualCoupler} coupler connects the {@link Target} to client code
   * @returns the {@link Target}
   */
  newIndexingTarget(title: string, coupler: IndexingCoupler): Target;
  /** Provides information on the current state of an indexing {@link Target}
   *  @param {string} title identifies the {@link Target}
   */
  getIndexingState(title: string): IndexingState;
  /** Creates a {@link Target} framing an indexing {@link Target}.
   *  @param {IndexingFramePolicy} policy defines both {@link Target}s
   *  */
  newIndexingFrame(policy: IndexingFramePolicy): Target;
  /* Adds the content tree to the application.
   * The tree added becomes the active tree
   * @param {@link Target} tree to be added
   */
  addContentTree(add: Target): void;
  /**
   * Activate an existing content tree.
   * @param {string} title identifies the tree
   */
  activateContentTree(title: string): void;
  /**
   * Constructs a tree of targeters using the initial {@link Target} tree.
   * @param {{@link Target}} targetTree the root of the {@link Target} tree
   */
  buildTargeterTree(): void;
  /**
   * Attach an internal facet to the targeter with the {@link Target} title passed.
   * @param {string} title identifies the targeter
   * @param {(state) => void} facetUpdated callback to update the UI with the {@link Target} state
   */
  attachFacet(title: string, facetUpdated: (state: SimpleState) => void): void;
  /**
   * Update the state of the {@link Target} identified.
   * @param {string} title identifies the {@link Target}
   * @param {SimpleState} update to update the {@link Target}
   */
  updateTargetState(title: string, update: SimpleState): void;
  /**
   * Obtain the the state of the {@link Target} identified.
   * @param {string} title identifies the {@link Target}
   * @returns {SimpleState} the state
   */
  getTargetState(title: string): SimpleState;
  /**
   * Notify the framework of an update and trigger a retargeting.
   * @param {string} title identifies the {@link Target}
   */
  notifyTargetUpdated(title: string): void;
  /**
   * Update {@link Target} and and trigger a retargeting.
   * @param {string} title identifies the {@link Target}
   * @param {SimpleState} update for {@link Target} state
   */
  updateTargetWithNotify(title: string, update: SimpleState): void;
  /** */
  setTargetLive(title: string, live: boolean): void;
  /** */
  isTargetLive(title: string): boolean;
  /**
   * If supplied by client code, called after retargeting the {@link Target} tree but
   * before retargeting facets.
   * @param {string} activeTitle of the last content activated with
   * {@link activateContentTree()}.
   */
  onRetargeted (activeTitle:string)
  /** */
  supplement: any;
  /**
   * Construct a new surface using the callbacks passed.
   * @param newTrees to define the initial content; returns passed successively to
   * {@link addContentTree()}
   * @param  buildLayout to construct the UI
   * @param  onRetargeted set as {@link onRetargeted}
   */
  buildSurface(newTrees: newFacetsTargetTrees, buildLayout: buildFacetsLayout,
               onRetargeted?:addOnRetargeted): void;
}
/**
 * Documents callback required by {@link buildSurface()}
 */
export type newFacetsTargetTrees=(facets: Facets)=>Target|Target[]
/**
 * Documents callback required by {@link buildSurface()}
 */
export type buildFacetsLayout=(facets: Facets)=>void
/** For attachment as {@link onRetargeted()}
 * */
export type addOnRetargeted=(facets: Facets,activeTarget?: string)=>void
/** Create a new {@link Facets} instance.
 *  @param trace should the instance log lifecycle events?
 * */
export function newInstance(trace: boolean): Facets;
