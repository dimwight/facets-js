import * as fs from 'fs';

const gone=[],missing=[];
['in/fjs/globals/Facets.js','public/Facets.js','node_modules/Facets.js']
  .forEach((path)=>{
  if(fs.existsSync(path)){
    gone.push(path);
    fs.unlinkSync(path);
  }
  else missing.push(path);
  });
gone.forEach((path)=>console.log('gone path='+path));
missing.forEach((path)=>console.log('missing path='+path));


