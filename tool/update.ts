import * as fs from 'fs-extra';

const basePath='C:/Users/Me/ts/',
  fjsRoot=basePath+'facets-js/',
  rrRoot=basePath+'react-rollup/',
  frRoot=basePath+'facets-react/',
  lib='public/Facets.js',
  dTs='index.d.ts',
  modules='node_modules/@types/facets-js/',
  fjsLib=fjsRoot+lib,
  rrLib=rrRoot+lib,
  frLib=frRoot+lib,
  fjsModules=fjsRoot+modules+dTs,
  rrModules=rrRoot+modules+dTs,
  frModules=frRoot+modules+dTs,
  fjsTop=fjsRoot+dTs,
  rrTop=rrRoot+dTs,
  frTop=frRoot+dTs;

const doCopy=function(src,dest){
  fs.copySync(src,dest);
  const stats=fs.statSync(dest);
  console.log(`${dest}=${stats.size}`)
};
[rrLib,frLib].forEach(dest=>doCopy(fjsLib,dest));
[fjsModules,rrModules,rrTop,frModules,frTop].forEach(
  (dest)=>doCopy(fjsTop,dest));
