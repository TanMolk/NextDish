/**
 * TO create local storage item
 * @type {(key: string, value: string) => void}
 * @author Wei Tan
 */
let originalSetItem = window.localStorage.setItem;

localStorage.setItem = function (key, newValue) {
    let setItemEvent = new Event("setItemEvent");
    setItemEvent.key = key;
    setItemEvent.newValue = newValue;
    window.dispatchEvent(setItemEvent);
    originalSetItem.apply(this, arguments);
}

/**
 * To get item from localStorage
 * @param key item key
 * @returns {string}
 */
function get(key) {
    return localStorage.getItem(key)
}

/**
 * To set item in localStorage
 * @param key item key
 * @param value
 */
function set(key, value) {
    return localStorage.setItem(key, value)
}

/**
 * To remove item from localStorage
 * @param key
 */
function remove(key) {
    localStorage.removeItem(key);
}


export default {
    set,
    get,
    remove,
}