Ext.define('Ext.procedure.component.ProcedurePanel', {
    extend: 'Ext.panel.Panel',
    width: 590,
    height: 600,
    title: 'Procedure',
    titleAlign: 'center',
    layout: 'form',
    moduleName: null,

    initComponent: function(){
        this.sqlStore = Ext.create('Ext.procedure.store.SqlStore');
        this.parameterStore = Ext.create('Ext.procedure.store.ParameterStore');
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function () {
        var me = this;
        var sqlGrid = Ext.create('Ext.procedure.component.SqlGrid',{
            store: this.sqlStore,
            width: 298,
            contentLength:120
        });
        var parameterGrid = Ext.create('Ext.procedure.component.ParameterGrid', {
            store: this.parameterStore
        });
        items = [
            {
                xtype:'form',
                layout: 'column',
                height: 30,
                items: [
                    { xtype: 'textfield', fieldLabel: 'procedureName', name: 'procedureName',
                        value: me.procedureName,  readOnly: true, id: 'procedureName'
                    }
                ]
            },
            {
                xtype: 'panel',
                layout: 'column',
                frame: true,
                height: 370,
                items:[
                    sqlGrid,
                    parameterGrid
                ]
            }
        ];
        this.sqlStore.load({
            params:{
                moduleName: me.moduleName,
                procedureName: me.procedureName
            }});
        this.parameterStore.load({
            params: {
                moduleName: me.moduleName,
                procedureName: me.procedureName
            }
        })
        return items;
    },


    panelSave: function () {
        var me = this;
        var procedureWidthModuleName = {};
        procedureWidthModuleName.moduleName = me.moduleName;
        var sqlGridRecords = me.sqlStore.getRange();
        var sqlBodyList = [];
        for( var i in sqlGridRecords){
            sqlBodyList.push({
                'dialectTypeName': sqlGridRecords[i].get('dialectTypeName'),
                'content': sqlGridRecords[i].get('content'),
            });
        };
        var procedure = {};
        procedure.procedureBodyList = sqlBodyList;
        var parameterGridRecords = me.parameterStore.getRange();
        var parameterList = [];
        for( var i in parameterGridRecords){
            parameterList.push({
                'standardFieldId': parameterGridRecords[i].get('standardFieldId'),
                'parameterType': parameterGridRecords[i].get('parameterType')
            })
        }
        procedure.name = me.procedureName;
        procedure.parameterList = parameterList;
        procedureWidthModuleName.procedure = procedure;

        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//procedure/saveProcedure',
            headers: {'ContentType': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json'
            },
            ContentType : 'application/json;charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(procedureWidthModuleName),
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