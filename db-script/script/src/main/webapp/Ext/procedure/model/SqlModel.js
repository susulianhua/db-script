Ext.define('Ext.procedure.model.SqlModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'name', type: 'string'},
        { name: 'dialectTypeName', type: 'string'},
        { name: 'content', type: 'string'}
    ]
})