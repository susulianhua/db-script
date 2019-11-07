Ext.define('Ext.businessType.component.PlaceHolderGrid', {
    extend: 'Ext.grid.Panel',
    loadMask: true,
    selType: 'rowmodel',
    autoScroll: true,
    businessId: null,

    initComponent: function(){
        this.columns = this.createColumns();
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },
    createColumns: function(){
        var me = this;
        return [
            { dataIndex: 'name', text: 'name', align: 'center'},
            { dataIndex: 'value', text: 'value', align: 'center'},
            {
                text: '操作1',
                xtype: 'actioncolumn',
                align:"center",
                width: 70,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(placeHolderValueGrid, rowIndex, colIndex) {
                            me.editPlaceHolderValue(placeHolderValueGrid, rowIndex, colIndex);
                        }
                    }]

            },
            {
                text: '操作2',
                xtype: 'actioncolumn',
                align:"center",
                width: 70,
                items: [
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(placeHolderValueGrid, rowIndex) {
                            me.deletePlaceHolderValue(placeHolderValueGrid, rowIndex);
                        }
                    }]

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
                    {
                        xtype: 'tbfill'
                    },
                    {
                        xtype:'button',
                        border: '1px',
                        width: 60,
                        text:'新增',
                        handler: function () {
                            me.addPlaceHolderValue();
                        }
                    }
                ]
            }
        ]
    },

    editPlaceHolderValue: function(placeHolderValueGrid, rowIndex){
        var me = this;
        var record = placeHolderValueGrid.getStore().getAt(rowIndex).data;
        var placeHolderValueForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'index-field修改',
            items: [
                { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                { fieldLabel: 'value', name: 'value', xtype: 'textfield',
                    allowBlank: false, regex: /^\w+$/, frame: true,}
                    ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = placeHolderValueGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        if(record.name == '' || record.value == '') Ext.Msg.alert('提示', '请填写完整')
                        else{
                            placeHolderValueGrid.store.remove(del);
                            var placeHolderModel = Ext.create('Ext.businessType.model.PlaceHolderModel');
                            placeHolderModel.set('value', record.value);
                            placeHolderModel.set('name', record.name);
                            placeHolderModel.set('businessId', me.businessId);
                            placeHolderValueGrid.store.insert(rowIndex,placeHolderModel.data);
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
        placeHolderValueForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 200,                          //高度
            width: 400,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [placeHolderValueForm]
        });
        win.show();
    },
    deletePlaceHolderValue: function(placeHolderValueGrid ,rowIndex) {
        var me = this;
        Ext.MessageBox.confirm('提示', '是否确认删除该索引字段', function (btn) {
            if(btn == 'yes'){
                var record = placeHolderValueGrid.getStore().getAt(rowIndex);
                me.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    },
    addPlaceHolderValue: function() {
        var me = this;
        var placeHolderForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            frame: true,
            title: 'placeHolder新增',
            items: [
                { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                { fieldLabel: 'value', name: 'value', xtype: 'textfield'}
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var record = this.up('form').getForm().getValues();
                        if(record.name == '' || record.value == '') Ext.Msg.alert('提示', '请填写完整')
                        else{
                            var placeHolderModel = Ext.create('Ext.businessType.model.PlaceHolderModel');
                            placeHolderModel.set('value', record.value);
                            placeHolderModel.set('name', record.name);
                            placeHolderModel.set('businessId', me.businessId);
                            me.store.insert(0,[placeHolderModel]);
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
            height: 200,                          //高度
            width: 400,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [placeHolderForm]
        });
        win.show();
    }
})