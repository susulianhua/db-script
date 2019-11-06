Ext.define('Ext.trigger.component.TriggerPanel', {
    extend: 'Ext.panel.Panel',
    width: 580,
    height: 600,
    title: 'Trigger',
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
            contentLength:390
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

    savePanel: function () {
        var me = this;
        var triggerWithModuleName = {};
        var trigger = {};
        triggerWithModuleName.moduleName = me.moduleName;
        var formValues = this.triggerForm.getForm().getValues();
        trigger.title = formValues.title;
        trigger.description = formValues.description;
        trigger.name = formValues.name;

        var sqlGridRecords = me.sqlBodyStore.getRange();
        var flag = true;
        if(sqlGridRecords.length == 0) flag = false;
        var triggerSqls = [];
        for( var i in sqlGridRecords){
            triggerSqls.push({
                'dialectTypeName': sqlGridRecords[i].get('dialectTypeName'),
                'content': sqlGridRecords[i].get('content'),
            });
        };
        trigger.triggerSqls = triggerSqls;
        triggerWithModuleName.trigger = trigger;

        if(flag){
            Ext.Ajax.request({
                url: 'http://localhost:8080/dbscript//trigger/saveTrigger',
                headers: {'ContentType': 'application/json;charset=UTF-8',
                    'Content-Type': 'application/json'
                },
                ContentType : 'application/json;charset=UTF-8',
                dataType: 'json',
                params: JSON.stringify(triggerWithModuleName),
                method: 'Post',
                success: function () {
                    Ext.Msg.alert('成功', '保存成功');
                },
                failure: function () {
                    Ext.Msg.alert('失败', '添加失败，请重试');
                }
            });
        }
        else Ext.Msg.alert('提示', '请填写完整')
    }
})