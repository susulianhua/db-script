Ext.define('Ext.sequence.component.ValueConfigForm', {
    extend: 'Ext.form.Panel',
    frame: true,
    layout: 'column',
    labelWidth: 30,
    height: 110,
    title: 'ValueConfig',
    reader: new Ext.data.JsonReader({
        type: 'json',
        totalProperty: 'total',
        root: 'data',
    },[
        { name: 'minValue', mapping: 'minValue'},
        { name: 'maxValue', mapping: 'maxValue'}
    ]),

    initComponent: function(){
      this.items = this.getItems();
      this.callParent(arguments);
    },

    getItems: function () {
        var items = [
            { fieldLabel: 'minValue' , name: 'minValue', xtype: 'textfield', columnWidth: 0.5, regex: /^[1-9]\d*$/ },
            { fieldLabel: 'maxValue', name: 'maxValue', xtype: 'textfield', columnWidth: 0.5, regex: /^[1-9]\d*$/ }
        ]
        return items;
    }
})