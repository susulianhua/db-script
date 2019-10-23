Ext.Loader.setConfig({ enabled: true,});
Ext.Loader.setPath({
    'Ext':'Ext'
});

Ext.onReady(function () {
    Ext.Ajax.request({
        url: 'http://localhost:8080/dbscript//tree/getModule',
        params: {
            id: 1
        },
        success: function(response){
            var responseText = response.responseText;
            var data =  JSON.parse(responseText);
            var treeStore = Ext.create('Ext.data.TreeStore',{
                id: 'treeStore',
                root:{
                    children:data.data
                }
            });
            var tree = Ext.create('Ext.tree.Panel', {
                title : 'Simple Tree',
                width : 400,
                height : 550,
                queryModel : 'local',
                id : 'treePanel',
                store : treeStore,
                renderTo: 'tree-panel',
                rootVisible : false,
                listeners : {
                    'itemcontextmenu': function ( view, record ,item, index, e) {
                        e.preventDefault();  //屏蔽默认右键菜单
                        var leafMenu = new Ext.menu.Menu({
                            items: [{
                                text: '编辑',
                                handler: Ext.bind(function(){
                                    var fileName = record.parentNode.parentNode.data.text;
                                    fileName = fileName.substring(3,fileName.length - 1) + '.table.xml';
                                    localStorage.setItem('obj',
                                        JSON.stringify(({'tableName': record.data.text, 'FileName': fileName})));
                                    console.log("fileName", fileName);
                                    var tablePanel = Ext.create('Ext.table.component.TablePanel', {
                                        FileName: fileName,
                                        tableName: record.data.text
                                    });
                                    var win = Ext.create("Ext.window.Window", {
                                        draggable: true,
                                        height: 480,                          //高度
                                        width: 650,                           //宽度
                                        layout: "fit",                        //窗口布局类型
                                        modal: true, //是否模态窗口，默认为false
                                        resizable: false,
                                        items: [tablePanel]
                                    });
                                    win.show();
                                },this)
                            }, {
                                text: '删除',
                                handler: Ext.bind(function(){
                                    if(record.parentNode.data.text == '表'){
                                        var fileName = record.parentNode.parentNode.data.text;
                                        fileName = fileName.substring(3,fileName.length - 1);
                                        Ext.Ajax.request({
                                            url: 'http://localhost:8080/dbscript/tree/deleteTable',
                                            params:{
                                                tableName: record.data.text,
                                                FileName: fileName
                                            },
                                            success: function () {
                                                var pnode = record.parentNode;
                                                pnode.removeChild(record);
                                                pnode.expand();
                                                Ext.Msg.alert("成功", "成功删除" + record.data.text)
                                            },
                                            failure: function () {
                                                Ext.Msg.alert("失败", "删除失败");
                                            }
                                        })
                                    }
                                    else{
                                        var fileName = record.parentNode.data.text;
                                        var curText = record.data.text;
                                        console.log('curText',curText);
                                        fileName = fileName.substring(3, fileName.length - 1);
                                        Ext.Ajax.request({
                                            url: 'http://localhost:8080/dbscript//tree/deleteOther',
                                            params:{
                                                curText: curText,
                                                FileName: fileName
                                            },
                                            success: function () {
                                                var pNode = record.parentNode;
                                                pNode.removeChild(record);
                                                pNode.expand();
                                                Ext.Msg.alert("成功", "成功删除" + curText);
                                            },
                                            failure: function () {
                                                Ext.Msg.alert("失败", "删除失败");
                                            }
                                        })
                                    }
                                },this,record)
                            }]
                        });
                        var tableMenu = new Ext.menu.Menu({
                            items: [{
                                text: '新增',
                                handler: Ext.bind(function(){
                                    Ext.MessageBox.prompt("请输入表格名称","",function (e,text) {
                                        if(e == "ok"){
                                            Ext.Ajax.request({
                                                url: "http://localhost:8080/dbscript/tree/addtable",
                                                params: {
                                                    tableName: text,
                                                    moduleName: record.parentNode.data.text,
                                                },
                                                success: function () {
                                                    var pNode = treeStore.getNodeById(record.data.id)
                                                    var newNode = [{text: text,leaf: true}];
                                                    pNode.appendChild(newNode);
                                                    pNode.expand();
                                                },
                                                failure: function () {
                                                    Ext.Msg.alert("添加失败")
                                                }

                                            })
                                        }
                                    })
                                },this, record)
                            }, {
                                text: '删除',
                                handler: Ext.bind(function(){
                                    Ext.Msg.alert('失败',record.data.text + '无法刪除')
                                },this, record)
                            }]
                        });
                        var moduleMenu = new Ext.menu.Menu({
                            allowOtherMenus: true,
                            items: [{
                                text: '新增',
                                menu: {
                                    items: [
                                        {
                                            text: "标准字段",
                                            handler: Ext.Function.bind(onMenuItem,null,[record],true)
                                        },
                                        {
                                            text: '视图',
                                            handler: Ext.Function.bind(onMenuItem,null,[record],true)
                                        },
                                        {
                                            text: '触发器',
                                            handler: Ext.Function.bind(onMenuItem,null,[record],true)
                                        },
                                        {
                                            text: '序列',
                                            handler: Ext.Function.bind(onMenuItem,null,[record],true)
                                        },
                                        {
                                            text: '存储过程',
                                            handler: Ext.Function.bind(onMenuItem,null,[record],true)
                                        }]
                                }
                            }, {
                                text: '删除',
                                handler: Ext.bind( deleteModule,this, [record])
                            }]
                        });
                        if(record.data.parentId == "root") moduleMenu.showAt(e.getXY());
                        if(record.data.text == "表") tableMenu.showAt(e.getXY());
                        if(record.get('leaf') == true) leafMenu.showAt(e.getXY());

                    }
                }
            });
            // process server response here
        }
    });

    function onMenuItem(item,event, record) {
        console.log("record:", record)
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript/tree/addOther',
            params:{
                otherName: item.text,
                moduleName: record.data.text
            },
            success: function () {
                var newNode = [{text: item.text,leaf: true}];
                record.appendChild(newNode);
                record.expand();
            },
            failure: function () {
                Ext.Msg.alert('失败', '新增失败')
            }
        })
    }
    function deleteModule(record) {
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript/tree/deleteModule',
            params:{
                moduleName: record.data.text
            },
            success: function () {
                var pNode = record.parentNode;
                pNode.removeChild(record);
                pNode.expand();
                Ext.Msg.alert('成功', '成功删除' + record.data.text)
            },
            failure: function () {
                Ext.Msg.alert('失败', "删除失败")
            }
        })
    }
})