Ext.define('Ext.trigger.component.TriggerForm', {
    extend: 'Ext.form.Panel',
    frame: true,
    labelWidth: 30,
    layout: 'column',
    reader: new Ext.data.JsonReader({
        type: 'json',
        totalProperty: 'total',
        root: 'data',
    }, [
        { name: 'moduleName', mapping: 'moduleName'},
        { name: 'name', mapping: 'name'},
        { name: 'title', mapping: 'title'},
        { name: 'description', mapping: 'description'},

    ]),

    initComponent: function(){
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function () {
        var items = [
            {
                layout: 'form',
                columnWidth: 0.45,
                frame: true,
                items: [
                    { fieldLabel: '模块名', name: 'moduleName', xtype: 'textfield', readOnly: true},
                    { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                ]
            },
            {
                layout: 'form',
                columnWidth: 0.45,
                frame: true,
                items: [
                    { fieldLabel: 'title', name: 'title', xtype: 'textfield'},
                    { fieldLabel: 'description', name: 'description', xtype: 'textfield'},
                ]
            }
        ]
        return items;
    }
})