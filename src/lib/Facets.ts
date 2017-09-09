/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
import { Notifiable } from '../../in/fjs/core/Notifiable';
import { SFacet } from '../../in/fjs/core/SFacet';
import { SIndexing } from '../../in/fjs/core/SIndexing';
import { STarget } from '../../in/fjs/core/STarget';
import { STargeter } from '../../in/fjs/core/STargeter';
import { STextual } from '../../in/fjs/core/STextual';
import { TargetCore } from '../../in/fjs/core/TargetCore';
import { Debug } from '../../in/fjs/util/Debug';
import { Tracer } from '../../in/fjs/util/Tracer';
export class Facets extends Tracer {
    public __trace : boolean;

    /*private*/ titleTargeters : any = <any>({});

    /*private*/ targeterTree : STargeter;

    /*private*/ notifiable : Notifiable = new Facets$0(this);

    constructor(top : string, trace : boolean) {
        super(top);
        this.__trace = false;
        this.targeterTree = null;
        this.__trace = trace;
    }

    traceOutput(msg : string) {
        if(this.__trace) super.traceOutput(msg);
    }

    updatedTarget(target : STarget, c : TargetCoupler) {
        let title : string = target.title();
        let state : any = target.state();
        this.trace$java_lang_String$java_lang_Object(" > Updated target " + target.title() + " with ", state);
        (target => (typeof target === 'function')?target(title, state):(<any>target).accept(title, state))(c.targetStateUpdated);
    }

    public newIndexingTarget(title : string, c : IndexingCoupler) : any {
        let indexing : SIndexing = new SIndexing(title, c.passIndexables, new Facets$1(this, c));
        let passIndex : number = c.passIndex;
        if(passIndex != null) indexing.setIndex(passIndex);
        this.trace$java_lang_String$java_lang_Object(" > Created indexing ", indexing);
        return indexing;
    }

    public newTextualTarget(title : string, c : TextualCoupler) : any {
        let textual : STextual = new STextual(title, new Facets$2(this, c));
        let passText : string = c.passText;
        if(passText != null) textual.setText(passText);
        this.trace$java_lang_String$java_lang_Object(" > Created textual ", textual);
        return textual;
    }

    public newTargetsGroup(title : string, ...members : any[]) : any {
        let group : TargetCore = <any>new (__Function.prototype.bind.apply(TargetCore, [null, title].concat(<any[]>STarget.newTargets(members))));
        this.trace$java_lang_String$java_lang_Object(" > Created target group " + Debug.info(group) + " ", members);
        return group;
    }

    public buildTargeterTree(targets : any) {
        this.trace$java_lang_String$java_lang_Object(" > Creating targeters for ", targets);
        this.targeterTree = (<TargetCore>targets).newTargeter();
        this.targeterTree.setNotifiable(this.notifiable);
        this.targeterTree.retarget(<STarget><any>targets);
        {
            let array131 = this.targeterTree.elements();
            for(let index130=0; index130 < array131.length; index130++) {
                let targeter = array131[index130];
                {
                    /* put */(this.titleTargeters[targeter.title()] = targeter);
                    this.trace$java_lang_String$java_lang_Object(" > Created targeter ", targeter);
                }
            }
        }
    }

