Ext.define('Ext.view.component.ViewPanel', {
    extend: 'Ext.panel.Panel',
    width: 600,
    height: 600,
    title: 'View',
    titleAlign: 'center',
    layout: 'form',
    viewName: null,
    moduleName: null,

    initComponent: function(){
        this.sqlBodyStore = Ext.create('Ext.view.store.SqlBodyStore');
        this.refViewIdStore = Ext.create('Ext.view.store.RefViewIdStore');
        this.viewForm = Ext.create('Ext.view.component.ViewForm');
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function () {
        var me = this;
        this.sqlBodyStore.load({params: { viewName: me.viewName, moduleName: me.moduleName }});
        this.refViewIdStore.load({params: { viewName: me.viewName, moduleName: me.moduleName}})
        var sqlBodyGrid = Ext.create('Ext.procedure.component.SqlGrid',{
            moduleName: me.moduelName,
            viewName: me.viewName,
            store: me.sqlBodyStore,
            width: 298,
            contentLength: 125
        });
        var refViewIdGrid = Ext.create('Ext.view.component.RefViewIdGrid', {
            moduleName: me.moduleName,
            viewName: me.viewName,
            store: me.refViewIdStore,
        })
        items = [
            me.viewForm,
            {
                xtype: 'panel',
                layout: 'column',
                frame: true,
                height: 370,
                items: [
                    sqlBodyGrid,
                    refViewIdGrid
                ]
            }
        ];
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//view/getViewInformation',
            method: 'post',
            reader:{
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            },
            params: {
                viewName: me.viewName,
                moduleName: me.moduleName
            },
            success: function (response) {
                var result = Ext.JSON.decode(response.responseText).data;
                var record = result[0];
                me.viewForm.getForm().setValues(record);
            },
            failure:function () {
            }
        });
        return items;
    },

    savePanel: function () {
        var me = this;
        var viewWidthModuleName = {};
        var view = {};
        viewWidthModuleName.moduleName = me.moduleName;
        var formValues = this.viewForm.getForm().getValues();
        view.id = formValues.id;
        view.title = formValues.title;
        view.description = formValues.description;
        view.name = formValues.name;
        view.schema = formValues.schema;

        var sqlGridRecords = me.sqlBodyStore.getRange();
        var sqlBodyList = [];
        for( var i in sqlGridRecords){
            sqlBodyList.push({
                'dialectTypeName': sqlGridRecords[i].get('dialectTypeName'),
                'content': sqlGridRecords[i].get('content'),
            });
        };
        view.sqlBodyList = sqlBodyList;

        var refViewIdGridRecords = me.refViewIdStore.getRange();
        var refViewIdList = [];
        for( var i in refViewIdGridRecords){
                refViewIdList.push(refViewIdGridRecords[i].get('refViewId'));
        };
        var refViewIds = {};
        refViewIds.refViewIdList = refViewIdList;
        view.refViewIds = refViewIds
        viewWidthModuleName.view = view;

        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//view/saveView',
            headers: {'ContentType': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json'
            },
            ContentType : 'application/json;charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(viewWidthModuleName),
            method: 'Post',
            success: function () {
                Ext.Msg.alert('成功', '保存成功');
            },
            failure: function () {
                Ext.Msg.alert('失败', '添加失败，请重试');
            }
        });
    }
})