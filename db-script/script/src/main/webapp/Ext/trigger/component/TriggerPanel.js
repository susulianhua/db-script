Ext.define('Ext.trigger.component.TriggerPanel', {
    extend: 'Ext.panel.Panel',
    width: 600,
    height: 600,
    title: 'Trigger',
    titleAlign: 'center',
    layout: 'form',
    triggerName: null,
    moduleName: null,

    initComponent: function(){
        this.sqlBodyStore = Ext.create('Ext.trigger.store.SqlBodyStore');
        this.triggerForm = Ext.create('Ext.trigger.component.TriggerForm');
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function () {
        var me = this;
        this.sqlBodyStore.load({params: { triggerName: me.triggerName, moduleName: me.moduleName }});
        var sqlBodyGrid = Ext.create('Ext.procedure.component.SqlGrid',{
            moduleName: me.moduelName,
            store: me.sqlBodyStore,
            width: 580,
            contentLength:200
        });
        items = [
            me.triggerForm,
            {
                xtype: 'panel',
                frame: true,
                height: 370,
                align: 'center',
                items: [
                    sqlBodyGrid,
                ]
            }
        ];
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//trigger/getTriggerInformation',
            method: 'post',
            reader:{
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            },
            params: {
                triggerName: me.triggerName,
                moduleName: me.moduleName
            },
            success: function (response) {
                var result = Ext.JSON.decode(response.responseText).data;
                var record = result[0];
                me.triggerForm.getForm().setValues(record);
            },
            failure:function () {
            }
        });
        return items;
    },

    panelSave: function () {
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