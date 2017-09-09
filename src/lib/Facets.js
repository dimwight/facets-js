import { SIndexing } from '../../in/fjs/core/SIndexing';
import { STarget } from '../../in/fjs/core/STarget';
import { STextual } from '../../in/fjs/core/STextual';
import { TargetCore } from '../../in/fjs/core/TargetCore';
import { Debug } from '../../in/fjs/util/Debug';
import { Tracer } from '../../in/fjs/util/Tracer';
export class Facets extends Tracer {
    constructor(top, trace) {
        super(top);
        /*private*/ this.titleTargeters = ({});
        /*private*/ this.notifiable = new Facets$0(this);
        this.__trace = false;
        this.targeterTree = null;
        this.__trace = trace;
    }
    traceOutput(msg) {
        if (this.__trace)
            super.traceOutput(msg);
    }
    updatedTarget(target, c) {
        let title = target.title();
        let state = target.state();
        this.trace$java_lang_String$java_lang_Object(" > Updated target " + target.title() + " with ", state);
        (target => (typeof target === 'function') ? target(title, state) : target.accept(title, state))(c.targetStateUpdated);
    }
    newIndexingTarget(title, c) {
        let indexing = new SIndexing(title, c.passIndexables, new Facets$1(this, c));
        let passIndex = c.passIndex;
        if (passIndex != null)
            indexing.setIndex(passIndex);
        this.trace$java_lang_String$java_lang_Object(" > Created indexing ", indexing);
        return indexing;
    }
    newTextualTarget(title, c) {
        let textual = new STextual(title, new Facets$2(this, c));
        let passText = c.passText;
        if (passText != null)
            textual.setText(passText);
        this.trace$java_lang_String$java_lang_Object(" > Created textual ", textual);
        return textual;
    }
    newTargetsGroup(title, ...members) {
        let group = new (__Function.prototype.bind.apply(TargetCore, [null, title].concat(STarget.newTargets(members))));
        this.trace$java_lang_String$java_lang_Object(" > Created target group " + Debug.info(group) + " ", members);
        return group;
    }
    buildTargeterTree(targets) {
        this.trace$java_lang_String$java_lang_Object(" > Creating targeters for ", targets);
        this.targeterTree = targets.newTargeter();
        this.targeterTree.setNotifiable(this.notifiable);
        this.targeterTree.retarget(targets);
        {
            let array131 = this.targeterTree.elements();
            for (let index130 = 0; index130 < array131.length; index130++) {
                let targeter = array131[index130];
                {
                    /* put */ (this.titleTargeters[targeter.title()] = targeter);
                    this.trace$java_lang_String$java_lang_Object(" > Created targeter ", targeter);
                }
            }
        }
    }
    attachFacet(title, facetUpdated) {
        if (facetUpdated == null)
            throw Object.defineProperty(new Error("Null facetUpdated for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        else if (title == null || ((o1, o2) => { if (o1 && o1.equals) {
            return o1.equals(o2);
        }
        else {
            return o1 === o2;
        } })(title, ""))
            throw Object.defineProperty(new Error("Null or empty title for " + facetUpdated), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        let targeter = ((m, k) => m[k] ? m[k] : null)(this.titleTargeters, title);
        if (targeter == null)
            throw Object.defineProperty(new Error("Null targeter for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        let facet = new Facets$3(this, facetUpdated);
        this.trace$java_lang_String$java_lang_Object(" > Attaching facet " + facet + " to", targeter);
        targeter.attachFacet(facet);
    }
    updateTargetState(title, update) {
        this.trace$java_lang_String$java_lang_Object(" > Updating target state for title=" + title + " update=", update);
        let target = this.titleTarget(title);
        target.updateState(update);
        target.notifyParent();
    }
    getTargetState(title) {
        let state = this.titleTarget(title).state();
        this.trace$java_lang_String$java_lang_Object(" > Getting target state for title=" + title + " state=", state);
        return state;
    }
    titleTarget(title) {
        let targeter = ((m, k) => m[k] ? m[k] : null)(this.titleTargeters, title);
        if (targeter == null)
            throw Object.defineProperty(new Error("Null target for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        let target = targeter.target();
        return target;
    }
}
Facets["__class"] = "fjs.globals.Facets";
Facets["__interfaces"] = ["fjs.util.Identified"];
// export namespace Facets {
export class TargetCoupler {
    constructor() {
        this.targetStateUpdated = null;
    }
}
TargetCoupler["__class"] = "fjs.globals.TargetCoupler";
export class Facets$0 {
    /**
     *
     * @param {*} notice
     */
    notify(notice) {
        this.__parent.trace(" > Surface for " + Debug.info(this.__parent.targeterTree) + " notified by " + notice);
        let targets = this.__parent.targeterTree.target();
        this.__parent.targeterTree.retarget(targets);
        this.__parent.trace(" > Targeters retargeted on ", targets);
        this.__parent.targeterTree.retargetFacets();
        this.__parent.trace(" > Facets retargeted in ", this.__parent.targeterTree);
    }
    /**
     *
     * @return {string}
     */
    title() {
        throw Object.defineProperty(new Error("Not implemented in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
    }
    constructor(__parent) {
        this.__parent = __parent;
    }
}
Facets$0["__interfaces"] = ["fjs.core.Notifiable", "fjs.util.Titled"];
export class Facets$1 extends SIndexing.Coupler {
    constructor(__parent, c) {
        super();
        this.c = c;
        this.__parent = __parent;
    }
    /**
     *
     * @param {SIndexing} target
     */
    indexSet(target) {
        this.__parent.updatedTarget(target, this.c);
    }
}
Facets$1["__interfaces"] = ["fjs.core.TargetCoupler", "java.io.Serializable"];
export class Facets$2 extends STextual.Coupler {
    constructor(__parent, c) {
        super();
        this.c = c;
        this.__parent = __parent;
    }
    /**
     *
     * @param {STextual} target
     */
    textSet(target) {
        this.__parent.updatedTarget(target, this.c);
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
Facets$2["__interfaces"] = ["fjs.util.Identified", "fjs.core.TargetCoupler", "java.io.Serializable"];
export class Facets$3 {
    constructor(__parent, facetUpdated) {
        this.facetUpdated = facetUpdated;
        this.__parent = __parent;
        this.id = Tracer.ids++;
        this.stateThen = null;
    }
    /**
     *
     * @param {*} target
     */
    retarget(target) {
        let state = target.state();
        if (false)
            this.__parent.trace(".attachFacet #" + this.__parent.id + ".retarget: state=" + state + " stateThen=" + this.stateThen);
        if (!((o1, o2) => { if (o1 && o1.equals) {
            return o1.equals(o2);
        }
        else {
            return o1 === o2;
        } })(state, this.stateThen)) {
            this.__parent.trace(" > Updating UI with state=", state);
            this.stateThen = state;
            (target => (typeof target === 'function') ? target(state) : target.accept(state))(this.facetUpdated);
        }
    }
    /**
     *
     * @return {string}
     */
    toString() {
        return "#" + this.__parent.id;
    }
}
Facets$3["__interfaces"] = ["fjs.core.SRetargetable", "fjs.core.SFacet"];
// }
export function newInstance(trace) {
    return new Facets("Facets", trace);
}
var __Function = Function;
//# sourceMappingURL=Facets.js.map