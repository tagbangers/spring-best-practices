var window = this;
var history = { location : { href: null } };

// var dummy = {
//     setAttribute: function() {},
//     appendChild: function() {}
// };
// var document = {
//     createElement: function () { return dummy; },
//     querySelector: function() {},
//     getElementsByTagName: function() { return [dummy]; }
// };
//
// var require = function() {};

var console = {};
console.debug = print;
console.warn = print;
console.log = print;

var setTimeout = function () {}
