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
  entry: 'src/fjs/SimpleSurface.js',
  dest: 'SimpleSurface.js',
});
const module = Object.assign({}, common, {
  entry: 'src/fjs/Superficial.ts',
  dest: 'Superficial.js',
});

const bundle = app;// |app|module|
console.log('Bundling to '+bundle.dest);
export default bundle;