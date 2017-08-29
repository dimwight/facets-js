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

const bundle = appInclude; //appIn|libInclude|libExclude|appInclude|appExclude
console.log('Bundling '+bundle.entry+' to '+bundle.dest);
export default bundle;