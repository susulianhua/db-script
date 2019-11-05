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
                        me.editMetadata(record);
                    },this)
                }, {
                    text: '删除',
                    handler: Ext.bind(function(){
                        me.deleteLeaf(record);
                    },this,record)
                }
                ]
            });
            var standardTypeMenu = Ext.create('Ext.menu.Menu', {
                items: [
                    { text: '查看', handler: Ext.bind(function () {
                            var panel = Ext.create('Ext.standardType.component.StandardTypePanel', {
                                standardTypeId: record.data.text
                            });
                            var win = Ext.create("Ext.tree.window.WindowWithNoSave", {
                                panel: panel
                            });
                            win.show();
                        }, this)}
                ]
            })
            var tableMenu = new Ext.menu.Menu({
                items: [{
                    text: '新增',
                    handler: Ext.bind(function () {
                        var upperCase = 'Table';
                        me.addMetaData(record, upperCase)
                    },this, record)
                }, {
                    text: '删除',
                    handler: Ext.bind(function(){
                        var fileName = 'TableFile'
                        Ext.MessageBox.confirm('提示','是否确认删除该' + record.data.text, function (btn) {
                            if(btn == 'yes'){
                                me.deleteMetadataFile(record, fileName);
                            }
                        })
                    },this, record)
                }]
            });
            var procedureMenu = new Ext.menu.Menu({
                items: [{
                    text: '新增',
                    handler: Ext.bind(function () {
                        var upperCase = 'Procedure';
                        me.addMetaData(record, upperCase)
                    },this, record)
                }, {
                    text: '删除',
                    handler: Ext.bind(function(){
                        Ext.Msg.alert('失败',record.data.text + '无法刪除')
                    },this, record)
                }]
            });
            var viewMenu = new Ext.menu.Menu({
                items: [{
                    text: '新增',
                    handler: Ext.bind(function(){
                        var upperCase = 'View';
                        me.addMetaData(record, upperCase);
                    },this, record)
                }, {
                    text: '删除',
                    handler: Ext.bind(function(){
                        Ext.Msg.alert('失败',record.data.text + '无法刪除')
                    },this, record)
                }]
            });
            var triggerMenu = new Ext.menu.Menu({
                items: [{
                    text: '新增',
                    handler: Ext.bind(function(){
                        var upperCase = 'Trigger';
                        me.addMetaData(record, upperCase);
                    },this, record)
                }, {
                    text: '删除',
                    handler: Ext.bind(function(){
                        Ext.Msg.alert('失败',record.data.text + '无法刪除')
                    },this, record)
                }]
            });
            var sequenceMenu = Ext.create('Ext.menu.Menu', {
                items: [{
                    text: '新增',
                    handler: Ext.bind(function(){
                        var upperCase = 'Sequence';
                        me.addMetaData(record, upperCase);
                    },this, record)
                }, {
                    text: '删除',
                    handler: Ext.bind(function(){
                        Ext.Msg.alert('失败',record.data.text + '无法刪除')
                    },this, record)
                }]
            })
            var functionMenu = Ext.create('Ext.menu.Menu', {
                items: [{
                    text: '新增',
                    handler: Ext.bind(function(){
                        var upperCase = 'Function';
                        me.addMetaData(record, upperCase);
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
                                text: '表',
                                handler: Ext.Function.bind(me.addMenuItem, null, [record], true)
                            },
                            {
                                text: '视图',
                                handler: Ext.Function.bind(me.addMenuItem, null, [record], true)
                            },
                            {
                                text: '触发器',
                                handler: Ext.Function.bind(me.addMenuItem, null, [record], true)
                            },
                            {
                                text: '存储过程',
                                handler: Ext.Function.bind(me.addMenuItem, null, [record], true)
                            },
                            {
                                text: '序列',
                                handler: Ext.Function.bind(me.addMenuItem, null, [record], true)
                            },
                            {
                                text: '函数',
                                handler: Ext.Function.bind(me.addMenuItem, null, [record], true)
                            },
                            {
                                text: '业务类型',
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            },
                            {
                                text: "标准字段",
                                handler: Ext.Function.bind(me.onMenuItem,null,[record],true)
                            }
                            ]
                    }
                }, {
                    text: '删除',
                    handler: Ext.bind( function (record) {
                        Ext.MessageBox.confirm('提示','是否确认删除' + record.data.text, function (btn) {
                            if(btn == 'yes') me.deleteModule(record);
                        })
                    },this, [record])
                }]
            });

            if(record.data.parentId == "root" && record.data.text != '标准类型')
                moduleMenu.showAt(e.getXY());
            if(record.data.text == '序列') sequenceMenu.showAt(e.getXY());
            if(record.data.text == '触发器') triggerMenu.showAt(e.getXY());
            if(record.data.text == "表") tableMenu.showAt(e.getXY());
            if(record.data.text == '视图') viewMenu.showAt(e.getXY());
            if(record.data.text == "存储过程") procedureMenu.showAt(e.getXY());
            if(record.data.text == '函数') functionMenu.showAt(e.getXY())
            if(record.parentNode.data.text == '标准类型') standardTypeMenu.showAt(e.getXY());
            else if(record.get('leaf') == true) leafMenu.showAt(e.getXY());
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
                    },'-',
                    {
                        xtype: 'button',
                        text: '返回',
                        handler: function () {
                            document.location.href="index.html";
                        }
                    }

                ]
            }
        ]
    },

    onMenuItem: function(item,event, record) {
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

    addMenuItem: function(item, event, record){
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript/tree/addFileName',
            params:{
                otherName: item.text,
                moduleName: record.data.text
            },
            success: function () {
                var newNode = [{text: item.text,leaf: false}];
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

    addMetaData: function(record, upperCase){
        var pNode = record;
        var chinese = record.data.text;
        Ext.MessageBox.prompt("请输入" + chinese + "名称","",function (e,text) {
            var array = pNode.childNodes;
            var flag = 1;
            for(var i in array){
                var string = pNode.childNodes[i].data.text;
                if(string == text) flag = 0;
            };
            if(text == '' && e == "ok") Ext.Msg.alert('失败', chinese +'名称不能为空');
            else if(e == "ok" && flag == 1){
                Ext.Ajax.request({
                    url: "http://localhost:8080/dbscript/addInTree/add" + upperCase,
                    params: {
                        metadataName: text,
                        moduleName: record.parentNode.data.text,
                    },
                    success: function () {
                        var newNode = [{text: text,leaf: true}];
                        pNode.appendChild(newNode);
                        pNode.expand();
                    },
                    failure: function () {
                        Ext.Msg.alert("添加失败")
                    }

                })
            }
            else if(e == 'ok' && flag == 0){
                Ext.Msg.alert('失败','已有该' + chinese + '，请检查输入名称')
            }
        })
    },

    editMetadata: function(record){
        var fileName;
        if(record.data.text == '标准字段'){
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
        else if(record.parentNode.data.text == '表') {
            fileName = record.parentNode.parentNode.data.text;
            fileName = fileName.substring(3, fileName.length - 1) + '.table.xml';
            var panel = Ext.create('Ext.table.component.TablePanel', {
                FileName: fileName,
                tableName: record.data.text
            });
        }
        else if(record.parentNode.data.text == '存储过程'){
            fileName = record.parentNode.parentNode.data.text;
            moduleName = fileName.substring(3, fileName.length - 1);
            var panel = Ext.create('Ext.procedure.component.ProcedurePanel', {
                moduleName: moduleName,
                procedureName: record.data.text
            });
        }
        else if(record.parentNode.data.text == '视图'){
            fileName = record.parentNode.parentNode.data.text;
            moduleName = fileName.substring(3, fileName.length - 1);
            var panel = Ext.create('Ext.view.component.ViewPanel', {
                moduleName: moduleName,
                viewName: record.data.text
            });
        }
        else if(record.parentNode.data.text == '触发器'){
            fileName = record.parentNode.parentNode.data.text;
            moduleName = fileName.substring(3, fileName.length - 1);
            var panel = Ext.create('Ext.trigger.component.TriggerPanel', {
                moduleName: moduleName,
                triggerName: record.data.text
            });
        }
        else if(record.parentNode.data.text == '序列'){
            fileName = record.parentNode.parentNode.data.text;
            moduleName = fileName.substring(3, fileName.length - 1);
            var panel = Ext.create('Ext.sequence.component.SequencePanel', {
                moduleName: moduleName,
                sequenceName: record.data.text
            });
        }
        else if(record.parentNode.data.text == '函数'){
            fileName = record.parentNode.parentNode.data.text;
            moduleName = fileName.substring(3, fileName.length - 1);
            var panel = Ext.create('Ext.function.component.FunctionPanel', {
                moduleName: moduleName,
                functionName: record.data.text
            });
        }
        var win = Ext.create("Ext.tree.window.WindowWithSave", {
            panel: panel
        });
        win.show();
    },

    deleteLeaf: function(record){
        var me = this;
        var upperCase;
        if(record.data.text == '业务类型' || record.data.text == '标准字段'){
            var fileName = record.parentNode.data.text;
            var curText = record.data.text;
            fileName = fileName.substring(3, fileName.length - 1);
            Ext.MessageBox.confirm('提示', '是否确认删除' + record.data.text, function (btn) {
                if(btn == 'yes') {
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
            })
        }
        else{
            if(record.parentNode.data.text == '表') upperCase = 'Table';
            else if(record.parentNode.data.text == '存储过程') var upperCase = 'Procedure';
            else if(record.parentNode.data.text == '视图')  upperCase = 'View';
            else if(record.parentNode.data.text == '触发器') var upperCase = 'Trigger';
            else if(record.parentNode.data.text == '序列') var upperCase = 'Sequence';
            else if(record.parentNode.data.text == '函数') var upperCase = 'Function';
            Ext.MessageBox.confirm('提示', '是否确认删除' + record.data.text, function (btn) {
                if(btn == 'yes') me.deleteMetaData(record, upperCase)
            })
        }
    },

    deleteMetaData: function(record,upperCase){
        var fileName = record.parentNode.parentNode.data.text;
        moduleName = fileName.substring(3,fileName.length - 1);
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript/deleteInTree/delete' + upperCase,
            params:{
                metadataName: record.data.text,
                moduleName: moduleName
            },
            success: function () {
                var pNode = record.parentNode;
                pNode.removeChild(record);
                pNode.expand();
                Ext.Msg.alert("成功", "成功删除" + record.data.text)
            },
            failure: function () {
                Ext.Msg.alert("失败", "删除失败");
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
                var newNode = [{ text: '模块(' + record + ')', leaf: false,}];
                root.appendChild(newNode);
            }
        })
    },

    deleteMetadataFile: function(record, fileName){
        var moduleAllName = record.parentNode.data.text;
        moduleName = moduleAllNameName.substring(3,moduleAllName.length - 1);
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript/deleteInTree/' + fileName,
            params:{
                moduleName: moduleName
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