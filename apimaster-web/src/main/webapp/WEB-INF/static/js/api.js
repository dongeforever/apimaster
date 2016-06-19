/**
 * Created by liuzhendong on 16/6/19.
 */













//时间绑定
$(document).ready(function(){
    $("h2").click(function(){
        alert("test");
    });
    var data = {
        "foobar": "foobaz"
    };
    $('#json-renderer').jsonViewer(data,{collapsed: false, withQuotes: true});
});


