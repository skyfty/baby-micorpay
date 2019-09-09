
$(function(){
    $(".startActivity").click(function(){
        var activityName = $(this).attr("activity");
        if (activityName) {
            cordova.exec(null, null, "BabyPlugin", "start_activity", [ activityName ]);
        }
    });

    $(".startWebActivity").click(function(){
        var url = $(this).attr("url");
        if (url) {
            cordova.exec(null, null, "BabyPlugin", "start_web_activity", [ url ]);
        }
    });

    $(".startAssetWebActivity").click(function(){
        var url = $(this).attr("url");
        if (url) {
            cordova.exec(null, null, "BabyPlugin", "start_asset_activity", [ url ]);
        }
    });
});

function getAppVersionName(callback) {
    cordova.exec(callback, null, "BabyPlugin", "get_app_version_name", []);
}

function getLoginInfo(callback) {
    cordova.exec(callback, null, "BabyPlugin", "get_login_info", []);
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

function showProgressDialog(msg) {
    cordova.exec(null, null, "BabyPlugin", "show_process_dialog", [msg]);
}

function hideProgressDialog() {
    cordova.exec(null, null, "BabyPlugin", "hide_process_dialog", []);
}

function showToash(msg) {
    cordova.exec(null, null, "BabyPlugin", "show_toash", [msg?msg:""]);
}


function finish_activity(result) {
    cordova.exec(null, null, "BabyPlugin", "finish_activity", [result?result:"ok"]);
}
//var P_AYIHUI_CN = "http://10.0.4.135:8012";
//var P_AYIHUI_CN = "http://192.168.1.100:8012";
var P_AYIHUI_CN = "http://a.ayihui.cn:80";

function U(m,a) {
    return P_AYIHUI_CN + "/?" + "a=" + a + "&m=" + m;
}

function request_customer(id, cb) {
    var url = U("PyIndex","getinfo");
    $.ajax({
        'type':'get',
        'dataType':'json',
        'url':url,
        'data':{
            'model':"customer",
            'model_id':id,
            'login_role_id':getQueryString("login_role_id"),
        },
        'success':function(data){
            cb(data);
        },
        'error':function(xhr,status,error) {
            cb({"code":-1,"error":status});
        }
    });
}

function select_customer(cb, param) {
    var rp = param?[param]:[];
    cordova.exec(function(id){
        request_customer(id, cb);
    }, function(err){
        cb({"code":-1,"error":err});
    }, "BabyPlugin", "select_customer", rp);
}

function request_product(id, cb) {
    var url = U("PyIndex","getproduct");
    $.ajax({
        'type':'get',
        'dataType':'json',
        'url':url,
        'data':{
            'product_id':id,
            'login_role_id':getQueryString("login_role_id"),
        },
        'success':function(data){
            cb(data);
        },
        'error':function(xhr,status,error) {
            cb({"code":-1,"error":status});
        }
    });
}

function select_product(cb, param) {
    var rp = param?[JSON.stringify(param)]:[];
    cordova.exec(function(id){
        request_product(id, cb);
    }, function(err){
        cb({"code":-1,"error":err});
    }, "BabyPlugin", "select_product", rp);
}