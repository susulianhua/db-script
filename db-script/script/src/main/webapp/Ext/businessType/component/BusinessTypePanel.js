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


})