Ext.define('Ext.view.component.ViewForm', {
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
        { name: 'id', mapping: 'id'},
        { name: 'name', mapping: 'name'},
        { name: 'title', mapping: 'title'},
        { name: 'description', mapping: 'description'},
        { name: 'schema', mapping: 'schema'}
    ]),

    initComponent: function(){
      this.items = this.getItems();
      this.callParent(arguments);
    },

    getItems: function () {
        var items = [
            {
                layout: 'form',
                columnWidth: 0.5,
                frame: true,
                items: [
                    { fieldLabel: '模块名', name: 'moduleName', xtype: 'textfield', readOnly: true},
                    { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                    { fieldLabel: 'schema', name: 'schema', xtype: 'textfield'}
                ]
            },
            {
                layout: 'form',
                columnWidth: 0.5,
                frame: true,
                items: [
                    { fieldLabel: 'id', name: 'id', xtype: 'textfield'},
                    { fieldLabel: 'title', name: 'title', xtype: 'textfield'},
                    { fieldLabel: 'description', name: 'description', xtype: 'textfield'}
                ]
            }
        ]
        return items;
    }

})