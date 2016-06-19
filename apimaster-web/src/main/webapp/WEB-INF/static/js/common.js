//iframe自适应高度
function iFrameHeight(id) {
    var ifm= document.getElementById(id);
    var subWeb = document.frames ? document.frames[id].document : ifm.contentDocument;
    if(ifm != null && subWeb != null) {
        ifm.height = subWeb.body.scrollHeight + 40;
        ifm.width = subWeb.body.scrollWidth;
    }
}