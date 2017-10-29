import * as fs from 'fs-extra';

const basePath='C:/Users/Me/ts/',
  fjsRoot=basePath+'facets-js/',
  rrRoot=basePath+'facets-react/',
  frRoot=basePath+'facets-react/',
  lib='public/Facets.js',
  dTs='index.d.ts',
  modules='node_modules/@types/facets-js/',
  fjsLib=fjsRoot+lib,
  rrLib=frRoot+lib,
  frLib=frRoot+lib,
  fjsModules=fjsRoot+modules+dTs,
  rrModules=frRoot+modules+dTs,
  frModules=frRoot+modules+dTs,
  rrTop=fjsRoot+dTs,
  fjsTop=fjsRoot+dTs,
  frTop=frRoot+dTs;

const doCopy=function(src,dest){
  fs.copySync(src,dest);
  const stats=fs.statSync(dest);
  console.log(`${dest}=${stats.size}`)
};
if(true)doCopy(fjsLib,frLib);
const master=fjsTop;
(master===fjsTop?[fjsModules,rrModules,rrTop,frModules,frTop]
:master===frModules?[fjsModules,rrModules,rrTop,frModules,frTop]
:null).forEach(
  (dest)=>doCopy(master,dest));
