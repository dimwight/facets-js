import * as fs from 'fs';

const workpath='C:/Users/Me/ts/',
  fromPath=workpath+'facets.js/',
  toPath=workpath+'react-rollup/',
  lib='public/Facets.js',
  dts='node_modules/@types/facets-js/'+'index.d.ts',
  fromLib=fromPath+lib,toLib=toPath+lib,
  fromDts=fromPath+dts,toDts=toPath+dts,
  fromStore=fromPath+'index.d.ts',toStore=toPath+'index.d.ts';


