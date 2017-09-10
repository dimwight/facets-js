# README for facets-js

## [Superficial](http://superficial.sourceforge.net/) in a JS module

- Hybrid project, written in Java and transpiled to JS 
with the admirable [JSweet](http://www.jsweet.org
) 
- Java based on the [core of the original Facets](https://github.com/dimwight/Facets/tree/master/Facets/facets/core)
- Output bundled using [Rollup](https://rollupjs.org 
)
- Stupidly simple API in ES6/IIFE 
- Demo React apps planned using [Blueprint](http://blueprintjs.com/)
### Getting to libs
1. `globals/Globals.java`
2. Clean `ts/` and copy `ts/fjs/` to `ws-in`
1. Tidy up `SimpleSurface.ts` import   
1. Build and run `simple`; launch fails. 
1. Build, run and launch `appIn`
1. Clean and build `libInclude`
1. Build, run and launch `appInclude`
1. Clean and build `libExclude`
1. Build and launch `appExclude`; run fails with `Facets is not defined`. 

 ```
//in/js/SimpleSurface.ts
 
import * as Globals from './globals/Globals';

//rollup.config.js
 
import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import sourcemaps from 'rollup-plugin-sourcemaps';

const base = {
  format: 'iife',
  sourceMap: true,
  plugins: [
    resolve(),
    commonjs(),
    sourcemaps()
  ]
};
const app = Object.assign({}, base, {
  dest: 'public/index.js',
  moduleName: 'unused',
});
const simple = Object.assign({}, app, {
  entry: 'in/fjs/SimpleSurface.js',
});
const appIn = Object.assign({}, app, {
  entry: 'src/main.js',
});
const lib = Object.assign({}, base, {
  entry: (false ? 'src/lib/Facets.js' : 'in/fjs/globals/Globals.js'),
  moduleName: 'Facets',
});
const libInclude = Object.assign({}, lib, {
  format: 'es',
  dest: 'node_modules/Facets.js',
});
const libExclude= Object.assign({}, lib, {
  dest: 'public/Facets.js',
});
const appInclude= Object.assign({}, appIn, {
});
const appExclude= Object.assign({}, appIn, {
  external: ['Facets'],
  globals: {'Facets': lib.moduleName,}
});

const bundle = appExclude; //simple|appIn|libInclude|appInclude|libExclude|appExclude
console.log('Bundling: entry='+bundle.entry+' dest='+bundle.dest + ' format='+bundle.format);
export default bundle;

node_modules/@types/facets-js/index.d.ts:
export as namespace Facets;
export = Facets;

export default Facets;

declare namespace Facets{
  /**
   Marker interface
   */
  interface Target{}
  interface TargetCoupler {
    targetStateUpdated: (p1: string, p2: any) => void;
  }
  /**
  Connects a textual target with client code.
   */
  interface TextualCoupler extends TargetCoupler{
    /**
     Sets initial state of the textual.
     */
    passText:string;
    isValidText?:(p1:string,p2:string)=>boolean;
    getText?:(p1:string)=>string;
    updateInterim?:(p1:string)=>void;
  }
  interface IndexingCoupler extends TargetCoupler{
    passIndexables:any[];
    passIndex:number;
  }
  function newInstance(trace:boolean):Facets;
  interface Facets{
    updatedTarget(target:any,c:TargetCoupler):void;
    newIndexingTarget(title:string,c:IndexingCoupler):Target;
    /**
     *
     * @param {string} title
     * @param {Facets.TextualCoupler} c
     * @returns {Facets.Target}
     */
    newTextualTarget(title:string,c:TextualCoupler):Target;
    newTargetsGroup(title:string,...members:Target[]):Target;
    buildTargeterTree(targets:any):void;
    attachFacet(title:string,facetUpdated:any):void;
    updateTargetState(title:string,update:any):void;
    getTargetState(title:string):any;
  }
}