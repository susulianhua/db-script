Ext.define('Ext.tree.Tree', {
    extend: 'Ext.tree.Panel',
    title: '数据库',
    width : 400,
    height : 450,
    queryModel : 'local',
    rootVisible : false,
    frame: true,
    listeners : {
        'itemcontextmenu': function ( view, record ,item, index, e) {
            var me = this;
            e.preventDefault();  //屏蔽默认右键菜单
            var leafMenu = new Ext.menu.Menu({
                items: [{
                    text: '编辑',
                    handler: Ext.bind(function(){
                        var fileName;
                        if(record.parentNode.data.text == '表') {
                            fileName = record.parentNode.parentNode.data.text;
                            fileName = fileName.substring(3, fileName.length - 1) + '.table.xml';
                            var panel = Ext.create('Ext.table.component.TablePanel', {
                                FileName: fileName,
                                tableName: record.data.text
                            });
                        }
                        else if(record.data.text == '标准字段'){
                            fileName = record.parentNode.data.text;
                            var moduleName = fileName.substring(3, fileName.length - 1);
                            var panel = Ext.create('Ext.standardField.component.StandardFieldPanel',{
                                moduleName: moduleName
                            })
                        }
                        else if(record.data.text == '业务类型'){
                            fileName = record.parentNode.data.text;
                            var moduleName = fileName.substring(3, fileName.length - 1);
                            var panel = Ext.create('Ext.businessType.component.BusinessTypePanel',{
                                moduleName: moduleName
                            })
                        }
                        var win = Ext.create("Ext.window.Window", {
                            draggable: true,
                            height: 500,                          //高度
                            width: 650,                           //宽度
                            layout: "fit",                        //窗口布局类型
                            modal: true, //是否模态窗口，默认为false
                            resizable: false,
                            constrainsTo: Ext.getDoc(),
                            items: [panel],
                            buttonAlign: 'center',
                            buttons: [
                                {
                                    text: '保存',
                                    handler: function () {
                                        panel.panelSave()
                                    }
                                },
                                {
                                    text: '关闭',
                                    handler: function () {
                                        win.close();
                                    }
                                }
                            ]
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
                                text: '业务类型',
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            },
                            {
                                text: "标准字段",
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            },
                            {
                                text: '视图',
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            },
                            {
                                text: '触发器',
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            },
                            {
                                text: '序列',
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            },
                            {
                                text: '存储过程',
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            }]
                    }
                }, {
                    text: '删除',
                    handler: Ext.bind( me.deleteModule,this, [record])
                }]
            });
            if(record.data.parentId == "root") moduleMenu.showAt(e.getXY());
            if(record.data.text == "表") tableMenu.showAt(e.getXY());
            if(record.get('leaf') == true) leafMenu.showAt(e.getXY());

        }
    },
    initComponent: function(){
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },
    createDockedItems: function(){
        var me = this;
        return [
            {
                xtype:'toolbar',
                dock: 'top',
                items: [
                    { xtype: 'textfield', filedLabel: '模块名', name: 'moduleName', labelWidth: 60, allowBlank: false,
                      id: 'inputModuleName'},
                    {
                        xtype:'button',
                        id: 'btn_tableFieldAdd',
                        handler: function(){
                            me.addModule();
                        },
                        border: '2px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    onMenuItem: function(item,event, record) {
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
},
    deleteModule: function(record) {
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
},

    addModule: function () {
        var me = this;
        var record = Ext.getCmp('inputModuleName').getValue();
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript/tree/addModule',
            type: 'post',
            params: {
                moduleName: record
            },
            success: function () {
                var root = me.getRootNode();
                var newNode = [{ text: '模块(' + record + ')', leaf: false, children: {
                    text: '表', leaf: false
                    }}];
                root.appendChild(newNode);
            }
        })

    }
})