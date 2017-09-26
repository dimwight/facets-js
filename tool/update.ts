import * as fs from 'fs-extra';

const workpath='C:/Users/Me/ts/',
  fromPath=workpath+'facets-js/',
  toPath=workpath+'react-rollup/',
  lib='public/Facets.js',
  dts='node_modules/@types/facets-js/'+'index.d.ts',
  fromLib=fromPath+lib,toLib=toPath+lib,
  fromDts=fromPath+dts,toDts=toPath+dts,
  fromStore=fromPath+'index.d.ts',toStore=toPath+'index.d.ts';

[[fromLib,toLib],
  [fromDts,toDts],
  [fromStore,toStore]
].forEach((pair)=>{
  fs.copySync(pair[0],pair[1]);
  pair.forEach((path)=>{
    const stats=fs.statSync(path);
    const date=new Date(stats.mtime);
    console.log(path,'\t\t', date, '\t',stats.size)
  })
})
