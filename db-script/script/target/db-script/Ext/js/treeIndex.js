Ext.Loader.setConfig({ enabled: true,});
Ext.Loader.setPath({
    'Ext':'Ext'
});

Ext.onReady(function () {

    Ext.Ajax.request({
        url: 'http://localhost:8080/dbscript//tree/getModule',
        params: {
            id: 1
        },
        success: function(response){
            var responseText = response.responseText;
            var data =  JSON.parse(responseText);
            var treeStore = Ext.create('Ext.data.TreeStore',{
                id: 'treeStore',
                root:{
                    children:data.data
                }
            });
            var tree = Ext.create('Ext.tree.Tree', {
                store : treeStore,
                renderTo: 'tree-panel'
            })
            // process server response here
        }
    });
})