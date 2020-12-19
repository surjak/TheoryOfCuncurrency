const walk = require('walkdir');
const fs = require('fs');
const async = require("async")
const {performance} = require('perf_hooks')

let count = 0;
const countLines = (file, cb) => { fs.createReadStream(file).on('data', function(chunk) {
                count += chunk.toString('utf8')
                .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
                .length-1;
            }).on('end', function() {
                // console.log(file, count);
                cb()
            }).on('error', function(err) {
                // console.error(err);
                cb()
            });
            
        }

const paths = walk.sync('PAM08');

const pathTasks = paths.map(path => (cb) => countLines(path, cb))
const callSync = async () => {
    const t1 = performance.now()
    async.waterfall(pathTasks, () => {console.log(count);
        const t2 = performance.now()
        console.log(t2 - t1);
    })
}

const callAsync = async () => {
    const t1 = performance.now()
    async.parallel(pathTasks, () => {console.log(count);
        const t2 = performance.now()
        console.log(t2 - t1);
    })
}
callSync()
// callAsync()