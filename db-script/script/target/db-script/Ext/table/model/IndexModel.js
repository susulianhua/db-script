Ext.define('Ext.table.model.IndexModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'index_name', type: 'string'},
        { name: 'index_unique', type: 'string'},
        { name: 'index_description', type: 'string'}
    ],
    hasMany: {
        model: 'Ext.table.model.IndexFieldModel',
        foreignKey: 'index_name'
    }
});