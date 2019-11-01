Ext.define('Ext.function.component.FunctionPanel',{
    extend: 'Ext.panel.Panel',
    width: 570,
    height: 600,
    title: 'DialectFunction',
    titleAlign: 'center',
    layout: 'form',
    functionName: null,
    moduleName: null,

    initComponent: function(){
        this.dialectStore = Ext.create('Ext.function.store.DialectStore');
        this.dialectFunctionForm = Ext.create('Ext.function.component.DialectFunctionForm');
        this.items = this.getItems();
        this.callParent(arguments);
    },
    getItems: function () {
        var me = this;
        var dialectGrid = Ext.create('Ext.function.component.DialectGrid', {
            dialectStore: me.dialectStore,
        });
        items = [
            me.dialectFunctionForm,
            dialectGrid
        ];
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//function/getDialectFunctionForm',
            method: 'post',
            reader:{
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            },
            params: {
                functionName: me.functionName,
                moduleName: me.moduleName
            },
            success: function (response) {
                var result = Ext.JSON.decode(response.responseText).data;
                var record = result[0];
                me.dialectFunctionForm.getForm().setValues(record);
            },
            failure:function () {
            }
        });
        me.dialectStore.load({params:{moduleName: me.moduleName, functionName: me.funcitonName}});
        return items;
    },
    
    savePanel: function () {
        var me = this;
        var functionFormValue = this.dialectFunctionForm.getForm().getValues();
        var functionWithModuleName = {};
        functionWithModuleName.moduleName = me.moduleName;
        var dialectFunction = {};
        dialectFunction.name = functionFormValue.name;
        dialectFunction.desc = functionFormValue.desc;
        dialectFunction.format = functionFormValue.format;
        var dialects = [];
        var records = this.dialectStore.getRange();
        for(var i in records){
            var dialect = {};
            dialect.name = records[i].get('name');
            dialect.functionName = records[i].get('functionName');
            dialects.push(dialect);
        };
        dialectFunction.dialects = dialects;
        functionWithModuleName.dialectFunction = dialectFunction;
        Ext.Ajax.request({
            url: 'http://localhost:8080//dbscript/function/saveFunction',
            headers: {'ContentType': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json'
            },
            ContentType: 'application/json:charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(functionWithModuleName),
            method: 'Post',
            success: function () {
                Ext.Msg.alert('成功', '添加成功');
            },
            failure: function () {
                Ext.Msg.alert('失败', '添加失败，请重试');
            }

        })

    }
})