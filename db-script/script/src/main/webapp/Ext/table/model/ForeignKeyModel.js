Ext.define('Ext.table.model.ForeignKeyModel', {
    extend: 'Ext.data.Model',
    fields:[
        { name: 'key_name', type: 'string'},
        { name: 'foreign_field', type: 'string'},
        { name: 'main_table', type: 'string'},
        { name: 'reference_field', type: 'string'}
    ]
});