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
    static shortName(className) {
        let semiColon = className.lastIndexOf(';');
        let stop = semiColon > 0 ? semiColon : className.length;
        return className.substring(className.lastIndexOf('.') + 1, stop);
    }
    static sigFigs(val, digits) {
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
        for (let index164 = 0; index164 < items.length; index164++) {
            let item = items[index164];
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
        if (typeof o === 'string') {
            let text = o;
            let length = text.length;
            return text.substring(0, Math.min(length, 60)) + (": " + ("length=" + length));
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
        Util.printOut$java_lang_String(">" + string);
    }
    static toStringWithHeader(array) {
        return Debug.info(array) + " [" + array.length + "] " + Objects.toLines(array);
    }
}
Debug.trace = false;
Debug["__class"] = "fjs.util.Debug";

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
var STarget;
(function (STarget) {
    function newTargets(src) {
        let type = "fjs.core.STarget";
        let array = new Array(src.length);
        if (false && src.length > 0 && !type.isAssignableFrom(src[0].constructor))
            throw Object.defineProperty(new Error(Debug.info(src[0]) + " should be STarget: \n" + type), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
        else {}
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
                this.top = Debug.info(this);
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
    /**
     * Outputs complete trace messages to console or elsewhere.
     * <p>Default prepends helpful classname to message.
     * @param {string} msg passed from one of the <code>public</code> methods
     */
    traceOutput(msg) {
        this.traceOutputWithId(msg);
    }
    traceOutputWithId(msg) {
        Util.printOut$java_lang_String(this.top + " #" + this.id + " " + msg);
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
    trace$java_lang_String$java_lang_Object(msg, o) {
        this.traceOutput(msg + this.traceObjectText(o));
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
 * <p>{@link NotifyingCore} is the shared superclass of both the {@link STarget} and
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
     * @param {string} msg
     */
    traceOutput(msg) {
        super.traceOutput(/* getSimpleName */ (c => c["__class"] ? c["__class"].substring(c["__class"].lastIndexOf('.') + 1) : c["name"].substring(c["name"].lastIndexOf('.') + 1))(this.constructor) + msg);
    }
    /**
     *
     * @return {*}
     */
    notifiable() {
        if (false && this.__notifiable == null)
            throw Object.defineProperty(new Error("No monitor in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
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
            this.__elements = null;
            this.wantsFocus = false;
            this.__title = null;
            this.live = true;
            this.__elements = null;
            this.wantsFocus = false;
            this.__title = null;
            (() => {
                TargetCore.targets++;
                if ((this.__title = title) == null || ((o1, o2) => { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(title, ""))
                    throw Object.defineProperty(new Error("Null or empty title in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                if (elements != null)
                    this.setElements(elements);
                if (Debug.trace)
                    Debug.traceEvent("Created " + Debug.info(this));
            })();
        }
        else if (((typeof title === 'string') || title === null) && elements === undefined) {
            let __args = Array.prototype.slice.call(arguments);
            {
                let __args = Array.prototype.slice.call(arguments);
                let elements = null;
                super();
                this.__elements = null;
                this.wantsFocus = false;
                this.__title = null;
                this.live = true;
                this.__elements = null;
                this.wantsFocus = false;
                this.__title = null;
                (() => {
                    TargetCore.targets++;
                    if ((this.__title = title) == null || ((o1, o2) => { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(title, ""))
                        throw Object.defineProperty(new Error("Null or empty title in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                    if (elements != null)
                        this.setElements(elements);
                    if (Debug.trace)
                        Debug.traceEvent("Created " + Debug.info(this));
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
        else if ((this.__elements = elements) == null)
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
        if (false && ((o1, o2) => { if (o1 && o1.equals) {
            return o1.equals(o2);
        }
        else {
            return o1 === o2;
        } })(this.__title, "P&aste")) {
            this.trace$java_lang_String$java_lang_Object(".setLive: ", Debug.info(this) + ": " + live + ": " + this.isLive());
        }
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
        return false;
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
 * Core constructor.
 * @param {string} title passed to superclass
 * @param {TextualCoupler} coupler can supply application-specific mechanism and policy;
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
     *
     * @return {boolean}
     */
    notifiesTargeter() {
        return true;
    }
    /**
     * Sets the text value to be exposed.
     * <p>Also calls {@link TextualCoupler#textSet(STextual)} if not initialising.
     * @param {string} text must be non-<code>null</code>; and non-blank
     * unless {@link TextualCoupler#isValidText(STextual, String)}
     * returns <code>true</code>.
     */
    setText(text) {
        if (text != null && !this.coupler.isValidText(this, text))
            throw Object.defineProperty(new Error("Invalid text in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
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
        let text = this.coupler.getText(this.title());
        if (text == null && !this.coupler.isValidText(this, text))
            throw Object.defineProperty(new Error("Null or invalid text in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
        return text;
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

/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Connects a {@link STextual} to the application.
 * <p>A {@link TextualCoupler} is required to supply a {@link STextual}
 * with client-specific mechanism.
 * @class
 * @extends Tracer
 */
class TextualCoupler extends Tracer {
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
    getText(title) {
        throw Object.defineProperty(new Error("Not implemented in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
    }
    constructor() {
        super();
    }
}
TextualCoupler["__class"] = "fjs.core.TextualCoupler";
TextualCoupler["__interfaces"] = ["fjs.util.Identified", "fjs.core.TargetCoupler", "java.io.Serializable"];

let trace = true;
let onlyJs = true;
let KEY_TEXTUAL_TEXT = "text";
let titleTargeters;
function titleTargeters_$LI$() { if (titleTargeters == null)
    titleTargeters = ({}); return titleTargeters; }

let t;
function t_$LI$() { if (t == null)
    t = new exports.Facets.Globals$0("Facets"); return t; }

let targeterTree = null;
function newTextual(title, couplerLike) {
    let coupler;
    if (couplerLike != null && couplerLike instanceof TextualCoupler)
        coupler = couplerLike;
    else {
        let jsText = (couplerLike[KEY_TEXTUAL_TEXT]);
        coupler = new exports.Facets.Globals$1(jsText);
    }
    let textual = new STextual(title, coupler);
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Created textual ", textual);
    return textual;
}
function newTargetGroup(title, ...members) {
    let group = new (__Function.prototype.bind.apply(TargetCore, [null, title].concat(STarget.newTargets(members))));
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Created group " + Debug.info(group) + " ", members);
    return group;
}
function buildTargeterTree(targets) {
    targeterTree = targets.newTargeter();
    targeterTree.setNotifiable(new exports.Facets.Globals$2());
    targeterTree.retarget(targets);
    {
        let array163 = targeterTree.elements();
        for (let index162 = 0; index162 < array163.length; index162++) {
            let targeter = array163[index162];
            {
                /* put */ (titleTargeters_$LI$()[targeter.title()] = targeter);
                t_$LI$().trace$java_lang_String$java_lang_Object(" > Created targeter ", targeter);
            }
        }
    }
}
function attachFacet(title, updateFn) {
    if (updateFn == null)
        throw Object.defineProperty(new Error("Null updateFn for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
    else if (title == null || ((o1, o2) => { if (o1 && o1.equals) {
        return o1.equals(o2);
    }
    else {
        return o1 === o2;
    } })(title, ""))
        throw Object.defineProperty(new Error("Null or empty title for " + updateFn), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
    let targeter = ((m, k) => m[k] ? m[k] : null)(titleTargeters_$LI$(), title);
    if (targeter == null)
        throw Object.defineProperty(new Error("Null targeter for " + title), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Attaching facet to ", targeter);
    targeter.attachFacet(new exports.Facets.Globals$3(updateFn));
}
function retargetFacets() {
    targeterTree.retargetFacets();
    t_$LI$().trace$java_lang_String$java_lang_Object(" > Retargeted facets on ", targeterTree);
}
function updateTarget(title, update) {
    let target = ((m, k) => m[k] ? m[k] : null)(titleTargeters_$LI$(), title).target();
    target.updateState(update);
    target.notifyParent();
}

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
    class Globals$1 extends TextualCoupler {
        constructor(jsText) {
            super();
            this.jsText = jsText;
        }
        getText(title) {
            return this.jsText;
        }
    }
    Facets.Globals$1 = Globals$1;
    Globals$1["__interfaces"] = ["fjs.util.Identified", "fjs.core.TargetCoupler", "java.io.Serializable"];
    class Globals$2 {
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
    Facets.Globals$2 = Globals$2;
    Globals$2["__interfaces"] = ["fjs.core.Notifiable", "fjs.util.Titled"];
    class Globals$3 {
        constructor(updateFn) {
            this.updateFn = updateFn;
        }
        /**
         *
         * @param {*} target
         */
        retarget(target) {
            this.updateFn.accept(target.state());
        }
    }
    Facets.Globals$3 = Globals$3;
    Globals$3["__interfaces"] = ["fjs.core.SRetargetable", "fjs.core.SFacet"];
})(exports.Facets || (exports.Facets = {}));
var __Function = Function;

exports.trace = trace;
exports.onlyJs = onlyJs;
exports.KEY_TEXTUAL_TEXT = KEY_TEXTUAL_TEXT;
exports.newTextual = newTextual;
exports.newTargetGroup = newTargetGroup;
exports.buildTargeterTree = buildTargeterTree;
exports.attachFacet = attachFacet;
exports.retargetFacets = retargetFacets;
exports.updateTarget = updateTarget;

}((this.Facets = this.Facets || {})));
