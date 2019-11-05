Ext.define('Ext.sequence.component.SequenceForm',{
    extend: 'Ext.form.Panel',
    frame: true,
    labelWidth: 30,
    layout: 'column',
    height: 200,
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
        var booleanStore = Ext.create('Ext.table.store.BooleanStore');
        var items = [
            {
                columnWidth: 0.5,
                frame: true,
                layout: 'form',
                items :[
                    { fieldLabel: 'moduleName', name: 'moduleName', xtype: 'textfield', readOnly: true},
                    { fieldLabel: 'dataType', name: 'dataType', xtype: 'textfield'},
                    { fieldLabel: 'startWith', name: 'startWith', xtype: 'textfield', allowBlank: false},
                    { fieldLabel: 'order', name: 'order', xtype: 'combobox', store: booleanStore,
                      displayField: 'name', fieldValue: 'boolean'}
                ]
            },
            {
                columnWidth: 0.5,
                frame: true,
                layout: 'form',
                items: [
                    { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                    { fieldLabel: 'incrementBy', name: 'incrementBy', xtype: 'textfield'},
                    { fieldLabel: 'cycle', name: 'cycle', xtype: 'combobox' , store: booleanStore,
                      displayField: 'name', fieldValue: 'boolean'},
                    { xtype: 'hidden'}
                ]
            }
        ];
        return items;
    }
})