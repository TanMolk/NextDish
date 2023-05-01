let originalSetItem = window.localStorage.setItem;

localStorage.setItem = function (key, newValue) {
    let setItemEvent = new Event("setItemEvent");
    setItemEvent.key = key;
    setItemEvent.newValue = newValue;
    window.dispatchEvent(setItemEvent);
    originalSetItem.apply(this, arguments);
}

function get(key) {
    return localStorage.getItem(key)
}

function set(key, value) {
    return localStorage.setItem(key, value)
}

function remove(key) {
    localStorage.removeItem(key);
}


export default {
    set,
    get,
    remove,
}