//v.2.1 build 90226

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/

dhtmlXGridObject.prototype.loadCSVFile = function(path,afterCall){this.load(path,afterCall,"csv")
};dhtmlXGridObject.prototype.enableCSVAutoID = function(mode){this._csvAID=convertStringToBoolean(mode)};dhtmlXGridObject.prototype.enableCSVHeader = function(mode){this._csvHdr=convertStringToBoolean(mode)};dhtmlXGridObject.prototype.setCSVDelimiter = function(str){this.csv.cell=str};dhtmlXGridObject.prototype.loadCSVString = function(str){this.parse(str,"csv")
};dhtmlXGridObject.prototype.serializeToCSV = function(textmode){this.editStop()
 if (this._mathSerialization)this._agetm="getMathValue";else if (this._strictText || textmode)this._agetm="getTitle";else this._agetm="getValue";var out=[];if (this._csvHdr){var a=[];var b=this.hdr.rows[1].cells;for (var i=0;i<b.length;i++)if ((!this._srClmn)||(this._srClmn[i]))
 a.push(_isIE?b[i].innerText:b[i].textContent);out.push(a.join(this.csv.cell))};var i=0;var leni=this.rowsBuffer.length;for(i;i<leni;i++){var temp=this._serializeRowToCVS(null,i) 
 if (temp!="")out.push(temp)};return out.join(this.csv.row)};dhtmlXGridObject.prototype._serializeRowToCVS = function(r,i,start,end){var out = new Array();if (!r){r=this.render_row(i)
 if (this._fake && !this._fake.rowsAr[r.idd])this._fake.render_row(i)};if (!this._csvAID)out[out.length]=r.idd;start = start||0;end = end||this._cCount;var changeFl=false;var ind=start;while (r.childNodes[start]._cellIndex>ind && start)start--;for(var jj=start;ind<end;jj++){if (!r.childNodes[jj])break;var real_ind=r.childNodes[jj]._cellIndex;if ((!this._srClmn)||(this._srClmn[real_ind])){var cvx=r.childNodes[jj];var zx=this.cells(r.idd,real_ind);while (ind!=real_ind){ind++;out.push("")
 if (ind>=end)break};if (ind>=end)break;ind++;if (zx.cell)zxVal=zx[this._agetm]();else zxVal="";if ((this._chAttr)&&(zx.wasChanged()))
 changeFl=true;out[out.length]=((zxVal===null)?"":zxVal)

 if ( this._ecspn && cvx.colSpan && cvx.colSpan >1 ){cvx=cvx.colSpan-1;for (var u=0;u<cvx;u++)out[out.length] = "";ind++}}else ind++};if ((this._onlChAttr)&&(!changeFl)) return "";return out.join(this.csv.cell)};dhtmlXGridObject.prototype.toClipBoard=function(val){if (window.clipboardData)window.clipboardData.setData("Text",val);else
 (new Clipboard()).copy(val)};dhtmlXGridObject.prototype.fromClipBoard=function(){if (window.clipboardData)return window.clipboardData.getData("Text");else
 return (new Clipboard()).paste()};dhtmlXGridObject.prototype.cellToClipboard = function(rowId,cellInd){if ((!rowId)||(!cellInd)){if (!this.selectedRows[0])return;rowId=this.selectedRows[0].idd;cellInd=this.cell._cellIndex};var ed=this.cells(rowId,cellInd);this.toClipBoard(ed.getLabel?ed.getLabel():ed.getValue())};dhtmlXGridObject.prototype.updateCellFromClipboard = function(rowId,cellInd){if ((!rowId)||(!cellInd)){if (!this.selectedRows[0])return;rowId=this.selectedRows[0].idd;cellInd=this.cell._cellIndex};var ed=this.cells(rowId,cellInd);ed[ed.setImage?"setLabel":"setValue"](this.fromClipBoard())};dhtmlXGridObject.prototype.rowToClipboard = function(rowId){var out="";if (this._mathSerialization)this._agetm="getMathValue";else if (this._strictText)this._agetm="getTitle";else this._agetm="getValue";if (rowId)out=this._serializeRowToCVS(this.getRowById(rowId));else
 for (var i=0;i<this.selectedRows.length;i++){if (out)out+=this.csv.row;out+=this._serializeRowToCVS(this.selectedRows[i])};this.toClipBoard(out)};dhtmlXGridObject.prototype.updateRowFromClipboard = function(rowId){var csv=this.fromClipBoard();if (!csv)return;if (rowId)var r=this.getRowById(rowId);else
 var r=this.selectedRows[0];if (!r)return;csv=(csv.split(this.csv.row)[0]).split(this.csv.cell);if (!this._csvAID)csv.splice(0,1);for (var i=0;i<csv.length;i++){var ed=this.cells3(r,i);ed[ed.setImage?"setLabel":"setValue"](csv[i])}};dhtmlXGridObject.prototype.addRowFromClipboard = function(){var csv=this.fromClipBoard();if (!csv)return;var z=csv.split(this.csv.row);for (var i=0;i<z.length;i++)if (z[i]){csv=z[i].split(this.csv.cell);if (this._csvAID)this.addRow(this.getRowsNum()+2,csv);else{if (this.rowsAr[csv[0]])csv[0]=this.uid();this.addRow(csv[0],csv.slice(1))}}};dhtmlXGridObject.prototype.gridToClipboard = function(){this.toClipBoard(this.serializeToCSV())};dhtmlXGridObject.prototype.gridFromClipboard = function(){var csv=this.fromClipBoard();if (!csv)return;this.loadCSVString(csv)};dhtmlXGridObject.prototype.getXLS = function(path){if (!this.xslform){this.xslform=document.createElement("FORM");this.xslform.action=(path||"")+"xls.php";this.xslform.method="post";this.xslform.target=(_isIE?"_blank":"");document.body.appendChild(this.xslform);var i1=document.createElement("INPUT");i1.type="hidden";i1.name="csv";this.xslform.appendChild(i1);var i2=document.createElement("INPUT");i2.type="hidden";i2.name="csv_header";this.xslform.appendChild(i2)};var cvs = this.serializeToCSV();this.xslform.childNodes[0].value = cvs;var cvs_header = [];var l = this._cCount;for (var i=0;i<l;i++){cvs_header.push(this.getHeaderCol(i))};cvs_header = cvs_header.join(',');this.xslform.childNodes[1].value = cvs_header;this.xslform.submit()};dhtmlXGridObject.prototype.printView = function(before,after){var html="<style>TD {font-family:Arial;text-align:center};</style>";var st_hr=null;if (this._fake){st_hr=this._hrrar;for (var i=0;i<this._fake._cCount;i++)this._hrrar[i]=null};html+="<base href='"+document.location.href+"'></base>";if (!this.parentGrid)html+=(before||"");html += '<table width="100%" border="2px" cellpadding="0" cellspacing="0">';var row_length = Math.max(this.rowsBuffer.length,this.rowsCol.length);var col_length = this._cCount;var width = this._printWidth();html += '<tr>';for (var i=0;i<col_length;i++){if (this._hrrar && this._hrrar[i])continue;var hcell=this.hdr.rows[1].cells[this.hdr.rows[1]._childIndexes?this.hdr.rows[1]._childIndexes[parseInt(i)]:i];var colspan=(hcell.colSpan||1);var rowspan=(hcell.rowSpan||1);for (var j=1;j<colspan;j++)width[i]+=width[j];html += '<td rowspan="'+rowspan+'" width="'+width[i]+'%" style="padding-left:2px;padding-right:2px;background-color:lightgrey;" colspan="'+colspan+'">'+this.getHeaderCol(i)+'</td>';i+=colspan-1};html += '</tr>';for (var i=2;i<this.hdr.rows.length;i++){if (_isIE){html+="<tr style='background-color:lightgrey'>";var cells=this.hdr.rows[i].childNodes;for (var j=0;j < cells.length;j++)if (!this._hrrar || !this._hrrar[cells[j]._cellIndex]){html+=cells[j].outerHTML};html+="</tr>"}else
 html+="<tr style='background-color:lightgrey'>"+(this._fake?this._fake.hdr.rows[i].innerHTML:"")+this.hdr.rows[i].innerHTML+"</tr>"};for (var i=0;i<row_length;i++){html += '<tr>';if (this.rowsCol[i] && this.rowsCol[i]._cntr){html+=this.rowsCol[i].innerHTML.replace(/<img[^>]*>/gi,"")+'</tr>';continue};if (this.rowsCol[i] && this.rowsCol[i].style.display=="none")continue;var row_id
 if (this.rowsCol[i])row_id=this.rowsCol[i].idd;else if (this.rowsBuffer[i])row_id=this.rowsBuffer[i].idd;else continue;for (var j=0;j<col_length;j++){if (this._hrrar && this._hrrar[j])continue;if(this.rowsAr[row_id] && this.rowsAr[row_id].tagName=="TR"){var c=this.cells(row_id, j);if (c._setState)var value="";else if (c.getContent)value = c.getContent();else if (c.getImage || c.combo)var value=c.cell.innerHTML;else var value = c.getValue()}else 
 var value=this._get_cell_value(this.rowsBuffer[i],j);var color = this.columnColor[j]?'background-color:'+this.columnColor[j]+';':'';var align = this.cellAlign[j]?'text-align:'+this.cellAlign[j]+';':'';var cspan = c.getAttribute("colspan");html += '<td style="padding-left:2px;padding-right:2px;'+color+align+'" '+(cspan?'colSpan="'+cspan+'"':'')+'>'+(value===""?"&nbsp;":value)+'</td>';if (cspan)j+=cspan-1};html += '</tr>';if (this.rowsCol[i] && this.rowsCol[i]._expanded){var sub=this.cells4(this.rowsCol[i]._expanded.ctrl);if (sub.getSubGrid)html += '<tr><td colspan="'+col_length+'">'+sub.getSubGrid().printView()+'</td></tr>';else
 html += '<tr><td colspan="'+col_length+'">'+this.rowsCol[i]._expanded.innerHTML+'</td></tr>'}};if (this.ftr)for (var i=1;i<this.ftr.childNodes[0].rows.length;i++)html+="<tr style='background-color:lightgrey'>"+((this._fake && _isFF)?this._fake.ftr.childNodes[0].rows[i].innerHTML:"")+this.ftr.childNodes[0].rows[i].innerHTML+"</tr>";html += '</table>';if (this.parentGrid)return html;html+=(after||"");var d = window.open('', '_blank');d.document.write(html);d.document.write("<script>window.onerror=function(){return true}</script>");d.document.close();if (this._fake){this._hrrar=st_hr}};dhtmlXGridObject.prototype._printWidth=function(){var width = [];var total_width = 0;for (var i=0;i<this._cCount;i++){var w = this.getColWidth(i);width.push(w);total_width += w};var percent_width = [];var total_percent_width = 0;for (var i=0;i<width.length;i++){var p = Math.floor((width[i]/total_width)*100);total_percent_width += p;percent_width.push(p)};percent_width[percent_width.length-1] += 100-total_percent_width;return percent_width};dhtmlXGridObject.prototype.loadObject = function(obj){};dhtmlXGridObject.prototype.loadJSONFile = function(path){};dhtmlXGridObject.prototype.serializeToObject = function(){};dhtmlXGridObject.prototype.serializeToJSON = function(){};if (!window.clipboardData)window.clipboardData={_make:function(){var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);if (!clip)return null;var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);if (!trans)return null;trans.addDataFlavor('text/unicode');var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);this._p=[clip,trans,str];return true},
 setData:function(type,text){netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');if (!this._make()) return false;this._p[2].data=text;this._p[1].setTransferData("text/unicode",this._p[2],text.length*2);var clipid=Components.interfaces.nsIClipboard;this._p[0].setData(this._p[1],null,clipid.kGlobalClipboard)},
 getData:function(type){netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');if (!this._make()) return false;this._p[0].getData(this._p[1],this._p[0].kGlobalClipboard);var strLength = new Object();var str = new Object();this._p[1].getTransferData("text/unicode",str,strLength);if (str)str = str.value.QueryInterface(Components.interfaces.nsISupportsString);if (str)return str.data.substring(0,strLength.value / 2);return ""}};
//v.2.1 build 90226

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/