Ext.Loader.setConfig({ enabled: true,});
Ext.Loader.setPath({
    'Ext.ux.form': 'js',
})
Ext.require([
    'Ext.ux.form.SearchField'
]);
Ext.onReady(function() {
    Ext.QuickTips.init();
    Ext.override(Ext.grid.RowEditor,
        {
            addFieldsForColumn : function(column, initial) {
                var me = this, i, length, field;
                if (Ext.isArray(column)) {
                    for (i = 0, length = column.length; i < length; i++) {
                        me.addFieldsForColumn(column[i], initial);
                    }
                    return;
                }
                if (column.getEditor) {
                    field = column.getEditor(null, {
                        xtype : 'displayfield',
                        getModelData : function() {
                            return null;
                        }
                    });
                    if (column.align === 'right') {
                        field.fieldStyle = 'text-align:right';
                    }
                    if (column.xtype === 'actioncolumn') {
                        field.fieldCls += ' ' + Ext.baseCSSPrefix+ 'form-action-col-field';
                    }
                    if (me.isVisible() && me.context) {
                        if (field.is('displayfield')) {
                            me.renderColumnData(field, me.context.record,column);
                        } else {
                            field.suspendEvents();
                            field.setValue(me.context.record.get(column.dataIndex));
                            field.resumeEvents();
                        }
                    }
                    if (column.hidden) {
                        me.onColumnHide(column);
                    } else if (column.rendered && !initial) {
                        me.onColumnShow(column);
                    }

                    // -- start edit
                    me.mon(field, 'change', me.onFieldChange, me);
                    // -- end edit
                }
            }
        });
    Ext.define('Book', {
        extend: 'Ext.data.Model',
        fields: [
            { name: 'book_id',      type: 'number' },
            { name: 'book_name',    type: 'string' },
            { name: 'author',       type: 'string' },
            { name: 'description',  type: 'string' },
            { name: 'price',        type: 'number' },
            { name: 'publish_time', type: 'data'   },
            { name: 'state',        type: 'number' }
        ]
    });
    var stateStore = Ext.create('Ext.data.Store', {
        fields: [ 'state', 'name'],
        data:[
            {'state': 0, 'name': '不可借'},
            {'state': 1, 'name': '可借'}
        ]
    });

    var bookStore = Ext.create('Ext.data.Store', {
        model: 'Book',
        pageSize: 5,
        autoLoad: false,
        disableSelection: false,
        data: [],
        proxy:{
            type: 'ajax',
            url: 'http://localhost:8080/BookManager//book/getAll',
            reader: {
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            }
        }
    });

    var selModel = Ext.create('Ext.selection.CheckboxModel');

    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
        pluginId: 'rowEditing',
        autoCancel: false,
        clickToEdit: 2,
        saveBtnText: '保存',
        cancelBtnText: '取消',
        listeners:{
            edit: function (e) {
                Ext.Ajax.request({
                    url: 'http://localhost:8080/BookManager//book/update',
                    type: 'ajax',
                    params:{
                        book_id: e.context.record.get('book_id'),
                        book_name: e.context.record.get('book_name'),
                        author: e.context.record.get('author'),
                        description: e.context.record.get('description'),
                        price: e.context.record.get('price'),
                        publish_time: e.context.record.get('publish_time'),
                        state: e.context.record.get('state')
                    },
                    method: 'Post',
                    success: function () {
                        bookStore.reload();
                    }
                })
            }
        }
    });
    var form = Ext.create('Ext.form.Panel', {
        renderTo: 'bookTable',
        width: 860,
        height: 70,
        defaultType: 'textfield',
        layout: {
            type: 'hbox',
            columns:8
        },
        reader: new Ext.data.JsonReader({
            type: 'json',
            totalProperty: 'total',
            root: 'data',
        }, [
            { name: 'book_id',mapping: 'book_id'},
            { name: 'book_name', mapping: 'book_name'},
            { name: 'author', mapping: 'author'},
            { name: 'description', mapping: 'description'},
            { name: 'publish_time', mapping: 'state'},
            { name: 'state', mapping: 'state'},
            { name: 'price', mapping: 'price'}
        ]),
        url: 'http://localhost:8080/BookManager//book/add',
        items: [
            {   fieldLabel: '书本id'   , labelAlign: 'left'     , name: 'book_id'     ,
                margin: '5 0 0 10     ', flex: 0.7              , labelWidth: 40      ,
                width:80               , regex: /^[1-9]\d*$/    , maxLength: 7　      ,
                allowBlank: false      , msgTarget: 'under'     , invalidText: 'illegal book id' ,
                blankText: '不能为空'},

            {   fieldLabel: '书名'     , labelAlign: 'left'     , name: 'book_name'   ,
                margin: '5 0 0 10'     , flex: 1                , labelWidth: 30      ,
                maxLength: 20          , allowBlank: false , },
            {   fieldLabel: '作者'     , labelAlign: 'left'      , name: 'author'      ,
                margin: '5 0 0 10'     , flex: 1                 , labelWidth: 40      ,
                maxLength: 20          , allowBlank: false      },

            {   fieldLabel: '描述'     , labelAlign: 'left'      , name: 'description' ,
                margin: '5 0 0 10'     , flex: 1                 , labelWidth: 40,
                allowBlank: false       },

            {   fieldLabel: '价格'     , labelAlign: 'left'      , name: 'price'        ,
                margin: '5 0 0 10'     , flex: 0.7               , labelWidth: 30       ,
                width: 60              ,  regex: /^[1-9]\d*$/    , maxLength: 4      },

            {   fieldLabel: '出版时间' , labelAlign: 'left'      , name: 'publish_time' ,
                margin: '5 0 0 10'     , format: 'Y/m/d'         , xtype: 'datefield'   ,
                flex: 1.5              , labelWidth: 55          , width: 150    ,  },

            {   fieldLabel: '状态'     , labelAlign: 'left'      , name: 'state'        ,
                margin: '5 0 0 10'     , flex: 1                 , labelWidth: 30       ,
                width: 90              , store: stateStore,
                xtype: 'combobox'      , displayField: 'name'    , valueField: 'state'
            }     ,

            { xtype: 'button' , text: '添加', margin: '5 0 0 10',
                width: 60 ,
                align: 'center',
                handler: function () {
                    var form = this.up('form').getForm();
                    if(form.isValid()){
                        form.submit({
                            success: function() {
                                bookStore.reload();
                                Ext.Msg.alert('Success', '添加成功');
                            },
                            failure: function() {
                                Ext.Msg.alert('failure', '添加失败');
                            }
                        });
                    }else{
                        Ext.Msg.alert('wrong form')
                    }
                }
            }
        ]
    });
    Ext.Ajax.request({
        url: 'http://localhost:8080/BookManager//book/select',
        method: 'get',
        reader:{
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        },
        success: function (response) {
            var result = Ext.JSON.decode(response.responseText).data;
            var record = result[0];
            form.getForm().setValues(record);

        },
        failure:function () {
        }
    });
    var grid = Ext.create('Ext.grid.Panel',{
        renderTo: 'bookTable',
        width: 800,
        height: 400,
        title: '图书',
        selModel:selModel,
        disableSelection: false,
        store: bookStore,
        loadMask: true,
        selType: 'rowmodel',
        plugins: [rowEditing],
        autoScroll:true,
        columns:[{
            dataIndex: 'book_id',
            text: '书本id',
            width: 100,
            sortable: true,
        },{
            dataIndex: 'book_name',
            width: 100,
            text: '书名',
            sortable: false,
            editor:{
                xtype: 'textfield',
                allowBlank: false,
                maxLength: 30,
                blankText: "不能为空",
                invalidText: '不合法'
            }
        },{
            dataIndex: 'author',
            width: 100,
            text: '作者',
            sortable: false,
            editor: {
                maxLength: 20,
                allowBlank: false,
                blankText: "不能为空",
                xtype: 'textfield',
                invalidText: '不合法输入'
            }
        },{
            dataIndex: 'description',
            width: 100,
            text: '描述',
            sortable: false,
            editor: {
                xtype: 'textfield',
                allowBlank: false,
                blankText: '不能为空',
                maxLength: 40,
                invalidText: '不合法输入'
            }
        },{
            dataIndex: 'price',
            width: 100,
            text: '价格',
            editor:{
                xtype: 'textfield',
                allowBlank: false,
                blankText: '不能为空',
                regex: /^[1-9]\d*$/,
                maxLength: 4,
                invalidText: '请输入价格'
            }
        },{
            dataIndex: 'publish_time',
            text: '出版时间',
            width: 100,
            editor: {
                xtype: 'datefield',
                format: 'Y/m/d'
            }
        },{
            dataIndex: 'state',
            text: '状态',
            width: 200,
            renderer: function (value) {
                if(value == 0) return '不可借';
                else return '可借';
            },
            editor: {
                xtype: 'combobox',
                allowBlank: false,
                blankText: "不能为空",
                triggerAction: 'all',
                store: stateStore       ,
                displayField: 'name'    , valueField: 'state',
                invalidText: '当前状态' , editable: false
            }
        }
        ],
        dockedItems: [
            {
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    width: 200,
                    fieldLabel: '搜索',
                    xtype: 'searchfield',
                    store: bookStore,
                },'-',{
                   width: 60,
                    text: '新增',
                    iconCls: 'add',
                   xtype: 'button',
                    handler: function () {

                       var bookModel = Ext.create('Book',{});
                       bookStore.insert(0, bookModel);
                       var rowediting = Ext.getCmp('grid').editingPlugin;
                       // add.cancelEdit();
                        rowediting.startEdit(0,0);
                        // Create a model instance

                    }

                },'-',
                    {
                    width: 90,
                    fieldLabel: '删除所选',
                    text: '删除所选',
                    xtype: 'button',
                    store: bookStore,
                    handler: function () {
                        var record = grid.getSelectionModel().getSelection();
                        if (record.length == 0) {
                            Ext.Msg.alert('failure', '请选择要删除的记录');
                        }
                        else{
                            var list = [];
                            for(var i = 0; i < record.length; i++){
                                var obj = new Object();
                                obj.book_id = record[i].get('book_id');
                                list.push(obj);
                            }
                            Ext.Ajax.request({
                                url: 'http://localhost:8080/BookManager//book/delete',
                                headers: {'ContentType': 'application/json;charset=UTF-8',
                                    'Content-Type': 'application/json'
                                },
                                ContentType : 'application/json;charset=UTF-8',
                                dataType: 'json',
                                params : JSON.stringify(list),
                                method: 'Post',
                                success: function () {
                                    bookStore.reload();
                                }
                            })
                        }
                    }
                }
                ]
            }, {
                xtype: 'pagingtoolbar',
                store: bookStore,   // same store GridPanel is using
                dock: 'bottom',
                displayInfo: true
            }],
    });
    function addFund() {

    }
    bookStore.loadPage(1);
});