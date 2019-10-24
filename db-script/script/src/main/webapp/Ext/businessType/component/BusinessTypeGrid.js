Ext.define('Ext.businessType.component.BusinessTypeGrid',{
    extend: 'Ext.grid.Panel',
    title: '业务类型',
    height: 350,
    disableSelection: false,
    loadMask: true,
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
            { dataIndex: 'typeId', text: 'standardType', width: 120, align: 'center'},
            { dataIndex: 'id', text : 'id', width: 80, align: 'center'},
            { dataIndex: 'name', text : 'name', width: 80, align: 'center'},
            { dataIndex: 'title', text : 'title', width: 80, align: 'center'},
            {   text: 'placeholder',
                xtype: 'gridcolumn',
                width: 150,
                align: 'center',
                renderer: function (value, metaData, record) {
                    var id = metaData.record.id;
                    metaData.tdAttr = 'data-qtip="新增placeholder"';
                    Ext.defer(function () {
                        Ext.widget('button', {
                            renderTo: id,
                            height: 20,
                            width: 50,
                            // style:"margin-left:5px;background:blue;",
                            text: '编辑',
                            handler: function () {
                                me.setPlaceHolderValue(record);
                            }
                        });
                    }, 50);
                    return Ext.String.format('<div id="{0}"></div>', id);
                }},
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 90,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(businessTypeGrid, rowIndex) {
                            me.editBusinessType(businessTypeGrid, rowIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(businessTypeGrid, rowIndex) {
                            me.deleteBusinessType(businessTypeGrid, rowIndex);
                        }
                    }
                ]
            }
        ]
    },

    createDockedItems: function(){
        var me = this;
        return  [
            {
                xtype:'toolbar',
                dock: 'top',
                items: [
                    { xtype: 'tbfill' },
                    {
                        xtype:'button',
                        id: 'btn_indexAdd',
                        handler: function(){
                            me.addBusinessType()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    addBusinessType: function () {
        var me = this;
        var addBusinessTypeForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '业务类型新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: '标准类型Id', name: 'typeId', xtype: 'textfield'},
                        { fieldLabel: 'name', name: 'name', xtype: 'textfield'}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'id', name: 'id', xtype: 'textfield'},
                        { fieldLabel: 'title', name: 'title', xtype: 'textfield'}
                    ]
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var record = this.up('form').getForm().getValues();
                        me.store.insert(0,record);
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
        var win = Ext.create("Ext.window.Window", {
            draggable: true,
            height: 300,                          //高度
            width: 500,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [addBusinessTypeForm]
        });
        win.show();
    },

    editBusinessType: function (businessTypeGrid, rowIndex) {
        var record = businessTypeGrid.getStore().getAt(rowIndex).data;
        var editBusinessTypeForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '业务类型修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: '标准类型', name: 'typeId', xtype: 'textfield',
                            regex: /^\w+$/, allowBlank: false},
                        { fieldLabel: 'name', name: 'name', xtype: 'textfield', allowBlank: false}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'id', name: 'id', xtype: 'textfield'},
                        { fieldLabel: 'title', name: 'title', xtype: 'textfield'}
                        ]
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = businessTypeGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        businessTypeGrid.store.remove(del);
                        businessTypeGrid.store.insert(rowIndex,record);
                        win.close();
                    }
                }, {
                    text: '关闭',
                    handler: function () {
                        win.close();
                    }
                }
            ]
        });
        editBusinessTypeForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [editBusinessTypeForm]
        });
        win.show();
    },

    deleteBusinessType: function(businessTypeGrid, rowIndex){
        var me = this;
        Ext.MessageBox.confirm('提示','是否确认删除该业务类型及其相关占位符', function (btn) {
            if(btn == 'yes'){
                var typeId = businessTypeGrid.getStore().getAt(rowIndex).data.typeId;
                var record = businessTypeGrid.getStore().getAt(rowIndex);
                var recordArray = me.store.getRange();
                for(var i in recordArray){
                    var char = recordArray[i].get('typeId');
                    if(char == typeId){
                        me.getStore().remove(recordArray[i]);
                    };
                };
                me.store.remove(record);
                Ext.Msg.alter('提示','删除成功');
            }
        })
    },

    setPlaceHolderValue: function(record) {
        var typeId = record.data.typeId;
        var placeHolderStore = this.placeHolderStore;
        var placeHolderGrid = Ext.create('Ext.businessType.component.PlaceHolderGrid',{
            store: placeHolderStore,
            typeId: typeId,
            title: typeId +': placeholderValue'
        });

        /**
         * 清空过滤器，如果不清空，将在上次过滤的基础上再次过滤
         * */
        placeHolderStore.clearFilter();

        /**
         * 过滤store显示对应数据
         * */
        placeHolderStore.filter('typeId', typeId);
        var win = Ext.create("Ext.window.Window", {
            draggable: true,
            height: 300,                          //高度
            width: 400,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [placeHolderGrid],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '关闭',
                    handler: function () {
                        win.close();
                    }
                }
            ]
        });
        win.show();
    },
})