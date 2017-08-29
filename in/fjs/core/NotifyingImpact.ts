/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
/**
 * Allows a {@link Notifying} to refine its notification.
 * <p>The members of {@link NotifyingImpact} can define the scope of the event
 * triggering a notification;
 * each member guarantees that its impact will be less than any member
 * with higher {@link #ordinal()}
 * <ul>
 * <li>{@link #MINI} no state change; typically a status message
 * <li>{@link #ACTIVE} active state eg of viewers may have changed
 * <li>{@link #SELECTION} content selection state may have changed
 * <li>{@link #CONTENT} content state may have changed
 * <li>{@link #DEFAULT} any application element or state may have changed
 * </ul>
 * @enum
 * @property {NotifyingImpact} MINI
 * @property {NotifyingImpact} ACTIVE
 * @property {NotifyingImpact} SELECTION
 * @property {NotifyingImpact} CONTENT
 * @property {NotifyingImpact} DEFAULT
 * @property {NotifyingImpact} DISPOSE
 * @class
 */
export enum NotifyingImpact {
    MINI, ACTIVE, SELECTION, CONTENT, DEFAULT, DISPOSE
}

/** @ignore */
export class NotifyingImpact_$WRAPPER {
    constructor(protected _$ordinal : number, protected _$name : string) {
    }

    public exceeds(other) : boolean {
        return this.ordinal() > NotifyingImpact[NotifyingImpact[other]];
    }
    public name() : string { return this._$name; }
    public ordinal() : number { return this._$ordinal; }
}
NotifyingImpact["__class"] = "fjs.core.NotifyingImpact";
NotifyingImpact["__interfaces"] = ["java.lang.Comparable","java.io.Serializable"];

NotifyingImpact["_$wrappers"] = [new NotifyingImpact_$WRAPPER(0, "MINI"), new NotifyingImpact_$WRAPPER(1, "ACTIVE"), new NotifyingImpact_$WRAPPER(2, "SELECTION"), new NotifyingImpact_$WRAPPER(3, "CONTENT"), new NotifyingImpact_$WRAPPER(4, "DEFAULT"), new NotifyingImpact_$WRAPPER(5, "DISPOSE")];



var __Function = Function;
