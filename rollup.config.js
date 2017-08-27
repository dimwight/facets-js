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

const bundle = appNode; //appIn|libNode|libWeb|appNode|appWeb
console.log('Bundling '+bundle.entry+' to '+bundle.dest);
export default bundle;