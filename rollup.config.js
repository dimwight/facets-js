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