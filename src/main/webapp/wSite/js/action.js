jQ(function(){
	jQ("body").append("<div id=\"dialog-message2\" style=\"display:none;\"><p></p></div>");
	
	if(jQ("input[hint]").size()==0) return;
	
	var p = jQ(".div_center").parent();
	var div = jQ("<DIV class='message_bar'><span id='msg'></span><DIV>");
	div.appendTo(p);
	
	jQ("input[hint]").bind({
		mouseenter : showTooltip,
		mouseleave: hideTooltip
	});
});

var showTooltip = function(event) {
	jQ("#msg").html(jQ(event.target).attr("hint"));
};

var hideTooltip = function() {
	jQ("#msg").html("");
};

function toPage(xml)
{
	if(xml==null || xml=="") return;
	
	document.myform.action = "RunAction.jsp";
	jQ("#_action").val(xml);
	document.myform.submit();
}

String.prototype.Trim = function() 
{ 
	return this.replace(/(^\s*)|(\s*$)/g, ""); 
}

function doActionSubmit(toUrl)
{
	var msg = "請確認是否儲存？"
	confirmDialog(msg, function() {
		if(toUrl)
		{
			document.myform._toUrl.value = toUrl;
		}
		document.myform.submit();
	});
}

function doNext(actionId)
{
	document.myform.toActionId.value = actionId;
	document.myform.submit();			    	
}

function doCancel()
{
	//doExit();
}

/*
function doExit()
{
	location.href="/wSite/Control?function=ReceivePage";
}
*/


function disableAllChild()
{
	jQ("input[type=text]").each(function(){
		jQ(this).attr("readonly",true);
		jQ(this).css("background-color","#DCDCDC");
	});
	jQ("input[type=radio],input[type=checkbox],select").each(function(){
		jQ(this).attr("disabled",true);
	});
	jQ("input[type=button],.ui-datepicker-trigger,.dhtmlxToolbar_dhx_blue").each(function(){
		jQ(this).hide();
	});
	jQ("img.check").each(function(){
		jQ(this).removeAttr("onclick");
	});
	jQ("input[type=file]").each(function(){
		jQ(this).attr("disabled",true);
	});
}

function enableObj(obj)
{
	obj.disabled=false;
    obj.style.backgroundColor="";
}

function disableObj(obj)
{
    obj.disabled = true;
    //if(obj.tagName)
    obj.style.backgroundColor="#c0c0c0";
}

function doOpenUrl(title)
{
	obj = event.srcElement ? event.srcElement : event.target;
	var url = "";
	if(obj.getAttribute("refurl"))
	{
		url = obj.getAttribute("refurl").Trim();
	}
	
	var win = window.open(url, title, "height=600,width=800");
}

var winObj=null;
function openWindow(url,title,width,height)
{
	winObj= window.open(url);
}

//On KeyUp Function (digit)
function check_digit(obj)
{
	obj.value=obj.value.replace(/[^\d]/g,'');
}

//On KeyUp Function (float)
function check_float(obj, size)
{
    var str = "";
    var dot = 0;

    dot = obj.value.indexOf(".") != -1 ? obj.value.indexOf(".") + (1 + size) : obj.value.length;

    for(var i = 0; i < dot; i++){
        if(obj.value.charAt(i) >= '0' && obj.value.charAt(i) <= '9' || (obj.value.charAt(i) == '.' && i < dot - 1 && str.indexOf(".") == -1)){
            str += obj.value.charAt(i)
        }
        
        if((i==0)&&(obj.value.charAt(i)=='-'))
        {
        	str += obj.value.charAt(i)
        }        
    }

    if(obj.value != str){
        obj.value = str;
    }
}

//Check Is Null
function check_isNull(obj,code)
{
	Trim(obj.value);
	if(obj.value == "")
	{	
		popupMessageAndFocus(obj,code + "欄位不應為空值，請確認！");
		
		return true;
	}
	return false;
}

function rTrim(s)
{
    var i;
	
    for(i = s.length - 1; i >= 0; i--)
    {
        if(s.charAt(i) != ' ')
        {
            break;
        }
    }
    return(s.substring(0, i + 1));
}

