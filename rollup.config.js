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
const includeLib = Object.assign({}, common, {
  entry: 'src/fjs/SimpleSurface.js',
  dest: 'Superficial.js',
  sourceMap: true,
  moduleName: 'Superficial',
});

const bundle = includeLib;//
console.log('Bundling to '+bundle.dest);
export default bundle;