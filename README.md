#README for facets-js

##[Superficial](doc/Superficial.pdf) as JS module

- Hybrid project, written in Java and transpiled to JS 
with the admirable [JSweet](http://www.jsweet.org
) 
- Java based on the [core of the original Facets](https://github.com/dimwight/Facets/tree/master/Facets/facets/core)
- Output bundled using [Rollup](https://rollupjs.org 
)
- API deliberately untyped for greatest flexibility
- Demo app with [Palantir](https://github.com/palantir/blueprint 
)components
### Getting to module?

1. globals/Globals.java
1. Rename .ts to Superficial externally and internally
1. Adjust in/fjs/SimpleSurface.ts as below.   
1. Build `appIn`, launches OK
1. Build `module`, warning:  
`The final argument to magicString.overwrite...`
1. Build `appSrc`, launch fails:  
`TypeError: Superficial.newTextual is not a function`
1. Looking at .js, WS "Cannot find declaration..."!
 ```
  in/fjs/SimpleSurface.ts
 
  import * as Superficial from "./globals/Superficial";
  ...
  let js : boolean = Superficial.onlyJs;
  
  ------------------------------------------------------
  rollup.config.js
  
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
  ```
 