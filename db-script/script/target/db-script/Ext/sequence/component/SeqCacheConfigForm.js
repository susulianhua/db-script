Ext.define('Ext.sequence.component.SeqCacheConfigForm', {
    extend: 'Ext.form.Panel',
    frame: true,
    labelWidth: 30,
    height: 100,
    layout: 'column',
    title: 'SeqCacheConfig',
    reader: new Ext.data.JsonReader({
        root: 'data',
        type: 'json',
        totalProperty: 'total'
    },[
        { name: 'cache', mapping: 'cache'},
        { name: 'number', mapping: 'number'}
    ]),

    initComponent: function () {
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function () {
        var booleanStore = Ext.create('Ext.table.store.BooleanStore');
        var items = [
            { fieldLabel: 'cache', name: 'cache', xtype: 'combobox', columnWidth: 0.5 ,
              store: booleanStore, displayField: 'name', fieldValue: 'boolean'},
            { fieldLabel: 'number', name: 'number', xtype: 'textfield', columnWidth: 0.5}
        ];

        return items;
    }
})