Ext.define('Ext.businessType.store.PlaceHolderStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.businessType.model.PlaceHolderModel',
    autoLoader: false,
    disableSelection: false,
    data:[],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript/businessType/getPlaceHolderValue',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})