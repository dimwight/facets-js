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
) components
### Getting to module
1. globals/Globals.java
2. Clean ts/fjs
1. Tidy up import, unused types.   
1. Build and run `appIn`
1. Rename Globals.ts to Facets externally and internally
1. Build `libNode`. 
1. Build and run `appNode`
1. Build `libWeb`. 
1. Build and launch `appWeb` 

 ```
 //rollup.config.js
 
 import resolve from 'rollup-plugin-node-resolve';
 import commonjs from 'rollup-plugin-commonjs';
 import sourcemaps from 'rollup-plugin-sourcemaps';
 import path from 'path';
 
 const common = {
   sourceMap: true,
   plugins: [
     resolve(),
     commonjs(),
     sourcemaps()
   ]
 };
 const app = Object.assign({}, common, {
   format: 'iife',
   dest: 'public/App.js',
   moduleName: 'App',
 });
 const appIn = Object.assign({}, app, {
   entry: 'in/fjs/SimpleSurface.js',
   moduleName: 'SimpleSurface',
 });
 const module = Object.assign({}, common, {
   entry: 'in/fjs/globals/Facets.js',
   moduleName: 'Facets',
 });
 const libNode = Object.assign({}, module, {
   format: 'es',
   dest: 'node_modules/Facets.js',
 });
 const libWeb= Object.assign({}, module, {
   format: 'iife',
   dest: 'public/Facets.js',
 });
 const appNode= Object.assign({}, app, {
   entry: 'src/App.js',
 });
 const appWeb= Object.assign({}, appNode, {
   external: [
     path.resolve( './public/Facets.js')
   ],
   globals: {'Facets': module.moduleName,}
 });
 
 const bundle = appWeb; //appIn|libNode|libWeb|appNode|appWeb
 console.log('Bundling '+bundle.entry+' to '+bundle.dest);
 export default bundle;