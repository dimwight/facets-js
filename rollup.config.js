import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import sourcemaps from 'rollup-plugin-sourcemaps';

const base = {
  format: true?'iife':'umd',
  sourceMap: true,
  plugins: [
    resolve(),
    commonjs({
      namedExports: {
        'node_modules/facets-js/index.js': [ 'Facets' ]
      }
    }),
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

const bundle = appWeb; //simple|libNode|appNode|libWeb|appWeb
console.log('Bundling: entry='+bundle.entry+' dest='+bundle.dest + ' format='+bundle.format);
export default bundle;