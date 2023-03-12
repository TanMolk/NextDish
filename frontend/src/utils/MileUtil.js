const FACTOR = 0.621371;

function mileToMeter(miles) {
    return miles / FACTOR * 1000;
}


export {
    mileToMeter
}