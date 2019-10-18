Ext.onReady(function () {

    Ext.Ajax.request({
        url: 'http://localhost:8080/dbscript//tree/module',
        params: {
            id: 1
        },
        success: function(response){
            var text = response.responseText;
            var data =  JSON.parse(text);
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
                    'itemcontextmenu': function ( view, record , item, index, e) {
                        e.preventDefault();  //屏蔽默认右键菜单
                        var leafMenu = new Ext.menu.Menu({
                            items: [{
                                text: '编辑',
                                handler: Ext.bind(function(){
                                    var fileName = record.parentNode.parentNode.data.text;
                                    fileName = fileName.substring(3,fileName.length - 1);
                                    localStorage.setItem('obj',
                                        JSON.stringify(({'tableName': record.data.text, 'FileName': fileName})));
                                    document.location.href="table.html"
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
                                            }
                                        })
                                    }
                                    else{
                                        console.log('这不是表')
                                    }
                                    Ext.Msg.alert('成功刪除','成功刪除' + record.data.text);
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
                                                    var pnode = treeStore.getNodeById(record.data.id)
                                                    var newnode = [{text: text,leaf: true}];
                                                    pnode.appendChild(newnode);
                                                    pnode.expand();
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
                        var moduleRightMenu = new Ext.menu.Menu({
                            items: [{
                                text: '表',
                                handler: Ext.bind(function () {

                                },this)
                            },{
                                text: '标准字段',
                                handler: Ext.bind(function () {

                                },this)
                            },{
                                text: '视图',
                                handler: Ext.bind(function () {

                                },this)
                            },{
                                text: '触发器',
                                handler: Ext.bind(function () {

                                },this)
                            },{
                                text: '序列',
                                handler: Ext.bind(function () {

                                },this)
                            },{
                                text: '存储过程',
                                handler: Ext.bind(function () {

                                },this)
                            }]
                        });
                        var moduleMenu = new Ext.menu.Menu({
                            items: [{
                                text: '新增',
                                handler: Ext.bind(function(){
                                    moduleRightMenu.showAt(e.getX()+ 100,e.getY())
                                },this)
                            }, {
                                text: '删除',
                                handler: Ext.bind(function(){
                                    Ext.Msg.alert('成功刪除','成功刪除' + record.data.text)
                                },this)
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





})