function lTrim(s)
{
    var i;
    for(i = 0; i < s.length; i++)
    {
        if(s.charAt(i) != ' ')
        {
            break;
        }
    }
    return(s.substring(i, s.length));
}

function Trim(s)
{
    if(s == null) return s;
    return(lTrim(rTrim(s)));
}

function toUpper()
{
    if(window.event){
       if(window.event.keyCode == 34 || window.event.keyCode == 39 || window.event.keyCode == 44 || window.event.keyCode == 92 || window.event.keyCode == 38 || window.event.keyCode == 63){
           return false;
       }
       if(window.event.keyCode >= 97 && window.event.keyCode <= 122){
            window.event.keyCode -= 32;
       }
    }

    return true;
}

function toLower()
{
    if(window.event){
       if(window.event.keyCode == 34 || window.event.keyCode == 39 || window.event.keyCode == 44 || window.event.keyCode == 92 || window.event.keyCode == 38 || window.event.keyCode == 63){
           return false;
       }
       if(window.event.keyCode >= 65 && window.event.keyCode <= 90){
            window.event.keyCode += 32;
       }
    }

    return true;
}

function IsInt(objStr,sign,zero)
{
    var reg;    
    var bolzero;    

    if(Trim(objStr)=="")
    {
        return false;
    }
    else
    {
        objStr=objStr.toString();
    }
    
    if((sign==null)||(Trim(sign)==""))
    {
        sign="+-";
    }

    if((zero==null)||(Trim(zero)==""))
    {
        bolzero=false;
    }
    else
    {
        zero=zero.toString();
        if(zero=="0")
        {
            bolzero=true;
        }
    }

    switch(sign)
	{
        case "+-":
            reg=/(^-?|^\+?)\d+$/;
            break;
        case "+":
            if(!bolzero)
            {
                reg=/^\+?[0-9]*[1-9][0-9]*$/;
            }
            else
            {
                reg=/^\+?[0-9]*[0-9][0-9]*$/;
            }
            break;
        case "-":
            if(!bolzero)
            {
                reg=/^-[0-9]*[1-9][0-9]*$/;
            }
            else
            {
                reg=/^-[0-9]*[0-9][0-9]*$/;
            }
            break;
        default:
            return false;
            break;
    }

    var r=objStr.match(reg);
	if(r==null)
    {
        return false;
    }
    else
    {        
        return true;     
    }
}

