Ext.define('Ext.standardType.model.DialectTypeModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'language', type: 'string'},
        { name: 'baseType', type: 'string'},
        { name: 'extType', type: 'string'},
        { name: 'defaultValue', type: 'string'}
    ]
})