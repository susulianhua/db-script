Ext.define('Ext.function.component.DialectFunctionForm', {
    extend: 'Ext.form.Panel',
    frame: true,
    labelWidth: 30,
    layout: 'column',
    height: 130,
    reader: new Ext.data.JsonReader({
        type: 'json',
        totalProperty: 'total',
        root: 'data',
    }, [
        { name: 'moduleName', mapping: 'moduleName'},
        { name: 'name', mapping: 'name'},
        { name: 'desc', mapping: 'desc'},
        { name: 'format', mapping: 'format'},
    ]),
    initComponent: function(){
        this.items = this.getItems();
        this.callParent(arguments);
    },
    getItems: function(){
        var items = [
            {
                columnWidth: 0.5,
                frame: true,
                layout: 'form',
                items :[
                    { fieldLabel: 'moduleName', name: 'moduleName', xtype: 'textfield', readOnly: true},
                    { fieldLabel: 'format', name: 'format', xtype: 'textfield', allowBlank: false},
                ]
            },
            {
                columnWidth: 0.5,
                frame: true,
                layout: 'form',
                items: [
                    { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                    { fieldLabel: 'desc', name: 'desc', xtype: 'textfield'},
                ]
            }
        ];
        return items;
    }
})