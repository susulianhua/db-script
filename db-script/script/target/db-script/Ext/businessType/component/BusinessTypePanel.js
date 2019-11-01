Ext.define('Ext.businessType.component.BusinessTypePanel', {
    extend: 'Ext.panel.Panel',
    height: 600,
    width: 600,
    title: 'BusinessType',
    titleAlign: 'center',
    layout: 'form',
    moduleName: null,

    initComponent: function(){
        this.businessTypeStore = Ext.create('Ext.businessType.store.BusinessTypeStore');
        this.placeHolderStore = Ext.create('Ext.businessType.store.PlaceHolderStore');
        this.businessTypeForm = Ext.create('Ext.businessType.component.BusinessTypeForm');
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function () {
        var me = this;
        this.businessTypeStore.load({ params: { moduleName: me.moduleName}});
        this.placeHolderStore.load({ params: { moduleName: me.moduleName}});
        var businessTypeGrid = Ext.create('Ext.businessType.component.BusinessTypeGrid', {
            store: me.businessTypeStore,
            placeHolderStore: me.placeHolderStore
        })

        items = [
            me.businessTypeForm,
            businessTypeGrid
        ];
        var record  = {};
        record.title = '基准类型';
        record.packageName = me.moduleName;
        me.businessTypeForm.getForm().setValues(record);
        return items;
    },

    savePanel: function () {
        var me = this;
        var businessTypes = {};
        businessTypes.packageName = me.moduleName;
        businessTypes.title = '基准类型';

        var businessTypeRecords = me.businessTypeStore.getRange();
        var placeHolderValueRecords = me.placeHolderStore.getRange();
        var businessTypeList = [];
        console.log('placeHolderRecords:', placeHolderValueRecords);
        for(var i in businessTypeRecords){
            var businessType = {};
            businessType.typeId = businessTypeRecords[i].get('typeId');
            businessType.id = businessTypeRecords[i].get('id');
            businessType.name = businessTypeRecords[i].get('name');
            businessType.title = businessTypeRecords[i].get('title');
            var placeholderValueList = [];
            for(var j in placeHolderValueRecords){
                if(placeHolderValueRecords[j].get('businessId') == businessType.id){
                    var placeholderValue = {};
                    placeholderValue.name = placeHolderValueRecords[j].get('name');
                    placeholderValue.value = placeHolderValueRecords[j].get('value');
                    placeholderValueList.push(placeholderValue)
                }
            }
            businessType.placeholderValueList = placeholderValueList;
            businessTypeList.push(businessType);
        }
        businessTypes.businessTypeList = businessTypeList;
        Ext.Ajax.request( {
            url: 'http://localhost:8080/dbscript/businessType/saveBusinessType',
            headers: {'ContentType': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json'
        },
        ContentType : 'application/json;charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(businessTypes),
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