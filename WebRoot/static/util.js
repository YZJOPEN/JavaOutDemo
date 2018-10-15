'use strict';

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var util = { //工具
    getQuery: function getQuery() {
        var search = window.location.search;
        return search.substr(1).split('&').filter(function (item) {
            return item.length > 0;
        }).map(function (item) {
            return item.split('=');
        }).reduce(function (obj, item) {
            obj[item[0]] = item[1];
            return obj;
        }, {});
    },
    setLocalData: function setLocalData(key, data) {
        try {
            localStorage.setItem(key, JSON.stringify(data));
        } catch (e) {
            alert('写入缓存失败！');
        }
    },
    getLocalData: function getLocalData(key) {
        var dataStr = localStorage.getItem(key);
        if (dataStr) {
            return JSON.parse(dataStr);
        }
        return null;
    },

    dateFormat: function dateFormat(date) {
        //时间格式化        
        (typeof date === 'undefined' ? 'undefined' : _typeof(date)) == 'object' || (date = this.stringToTimestamp(date));
        return date.getFullYear() + "-" + this.repairZero(date.getMonth() + 1) + "-" + this.repairZero(date.getDate()) + " " + this.repairZero(date.getHours()) + ":" + this.repairZero(date.getMinutes());
    },
    repairZero: function repairZero(num) {
        if (num < 10) {
            num = "0" + num;
        }
        return num;
    },
    stringToTimestamp: function stringToTimestamp(dataString) {
        return new Date(dataString.replace(/-/g, "/"));
    },
    whichDay: function whichDay(date) {
        var today = new Date();
        var oneDay = 24 * 60 * 60 * 1000;
        if (date.setHours(0, 0, 0, 0) == today.setHours(0, 0, 0, 0)) {
            return 0;
        }
        if (date.setHours(0, 0, 0, 0) == today.setHours(0, 0, 0, 0) - oneDay) {
            return -1;
        }
        if (date.setHours(0, 0, 0, 0) == today.setHours(0, 0, 0, 0) + oneDay) {
            return 1;
        }
    },
    isYzjApp: function isYzjApp() {
        return navigator.userAgent.match(/Qing\/.*;(iOS|iPhone|Android).*/) ? true : false;
    }
};
//提示信息
(function () {
    var ToastConstructor = Vue.extend({
        template: '<transition name="fade"><div class="toast" v-show="visible" :class="{success : success}"><span class="toast-text">{{ success ? "操作成功" : message }}</span></div></transition>',
        data: function data() {
            return {
                visible: false,
                message: '',
                success: false
            };
        }
    });
    var toastPool = [];

    var getAnInstance = function getAnInstance() {
        if (toastPool.length > 0) {
            var instance = toastPool[0];
            toastPool.splice(0, 1);
            return instance;
        }
        return new ToastConstructor().$mount(document.createElement('div'));
    };

    var returnAnInstance = function returnAnInstance(instance) {
        if (instance) {
            toastPool.push(instance);
        }
    };

    var removeDom = function removeDom(event) {
        if (event.target.parentNode) {
            event.target.parentNode.removeChild(event.target);
        }
    };

    ToastConstructor.prototype.close = function () {
        this.visible = false;
        this.$el.addEventListener('transitionend', removeDom);
        this.closed = true;
        returnAnInstance(this);
    };

    var Toast = function Toast(options) {
        var duration = options.duration || 1500;

        var instance = getAnInstance();
        instance.closed = false;
        clearTimeout(instance.timer);
        instance.message = typeof options === 'string' ? options : options.message;
        instance.success = typeof options === 'string' ? false : options.success;
        document.body.appendChild(instance.$el);
        Vue.nextTick(function () {
            instance.visible = true;
            instance.$el.removeEventListener('transitionend', removeDom);
            ~duration && (instance.timer = setTimeout(function () {
                if (instance.closed) return;
                instance.close();
            }, duration));
        });
        return instance;
    };
    Vue.prototype.$toast = Toast;
})();

//加载动画
(function () {

    var LoadingConstructor = Vue.extend({
        template: '<transition name="fade"><div class="loadWrap" v-show="visible"><div class="loading" ><span class="loading-text"></span></div></div></transition>',
        data: function data() {
            return {
                visible: false,
                message: '加载中...'
            };
        }
    });

    var defaults = {
        text: null
    };

    LoadingConstructor.prototype.close = function () {
        this.visible = false;
        if (this.$el && this.$el.parentNode) {
            this.$el.parentNode.removeChild(this.$el);
        }
        this.$destroy();
    };

    var Loading = function Loading(options) {
        options = defaults;
        var instance = new LoadingConstructor({
            el: document.createElement('div'),
            data: options
        });
        document.body.appendChild(instance.$el);
        Vue.nextTick(function () {
            instance.visible = true;
        });
        return instance;
    };
    Vue.prototype.$loading = Loading;
})();

XuntongJSBridge.call('setBounce', { enable: 0 }); //取消ios回弹效果

var appid = util.getQuery().appid,
    ticket = util.getQuery().ticket;
appid && util.setLocalData('demoappId', appid);
ticket && util.setLocalData('demoticket', ticket);

var params = new URLSearchParams(); //注意兼容性    
params.append('appid', util.getQuery().appid || util.getQuery().client_id || util.getLocalData('demoappId'));
params.append('ticket', util.getQuery().ticket || util.getLocalData('demoticket'));

Vue.prototype.$axios = axios.create({
    baseURL: '/j2eedemo/app/request',
    // baseURL: '/static/mock/data.json',
    timeout: 10000,
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    params: params
});

var load = null;

Vue.prototype.$axios.interceptors.request.use(function (config) {
    load = Vue.prototype.$loading('加载中...');
    return config;
}, function (error) {
    Vue.prototype.$toast(error);
    return Promise.reject(error);
});

Vue.prototype.$axios.interceptors.response.use(function (response) {
    // 返回响应时做一些处理
    load.close();
    if (response.status === 200 || response.status === 304) {
        if (response.data.success) {
            return response;
        } else {
            Vue.prototype.$toast(response.data.error);
        }
    }
}, function (error) {
    load.close();
    Vue.prototype.$toast(error);
    return Promise.reject(error);
});