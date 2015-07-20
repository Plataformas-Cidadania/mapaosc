$(document).ready(function() {
  $(".inc-font").click(
    function () {
      var size = $("body").css('font-size');
       
      size = size.replace('px', '');
      size = Number(size) + 0.5;
      if (size <= 14.5) {
        $("body").animate({'font-size' : size + 'px'});
      }
    }
  );
  $(".dec-font").click(
    function () {
      var size = $("body").css('font-size');
       
      size = size.replace('px', '');
      size = Number(size) - 0.5;
      if (size >= 12) {
        $("body").animate({'font-size' : size + 'px'});
      }
  });
  $(".res-font").click(function () {   
    $("body").animate({'font-size' : '13px'});
  });
});

var manual_or_random="manual"
var randomsetting="5 days"
function getCookie(Name){var re=new RegExp(Name+"=[^;]+","i");if(document.cookie.match(re))return document.cookie.match(re)[0].split("=")[1]
return null}function setCookie(name,value,days){var expireDate=new Date()
var expstring=(typeof days!="undefined")?expireDate.setDate(expireDate.getDate()+parseInt(days)):expireDate.setDate(expireDate.getDate()-5)
document.cookie=name+"="+value+"; expires="+expireDate.toGMTString()+"; path=/";}
function deleteCookie(name){setCookie(name,"moot")}function setStylesheet(title,randomize){var i,cacheobj,altsheets=[""]
for(i=0;(cacheobj=document.getElementsByTagName("link")[i]);i++){if(cacheobj.getAttribute("rel").toLowerCase()=="alternate stylesheet"&&cacheobj.getAttribute("title")){cacheobj.disabled=true
altsheets.push(cacheobj)
if(cacheobj.getAttribute("title")==title)
cacheobj.disabled=false}}if(typeof randomize!="undefined"){var randomnumber=Math.floor(Math.random()*altsheets.length)
altsheets[randomnumber].disabled=false}return(typeof randomize!="undefined"&&altsheets[randomnumber]!="")?altsheets[randomnumber].getAttribute("title"):""}
function chooseStyle(styletitle,days){if(document.getElementById){setStylesheet(styletitle)
setCookie("mysheet",styletitle,days)}}function indicateSelected(element){if(selectedtitle!=null&&(element.type==undefined||element.type=="select-one")){var element=(element.type=="select-one")?element.options:element
for(var i=0;i<element.length;i++){if(element[i].value==selectedtitle){if(element[i].tagName=="OPTION")element[i].selected=true
else element[i].checked=true
break}}}}if(manual_or_random=="manual"){var selectedtitle=getCookie("mysheet")
if(document.getElementById&&selectedtitle!=null)setStylesheet(selectedtitle)}else if(manual_or_random=="random"){if(randomsetting=="eachtime")setStylesheet("","random")
else if(randomsetting=="sessiononly"){if(getCookie("mysheet_s")==null)document.cookie="mysheet_s="+setStylesheet("","random")+"; path=/"
else setStylesheet(getCookie("mysheet_s"))}else if(randomsetting.search(/^[1-9]+ days/i)!=-1){if(getCookie("mysheet_r")==null||parseInt(getCookie("mysheet_r_days"))!=parseInt(randomsetting)){setCookie("mysheet_r",setStylesheet("","random"),parseInt(randomsetting))
setCookie("mysheet_r_days",randomsetting,parseInt(randomsetting))}else setStylesheet(getCookie("mysheet_r"))}}

function dropdown(mySel)
{
var myWin, myVal;
myVal = mySel.options[mySel.selectedIndex].value;
if(myVal)
   {
   if(mySel.form.target)myWin = parent[mySel.form.target];
   else myWin = window;
   if (! myWin) return true;
   myWin.location = myVal;
   }
return false;
}

