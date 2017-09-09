import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import sourcemaps from 'rollup-plugin-sourcemaps';

const base = {
  sourceMap: true,
  plugins: [
    resolve(),
    commonjs(),
    sourcemaps()
  ]
};
const app = Object.assign({}, base, {
  format: 'iife',
  dest: 'public/App.js',
  moduleName: 'App',
});
const simple = Object.assign({}, app, {
  entry: 'in/fjs/SimpleSurface.js',
  moduleName: 'unused',
});
const appIn = Object.assign({}, app, {
  entry: 'src/App.js',
  moduleName: 'unused',
});
const lib = Object.assign({}, base, {
  entry: (true?'in/fjs/globals/Globals.js':'src/lib/Facets.js'),
  moduleName: 'Facets',
});
const libInclude = Object.assign({}, lib, {
  format: 'es',
  dest: 'node_modules/FacetsNode.js',
});
const libExclude= Object.assign({}, lib, {
  format: 'iife',
  dest: 'public/FacetsPublic.js',
});
const appInclude= Object.assign({}, appIn, {
});
const appExclude= Object.assign({}, appInclude, {
  external: ['Facets.js'],
  globals: {'Facets.js': lib.moduleName,}
});

const bundle = appIn; //simple|appIn|libInclude|appInclude|libExclude|appExclude
console.log('Bundling: entry='+bundle.entry+' dest='+bundle.dest + ' format='+bundle.format);
export default bundle;