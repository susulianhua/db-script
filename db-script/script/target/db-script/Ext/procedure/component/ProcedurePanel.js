Ext.define('Ext.procedure.component.ProcedurePanel', {
    extend: 'Ext.panel.Panel',
    width: 600,
    height: 600,
    title: 'Table',
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
            store: this.sqlStore
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
        var table = {};
        var formValues = me.tableBaseForm.getForm().getValues();
        table.id = formValues.table_id;
        table.title = formValues.table_title;
        table.name = formValues.table_name;
        table.description = formValues.table_description;
        table.packageName = formValues.package_name;
        /**
         * 获取字段信息转化为Json数组
         */
        var tableFieldRecords = me.fieldStore.getRange();
        var fieldList = [];
        for( var i in tableFieldRecords){
            fieldList.push({
                'standardFieldId': tableFieldRecords[i].get('standardFieldId'),
                'id': tableFieldRecords[i].get('id'),
                'primary': tableFieldRecords[i].get('primary'),
                'notNull': tableFieldRecords[i].get('notNull'),
                'unique': tableFieldRecords[i].get('unique'),
                'autoIncrease': tableFieldRecords[i].get('autoIncrease')
            });
        };
        table.fieldList = fieldList;

        /**
         * 获取外键信息转化为Json数组
         */
        var foreignRecords = me.foreignKeyStore.getRange();
        var foreignList = [];
        for( var i in foreignRecords){
            foreignList.push({
                'name': foreignRecords[i].get('key_name'),
                'foreignField': foreignRecords[i].get('foreign_field'),
                'mainTable': foreignRecords[i].get('main_table'),
                'referenceField': foreignRecords[i].get('reference_field')
            });
        };
        table.foreignReferences = foreignList;
        /**
         * 获取index信息转化为Json数组
         */
        var indexFieldRecords = me.indexFieldStore.getRange();
        console.log('indexFieldRecords:', indexFieldRecords);
        var indexRecords = me.indexStore.getRange();
        var indexList = [];
        for( var i in indexRecords){
            var index = {};
            index.name = indexRecords[i].get('index_name');
            index.unique = indexRecords[i].get('index_unique');
            index.description = indexRecords[i].get('index_description');
            var indexFieldList = [];
            var indexFieldRecordsByindex = [];
            for(var i in indexFieldRecords){
                if(indexFieldRecords[i].data.index_name == index.name){
                    indexFieldRecordsByindex.push(indexFieldRecords[i]);
                }
            }
            for( var i in indexFieldRecordsByindex){
                indexFieldList.push({
                    'field': indexFieldRecordsByindex[i].get('field'),
                    'direction': indexFieldRecordsByindex[i].get('direction')
                });
            };
            index.fields = indexFieldList;
            indexList.push(index);

        };
        table.indexList = indexList;

        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//table/tableSave',
            headers: {'ContentType': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json'
            },
            ContentType : 'application/json;charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(table),
            method: 'Post',
            success: function () {
                Ext.Msg.alert('成功', '添加成功');
            },
            failure: function () {
                Ext.Msg.alert('失败', '添加失败，请重试');
            }
        });
    },

    /*updateTwoGridData: function () {
        var me = this;
        var name = Ext.getCmp('procedureName').getValue();
        me.sqlStore.clearFilter();
        me.parameterStore.clearFilter();
        me.sqlStore.filter('name', name);
        me.parameterStore.filter('name', name);
    }*/
})