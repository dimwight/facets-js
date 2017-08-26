#README for facets-js

##[Facets](doc/Facets.pdf) as JS module

- Hybrid project, written in Java and transpiled to JS 
with the admirable [JSweet](http://www.jsweet.org
) 
- Java based on the [core of the original Facets](https://github.com/dimwight/Facets/tree/master/Facets/facets/core)
- Output bundled using [Rollup](https://rollupjs.org 
)
- API deliberately untyped for greatest flexibility
- Demo app with [Palantir](https://github.com/palantir/blueprint 
)components
### Getting to module?

1. globals/Globals.java
1. Rename .ts to Facets externally and internally
1. Adjust in/fjs/SimpleSurface.ts as below.   
1. Build `appIn`, launches OK
1. Build `moduleNode|modulePublic`. 
1. `src/App.ts` 
1. Build `appSrc` with either import, warning
`The final argument to magicString.overwrite...`
1. Launch fails:  
`TypeError: Facets.newTextual is not a function`
1. Looking at .ts/.js, WS "Cannot find declaration..."!
 ```
  in/fjs/SimpleSurface.ts
 
  import * as Facets from "./globals/Facets";
  ...
  let js : boolean = Facets.onlyJs;
 -------------------------------------------------------
 src/App.ts
 
 import Facets from 'Facets';
 // import Facets from 'Facets.js';

  
 ------------------------------------------------------
 import resolve from 'rollup-plugin-node-resolve';
 import commonjs from 'rollup-plugin-commonjs';
 import sourcemaps from 'rollup-plugin-sourcemaps';
 
 const common = {
   format: 'iife',
   plugins: [
     resolve(),
     commonjs(),
     sourcemaps()
   ]
 };
 const app = Object.assign({}, common, {
   sourceMap: true,
   dest: 'public/App.js',
 });
 const appIn = Object.assign({}, app, {
   entry: 'in/fjs/SimpleSurface.js',
   moduleName: 'SimpleSurface',
 });
 const module = Object.assign({}, common, {
   entry: 'in/fjs/globals/Facets.js',
   moduleName: 'Facets',
 });
 const moduleNode = Object.assign({}, module, {
   dest: 'node_modules/Facets.js',
 });
 const modulePublic= Object.assign({}, module, {
   dest: 'public/Facets.js',
 });
 const appSrc= Object.assign({}, app, {
   entry: 'src/App.js',
   moduleName: 'App',
   // external: ['Facets',],globals: {'Facets': module.moduleName,}
 });
 
 const bundle = appSrc; //appIn|moduleNode|modulePublic|appSrc
 console.log('Bundling '+bundle.entry+' to '+bundle.dest);
 export default bundle;