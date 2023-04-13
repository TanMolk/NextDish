function get(key) {
    return localStorage.getItem(key)
}

function set(key, value) {
    return localStorage.setItem(key, value)
}

function remove(key){
    localStorage.removeItem(key);
}


export default {
    set,
    get,
    remove,
}