import * as fs from 'fs';
['in/fjs/globals/Facets.js','public/Facets.js','node_modules/Facets.js']
  .forEach((path)=>{
  if(fs.existsSync(path)){
    console.log('unlinkSync path='+path)
    fs.unlinkSync(path);
  }
  else console.log('No path='+path);
});

