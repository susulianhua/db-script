Ext.define('Ext.standardField.model.StandardFieldModel',{
    extend: 'Ext.data.Model',
    fields: [
        { name: 'typeId', type: 'string'},
        { name: 'id', type: 'string'},
        { name: 'title', type: 'string'},
        { name: 'name', type: 'string'},
        { name: 'description', type: 'string'}
    ]
})