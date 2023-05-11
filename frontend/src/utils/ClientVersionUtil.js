/**
 * This utility provides function to check whether user's device is mobile
 * @author Wei Tan
 */

/**
 * To check whether user's device is mobile
 * @returns {RegExpMatchArray}
 */
function isMobile() {
    return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
}


export default {
    isMobile,
}