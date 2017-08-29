/* Generated from Java with JSweet 2.0.0-rc1 - http://www.jsweet.org */
import { Debug } from '../util/Debug'; 
import { NotifyingImpact } from './NotifyingImpact'; 
import { Notifying } from './Notifying'; 
/**
 * Object to be passed by {@link Notifying} to {@link Notifiable}.
 * @param {*} source
 * @param {NotifyingImpact} impact
 * @class
 */
export class Notice {
    public impact : NotifyingImpact;

    public sources : Array<Notifying> = <any>([]);

    public constructor(source : Notifying, impact : NotifyingImpact) {
        this.impact = null;
        this.impact = impact;
        this.addSource(source);
    }

    public addSource(source : Notifying) : Notice {
        if(source == null) throw Object.defineProperty(new Error("Null source in " + Debug.info(this)), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        /* add */(this.sources.push(source)>0);
        return this;
    }

    public toString() : string {
        return Debug.info(this) + " " + NotifyingImpact[this.impact];
    }
}
Notice["__class"] = "fjs.core.Notice";



var __Function = Function;
