/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
import { Notifiable } from '../../in/fjs/core/Notifiable';
import { SFacet } from '../../in/fjs/core/SFacet';
import { STarget } from '../../in/fjs/core/STarget';
import { STargeter } from '../../in/fjs/core/STargeter';
import { STextual } from '../../in/fjs/core/STextual';
import { TargetCore } from '../../in/fjs/core/TargetCore';
import { Debug } from '../../in/fjs/util/Debug';
import { Tracer } from '../../in/fjs/util/Tracer';
export let trace : boolean = true;

let t : Tracer.TracerTopped; function t_$LI$() : Tracer.TracerTopped { if(t == null) t = new Facets.Globals$0("Facets"); return t; };

let titleTargeters : any; function titleTargeters_$LI$() : any { if(titleTargeters == null) titleTargeters = <any>({}); return titleTargeters; };

let targeterTree : STargeter = null;

let notifiable : Notifiable; function notifiable_$LI$() : Notifiable { if(notifiable == null) notifiable = new Facets.Globals$1(); return notifiable; };

export function newTextualTarget(title : string, c : TextualCoupler) : any {
    let textual : STextual = new STextual(title, new Facets.Globals$2(c));
    let passText : string = c.passText;
    if(passText != null) textual.setText(passText);
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Created textual ", textual);
    return textual;
}

export function newTargetsGroup(title : string, ...members : any[]) : any {
    let group : TargetCore = <any>new (__Function.prototype.bind.apply(TargetCore, [null, title].concat(<any[]>STarget.newTargets(members))));
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Created target group " + Debug.info(group) + " ", members);
    return group;
}

export function buildTargeterTree(targets : any) {
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Creating targeters for ", targets);
    targeterTree = (<TargetCore>targets).newTargeter();
    targeterTree.setNotifiable(notifiable_$LI$());
    targeterTree.retarget(<STarget><any>targets);
    {
        let array122 = targeterTree.elements();
        for(let index121=0; index121 < array122.length; index121++) {
            let targeter = array122[index121];
            {
                /* put */(titleTargeters_$LI$()[targeter.title()] = targeter);
                t_$LI$().trace$java_lang_String$java_lang_Object(" > Created targeter ", targeter);
            }
        }
    }
}

export function attachFacet(title : string, facetUpdated : any) {
    if(facetUpdated == null) throw Object.defineProperty(new Error("Null facetUpdated for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] }); else if(title == null || /* equals */(<any>((o1: any, o2: any) => { if(o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(title,""))) throw Object.defineProperty(new Error("Null or empty title for " + facetUpdated), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
    let targeter : STargeter = /* get */((m,k) => m[k]?m[k]:null)(titleTargeters_$LI$(), title);
    if(targeter == null) throw Object.defineProperty(new Error("Null targeter for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Attaching facet to", targeter);
    targeter.attachFacet(new Facets.Globals$3(facetUpdated));
}

export function updateTargetState(title : string, update : any) {
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Updating target state for title=" + title + " update=", update);
    let target : STarget = titleTarget(title);
    target.updateState(update);
    target.notifyParent();
}

export function getTargetState(title : string) : any {
    let state : any = titleTarget(title).state();
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Getting target state for title=" + title + " state=", state);
    return state;
}

function titleTarget(title : string) : STarget {
    let target : STarget = /* get */((m,k) => m[k]?m[k]:null)(titleTargeters_$LI$(), title).target();
    return target;
}

export interface TextualCoupler extends Facets.TargetCoupler {
  passText? : string;

  isValidText? : (p1: string, p2: string) => boolean;

  getText? : (p1: string) => string;

  updateInterim? : (p1: string) => boolean;
}


export namespace Facets {

    export class TargetCoupler {
        public targetStateUpdated : (p1: string) => void;

        constructor() {
            this.targetStateUpdated = null;
        }
    }
    TargetCoupler["__class"] = "fjs.globals.Facets.TargetCoupler";


    export class Globals$0 extends Tracer.TracerTopped {
        doTrace() : boolean {
            return trace;
        }

        constructor(__arg0: any) {
            super(__arg0);
        }
    }
    Globals$0["__interfaces"] = ["fjs.util.Identified"];



    export class Globals$1 implements Notifiable {
        /**
         * 
         * @param {*} notice
         */
        public notify(notice : any) {
            t_$LI$().trace$java_lang_String(" > Surface for " + Debug.info(targeterTree) + " notified by " + notice);
            let targets : STarget = targeterTree.target();
            targeterTree.retarget(targets);
            t_$LI$().trace$java_lang_String$java_lang_Object(" > Targeters retargeted on ", targets);
            targeterTree.retargetFacets();
            t_$LI$().trace$java_lang_String$java_lang_Object(" > Facets retargeted in ", targeterTree);
        }

        /**
         * 
         * @return {string}
         */
        public title() : string {
            throw Object.defineProperty(new Error("Not implemented in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        constructor() {
        }
    }
    Globals$1["__interfaces"] = ["fjs.core.Notifiable","fjs.util.Titled"];



    export class Globals$2 extends STextual.Coupler {
        /**
         * 
         * @param {STextual} textual
         */
        public textSet(textual : STextual) {
            let title : string = textual.title();
            t_$LI$().trace$java_lang_String$java_lang_Object(" > Updated textual ", textual);
            (target => (typeof target === 'function')?target(title):(<any>target).accept(title))(this.c.targetStateUpdated);
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

        constructor(private c: any) {
            super();
        }
    }
    Globals$2["__interfaces"] = ["fjs.util.Identified","fjs.core.TargetCoupler","java.io.Serializable"];



    export class Globals$3 implements SFacet {
        /**
         * 
         * @param {*} target
         */
        public retarget(target : STarget) {
            let state : any = target.state();
            (target => (typeof target === 'function')?target(state):(<any>target).accept(state))(this.facetUpdated);
        }

        constructor(private facetUpdated: any) {
        }
    }
    Globals$3["__interfaces"] = ["fjs.core.SRetargetable","fjs.core.SFacet"];


}



var __Function = Function;
