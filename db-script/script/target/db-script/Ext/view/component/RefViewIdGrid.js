Ext.define('Ext.view.component.RefViewIdGrid', {
    extend: 'Ext.grid.Panel',
    moduleName: null,
    disableSelection: false,
    loadMask: true,
    height: 350,
    width: 278,
    selType: 'rowmodel',
    autoScroll:true,

    initComponent: function(){
        this.columns = this.createColumns();
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },

    createColumns: function () {
        var me = this;
        return [
            { dataIndex: 'refViewId', text: 'refViewId', width: 105, align: 'center'},
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 70,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(refViewIdGrid, rowIndex, colIndex) {
                            me.editRefViewId(refViewIdGrid, rowIndex, colIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(refViewIdGrid, rowIndex) {
                            me.deleteRefViewId(refViewIdGrid, rowIndex);
                        }
                    }
                ]
            }
        ]
    },

    createDockedItems: function(){
        var me = this;
        return [
            {
                xtype:'toolbar',
                dock: 'top',
                items: [
                    { xtype: 'tbfill' },
                    {
                        xtype:'button',
                        handler: function(){
                            me.addRefViewId()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    addRefViewId: function(){
        var me = this;
        var refViewIdForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            title: 'sql新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.4,
                    frame: true,
                    items: { fieldLabel: 'RefViewId', name: 'refViewId', xtype: 'textfield',
                        height: 20}
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var record = this.up('form').getForm().getValues();
                        if(record.refViewId == '') Ext.Msg.alert('提示', '请填写完整')
                        else{
                            var refViewIdModel = Ext.create('Ext.view.model.RefViewIdModel');
                            refViewIdModel.set('refViewId', record.refViewId);
                            me.store.insert(0,record);
                            win.close();
                        }
                    }
                }, {
                    text: '关闭',
                    handler: function () {
                        win.close();
                    }
                }
            ]
        });
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [refViewIdForm]
        });
        win.show();
    },

    deleteRefViewId:function(refViewIdGrid, rowIndex){
        Ext.MessageBox.confirm('提示','是否确认删除该sql',function (btn) {
            if(btn == 'yes'){
                var record = refViewIdGrid.getStore().getAt(rowIndex);
                refViewIdGrid.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    },

    editRefViewId: function(refViewIdGrid, rowIndex) {
        var me = this;
        var record = refViewIdGrid.getStore().getAt(rowIndex).data;
        var refViewIdForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'sql修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.4,
                    frame: true,
                    items: { fieldLabel: 'RefViewId', name: 'refViewId', xtype: 'textfield'},
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = refViewIdGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        if(record.refViewId == '') Ext.Msg.alert('提示', '请填写完整')
                        else{
                            refViewIdGrid.store.remove(del);
                            var refViewIdModel = Ext.create('Ext.view.model.RefViewIdModel');
                            refViewIdModel.set('refViewId', record.refViewId);
                            me.store.insert(rowIndex,record);
                            win.close();
                        }
                    }
                },
                {
                    text: '关闭',
                    handler: function () {
                        win.close();
                    }
                }
            ]
        });
        refViewIdForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,
            layout: "fit",
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [refViewIdForm]
        });
        win.show();
    },
})