Ext.onReady(function () {
    var store = Ext.create('Ext.data.TreeStore', {
        root: {
            expanded: true,
            children: [
                { text: '模块一（crud）', id: '0', expanded: true, children: [
                        {text: '表' , id:'01', expanded: true, children: [
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
            url: 'http://localhost:8080/dbscript//tree/module',
            reader:{
                type: 'json',
                root: 'data'
            },
            root:{
                text: '模块',
                expanded: true
            }
        }
    });
    var tree = Ext.create('Ext.tree.Panel', {
        title : 'Simple Tree',
        width : 400,
        height : 550,
        queryModel : 'local',
        id : 'treePanel-id',
        store : store,
        renderTo: 'tree-panel',
        rootVisible : false,
        listeners : {
            'itemcontextmenu': function ( view, record, item, index, e, eOpts) {
                e.preventDefault();  //屏蔽默认右键菜单
                var leafMenu = new Ext.menu.Menu({
                    items: [{
                        text: '编辑',
                        handler: Ext.bind(function(){
                        },this)
                    }, {
                        text: '删除',
                        handler: Ext.bind(function(){
                            Ext.Ajax.request({
                                url: 'http://localhost:8080/dbscript/tree/delete'
                            })
                            Ext.Msg.alert('成功刪除','成功刪除' + record.data.text);
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
                            Ext.Msg.alert('成功刪除','成功刪除' + record.data.text)
                        },this)
                    }]
                });
                var moduleRightMenu = new Ext.menu.Menu({
                    items: [{
                        text: '表',
                        handler: Ext.bind(function () {

                        },this)
                    },{
                        text: '标准字段',
                        handler: Ext.bind(function () {

                        },this)
                    },{
                        text: '视图',
                        handler: Ext.bind(function () {

                        },this)
                    },{
                        text: '触发器',
                        handler: Ext.bind(function () {

                        },this)
                    },{
                        text: '序列',
                        handler: Ext.bind(function () {

                        },this)
                    },{
                    text: '存储过程',
                        handler: Ext.bind(function () {

                    },this)
                }]
                });
                var moduleMenu = new Ext.menu.Menu({
                    items: [{
                        text: '新增',
                        handler: Ext.bind(function(){
                            moduleRightMenu.showAt(e.getX()+ 100,e.getY())
                        },this)
                    }, {
                        text: '删除',
                        handler: Ext.bind(function(){
                            Ext.Msg.alert('成功刪除','成功刪除' + record.data.text)
                        },this)
                    }]
                });
                if(record.data.parentId == "root") moduleMenu.showAt(e.getXY());
                if(record.data.text == "表") tableMenu.showAt(e.getXY());
                if(record.get('leaf') == true) leafMenu.showAt(e.getXY());

            }
        },
    });
    treeStore.load();



})