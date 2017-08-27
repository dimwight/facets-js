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
});
const appIn = Object.assign({}, app, {
  entry: 'in/fjs/SimpleSurface.js',
  moduleName: 'SimpleSurface',
});
const module = Object.assign({}, common, {
  entry: 'in/fjs/globals/Facets.js',
  moduleName: 'Facets',
});
const moduleEs = Object.assign({}, module, {
  format: 'es',
  dest: 'node_modules/Facets.js',
});
const publicUmd= Object.assign({}, module, {
  format: 'umd',
  dest: 'public/Facets.js',
});
const appSrc= Object.assign({}, app, {
  entry: 'src/App.js',
  moduleName: 'App',
  external: ['Facets',],
  globals: {'': module.moduleName,}
});

const bundle = appSrc; //appIn|moduleEs|publicUmd|appSrc
console.log('Bundling '+bundle.entry+' to '+bundle.dest);
export default bundle;