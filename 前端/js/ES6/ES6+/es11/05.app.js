// import * as m1 from './05.hello.js';
// 获取元素
const btn = document.getElementById('btn');

btn.onclick = function () {
    // m1.hello();
    import('./05.hello.js').then(module => {
        module.hello();
    });
}