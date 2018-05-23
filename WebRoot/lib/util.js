var util = {//工具
    getQuery: function () {
        var search = window.location.search;
        return search.substr(1)
            .split('&')
            .filter(item => item.length > 0)
            .map(item => item.split('='))
            .reduce((obj, item) => {
                obj[item[0]] = item[1]
                return obj
            }, {})
    },
    setLocalData: function (key, data) {
        localStorage.setItem(key, JSON.stringify(data));
    },
    getLocalData(key) {
        var dataStr = localStorage.getItem(key);
        if (dataStr) {
            return JSON.parse(dataStr);
        }
        return null;
    },
    dateFormat: function (date) {//时间格式化
        return date.getFullYear() + "-" + repairZero(date.getMonth() + 1) + "-" + repairZero(date.getDate()) + " " + repairZero(date.getHours()) + ":" + repairZero(date.getMinutes());

        function repairZero(num) {
            if (num < 10) {
                num = "0" + num;
            }
            return num;
        }
    }
}
//提示信息
var ToastConstructor = Vue.extend({
    template: '<transition name="fade"><div class="toast" v-show="visible"><span class="toast-text">{{ message }}</span></div></transition>',
    data: function () {
        return {
            visible: false,
            message: ''
        }
    }
});
var toastPool = [];

var getAnInstance = () => {
    if (toastPool.length > 0) {
        var instance = toastPool[0];
        toastPool.splice(0, 1);
        return instance;
    }
    return (new ToastConstructor()).$mount(document.createElement('div'));
};

var returnAnInstance = function (instance) {
    if (instance) {
        toastPool.push(instance);
    }
};

var removeDom = function (event) {
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

var Toast = function (options) {
    var duration = options.duration || 1500;

    var instance = getAnInstance();
    instance.closed = false;
    clearTimeout(instance.timer);
    instance.message = typeof options === 'string' ? options : options.message;

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

XuntongJSBridge.call('setBounce', { enable: 0 });//取消ios回弹效果

var appid = util.getQuery().appid, ticket = util.getQuery().ticket;
appid && util.setLocalData('demoappId', appid);
ticket && util.setLocalData('demoticket', ticket)

var params = new URLSearchParams();//注意兼容性    
params.append('appid', util.getQuery().appid || util.getQuery().client_id || util.getLocalData('demoappId'));
params.append('ticket', util.getQuery().ticket || util.getLocalData('demoticket'));

Vue.prototype.$axios = axios.create({
    baseURL: '/j2eedemo/app/request',
    timeout: 10000,
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    params: params
})


Vue.prototype.$axios.interceptors.response.use(function (response) {
    // 返回响应时做一些处理   
    if (response.status === 200 || response.status === 304) {
        if (response.data.success) {
            return response;
        } else {
            Toast(response.data.error)
        }
    }
}, function (error) {
    Toast(error)
    return Promise.reject(error)
});


