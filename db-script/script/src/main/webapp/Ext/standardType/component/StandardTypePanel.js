Ext.define('Ext.standardType.component.StandardTypePanel', {
    extend: 'Ext.panel.Panel',
    width: 590,
    height: 600,
    title: 'StandardType',
    titleAlign: 'center',
    layout: 'form',
    standardTypeId: null,

    initComponent: function(){
        this.dialectTypeStore = Ext.create('Ext.standardType.store.DialectTypeStore');
        this.placeholderStore = Ext.create('Ext.standardType.store.PlaceholderStore');
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function () {
        var me = this;
        var dialectTypeGrid = Ext.create('Ext.standardType.component.DialectTypeGrid',{
            store: this.dialectTypeStore,
            standardTypeId: me.standardTypeId
        });
        var placeholderGrid = Ext.create('Ext.standardType.component.PlaceholderGrid', {
            store: this.placeholderStore
        });
        items = [
            {
                xtype:'form',
                layout: 'column',
                height: 30,
                items: [
                    { xtype: 'textfield', fieldLabel: 'StandardTypeId', name: 'id',
                        value: me.standardTypeId,  readOnly: true
                    }
                ]
            },
            dialectTypeGrid,
            placeholderGrid
        ];
        this.dialectTypeStore.load({
            params:{
                standardTypeId: me.standardTypeId
            }});
        this.placeholderStore.load({
            params: {
                standardTypeId: me.standardTypeId
            }
        })
        return items;
    },

})