Ext.onReady(function () {
    var store = Ext.create('Ext.data.TreeStore', {
        root: {
            expanded: true,
            children: [
                { text: '模块一（crud）', expanded: true, children: [
                        {text: '表' , expanded: true, children: [
                                {text: 't_user', leaf: true}
                            ]},
                        { text: '标准字段', leaf: true},
                        { text: '视图', leaf: true},
                        { text: '触发器', leaf: true}
                    ]},
                { text: '模块二（cddd）', expanded: true, children: [
                        { text: '表', expanded: true}
                    ]}
            ]
        }
    });
    var treeStore = Ext.create('Ext.data.TreeStore',{
        id: 'treeStore',
        proxy: {
            type: 'ajax',
            url: 'http://localhost:8080/BookManager//student/module'
        }
    });
    var tree = Ext.create('Ext.tree.Panel', {
        title : 'Simple Tree',
        width : 400,
        height : 650,
        queryModel : 'local',
        id : 'treePanel-id',
        store : store,
        renderTo: 'tree-panel',
        rootVisible : false,
        listeners : {
            'itemclick' : function(view, record, items, index, e) {
                alert(items);
                if (record.get('leaf') == false) {
                    return;
                } else {
                    Ext.MessageBox.show({
                        title : '节点操作',
                        msg : 'itemclick：index=' + index + ",text="
                            + record.data.text,
                        icon : Ext.MessageBox.INFO
                    });
                }
            },
            'itemcontextmenu': function ( view, record, item, index, e, eOpts) {
                e.preventDefault();  //屏蔽默认右键菜单
                if(record.get('leaf') == true)
                rightMenu.showAt(e.getXY());
                if(record.get('leaf') == false) tableMenu.showAt(e.getXY());
            }
        },
    });
    treeStore.load();

    var rightMenu = new Ext.menu.Menu({
        items: [{
            text: '编辑',
            handler: Ext.bind(function(){
                location.href="http://www.mahaixiang.cn/"
            },this)
        }, {
            text: '删除',
            handler: Ext.bind(function(){

            },this)
        }]
    });
    var tableMenu = new Ext.menu.Menu({
        items: [{
            text: '新增',
            handler: Ext.bind(function(){

            },this)
        }, {
            text: '删除',
            handler: Ext.bind(function(){

            },this)
        }]
    });

})