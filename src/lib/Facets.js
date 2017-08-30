import { STarget } from '../../in/fjs/core/STarget';
import { STextual } from '../../in/fjs/core/STextual';
import { TargetCore } from '../../in/fjs/core/TargetCore';
import { Debug } from '../../in/fjs/util/Debug';
import { Tracer } from '../../in/fjs/util/Tracer';
export let trace = false;
let t;
function t_$LI$() { if (t == null)
    t = new Facets.Globals$0("FacetsLib"); return t; }
;
let titleTargeters;
function titleTargeters_$LI$() { if (titleTargeters == null)
    titleTargeters = ({}); return titleTargeters; }
;
let targeterTree = null;
let notifiable;
function notifiable_$LI$() { if (notifiable == null)
    notifiable = new Facets.Globals$1(); return notifiable; }
;
export function newTextual(title, c) {
    let textual = new STextual(title, new Facets.Globals$2(c));
    let passText = c.passText;
    if (passText != null)
        textual.setText(passText);
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Created textual ", textual);
    return textual;
}
export function newTargetGroup(title, ...members) {
    let group = new (__Function.prototype.bind.apply(TargetCore, [null, title].concat(STarget.newTargets(members))));
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Created target group " + Debug.info(group) + " ", members);
    return group;
}
export function buildTargeterTree(targets) {
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Creating targeters for ", targets);
    targeterTree = targets.newTargeter();
    targeterTree.setNotifiable(notifiable_$LI$());
    targeterTree.retarget(targets);
    {
        let array170 = targeterTree.elements();
        for (let index169 = 0; index169 < array170.length; index169++) {
            let targeter = array170[index169];
            {
                /* put */ (titleTargeters_$LI$()[targeter.title()] = targeter);
                t_$LI$().trace$java_lang_String$java_lang_Object(" > Created targeter ", targeter);
            }
        }
    }
}
export function attachFacet(title, updater) {
    if (updater == null)
        throw Object.defineProperty(new Error("Null updateFn for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
    else if (title == null || ((o1, o2) => { if (o1 && o1.equals) {
        return o1.equals(o2);
    }
    else {
        return o1 === o2;
    } })(title, ""))
        throw Object.defineProperty(new Error("Null or empty title for " + updater), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
    let targeter = ((m, k) => m[k] ? m[k] : null)(titleTargeters_$LI$(), title);
    if (targeter == null)
        throw Object.defineProperty(new Error("Null targeter for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Attaching facet to ", targeter);
    targeter.attachFacet(new Facets.Globals$3(updater));
}
export function updateTarget(title, update) {
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Updating target: title=" + title + " update=", update);
    let target = titleTarget(title);
    target.updateState(update);
    target.notifyParent();
}
function titleTarget(title) {
    let target = ((m, k) => m[k] ? m[k] : null)(titleTargeters_$LI$(), title).target();
    return target;
}
export function getTargetState(title) {
    return titleTarget(title).state();
}
export var Facets;
(function (Facets) {
    class Globals$0 extends Tracer.TracerTopped {
        constructor(__arg0) {
            super(__arg0);
        }
        doTrace() {
            return trace;
        }
    }
    Facets.Globals$0 = Globals$0;
    Globals$0["__interfaces"] = ["fjs.util.Identified"];
    class Globals$1 {
        constructor() {
        }
        /**
         *
         * @param {*} notice
         */
        notify(notice) {
            t_$LI$().trace$java_lang_String(" > Surface for " + Debug.info(targeterTree) + " notified by " + notice);
            let targets = targeterTree.target();
            targeterTree.retarget(targets);
            t_$LI$().trace$java_lang_String$java_lang_Object(" > Targeters retargeted on ", targets);
            targeterTree.retargetFacets();
            t_$LI$().trace$java_lang_String$java_lang_Object(" > Facets retargeted in ", targeterTree);
        }
        /**
         *
         * @return {string}
         */
        title() {
            throw Object.defineProperty(new Error("Not implemented in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        }
    }
    Facets.Globals$1 = Globals$1;
    Globals$1["__interfaces"] = ["fjs.core.Notifiable", "fjs.util.Titled"];
    class Globals$2 extends STextual.Coupler {
        constructor(c) {
            super();
            this.c = c;
        }
        /**
         *
         * @param {STextual} t
         */
        textSet(t) {
            (target => (typeof target === 'function') ? target(t.title()) : target.accept(t.title()))(this.c.stateUpdated);
        }
        /**
         *
         * @param {STextual} t
         * @param {string} text
         * @return {boolean}
         */
        isValidText(t, text) {
            let isValidText = (this.c.isValidText);
            return isValidText == null ? true : (target => (typeof target === 'function') ? target(t.title(), text) : target.test(t.title(), text))(isValidText);
        }
        /**
         *
         * @param {STextual} t
         * @return {boolean}
         */
        updateInterim(t) {
            let updateInterim = (this.c.updateInterim);
            return updateInterim == null ? false : (target => (typeof target === 'function') ? target(t.title()) : target.test(t.title()))(updateInterim);
        }
        /**
         *
         * @param {STextual} t
         * @return {string}
         */
        getText(t) {
            let getText = (this.c.getText);
            if (getText == null)
                throw Object.defineProperty(new Error("Null getText in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
            else
                return (target => (typeof target === 'function') ? target(t.title()) : target.apply(t.title()))(getText);
        }
    }
    Facets.Globals$2 = Globals$2;
    Globals$2["__interfaces"] = ["fjs.util.Identified", "fjs.core.TargetCoupler", "java.io.Serializable"];
    class Globals$3 {
        constructor(updater) {
            this.updater = updater;
        }
        /**
         *
         * @param {*} target
         */
        retarget(target) {
            let state = target.state();
            (target => (typeof target === 'function') ? target(state) : target.accept(state))(this.updater);
        }
    }
    Facets.Globals$3 = Globals$3;
    Globals$3["__interfaces"] = ["fjs.core.SRetargetable", "fjs.core.SFacet"];
})(Facets || (Facets = {}));
var __Function = Function;
//# sourceMappingURL=Facets.js.map