function IsFloat(objStr,sign,zero)
{
    var reg;    
    var bolzero;    

    if(Trim(objStr)=="")
    {
        return false;
    }
    else
    {
        objStr=objStr.toString();
    }    

    if((sign==null)||(Trim(sign)==""))
    {
        sign="+-";
    }

    if((zero==null)||(Trim(zero)==""))
    {
        bolzero=false;
    }
    else
    {
        zero=zero.toString();
        if(zero=="0")
        {
            bolzero=true;
        }
    }

    switch(sign)
    {
        case "+-":
            reg=/^((-?|\+?)\d+)(\.\d+)?$/;
            break;
        case "+": 
            if(!bolzero)           
            {
                reg=/^\+?(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;
            }
            else
            {
                reg=/^\+?\d+(\.\d+)?$/;
            }
            break;
        case "-":
            if(!bolzero)
            {
                reg=/^-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;
            }
            else
            {
                reg=/^((-\d+(\.\d+)?)|(0+(\.0+)?))$/;
            }            
            break;
        default:
            return false;
            break;
    }

    var r=objStr.match(reg);
    if(r==null)
    {
        return false;
    }
    else
    {        
        return true;
    }
}

function clearSelect(selectObj)
{
	if (!selectObj||!selectObj.options)
	{
		return;
	}
	
	for(var i=selectObj.options.length-1;i>=0;i--)
	{
		selectObj.options.remove(i);
	}	
}

function addSelectOption(selectObj,text,val)
{
	if (!text) text = "";
	if (!val) val = text;
	var oOption = document.createElement("OPTION");
	selectObj.options.add(oOption);
	oOption.innerText = text;
	oOption.value = val;
}

//檢查欄位長度
function check_length(obj)
{
	var min = obj.minlength == '' ? 1 : obj.minlength;
	var max = obj.maxLength;
	
	Trim(obj.value);
	if(obj.value != "" && (obj.value.length < min || obj.value.length > max))
	{
		popupMessageAndFocus(obj, "欄位長度不符");
	}
}

//檢查email
function check_email(obj)
{
	var reg = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	
	Trim(obj.value);
	if(obj.value != "" && !reg.test(obj.value))
	{
		popupMessageAndFocus(obj, "輸入格式不符，請重新輸入電子郵件 !!");
	}
}

//檢查身份證號/統一編號
function check_idno(obj)
{
	var reg;	

	Trim(obj.value);
	obj.value = obj.value.toUpperCase();
	if(obj.value.length == 8) //統一編號
	{
		reg = /^[0-9]{8}$/;
		if(obj.value != "" && !reg.test(obj.value)) 
		{
			popupMessageAndFocus(obj, "輸入格式不符，請重新輸入身份證號/統一編號 !!");
		}		
	}
	else //身份證號
	{
		reg = /^[A-Z]{1}[1-2]{1}[0-9]{8}$/;		
		if(obj.value != "" && !reg.test(obj.value)) 
		{
			popupMessageAndFocus(obj, "輸入格式不符，請重新輸入身份證號/統一編號 !!");
		}		
	}
}

function popupMessageAndFocus(obj, msg)
{
	jQ("#dialog-message2 p").text(msg);
	jQ("#dialog-message2").dialog({
		resizable: false,
		modal: true,
		title: "提示訊息",
		buttons: {
			Ok: function() {
				jQ(this).dialog("close");
				obj.focus();
			}
		}
	});
}

function popupMessage(msg, param)
{
	if(param)
	{
		msg = msg + "\n" + param;
	}
	
	jQ("#dialog-message2 p").text(msg);
	jQ("#dialog-message2").dialog({
		resizable: false,
		modal: true,
		title: "系統訊息",
		width: 400,
		buttons: {
			Ok: function() {
				jQ(this).dialog("close");
			}
		}
	});	
}

function confirmDialog(msg, func, func2)
{
	jQ("#dialog-message2 p").text(msg);
	jQ("#dialog-message2").dialog({
		resizable: false,
		modal: true,
		title: "確認視窗",
		buttons: {
			"確認": function() {
				jQ(this).dialog("close");
				func.call();
			},
			"取消": function() {
				jQ(this).dialog("close");
				
				if(func2)
				{
					func2.call();
				}
			}
		}
	});
}

function sleep( seconds ) {
	var timer = new Date();
	var time = timer.getTime();
	do
		timer = new Date();
	while( (timer.getTime() - time) < (seconds * 1000) );
}

function newServiceInput(serviceName, operationName)
{
	var si = {};
	si.url = "../ActionServiceServlet";
	si.serviceName = serviceName;
	si.operationName = operationName;
	return si;
}

function getGridInitData(gridObj)
{
	var data = new Array();
	for(i=0;i<gridObj.getColumnsNum();i++)
	{
		var v = "";
		if(gridObj.getColType(i)=="ch") v=0;
		
		data[i] = v;
	}
	return data;
}


function newCalendar(objId){
	//showButtonPanel: true
	
	jQ("#"+objId ).datepicker({
		showButtonPanel: true,
		changeMonth: true,
		changeYear: true,
		showOn: "button",
		buttonImage: "js/jquery/themes/calendar-icon.png",
		buttonImageOnly: true,
		dateFormat : "yy/mm/dd"
	});
}

function Lpad(obj,PadLength,PadChar)
{
	if(!obj) return;
	
	var PaddedString=obj.val();
	for(i=obj.val().length+1;i<=PadLength;i++)
	{
		PaddedString=PadChar+PaddedString;
	}
	obj.val(PaddedString);
}

function Rpad(obj,PadLength,PadChar)
{
	if(!obj) return;
	
	var PaddedString=obj.val();
	for(i=obj.val().length+1;i<=PadLength;i++)
	{
		PaddedString=PaddedString+PadChar;
	}
	obj.val(PaddedString);
}


