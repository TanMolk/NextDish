const FACTOR = 0.621371;

/**
 * convert miles to meters
 * @param miles miles
 * @returns {number} meters
 */
function mileToMeter(miles) {
    return miles / FACTOR * 1000;
}


export {
    mileToMeter
}