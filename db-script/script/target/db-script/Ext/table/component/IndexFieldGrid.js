Ext.define('Ext.table.component.IndexFieldGrid',{
    extend: 'Ext.grid.Panel',
    loadMask: true,
    selType: 'rowmodel',
    autoScroll: true,
    config:{
        indexFieldStore: null,
        indexName: null
    },

    initComponent: function(){
      this.columns = this.createColumns();
      this.dockedItems = this.createDockedItems();
      this.callParent(arguments);
    },
    createColumns: function(){
        var me = this;
        return [
            { dataIndex: 'field', text: 'field', align: 'center' },
            { dataIndex: 'direction', text: 'direction', align: 'center'},
            {
                text: '操作1',
                xtype: 'actioncolumn',
                align:"center",
                width: 70,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(indexFieldGrid, rowIndex, colIndex) {
                            me.editIndexField(indexFieldGrid, rowIndex, colIndex);
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
                        handler: function(indexFieldGrid, rowIndex) {
                            me.deleteIndexField(indexFieldGrid, rowIndex);
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
                            me.indexFieldAdd();
                        }
                    }
                ]
            }
        ]
    },

    editIndexField: function(indexFieldGrid, rowIndex){
        var record = indexFieldGrid.getStore().getAt(rowIndex).data;
        var indexFieldForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'index-field修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'field', name: 'field', xtype: 'textfield',
                            allowBlank: false, regex: /^\w+$/}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'direction', name: 'direction', xtype: 'combobox', editable: false,
                            store: Ext.create('Ext.table.store.DirectionStore'), displayField: 'name', allowBlank: false}
                    ]
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = indexFieldGrid.getStore().getAt(rowIndex);
                        var indexName = del.data.index_name;
                        var record = this.up('form').getForm().getValues();
                        indexFieldGrid.store.remove(del);
                        var indexField_model = Ext.create('Ext.table.model.IndexFieldModel');
                        indexField_model.data.field = record.field;
                        indexField_model.data.direction = record.direction;
                        indexField_model.data.index_name = indexName;
                        indexFieldGrid.store.insert(rowIndex,indexField_model.data);
                        win.close(this);
                    }
                }, {
                    text: '关闭',
                    handler: function () {
                        win.close(this);
                    }
                }
            ]
        });
        indexFieldForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 200,                          //高度
            width: 400,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [indexFieldForm]
        });
        win.show();
    },
    deleteIndexField: function(indexFieldGrid ,rowIndex) {
        var me = this;
        Ext.MessageBox.confirm('提示', '是否确认删除该索引字段', function (btn) {
            if(btn == 'yes'){
                var record = indexFieldGrid.getStore().getAt(rowIndex);
                console.log('record:', record);
                me.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    },
    indexFieldAdd: function() {
        var me = this;
        var indexFieldAddForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            columnWidth: 0.45,
            frame: true,
            title: 'index-field新增',
            items: [
                { fieldLabel: 'field', name: 'field', xtype: 'textfield'},
                { fieldLabel: 'direction', name: 'direction', xtype: 'combobox',
                    store: Ext.create('Ext.table.store.DirectionStore'),
                    displayField: 'name' , editable: false},
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var record = this.up('form').getForm().getValues();
                        var indexFieldModel = Ext.create('Ext.table.model.IndexFieldModel');
                        indexFieldModel.set('field', record.field);
                        indexFieldModel.set('direction', record.direction);
                        indexFieldModel.set('index_name', me.getIndexName());
                        me.store.insert(0,[indexFieldModel]);
                        win.close();
                    }
                }, {
                    text: '关闭',
                    handler: function () {
                        win.close(this);
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
            items: [indexFieldAddForm]
        });
        win.show();
    }
})