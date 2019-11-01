Ext.define('Ext.standardField.component.StandardFieldPanel', {
    extend: 'Ext.panel.Panel',
    width: 600,
    height: 600,
    title: 'StandardField',
    titleAlign: 'center',
    layout: 'form',
    moduleName: null,

    initComponent: function(){
        this.standardFieldStore = Ext.create('Ext.standardField.store.StandardFieldStore');
        this.businessTypeIdStore = Ext.create('Ext.standardField.store.BusinessTypeIdStore');
        this.standardFieldBaseForm = Ext.create('Ext.standardField.component.StandardFieldForm');
        this.items = this.getItems();
        this.callParent(arguments);
    },
    getItems: function () {
        var me = this;
        this.businessTypeIdStore.load({params: {moduleName: me.moduleName}});
        this.standardFieldStore.load({params:{moduleName: me.moduleName}});
        var standardFieldGrid = Ext.create('Ext.standardField.component.StandardFieldGrid', {
            store: me.standardFieldStore,
            businessTypeIdStore: me.businessTypeIdStore
        });

        items = [
            me.standardFieldBaseForm,
            standardFieldGrid,
        ];
        var fileName = me.moduleName + '.stdfield.xml';
        var record  = {};
        record.fileName = fileName;
        record.packageName = me.moduleName;
        me.standardFieldBaseForm.getForm().setValues(record);
        return items;
    },

    savePanel: function () {
        var me = this;
        var standardFields = {};
        var formValues = me.standardFieldBaseForm.getForm().getValues();
        standardFields.packageName = formValues.packageName;
        standardFieldList = [];

        var standardFieldRecords = me.standardFieldStore.getRange();
        for( var i in standardFieldRecords){
            var standardField = {};
            standardField.id = standardFieldRecords[i].get('id');
            standardField.title = standardFieldRecords[i].get('title');
            standardField.description = standardFieldRecords[i].get('description');
            standardField.typeId = standardFieldRecords[i].get('typeId');
            standardField.name = standardFieldRecords[i].get('name');
            standardFieldList.push(standardField);
        };
        standardFields.standardFieldList = standardFieldList;

        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//standardField/standardFieldSave',
            headers: {'ContentType': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json'
            },
            ContentType : 'application/json;charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(standardFields),
            method: 'Post',
            success: function () {
                Ext.Msg.alert('成功', '添加成功');
            },
            failure: function () {
                Ext.Msg.alert('失败', '添加失败，请重试');
            }
        });
    }

})