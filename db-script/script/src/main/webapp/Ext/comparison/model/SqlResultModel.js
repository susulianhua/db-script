Ext.define('Ext.comparison.model.SqlResultModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'databaseObject', type: 'string'},
        { name: 'databaseType', type: 'string'},
        { name: 'differentType', type: 'string'},
        { name: 'differentDetail', type: 'string'}
    ]
})