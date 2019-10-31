Ext.define('Ext.businessType.component.BusinessTypeForm', {
    extend: 'Ext.form.Panel',
    frame: true,
    labelWidth: 30,
    layout: 'column',
    reader: new Ext.data.JsonReader({
        type: 'json',
        totalProperty: 'total',
        root: 'data',
    }, [
        { name: 'title', mapping: 'title'},
        { name: 'packageName', mapping: 'packageName'},
    ]),

    initComponent: function(){
        this.items = this.getItems();
        this.callParent(arguments);
    },

    getItems: function(){
        var items = [
            {
                layout: 'form',
                columnWidth: 0.5,
                frame: true,
                items: [
                    { fieldLabel: 'title', name: 'title', xtype: 'textfield', readOnly: true}
                ]
            },
            {
                layout: 'form',
                columnWidth: 0.5,
                frame: true,
                border: false,
                items: [
                    { fieldLabel: 'packageName', name: 'packageName', xtype: 'textfield'},
                ]
            }
        ];
        return items;
    }
})