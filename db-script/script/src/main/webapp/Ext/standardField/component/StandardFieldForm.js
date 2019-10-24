Ext.define('Ext.standardField.component.StandardFieldForm',{
    extend: 'Ext.form.Panel',
    frame: true,
    labelWidth: 30,
    layout: 'column',
    reader: new Ext.data.JsonReader({
        type: 'json',
        totalProperty: 'total',
        root: 'data',
    }, [
        { name: 'fileName', mapping: 'fileName'},
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
                columnWidth: 0.45,
                frame: true,
                items: [
                    { fieldLabel: '文件名', name: 'fileName', xtype: 'textfield', readOnly: true}
                ]
            },
            {
                layout: 'form',
                columnWidth: 0.45,
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