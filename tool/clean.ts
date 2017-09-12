import * as fs from 'fs';
const gone=[],missing=[];
[
  // 'in/Facets/globals/Facets.js',
  'node_modules/facets-js/index.js',
  'src/Facets.js',
  'public/Facets.js',
].forEach((path)=>{
    if(fs.existsSync(path)){
      gone.push(path);
      fs.unlinkSync(path);
    }
    else missing.push(path);
  });
gone.forEach((path)=>console.log('gone path='+path));
missing.forEach((path)=>console.log('missing path='+path));


