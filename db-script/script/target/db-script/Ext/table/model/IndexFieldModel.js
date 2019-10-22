Ext.define('Ext.table.model.IndexFieldModel',{
    extend: 'Ext.data.Model',
    fields: [
        { name: 'field', type: 'string'},
        { name: 'direction', type: 'string'},
        { name: 'index_name', type: 'string'}
    ]
});