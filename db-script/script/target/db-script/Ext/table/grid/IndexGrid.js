Ext.define('Ext.table.grid.IndexGrid',{
    extend: 'Ext.grid.Panel',
    title: '索引',
    height: 250,
    disableSelection: false,
    loadMask: true,
    selType: 'rowmodel',
    autoScroll:true,
    indexFieldStore: null,
    FileName: null,
    tableName: null,
    initComponent: function(){
        this.columns = this.createColumns();
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },
    createColumns: function(){
        var me = this;
        return [
            { dataIndex: 'index_name', text: 'name', align: 'center'},
            { dataIndex: 'index_unique', text: 'unique', align: 'center'},
            { dataIndex: 'index_description', text: 'description', align: 'center'},
            {   text: 'index-field',
                xtype: 'gridcolumn',
                width: 150,
                align: 'center',
                renderer: function (value, metaData, record) {
                    var id = metaData.record.id;
                    metaData.tdAttr = 'data-qtip="新增编辑index-field"';
                    Ext.defer(function () {
                        Ext.widget('button', {
                            renderTo: id,
                            height: 20,
                            width: 50,
                            // style:"margin-left:5px;background:blue;",
                            text: '编辑',
                            handler: function () {
                                me.indexFieldEdit(record);
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
                        handler: function(indexGrid, rowIndex) {
                            me.editIndex(indexGrid, rowIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(indexGrid, rowIndex) {
                            me.deleteIndex(indexGrid, rowIndex);
                        }
                    }
                ]

            }
        ]
    },
    createDockedItems: function(){
      return  [
          {
              xtype:'toolbar',
              dock: 'top',
              items: [
                  { xtype: 'tbfill' },
                  {
                      xtype:'button',
                      id: 'btn_indexAdd',
                      border: '1px',
                      width: 60,
                      text:'新增'
                  }
              ]
          }
      ]
    },
    editIndex: function(indexGrid, rowIndex) {
        var record = indexGrid.getStore().getAt(rowIndex).data;
        var indexForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '索引修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'name', name: 'index_name', xtype: 'textfield', readOnly: true,
                            regex: /^\w+$/, allowBlank: false
                        }, {
                        fieldLabel: 'unique', name: 'index_unique', xtype: 'combobox', displayField: 'name',
                            allowBlank: false, store: Ext.create('Ext.table.store.BooleanStore'), editable: false}
                            ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                    { fieldLabel: 'description', name: 'index_description', xtype: 'textfield'}
                    ]
                }
                ],
            buttonAlign: 'center',
            buttons: [
            {
                text: '保存',
                handler: function () {
                    var del = indexGrid.getStore().getAt(rowIndex);
                    var record = this.up('form').getForm().getValues();
                    indexGrid.store.remove(del);
                    indexGrid.store.insert(rowIndex,record);
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
        indexForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [indexForm]
        });
        win.show();
        },
    indexFieldEdit: function(record) {
        var indexName = record.data.index_name;
        console.log('indexName:' , indexName);
        var indexFieldStore = this.indexFieldStore;
        console.log('indexFieldStore:', indexFieldStore);
        var indexFieldGrid = Ext.create('Ext.table.grid.IndexFieldGrid',{
            store: indexFieldStore,
            indexName: indexName,
            title: 'index:' + indexName
        });

        /**
     * 清空过滤器，如果不清空，将在上次过滤的基础上再次过滤
     * */
        indexFieldStore.clearFilter();

        /**
     * 过滤store显示对应数据
     * */
        indexFieldStore.filter('index_name', indexName);
        var win = Ext.create("Ext.window.Window", {
            draggable: true,
            height: 300,                          //高度
            width: 400,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [indexFieldGrid],
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
    deleteIndex: function(indexGrid, rowIndex){
        var me = this;
        Ext.MessageBox.confirm('提示','是否确认删除该索引及其相关索引字段', function (btn) {
            if(btn == 'yes'){
                var indexName = indexGrid.getStore().getAt(rowIndex).data.index_name;
                var record = indexGrid.getStore().getAt(rowIndex);
                var recordArray = me.indexFieldStore.getRange();
                for(var i in recordArray){
                    var char = recordArray[i].get('index_name');
                    if(char == indexName){
                        indexFieldStore.remove(recordArray[i]);
                    };
                };
                indexGrid.store.remove(record);
                Ext.Msg.alter('提示','删除成功');
            }
        })
    },
})