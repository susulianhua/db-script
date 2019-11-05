Ext.define('Ext.tree.menu.ModuleMenu', {
    extend: 'Ext.menu.Menu',
    allowOtherMenus: true,

    initComponent: function(){
      this.items = this.getItems();
      this.callParent(arguments);
    },
    getItems: function(){
        var me = this;
        items = [{
            text: '新增',
            menu: {
                items: [
                    {
                        text: '表',
                        handler: Ext.Function.bind(me.addMenuItem, null, [me.record], true)
                    },
                    {
                        text: '视图',
                        handler: Ext.Function.bind(me.addMenuItem, null, [me.record], true)
                    },
                    {
                        text: '触发器',
                        handler: Ext.Function.bind(me.addMenuItem, null, [me.record], true)
                    },
                    {
                        text: '存储过程',
                        handler: Ext.Function.bind(me.addMenuItem, null, [me.record], true)
                    },
                    {
                        text: '序列',
                        handler: Ext.Function.bind(me.addMenuItem, null, [me.record], true)
                    },
                    {
                        text: '函数',
                        handler: Ext.Function.bind(me.addMenuItem, null, [me.record], true)
                    },
                    {
                        text: '业务类型',
                        handler: Ext.Function.bind(me.onMenuItem,null,[me.record],true)
                    },
                    {
                        text: "标准字段",
                        handler: Ext.Function.bind(me.onMenuItem,null,[me.record],true)
                    }
                ]
            }
        }, {
            text: '删除',
            handler: Ext.bind( function () {
                Ext.MessageBox.confirm('提示','是否确认删除' + me.record.data.text, function (btn) {
                    if(btn == 'yes') me.deleteModule(me.record);
                })
            },this, [me.record])
        }];
        return items;
    },

    addMenuItem: function(item, event, record){
        var me = this;
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript/tree/addFileName',
            params:{
                otherName: me.item.text,
                moduleName: me.record.data.text
            },
            success: function () {
                var newNode = [{text: me.item.text,leaf: false}];
                record.appendChild(newNode);
                record.expand();
            },
            failure: function () {
                Ext.Msg.alert('失败', '新增失败')
            }
        })
    },
})