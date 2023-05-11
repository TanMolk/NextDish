const FACTOR = 0.621371;

/**
 * This utility provides function is to convert miles to meters
 * @author Wei Tan
 */

/**
 * To convert miles to meters
 * @param miles miles
 * @returns {number} meters
 */
function mileToMeter(miles) {
    return miles / FACTOR * 1000;
}


export {
    mileToMeter
}