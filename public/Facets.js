(function (exports) {
'use strict';

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
class Util {
    static printOut$java_lang_String(s) {
        let text = s == null ? "null" : s.toString();
        console.info(text);
    }
    static printOut$java_lang_String$java_lang_Object(msg, o) {
        Util.printOut$java_lang_String(msg + o);
    }
    static printOut(msg, o) {
        if (((typeof msg === 'string') || msg === null) && ((o != null) || o === null)) {
            return Util.printOut$java_lang_String$java_lang_Object(msg, o);
        }
        else if (((typeof msg === 'string') || msg === null) && o === undefined) {
            return Util.printOut$java_lang_String(msg);
        }
        else
            throw new Error('invalid overload');
    }
    static arrayPrintString(toPrint) {
        let msg = toPrint == null ? "null" : Debug.toStringWithHeader(toPrint);
        return msg;
    }
    static sf(val) {
        return Util.sigFigs(val, Util.DIGITS_SF);
    }
    static sfs(val) {
        let sf = new String(Util.sf(val)).toString();
        let sfs = sf.replace(new RegExp("(\\d{" + Util.DIGITS_SF + ",})\\.0(\\D?)", 'g'), "$1$2").replace(new RegExp("\\.0\\z", 'g'), "");
        return sfs;
    }
    static fxs(val) {
        return "0." + (Util.DECIMALS_FX === 1 ? "0" : Util.DECIMALS_FX === 2 ? "00" : "000");
    }
    /*private*/ static shortName(className) {
        let semiColon = className.lastIndexOf(';');
        let stop = semiColon > 0 ? semiColon : className.length;
        return className.substring(className.lastIndexOf('.') + 1, stop);
    }
    /*private*/ static sigFigs(val, digits) {
        if (digits < 0)
            throw Object.defineProperty(new Error("Digits <1=" + digits), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        else if (((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(val))
            throw Object.defineProperty(new Error("Infinite value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        else if (digits === 0 || val === 0 || val !== val)
            return val;
        let ceiling = Math.pow(10, digits);
        let floor = ceiling / 10;
        let signum = (f => { if (f > 0) {
            return 1;
        }
        else if (f < 0) {
            return -1;
        }
        else {
            return 0;
        } })(val);
        let sf = Math.abs(val);
        if (sf < 0.001)
            return 0;
        let shiftUp = sf < floor;
        let factor = shiftUp ? 10 : 0.1;
        let shifted = 0;
        for (; sf > ceiling || sf < floor; shifted += shiftUp ? 1 : -1)
            sf *= factor;
        let exp = Math.pow(10, shifted);
        sf = (d => { if (d === Number.NaN) {
            return d;
        }
        else if (Number.POSITIVE_INFINITY === d || Number.NEGATIVE_INFINITY === d) {
            return d;
        }
        else if (d == 0) {
            return d;
        }
        else {
            return Math.round(d);
        } })(sf) / exp;
        if (true || !shiftUp)
            return sf * signum;
        let trim = sf;
        if (trim !== sf)
            Util.printOut$java_lang_String("Doubles.sigFigs: val=" + sf + " trim=" + trim);
        return trim * signum;
    }
}
Util.DIGITS_SF = 3;
Util.DECIMALS_FX = 2;
Util["__class"] = "fjs.util.Util";

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Utility methods for arrays.
 * @class
 */
class Objects {
    static toString$java_lang_Object_A$java_lang_String(items, spacer) {
        if (items == null)
            return "null";
        else if (items.length === 0)
            return "";
        let list = ([]);
        let trim = false && !((o1, o2) => { if (o1 && o1.equals) {
            return o1.equals(o2);
        }
        else {
            return o1 === o2;
        } })(spacer, "\n");
        let at = 0;
        for (let index144 = 0; index144 < items.length; index144++) {
            let item = items[index144];
            /* add */ (list.push((item == null ? "null" : trim ? item.toString().trim() : item) + (++at === items.length ? "" : spacer)) > 0);
        }
        return ('[' + list.join(', ') + ']');
    }
    static toString(items, spacer) {
        if (((items != null && items instanceof Array && (items.length == 0 || items[0] == null || (items[0] != null))) || items === null) && ((typeof spacer === 'string') || spacer === null)) {
            return Objects.toString$java_lang_Object_A$java_lang_String(items, spacer);
        }
        else if (((items != null && items instanceof Array && (items.length == 0 || items[0] == null || (items[0] != null))) || items === null) && spacer === undefined) {
            return Objects.toString$java_lang_Object_A(items);
        }
        else
            throw new Error('invalid overload');
    }
    static toString$java_lang_Object_A(array) {
        return Objects.toString$java_lang_Object_A$java_lang_String(array, ",");
    }
    static toLines(array) {
        if (array == null)
            return "null";
        let list = ([]);
        for (let i = 0; i < array.length; i++)
            (list.push((array[i] == null ? "null" : array[i].toString()) + (i < array.length - 1 ? "\n" : "")) > 0);
        return ('[' + list.join(', ') + ']').replace(new RegExp("\n", 'g'), " ");
    }
}
Objects.debug = false;
Objects["__class"] = "fjs.util.Objects";

/**
 * Utilities for use during development.
 * @class
 */
class Debug {
    /**
     * Returns basic Debug.information about an object's type and identity.
     * <p>This will be some combination of
     * <ul>
     * <li>the non-trivial simple class name
     * <li>{@link Titled#title()} if available
     * </ul>
     * @param {*} o
     * @return {string}
     */
    static info(o) {
        if (o == null)
            return "null";
        if (typeof o === 'boolean')
            return o.toString();
        else if (typeof o === 'number')
            return new String(o).toString();
        else if (typeof o === 'string') {
            let text = o;
            let length = text.length;
            return text.substring(0, Math.min(length, 60)) + ("");
        }
        return ((c => c["__class"] ? c["__class"].substring(c["__class"].lastIndexOf('.') + 1) : c["name"].substring(c["name"].lastIndexOf('.') + 1))(o.constructor)) + ((o != null && (o["__interfaces"] != null && o["__interfaces"].indexOf("fjs.util.Identified") >= 0 || o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("fjs.util.Identified") >= 0)) ? (" #" + o.identity()) : "") + ((o != null && (o["__interfaces"] != null && o["__interfaces"].indexOf("fjs.util.Titled") >= 0 || o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("fjs.util.Titled") >= 0)) ? (" " + o.title()) : "");
    }
    /**
     * Returns an array of <code>Debug.info</code>s.
     * @param {Array} array
     * @return {string}
     */
    static arrayInfo(array) {
        return "arrayInfo";
    }
    static traceEvent(string) {
        Util.printOut$java_lang_String(">>" + string);
    }
    static toStringWithHeader(array) {
        return Debug.info(array) + " [" + array.length + "] " + Objects.toLines(array);
    }
}
Debug.trace = false;
Debug["__class"] = "fjs.util.Debug";

/**
 * Utility superclass that can issue trace messages.
 * @param {string} top
 * @class
 */
class Tracer {
    constructor(top) {
        /*private*/ this.id = ++Tracer.ids;
        if (((typeof top === 'string') || top === null)) {
            let __args = Array.prototype.slice.call(arguments);
            this.top = null;
            this.id = ++Tracer.ids;
            this.top = null;
            (() => {
                this.top = top;
            })();
        }
        else if (top === undefined) {
            let __args = Array.prototype.slice.call(arguments);
            this.top = null;
            this.id = ++Tracer.ids;
            this.top = null;
            (() => {
                this.top = null;
            })();
        }
        else
            throw new Error('invalid overload');
    }
    /**
     *
     * @return {*}
     */
    identity() {
        return this.id;
    }
    static newTopped(top, live) {
        return new Tracer.TracerTopped(top);
    }
    trace$java_lang_String$java_lang_Object(msg, o) {
        this.traceOutput("" + msg + this.traceObjectText(o));
    }
    /**
     * Outputs complete trace messages to console or elsewhere.
     * <p>Default prepends helpful classname to message.
     * @param {string} msg passed from one of the <code>public</code> methods
     */
    traceOutput(msg) {
        this.traceOutputWithId("" + msg);
    }
    traceOutputWithId(msg) {
        Util.printOut$java_lang_String((this.top != null ? (this.top + " #" + this.id) : Debug.info(this)) + " " + msg);
    }
    trace$java_lang_String(msg) {
        this.traceOutput(msg);
    }
    trace$java_lang_String$java_lang_Throwable$boolean(msg, t, stack) {
        if (stack = t != null && stack) {
            this.traceOutput(msg);
            console.error(t.message, t);
        }
        else
            this.traceOutput(msg + this.traceObjectText(t));
    }
    trace(msg, t, stack) {
        if (((typeof msg === 'string') || msg === null) && ((t != null && (t["__classes"] && t["__classes"].indexOf("java.lang.Throwable") >= 0) || t != null && t instanceof Error) || t === null) && ((typeof stack === 'boolean') || stack === null)) {
            return this.trace$java_lang_String$java_lang_Throwable$boolean(msg, t, stack);
        }
        else if (((typeof msg === 'string') || msg === null) && ((t != null && (t instanceof Array)) || t === null) && stack === undefined) {
            return this.trace$java_lang_String$java_util_Collection(msg, t);
        }
        else if (((typeof msg === 'string') || msg === null) && ((t != null && t instanceof Array && (t.length == 0 || t[0] == null || (t[0] != null))) || t === null) && stack === undefined) {
            return this.trace$java_lang_String$java_lang_Object_A(msg, t);
        }
        else if (((typeof msg === 'string') || msg === null) && ((t != null) || t === null) && stack === undefined) {
            return this.trace$java_lang_String$java_lang_Object(msg, t);
        }
        else if (((typeof msg === 'string') || msg === null) && t === undefined && stack === undefined) {
            return this.trace$java_lang_String(msg);
        }
        else
            throw new Error('invalid overload');
    }
    trace$java_lang_String$java_util_Collection(msg, c) {
        this.traceOutput(msg + this.traceArrayText(/* toArray */ c.slice(0)));
    }
    trace$java_lang_String$java_lang_Object_A(msg, array) {
        this.traceOutput(msg + this.traceArrayText(array));
    }
    traceDebug$java_lang_String$java_lang_Object(msg, o) {
        this.traceOutput(msg + Debug.info(o));
    }
    traceDebug$java_lang_String$java_lang_Object_A(msg, array) {
        this.traceOutput(msg + (Debug.arrayInfo(array)));
    }
    traceDebug(msg, array) {
        if (((typeof msg === 'string') || msg === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
            return this.traceDebug$java_lang_String$java_lang_Object_A(msg, array);
        }
        else if (((typeof msg === 'string') || msg === null) && ((array != null) || array === null)) {
            return this.traceDebug$java_lang_String$java_lang_Object(msg, array);
        }
        else
            throw new Error('invalid overload');
    }
    traceObjectText(o) {
        return Debug.info(o);
    }
    traceArrayText(array) {
        return Util.arrayPrintString(array);
    }
}
Tracer.ids = 0;
Tracer["__class"] = "fjs.util.Tracer";
Tracer["__interfaces"] = ["fjs.util.Identified"];
(function (Tracer) {
    class TracerTopped extends Tracer {
        constructor(top) {
            super(top);
            this.__fjs_util_Tracer_TracerTopped_top = null;
            this.__fjs_util_Tracer_TracerTopped_top = top;
        }
        /**
         *
         * @param {string} msg
         */
        traceOutput(msg) {
            if (this.doTrace())
                super.traceOutput(msg);
        }
        doTrace() {
            return true;
        }
    }
    Tracer.TracerTopped = TracerTopped;
    TracerTopped["__class"] = "fjs.util.Tracer.TracerTopped";
    TracerTopped["__interfaces"] = ["fjs.util.Identified"];
})(Tracer || (Tracer = {}));

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Core implementation of key interfaces.
 * <p>{@link NotifyingCore} is the base class of both the {@link STarget} and
 * {@link STargeter} class hierarchies.
 * <p>Declared <code>public</code> for documentation purposes only; client code should
 * use the concrete subclass hierarchies.
 * @extends Tracer
 * @class
 */
class NotifyingCore extends Tracer {
    constructor() {
        super();
        /*private*/ this.__identity = NotifyingCore.identities++;
        this.__notifiable = null;
    }
    /**
     *
     * @return {*}
     */
    notifiable() {
        if (this.__notifiable == null)
            throw Object.defineProperty(new Error("No notifiable in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        else
            return this.__notifiable;
    }
    /**
     *
     * @param {*} notice
     */
    notify(notice) {
        if (Debug.trace)
            Debug.traceEvent("Notified in " + this + " with " + notice);
        if (this.__notifiable == null)
            return;
        if (!this.blockNotification())
            this.__notifiable.notify(notice);
        else if (Debug.trace)
            Debug.traceEvent("Notification blocked in " + this);
    }
    /**
     *
     */
    notifyParent() {
        if (this.__notifiable == null)
            return;
        this.__notifiable.notify(Debug.info(this));
    }
    /**
     * Enables notification to be restricted to this member of the tree.
     * <p>Checked by {@link #notify(Object)}; default returns <code>false</code>.
     * @return {boolean}
     */
    blockNotification() {
        return false;
    }
    /**
     *
     * @param {*} n
     */
    setNotifiable(n) {
        this.__notifiable = n;
    }
    /**
     *
     * @return {string}
     */
    toString() {
        return Debug.info(this);
    }
}
NotifyingCore.identities = 0;
NotifyingCore["__class"] = "fjs.core.NotifyingCore";
NotifyingCore["__interfaces"] = ["fjs.util.Identified", "fjs.core.Notifying", "fjs.core.Notifiable", "fjs.util.Titled"];

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
var NotifyingType;
(function (NotifyingType) {
    NotifyingType[NotifyingType["Target"] = 0] = "Target";
    NotifyingType[NotifyingType["Targeter"] = 1] = "Targeter";
    NotifyingType[NotifyingType["Frame"] = 2] = "Frame";
})(NotifyingType || (NotifyingType = {}));

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Construct a {@link TargeterCore} to match <code>target</code>.
 * @param {*} targetType set as {@link #targetType}.
 * @class
 * @extends NotifyingCore
 */
class TargeterCore extends NotifyingCore {
    constructor(targetType) {
        super();
        /*private*/ this.facets = ([]);
        this.__elements = null;
        this.__target = null;
        this.targetType = null;
        this.targetType = targetType;
        /* add */ (TargeterCore.targeters_$LI$().push(this) > 0);
        if (Debug.trace)
            Debug.traceEvent("Created " + this);
    }
    static targeters_$LI$() { if (TargeterCore.targeters == null)
        TargeterCore.targeters = ([]); return TargeterCore.targeters; }
    ;
    retarget(target) {
        if (target == null)
            throw Object.defineProperty(new Error("Null target in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        this.__target = target;
        let targets = target.elements();
        if (targets == null)
            throw Object.defineProperty(new Error("No targets in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        if (this.__elements == null) {
            this.__elements = new Array(targets.length);
            for (let i = 0; i < this.__elements.length; i++) {
                this.__elements[i] = targets[i].newTargeter();
                this.__elements[i].setNotifiable(this);
            }
            
        }
        if (targets.length === this.__elements.length)
            for (let i = 0; i < this.__elements.length; i++)
                this.__elements[i].retarget(targets[i]);
        if (target.notifiesTargeter())
            target.setNotifiable(this);
    }
    attachFacet(facet) {
        if (facet == null)
            throw Object.defineProperty(new Error("Null facet in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        if (!(this.facets.indexOf((facet)) >= 0)) {
            facet.retarget(this.__target);
            /* add */ (this.facets.push(facet) > 0);
        }
        if (Debug.trace)
            Debug.traceEvent("Attached facet " + Debug.info(facet) + " to " + Debug.info(this));
    }
    retargetFacets() {
        let facets = ((a1, a2) => { if (a1.length >= a2.length) {
            a1.length = 0;
            a1.push.apply(a1, a2);
            return a1;
        }
        else {
            return a2.slice(0);
        } })([], this.facets);
        for (let i = 0; i < this.__elements.length; i++)
            this.__elements[i].retargetFacets();
        if (facets == null)
            return;
        for (let i = 0; i < facets.length; i++) {
            facets[i].retarget(this.__target);
            if (Debug.trace)
                Debug.traceEvent("Retargeted facet " + Debug.info(facets[i]) + " in " + this);
        }
        
    }
    elements() {
        if (this.__elements == null)
            throw Object.defineProperty(new Error("No elements in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        return this.__elements;
    }
    target() {
        if (this.__target == null)
            throw Object.defineProperty(new Error("No target in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        return this.__target;
    }
    title() {
        return this.__target == null ? "Untargeted" : this.__target.title();
    }
    toString() {
        let targetInfo = this.__target == null ? "" : Debug.info(this.__target);
        return Debug.info(this) + ("");
    }
    /**
     *
     * @return {NotifyingType}
     */
    type() {
        return NotifyingType.Targeter;
    }
}
TargeterCore["__class"] = "fjs.core.TargeterCore";
TargeterCore["__interfaces"] = ["fjs.util.Identified", "fjs.core.Notifying", "fjs.core.SRetargetable", "fjs.core.Notifiable", "fjs.util.Titled", "fjs.core.Facetable", "fjs.core.STargeter"];
TargeterCore.targeters_$LI$();

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Core constructor.
 * @param {string} title should be suitable for return as the (immutable)
 * <code>title</code> property
 * @param {Array} elements may be <code>null</code> (in which case
 * suitable elements may be created using <code>lazyElements</code>); otherwise
 * passed to {@link #setElements(STarget[])}
 * @class
 * @extends NotifyingCore
 */
class TargetCore extends NotifyingCore {
    constructor(title, ...elements) {
        if (((typeof title === 'string') || title === null) && ((elements != null && elements instanceof Array && (elements.length == 0 || elements[0] == null || (elements[0] != null && (elements[0]["__interfaces"] != null && elements[0]["__interfaces"].indexOf("fjs.core.STarget") >= 0 || elements[0].constructor != null && elements[0].constructor["__interfaces"] != null && elements[0].constructor["__interfaces"].indexOf("fjs.core.STarget") >= 0)))) || elements === null)) {
            let __args = Array.prototype.slice.call(arguments);
            super();
            this.__title = null;
            this.__elements = null;
            this.live = true;
            this.__title = null;
            this.__elements = null;
            (() => {
                this.__title = title;
                if (elements != null)
                    this.setElements(elements);
                if (title == null || ((o1, o2) => { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(title, ""))
                    throw Object.defineProperty(new Error("Null or empty title in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                if (Debug.trace)
                    Debug.traceEvent("Created " + Debug.info(this));
                TargetCore.targets++;
            })();
        }
        else if (((typeof title === 'string') || title === null) && elements === undefined) {
            let __args = Array.prototype.slice.call(arguments);
            {
                let __args = Array.prototype.slice.call(arguments);
                let elements = null;
                super();
                this.__title = null;
                this.__elements = null;
                this.live = true;
                this.__title = null;
                this.__elements = null;
                (() => {
                    this.__title = title;
                    if (elements != null)
                        this.setElements(elements);
                    if (title == null || ((o1, o2) => { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(title, ""))
                        throw Object.defineProperty(new Error("Null or empty title in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                    if (Debug.trace)
                        Debug.traceEvent("Created " + Debug.info(this));
                    TargetCore.targets++;
                })();
            }
        }
        else
            throw new Error('invalid overload');
    }
    /**
     * Sets the {@link STarget} children of the {@link TargetCore}.
     * <p>Intended for use in specialised subclass construction;
     * elements set are thereafter immutable.
     * @param {Array} elements (which may not be <code>null</code> nor contain <code>null</code>
     * members) will be returned as the <code>elements</code> property.
     */
    setElements(elements) {
        if (this.__elements != null)
            throw Object.defineProperty(new Error("Immutable elements in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        this.__elements = elements;
        if ((this.__elements = elements) == null)
            throw Object.defineProperty(new Error("Null elements in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        else
            for (let i = 0; i < elements.length; i++)
                if (elements[i] == null)
                    throw Object.defineProperty(new Error("Null element " + i + " in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        
    }
    /**
     * Implements interface method.
     * <p>If no elements have been set, attempts to create them with
     * <code>lazyElements</code>.
     * <p>Each call to this method also sets the {@link TargetCore}
     * as notification monitor of any element that is not a {@link
     * fjs.core.SFrameTarget}.
     *
     * @return {Array}
     */
    elements() {
        if (this.__elements == null)
            this.setElements(this.lazyElements());
        if (this.__elements == null)
            throw Object.defineProperty(new Error("No elements in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        for (let i = 0; i < this.__elements.length; i++)
            if (!(this.__elements[i].type() === NotifyingType.Frame))
                this.__elements[i].setNotifiable(this);
        
        return this.__elements;
    }
    /**
     * Lazily creates <code>element</code>s for this target.
     * <p>Called at most once from {@link #elements()}.
     * <p>Though defined in {@link TargetCore} this method is primarily for use by
     * {@link SFrameTarget}s, which always create their elements dynamically
     * by reimplementing this method.
     * Default implementation returns an empty {@link STarget}[].
     * @return {Array}
     */
    lazyElements() {
        return [];
    }
    /**
     * Create and return a targeter suitable for retargeting to
     * this target.
     * <p>This is the key method used by Facets to implement dynamic
     * creation of a surface targeter tree. During initial retargeting
     * each {@link TargeterCore} queries its <code>target</code>
     * for any child elements, and calls this method on each child
     * to obtain suitable {@link STargeter} instances which
     * it then adds to its elements.
     * <p>This method may be also called on subsequent retargetings
     * where the specific type of a target is subject
     * to change (for instance when it represents a selection).
     * Either the {@link STargeter} returned can be matched
     * to an existing one to which facet have already been attached,
     * or such facet can be attached and the surface layout adjusted
     * accordingly.
     *
     * @return {*}
     */
    newTargeter() {
        return new TargeterCore(this.constructor);
    }
    isLive() {
        let n = this.notifiable();
        let notifiesTarget = n != null && (n != null && (n["__interfaces"] != null && n["__interfaces"].indexOf("fjs.core.STarget") >= 0 || n.constructor != null && n.constructor["__interfaces"] != null && n.constructor["__interfaces"].indexOf("fjs.core.STarget") >= 0));
        return !notifiesTarget ? this.live : this.live && n.isLive();
    }
    setLive(live) {
        this.live = live;
    }
    title() {
        return this.__title;
    }
    toString() {
        return Debug.info(this);
    }
    /**
     * Used to construct the notification tree.
     * <p><b>NOTE</b> This method must NOT be overridden in application code.
     * @return {boolean}
     */
    notifiesTargeter() {
        return true && this.__elements != null;
    }
    /**
     *
     * @return {NotifyingType}
     */
    type() {
        return NotifyingType.Target;
    }
    /**
     *
     * @return {*}
     */
    state() {
        throw Object.defineProperty(new Error("Not implemented in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
    }
    /**
     *
     * @param {*} update
     */
    updateState(update) {
        throw Object.defineProperty(new Error("Not implemented in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
    }
}
TargetCore.targets = 0;
TargetCore["__class"] = "fjs.core.TargetCore";
TargetCore["__interfaces"] = ["fjs.core.STarget", "fjs.util.Identified", "fjs.core.Notifying", "fjs.core.Notifiable", "fjs.util.Titled"];

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Unique constructor.
 * @param {string} title passed to superclass
 * @param {Array} indexables the objects to be indexed; may not be empty but
 * may be <code>null</code> in which case they are
 * read dynamically from the coupler
 * @param {SIndexing.Coupler} coupler supplies application-specific mechanism and policy
 * @class
 * @extends TargetCore
 */
class SIndexing extends TargetCore {
    constructor(title, indexables, coupler) {
        super(title);
        /*private*/ this.__indices = null;
        this.coupler = null;
        this.__indexables = null;
        this.__indexings = null;
        this.coupler = coupler;
        if (indexables != null && indexables.length === 0)
            throw Object.defineProperty(new Error("Null or empty indexables in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        this.__indexables = indexables;
    }
    static NO_INDEXABLES_$LI$() { if (SIndexing.NO_INDEXABLES == null)
        SIndexing.NO_INDEXABLES = ["Not indexable"]; return SIndexing.NO_INDEXABLES; }
    ;
    /**
     * The first index into the <code>indexables</code>.
     * @return {number}
     */
    index() {
        if (this.__indices == null || this.__indices.length === 0)
            throw Object.defineProperty(new Error("Null or empty indices in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        return this.__indices[0];
    }
    /**
     * The indices last set into the <code>indexables</code>.
     * @return {Array}
     */
    indices() {
        if (this.__indices == null)
            throw Object.defineProperty(new Error("Null indices in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        return this.__indices;
    }
    /**
     * The items exposed to indexing.
     * <p>These will either have been set during construction or be read
     * dynamically from the coupler.
     * @return {Array}
     */
    indexables() {
        let indexables = this.__indexables != null ? this.__indexables : this.coupler.getIndexables();
        if (indexables == null)
            throw Object.defineProperty(new Error("Null indexables in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        if (indexables.length === 0)
            return SIndexing.NO_INDEXABLES_$LI$();
        if (indexables.slice(0).slice(0).length !== indexables.length)
            throw Object.defineProperty(new Error("Duplicate indexables in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        else
            return indexables;
    }
    /**
     * Sets a single index into the <code>indexables</code>.
     * @param {number} index
     */
    setIndex(index) {
        this.setIndices([index]);
    }
    setIndexed(definition) {
        for (let at = 0; at < this.__indexables.length; at++)
            if (((o1, o2) => { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(this.__indexables[at], definition))
                this.setIndex(at);
        
    }
    /**
     * Sets indices into the <code>indexables</code>.
     * @param {Array} indices may be empty but may not be <code>null</code>
     */
    setIndices(indices) {
        if (indices == null)
            throw Object.defineProperty(new Error("Null indices in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        let first = this.__indices == null;
        for (let i = 0; indices.length > 0 && i < indices.length; i++)
            if (indices[i] < 0)
                throw Object.defineProperty(new Error("Bad index in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        
        this.__indices = indices;
        if (this.__indexings != null) {
            let indexables = this.indexables();
            let old = this.__indexings;
            this.__indexings = new Array(old.length);
            this.__indexings[0] = indexables[indices[0]];
            for (let i = 0, indexion = 1; i < old.length; i++)
                if (old[i] !== this.__indexings[0])
                    this.__indexings[indexion++] = old[i];
            
        }
        if (!first)
            this.coupler.indexSet(this);
    }
    /**
     * The item denoted by the current <code>index</code>.
     * @return {*}
     */
    indexed() {
        if (this.__indices == null)
            throw Object.defineProperty(new Error("No index in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        return this.__indices.length === 0 ? SIndexing.NOT_INDEXED : this.indexables()[this.index()];
    }
    /**
     * The <code>indexables</code> ordered by most recently indexed.
     * @return {Array}
     */
    indexings() {
        if (this.__indexings == null)
            throw Object.defineProperty(new Error("Null indexings in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        return this.__indexings;
    }
    /**
     *
     * @return {*}
     */
    state() {
        return this.index();
    }
    /**
     *
     * @param {*} update
     */
    updateState(update) {
        this.setIndex(update);
    }
    toString() {
        return super.toString() + " " + this.indices().length;
    }
}
SIndexing.NOT_INDEXED = "Not indexed";
SIndexing["__class"] = "fjs.core.SIndexing";
SIndexing["__interfaces"] = ["fjs.core.STarget", "fjs.util.Identified", "fjs.core.Notifying", "fjs.core.Notifiable", "fjs.util.Titled"];
(function (SIndexing) {
    /**
     * Connects an {@link SIndexing} to the application.
     * <p>A {@link Coupler} supplies client-specific mechanism and
     * policy for an {@link SIndexing}.
     * @class
     */
    class Coupler {
        constructor() {
        }
        /**
         * Called whenever an index changes.
         * @param {SIndexing} i
         */
        indexSet(i) {
        }
        /**
         * Can return dynamic indexables.
         * <p>Called by {@link SIndexing#indexables()} if none set during construction.
         * @return {Array} a non-empty array
         */
        getIndexables() {
            throw Object.defineProperty(new Error("Not implemented in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        }
        /**
         * Returns strings to represent the indexables.
         * <p>These will appear in facet lists, or as labels for menu items or
         * radio buttons.
         * <p>The default returns titles based on the type of the {@link SIndexing}'s
         * <code>indexables</code>:
         * <ul>
         * <li>if they are {@link String}s, returns them</li>
         * <li>if they are {@link fjs.util.Titled}s, returns their <code>title</code>  properties</li>
         * <li>otherwise, returns their <code>toString</code> properties</li>
         * <li>if they the {@link SIndexing#NO_INDEXABLES} constant, returns an
         * empty {@link String}[].
         * </ul>
         * @param {SIndexing} i
         * @return {Array}
         */
        newIndexableTitles(i) {
            let indexables = i.indexables();
            if (indexables === SIndexing.NO_INDEXABLES_$LI$())
                return [];
            else if (indexables != null && indexables instanceof Array && (indexables.length == 0 || indexables[0] == null || typeof indexables[0] === 'string'))
                return indexables;
            let titles = new Array(indexables.length);
            let titled = (indexables != null && indexables instanceof Array && (indexables.length == 0 || indexables[0] == null || (indexables[0] != null && (indexables[0]["__interfaces"] != null && indexables[0]["__interfaces"].indexOf("fjs.util.Titled") >= 0 || indexables[0].constructor != null && indexables[0].constructor["__interfaces"] != null && indexables[0].constructor["__interfaces"].indexOf("fjs.util.Titled") >= 0))));
            for (let t = 0; t < titles.length; t++)
                titles[t] = titled ? indexables[t].title() : indexables[t].toString();
            return titles;
        }
    }
    SIndexing.Coupler = Coupler;
    Coupler["__class"] = "fjs.core.SIndexing.Coupler";
    Coupler["__interfaces"] = ["fjs.core.TargetCoupler", "java.io.Serializable"];
})(SIndexing || (SIndexing = {}));
SIndexing.NO_INDEXABLES_$LI$();

var STarget;
(function (STarget) {
    function newTargets(src) {
        let type = "fjs.core.STarget";
        let array = new Array(src.length);
        /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
            while (--size >= 0)
                dstPts[dstOff++] = srcPts[srcOff++];
        }
        else {
            let tmp = srcPts.slice(srcOff, srcOff + size);
            for (let i = 0; i < size; i++)
                dstPts[dstOff++] = tmp[i];
        } })(src, 0, array, 0, array.length);
        return array;
    }
    STarget.newTargets = newTargets;
})(STarget || (STarget = {}));

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Core constructor.
 * @param {string} title passed to superclass
 * @param {STextual.Coupler} coupler can supply application-specific mechanism and policy;
 * must be non-<code>null</code>
 * @class
 * @extends TargetCore
 */
class STextual extends TargetCore {
    constructor(title, coupler) {
        super(title);
        this.coupler = null;
        this.__text = null;
        if ((this.coupler = coupler) == null)
            throw Object.defineProperty(new Error("Null coupler in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
    }
    /**
     * Sets the text value to be exposed.
     * <p>Also calls {@link STextual.Coupler#textSet(STextual)} if not initialising.
     * @param {string} text must be non-<code>null</code>; and non-blank
     * unless {@link STextual.Coupler#isValidText(STextual, String)}
     * returns <code>true</code>.
     */
    setText(text) {
        if (text == null || !this.coupler.isValidText(this, text))
            throw Object.defineProperty(new Error("Null or invalid text in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        let firstSet = this.__text == null;
        this.__text = text;
        if (!firstSet)
            this.coupler.textSet(this);
    }
    /**
     * The text value represented.
     * @return {string}
     */
    text() {
        if (this.__text != null)
            return this.__text;
        else
            this.__text = this.coupler.getText(this);
        if (this.__text == null || !this.coupler.isValidText(this, this.__text))
            throw Object.defineProperty(new Error("Null or invalid text in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        else
            return this.__text;
    }
    /**
     *
     * @return {*}
     */
    state() {
        return this.text();
    }
    /**
     *
     * @param {*} update
     */
    updateState(update) {
        this.setText(update);
    }
    toString() {
        return super.toString() + ("");
    }
}
STextual["__class"] = "fjs.core.STextual";
STextual["__interfaces"] = ["fjs.core.STarget", "fjs.util.Identified", "fjs.core.Notifying", "fjs.core.Notifiable", "fjs.util.Titled"];
(function (STextual) {
    /**
     * Connects a {@link STextual} to the application.
     * <p>A {@link Coupler} is required to supply a {@link STextual}
     * with client-specific policy or mechanism.
     * @class
     * @extends Tracer
     */
    class Coupler extends Tracer {
        constructor() {
            super();
        }
        /**
         * Called when <code>setText</code> is called on <code>t</code>.
         * @param {STextual} t
         */
        textSet(t) {
        }
        /**
         * Is this text valid for the {@link STextual}?
         * <p>Default returns <code>true</code> for non-blank text.
         * @param {STextual} t
         * @param {string} text
         * @return {boolean}
         */
        isValidText(t, text) {
            return true || !((o1, o2) => { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(text.trim(), "");
        }
        /**
         * Can the {@link STextual} accept interim updates?
         * <p>Default returns <code>false</code>.
         * @param {STextual} t
         * @return {boolean}
         */
        updateInterim(t) {
            return false;
        }
        getText(t) {
            throw Object.defineProperty(new Error("Not implemented in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        }
    }
    STextual.Coupler = Coupler;
    Coupler["__class"] = "fjs.core.STextual.Coupler";
    Coupler["__interfaces"] = ["fjs.util.Identified", "fjs.core.TargetCoupler", "java.io.Serializable"];
})(STextual || (STextual = {}));

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Unique constructor.
 * @param {string} title passed to superclass
 * @param {boolean} state initial state of the toggling
 * @param {SToggling.Coupler} coupler can supply application-specific mechanism
 * @class
 * @extends TargetCore
 */
class SToggling extends TargetCore {
    constructor(title, state, coupler) {
        super(title);
        this.coupler = null;
        this.__state = false;
        this.__state = state;
        this.coupler = coupler;
    }
    /**
     * The Boolean state of the toggling.
     * <p>The value returned will that set using
     * <code>setState</code> or during construction.
     * @return {boolean}
     */
    isSet() {
        return this.__state;
    }
    /**
     * Sets the Boolean state.
     * <p> Subsequently calls {@link fjs.core.SToggling.Coupler#stateSet(SToggling)}.
     * @param {boolean} state
     */
    set(state) {
        this.__state = state;
        this.coupler.stateSet(this);
    }
    /**
     *
     * @param {*} update
     */
    updateState(update) {
        this.set(update);
    }
    /**
     *
     * @return {*}
     */
    state() {
        return this.isSet();
    }
    toString() {
        return super.toString() + (" " + this.__state);
    }
}
SToggling["__class"] = "fjs.core.SToggling";
SToggling["__interfaces"] = ["fjs.core.STarget", "fjs.util.Identified", "fjs.core.Notifying", "fjs.core.Notifiable", "fjs.util.Titled"];
(function (SToggling) {
    /**
     * Connects a {@link SToggling} to the application.
     * <p>A {@link Coupler} supplies application-specific mechanism
     * for a {@link SToggling}.
     * @class
     */
    class Coupler {
        constructor() {
        }
        /**
         * Called by the toggling whenever its state is set.
         * @param {SToggling} t
         */
        stateSet(t) {
        }
    }
    SToggling.Coupler = Coupler;
    Coupler["__class"] = "fjs.core.SToggling.Coupler";
    Coupler["__interfaces"] = ["fjs.core.TargetCoupler", "java.io.Serializable"];
})(SToggling || (SToggling = {}));

class Facets extends Tracer {
    constructor(top, trace) {
        super(top);
        /*private*/ this.titleTargeters = ({});
        /*private*/ this.notifiable = new Facets.Facets$0(this);
        this.__trace = false;
        this.targeterTree = null;
        this.__trace = trace;
    }
    /**
     *
     * @param {string} msg
     */
    traceOutput(msg) {
        if (this.__trace)
            super.traceOutput(msg);
    }
    updatedTarget(target, c) {
        let title = target.title();
        this.trace$java_lang_String$java_lang_Object(" > Updated target ", target);
        if (c.targetStateUpdated != null) {
            let state = target.state();
            (target => (typeof target === 'function') ? target(title, state) : target.accept(title, state))(c.targetStateUpdated);
        }
    }
    newTextualTarget(title, c) {
        let textual = new STextual(title, new Facets.Facets$1(this, c));
        let passText = c.passText;
        if (passText != null)
            textual.setText(passText);
        this.trace$java_lang_String$java_lang_Object(" > Created textual ", textual);
        return textual;
    }
    newTogglingTarget(title, c) {
        let passSet = c.passSet;
        if (passSet == null)
            throw Object.defineProperty(new Error("Null passSet in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        let toggling = new SToggling(title, passSet, new Facets.Facets$2(this, c));
        this.trace$java_lang_String$java_lang_Object(" > Created toggling ", toggling);
        return toggling;
    }
    newIndexingTarget(title, c) {
        let indexing = new SIndexing(title, c.passIndexables, new Facets.Facets$3(this, c));
        let passIndex = c.passIndex;
        if (passIndex == null)
            throw Object.defineProperty(new Error("Null passIndex in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        else
            indexing.setIndex(passIndex);
        this.trace$java_lang_String$java_lang_Object(" > Created indexing ", indexing);
        return indexing;
    }
    newTargetsGroup(title, ...members) {
        let group = new (__Function$1.prototype.bind.apply(TargetCore, [null, title].concat(STarget.newTargets(members))));
        this.trace$java_lang_String$java_lang_Object(" > Created target group " + Debug.info(group) + " ", members);
        return group;
    }
    buildTargeterTree(targetTree) {
        this.trace$java_lang_String$java_lang_Object(" > Creating targeters for ", targetTree);
        this.targeterTree = targetTree.newTargeter();
        this.targeterTree.setNotifiable(this.notifiable);
        this.targeterTree.retarget(targetTree);
        this.addTitleTargeters(this.targeterTree);
    }
    addTitleTargeters(t) {
        let then = (this.titleTargeters[t.title()] = t);
        {
            let array143 = t.elements();
            for (let index142 = 0; index142 < array143.length; index142++) {
                let e = array143[index142];
                this.addTitleTargeters(e);
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
        let facet = new Facets.Facets$4(this, facetUpdated);
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
        let target = this.titleTarget(title);
        if (target == null)
            return null;
        let state = target.state();
        this.trace$java_lang_String$java_lang_Object(" > Getting target state for title=" + title + " state=", state);
        return state;
    }
    setTargetLive(title, live) {
        let target = this.titleTarget(title);
        if (target == null)
            throw Object.defineProperty(new Error("Null target in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        else
            target.setLive(live);
    }
    isTargetLive(title) {
        let target = this.titleTarget(title);
        if (target == null)
            throw Object.defineProperty(new Error("Null target in " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        else
            return target.isLive();
    }
    titleTarget(title) {
        let targeter = ((m, k) => m[k] ? m[k] : null)(this.titleTargeters, title);
        return targeter == null ? null : targeter.target();
    }
}
Facets["__class"] = "fjs.globals.Facets";
Facets["__interfaces"] = ["fjs.util.Identified"];
(function (Facets) {
    class TargetCoupler {
        constructor() {
            this.targetStateUpdated = null;
        }
    }
    Facets.TargetCoupler = TargetCoupler;
    TargetCoupler["__class"] = "fjs.globals.Facets.TargetCoupler";
    class Facets$0 {
        constructor(__parent) {
            this.__parent = __parent;
        }
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
    }
    Facets.Facets$0 = Facets$0;
    Facets$0["__interfaces"] = ["fjs.core.Notifiable", "fjs.util.Titled"];
    class Facets$1 extends STextual.Coupler {
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
    Facets.Facets$1 = Facets$1;
    Facets$1["__interfaces"] = ["fjs.util.Identified", "fjs.core.TargetCoupler", "java.io.Serializable"];
    class Facets$2 extends SToggling.Coupler {
        constructor(__parent, c) {
            super();
            this.c = c;
            this.__parent = __parent;
        }
        /**
         *
         * @param {SToggling} target
         */
        stateSet(target) {
            this.__parent.updatedTarget(target, this.c);
        }
    }
    Facets.Facets$2 = Facets$2;
    Facets$2["__interfaces"] = ["fjs.core.TargetCoupler", "java.io.Serializable"];
    class Facets$3 extends SIndexing.Coupler {
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
    Facets.Facets$3 = Facets$3;
    Facets$3["__interfaces"] = ["fjs.core.TargetCoupler", "java.io.Serializable"];
    class Facets$4 {
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
            if (!((o1, o2) => { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(state, this.stateThen)) {
                this.__parent.trace(" > Updating UI with state=", state);
                (target => (typeof target === 'function') ? target(this.stateThen = state) : target.accept(this.stateThen = state))(this.facetUpdated);
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
    Facets.Facets$4 = Facets$4;
    Facets$4["__interfaces"] = ["fjs.core.SRetargetable", "fjs.core.SFacet"];
})(Facets || (Facets = {}));
var __Function$1 = Function;

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
function newInstance(trace) {
    return new Facets("Facets", trace);
}

exports.newInstance = newInstance;

}((this.Facets = this.Facets || {})));
//# sourceMappingURL=Facets.js.map
