Ext.define('Ext.sequence.component.SequenceForm',{
    extend : 'Ext.form.FormPanel',
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
        { name: 'dataType', mapping: 'dataType'},
        { name: 'incrementBy', mapping: 'incrementBy'},
        { name: 'startWith', mapping: 'startWith'},
        { name: 'cycle', mapping: 'cycle'},
        { name: 'order', mapping: 'order'},
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
                    { fieldLabel: 'moduleName', name: 'moduleName', xtype: 'textfield', readOnly: true},
                    { fieldLabel: 'dataType', name: 'dataType', xtype: 'textfield'},
                    { fieldLabel: 'startWith', name: 'startWith', xtype: 'textfield', allowBlank: false},
                    { fieldLabel: 'order', name: 'order', xtype: 'textfield'}
                ]
            },
            {
                layout: 'form',
                columnWidth: 0.5,
                frame: true,
                border: false,
                items: [
                    { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                    { fieldLabel: 'incrementBy', name: 'incrementBy', xtype: 'textfield'},
                    { fieldLabel: 'cycle', name: 'cycle', xtype: 'cycle' },
                ]
            }
        ];
        return items;
    }
})