# README for facets-js

## [Superficial](doc/Superficial.pdf) as JS module

- Hybrid project, written in Java and transpiled to JS 
with the admirable [JSweet](http://www.jsweet.org
) 
- Java based on the [core of the original Facets](https://github.com/dimwight/Facets/tree/master/Facets/facets/core)
- Output bundled using [Rollup](https://rollupjs.org 
)
- Super-simple API in ES5/IIFE 
- Demo React apps planned using [Blueprint](http://blueprintjs.com/)
### Getting to libs
1. globals/Globals.java
2. Clean ts/fjs
1. Tidy up import, unused types   
1. Build and run `appIn`
1. Globals to Facets 
1. Build `libInclude`, build, run and launch`appInclude`
1. Build `libExclude`, build and launch `appExclude` 

 ```
 //rollup.config.js
 
 import resolve from 'rollup-plugin-node-resolve';
 import commonjs from 'rollup-plugin-commonjs';
 import sourcemaps from 'rollup-plugin-sourcemaps';
 
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
 const libInclude = Object.assign({}, module, {
   format: 'es',
   dest: 'node_modules/Facets.js',
 });
 const libExclude= Object.assign({}, module, {
   format: 'iife',
   dest: 'public/Facets.js',
 });
 const appInclude= Object.assign({}, app, {
   entry: 'src/App.js',
 });
 const appExclude= Object.assign({}, appInclude, {
   external: ['Facets.js'],
   globals: {'Facets.js': module.moduleName,}
 });
 
 const bundle = appExclude; //appIn|libInclude|libExclude|appInclude|appExclude
 console.log('Bundling '+bundle.entry+' to '+bundle.dest);
 export default bundle;