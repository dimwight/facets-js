import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import sourcemaps from 'rollup-plugin-sourcemaps';

const common = {
  format: 'iife',
  moduleName: 'Superficial',
  plugins: [
    resolve(),
    commonjs(),
    sourcemaps()
  ]
};
const app = Object.assign({}, common, {
  sourceMap: true,
  dest: 'App.js',
});
const appIn = Object.assign({}, app, {
  entry: 'in/fjs/SimpleSurface.js',
});
const module = Object.assign({}, common, {
  entry: 'in/fjs/globals/Superficial.js',
  dest: 'src/Superficial.js',
});
const appSrc = Object.assign({}, app, {
  entry: 'src/SimpleSurface.js',
});

const bundle = appSrc;// |appIn|module|appSrc
console.log('Bundling to '+bundle.dest);
export default bundle;