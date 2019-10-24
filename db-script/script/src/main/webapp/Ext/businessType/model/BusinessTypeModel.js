Ext.define('Ext.businessType.model.BusinessTypeModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'string'},
        { name: 'name', type: 'string'},
        { name: 'title', type: 'string'},
        { name: 'typeId', type: 'string'}
    ]
})