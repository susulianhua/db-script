Ext.define('Ext.table.component.TableBaseForm',{
    extend : 'Ext.form.FormPanel',
    frame: true,
    labelWidth: 30,
    layout: 'column',
    reader: new Ext.data.JsonReader({
        type: 'json',
        totalProperty: 'total',
        root: 'data',
    }, [
        { name: 'fileName', mapping: 'fileName'},
        { name: 'package_name', mapping: 'package_name'},
        { name: 'table_id', mapping: 'table_id'},
        { name: 'table_name', mapping: 'table_name'},
        { name: 'table_title', mapping: 'table_title'},
        { name: 'table_description', mapping: 'table_description'}
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
                    { fieldLabel: '文件名', name: 'fileName', xtype: 'textfield', readOnly: true},
                    { fieldLabel: 'id', name: 'table_id', xtype: 'textfield', readOnly: true},
                    { fieldLabel: 'title', name: 'table_title', xtype: 'textfield', allowBlank: false}
                ]
            },
            {
                layout: 'form',
                columnWidth: 0.45,
                frame: true,
                border: false,
                items: [
                    { fieldLabel: 'package-name', name: 'package_name', xtype: 'textfield'},
                    { fieldLabel: 'name', name: 'table_name', xtype: 'textfield'},
                    { fieldLabel: 'description', name: 'table_description', xtype: 'textfield' },
                ]
            }
        ];
        return items;
    }
})