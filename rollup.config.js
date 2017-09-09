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

const bundle = appInclude; //simple|appIn|libInclude|appInclude|libExclude|appExclude
console.log('Bundling: entry='+bundle.entry+' dest='+bundle.dest + ' format='+bundle.format);
export default bundle;