# facets-js

## [Superficial](http://superficial.sourceforge.net/) in a JS module

- Hybrid project, written in Java and transpiled to JS 
with the admirable [JSweet](http://www.jsweet.org
) 
- Java based on the [core of the original Facets](https://github.com/dimwight/Facets/tree/master/Facets/facets/core)
- Output bundled using [Rollup](https://rollupjs.org 
)
- Stupidly simple API in ES6/IIFE 

### [Getting to libs]
1. `globals/Globals.java`
2. Clean `ts/` and copy `ts/Facets/` to `ws-in`
1. Tidy up _SimpleSurface.ts_ import   
1. Build, run (and launch) `simple`
1. Clean and build `libNode`
1. Build, run and launch `appNode` 
1. Clean and build `libWeb`
1. Build and launch `appWeb`; 
run fails with `Facets is not defined` 

 ```
//in/Facets/SimpleSurface.ts
 
import * as Globals from './globals/Globals';

//rollup.config.js
 
import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import sourcemaps from 'rollup-plugin-sourcemaps';

const base = {
  format: true?'iife':'umd',
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
  entry: 'in/fjs/' +(false?'SimpleSurface.js':'SelectingSurface.js'),
});
const main = Object.assign({}, app, {
  entry: 'src/main.js',
});
const lib = Object.assign({}, base, {
  entry: 'in/fjs/globals/' +(false?'Facets.js':'Globals.js'),
  moduleName: 'Facets',
});
const libNode = Object.assign({}, lib, {
  format: 'es',
  dest: 'node_modules/facets-js/index.js',
});
const libWeb= Object.assign({}, lib, {
  dest: 'public/Facets.js',
});
const appNode= Object.assign({}, main, {
});
const appWeb= Object.assign({}, main, {
  external: ['facets-js'],
  globals: {'facets-js': lib.moduleName,}
});

const bundle = libWeb; //simple|libNode|appNode|libWeb|appWeb
console.log('Bundling: entry='+bundle.entry+' dest='+bundle.dest + ' format='+bundle.format);
export default bundle;