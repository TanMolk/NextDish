function get(key) {
    return localStorage.getItem(key)
}

function set(key, value) {
    return localStorage.setItem(key, value)
}


export default {
    set,
    get
}