/***
 *
 * Description:
 *
 * <p>author: Ives.l
 * <p>date: 2018/11/27
 * <p>time: 11:49
 *
 */

let a = 5;
let b = 10;

function tag(s, v1, v2) {
    console.log(s[0]);
    console.log(s[1]);
    console.log(s[2]);
    console.log(v1);
    console.log(v2);
    return "OK";
}

tag`Hello ${a + b} world ${a * b}`;
// "Hello "
// " world "
// ""
// 15
// 50
// "OK"


let total = 30;
let msg = passThru`The total is ${total} (${total * 1.05} with tax)`;

function passThru(literals) {
    let result = '';
    let i = 0;
    while (i < literals.length) {
        result += literals[i++];
        if (i < arguments.length) {
            result += arguments[i];
        }
    }
    return result;
}

console.log(msg); // "The total is 30 (31.5 with tax)"



