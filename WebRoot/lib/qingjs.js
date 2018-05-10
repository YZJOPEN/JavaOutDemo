(function() {
    if (window.XuntongJSBridge) {
        return;
    };
    var _CUSTOM_PROTOCOL_SCHEME = 'xuntong',
        callbacksCount = 1,
        callbacks = {};
    function _handleMessageFromXT(callbackId, message) {
        try {
            var callback = callbacks[callbackId];
            if (!callback) return;
            callback.apply(null, [message]);
        } catch (e) {
            alert(e)
        }
    }
    function getOS() {
        var userAgent = navigator.userAgent;
        return userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/) ? 'ios' : userAgent.match(/Android/i) ? 'android' : '';
    }
    function isCloudHub() {
        var userAgent = navigator.userAgent;
        return userAgent.match(/App\/cloudhub/);
    }
    function _call(functionName, message, callback) {
        if ( !(getOS() || isCloudHub()) )return false;
        var hasCallback = callback && typeof callback == "function";
        var callbackId = hasCallback ? callbacksCount++ : 0;
        if (hasCallback)
            callbacks[callbackId] = callback;
        var iframe = document.createElement("IFRAME");
        iframe.setAttribute("src", _CUSTOM_PROTOCOL_SCHEME + ":" + functionName + ":" + callbackId + ":" + encodeURIComponent(JSON.stringify(message)));
        iframe.setAttribute("height", "1px");
        iframe.setAttribute("width", "1px");
        document.documentElement.appendChild(iframe);
        iframe.parentNode.removeChild(iframe);
        iframe = null;

    }
    var __XuntongJSBridge = {
        invoke: _call,
        call: _call,
        handleMessageFromXT: _handleMessageFromXT
    };
    window.XuntongJSBridge = __XuntongJSBridge;
})();