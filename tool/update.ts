import * as fs from 'fs-extra';

const basePath='C:/Users/Me/ts/',
  fromRoot=basePath+'facets-js/',
  toRoot=basePath+'react-rollup/',
  lib='public/Facets.js',
  dTs='index.d.ts',
  modules='node_modules/@types/facets-js/',
  fromLib=fromRoot+lib,toLib=toRoot+lib,
  fromModules=fromRoot+modules+dTs,toModules=toRoot+modules+dTs,
  fromTop=fromRoot+dTs,toTop=toRoot+dTs;

const doCopy=function(src,dest){
  fs.copySync(src,dest);
  const stats=fs.statSync(dest);
  console.log(`${dest}=${stats.size}`)
};
doCopy(fromLib,toLib);
const master=fromTop;
(master===fromTop?[fromModules,toModules,toTop]
:master===toModules?[fromModules,fromTop,toTop]
:[]
).forEach(
  (dest)=>doCopy(master,dest));
