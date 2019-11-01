Ext.define('Ext.table.component.TablePanel', {
    extend: 'Ext.panel.Panel',
    width: 570,
    height: 600,
    title: 'Table',
    titleAlign: 'center',
    layout: 'form',
    tableName: null,
    FileName: null,

    initComponent: function(){
        this.fieldStore = Ext.create('Ext.table.store.FieldStore');
        this.foreignKeyStore = Ext.create('Ext.table.store.ForeignKeyStore');
        this.indexStore = Ext.create( 'Ext.table.store.IndexStore');
        this.indexFieldStore = Ext.create( 'Ext.table.store.IndexFieldStore');
        this.standardFieldIdStore = Ext.create('Ext.table.store.StandardFieldIdStore');
        this.tableBaseForm = Ext.create('Ext.table.component.TableBaseForm');
        this.items = this.getItems();
        this.callParent(arguments);
    },
    getItems: function () {
        var me = this;

        this.standardFieldIdStore.load({params:{FileName: me.FileName}});
        var fieldGrid = Ext.create('Ext.table.component.FieldGrid', {
            fieldStore: me.fieldStore,
            standardFieldIdStore: me.standardFieldIdStore
        });
        var foreignGrid = Ext.create('Ext.table.component.ForeignGrid', { store: me.foreignKeyStore});
        var indexGrid = Ext.create('Ext.table.component.IndexGrid', {
            store: me.indexStore,
            indexFieldStore: me.indexFieldStore,
            FileName: me.FileName,
            tableName: me.tableName
        });

        items = [
            me.tableBaseForm,
            {
                xtype: 'tabpanel',
                frame: true,
                heigth: 370,
                items:[
                    fieldGrid,
                    foreignGrid,
                    indexGrid,
                ]
            }
        ];
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//table/tableBase',
            method: 'post',
            reader:{
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            },
            params: {
                tableName: me.tableName,
                FileName: me.FileName
            },
            success: function (response) {
                var result = Ext.JSON.decode(response.responseText).data;
                var record = result[0];
                me.tableBaseForm.getForm().setValues(record);
            },
            failure:function () {
            }
        });
        me.fieldStore.load({params:{FileName: me.FileName, tableName: me.tableName}});
        me.foreignKeyStore.load({params:{FileName: me.FileName, tableName: me.tableName}});
        me.indexStore.load({params:{FileName: me.FileName, tableName: me.tableName}});
        me.indexFieldStore.load({params:{FileName: me.FileName, tableName: me.tableName}});
        return items;
    },

    savePanel: function () {
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

})