    public attachFacet(title : string, facetUpdated : any) {
        if(facetUpdated == null) throw Object.defineProperty(new Error("Null facetUpdated for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] }); else if(title == null || /* equals */(<any>((o1: any, o2: any) => { if(o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(title,""))) throw Object.defineProperty(new Error("Null or empty title for " + facetUpdated), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        let targeter : STargeter = /* get */((m,k) => m[k]?m[k]:null)(this.titleTargeters, title);
        if(targeter == null) throw Object.defineProperty(new Error("Null targeter for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        let facet : SFacet = new Facets$3(this, facetUpdated);
        this.trace$java_lang_String$java_lang_Object(" > Attaching facet " + facet + " to", targeter);
        targeter.attachFacet(facet);
    }

    public updateTargetState(title : string, update : any) {
        this.trace$java_lang_String$java_lang_Object(" > Updating target state for title=" + title + " update=", update);
        let target : STarget = this.titleTarget(title);
        target.updateState(update);
        target.notifyParent();
    }

    public getTargetState(title : string) : any {
        let state : any = this.titleTarget(title).state();
        this.trace$java_lang_String$java_lang_Object(" > Getting target state for title=" + title + " state=", state);
        return state;
    }

    titleTarget(title : string) : STarget {
        let targeter : STargeter = /* get */((m,k) => m[k]?m[k]:null)(this.titleTargeters, title);
        if(targeter == null) throw Object.defineProperty(new Error("Null target for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        let target : STarget = targeter.target();
        return target;
    }
}
Facets["__class"] = "fjs.globals.Facets";
Facets["__interfaces"] = ["fjs.util.Identified"];



// export namespace Facets {

    export class TargetCoupler {
        public targetStateUpdated : (p1: string, p2: any) => void;

        constructor() {
            this.targetStateUpdated = null;
        }
    }
    TargetCoupler["__class"] = "fjs.globals.TargetCoupler";


    export interface TextualCoupler extends TargetCoupler {
        passText? : string;

        isValidText? : (p1: string, p2: string) => boolean;

        getText? : (p1: string) => string;

        updateInterim? : (p1: string) => boolean;
    }

    export interface IndexingCoupler extends TargetCoupler {
        passIndexables : any[];

        passIndex : number;
    }

    export class Facets$0 implements Notifiable {
        public __parent: any;
        /**
         *
         * @param {*} notice
         */
        public notify(notice : any) {
            this.__parent.trace(" > Surface for " + Debug.info(this.__parent.targeterTree) + " notified by " + notice);
            let targets : STarget = this.__parent.targeterTree.target();
            this.__parent.targeterTree.retarget(targets);
            this.__parent.trace(" > Targeters retargeted on ", targets);
            this.__parent.targeterTree.retargetFacets();
            this.__parent.trace(" > Facets retargeted in ", this.__parent.targeterTree);
        }

        /**
         *
         * @return {string}
         */
        public title() : string {
            throw Object.defineProperty(new Error("Not implemented in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        constructor(__parent: any) {
            this.__parent = __parent;
        }
    }
    Facets$0["__interfaces"] = ["fjs.core.Notifiable","fjs.util.Titled"];



    export class Facets$1 extends SIndexing.Coupler {
        public __parent: any;
        /**
         *
         * @param {SIndexing} target
         */
        public indexSet(target : SIndexing) {
            this.__parent.updatedTarget(target, this.c);
        }

        constructor(__parent: any, private c: any) {
            super();
            this.__parent = __parent;
        }
    }
    Facets$1["__interfaces"] = ["fjs.core.TargetCoupler","java.io.Serializable"];



    export class Facets$2 extends STextual.Coupler {
        public __parent: any;
        /**
         *
         * @param {STextual} target
         */
        public textSet(target : STextual) {
            this.__parent.updatedTarget(target, this.c);
        }

        /**
         *
         * @param {STextual} t
         * @param {string} text
         * @return {boolean}
         */
        public isValidText(t : STextual, text : string) : boolean {
            let isValidText : (p1: string, p2: string) => boolean = <any>(this.c.isValidText);
            return isValidText == null?true:(target => (typeof target === 'function')?target(t.title(), text):(<any>target).test(t.title(), text))(isValidText);
        }

        /**
         *
         * @param {STextual} t
         * @return {boolean}
         */
        public updateInterim(t : STextual) : boolean {
            let updateInterim : (p1: string) => boolean = <any>(this.c.updateInterim);
            return updateInterim == null?false:(target => (typeof target === 'function')?target(t.title()):(<any>target).test(t.title()))(updateInterim);
        }

        /**
         *
         * @param {STextual} t
         * @return {string}
         */
        getText(t : STextual) : string {
            let getText : (p1: string) => string = <any>(this.c.getText);
            if(getText == null) throw Object.defineProperty(new Error("Null getText in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] }); else return (target => (typeof target === 'function')?target(t.title()):(<any>target).apply(t.title()))(getText);
        }

        constructor(__parent: any, private c: any) {
            super();
            this.__parent = __parent;
        }
    }
    Facets$2["__interfaces"] = ["fjs.util.Identified","fjs.core.TargetCoupler","java.io.Serializable"];



    export class Facets$3 implements SFacet {
        public __parent: any;
        id : number;

        stateThen : any;

        /**
         *
         * @param {*} target
         */
        public retarget(target : STarget) {
            let state : any = target.state();
            if(false) this.__parent.trace(".attachFacet #" + this.__parent.id + ".retarget: state=" + state + " stateThen=" + this.stateThen);
            if(!/* equals */(<any>((o1: any, o2: any) => { if(o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(state,this.stateThen))) {
                this.__parent.trace(" > Updating UI with state=", state);
                this.stateThen = state;
                (target => (typeof target === 'function')?target(state):(<any>target).accept(state))(this.facetUpdated);
            }
        }

        /**
         *
         * @return {string}
         */
        public toString() : string {
            return "#" + this.__parent.id;
        }

        constructor(__parent: any, private facetUpdated: any) {
            this.__parent = __parent;
            this.id = Tracer.ids++;
            this.stateThen = null;
        }
    }
    Facets$3["__interfaces"] = ["fjs.core.SRetargetable","fjs.core.SFacet"];


// }

export function newInstance(trace : boolean) : Facets {
  return new Facets("Facets", trace);
}
var __Function = Function;
