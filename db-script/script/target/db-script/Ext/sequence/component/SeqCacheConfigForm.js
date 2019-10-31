Ext.define('Ext.sequence.component.SeqCacheConfigForm', {
    extend: 'Ext.form.Panel',
    frame: true,
    labelWidth: 30,
    layout: 'column',
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
        items = [
            { labelWidth: 'cache', name: 'cache', xtype: 'textfield', columnWidth: 0.5 },
            { labelWidth: 'number', name: 'number', xtype: 'textfield', columnWidth: 0.5}
        ];

        return items;
    }
})