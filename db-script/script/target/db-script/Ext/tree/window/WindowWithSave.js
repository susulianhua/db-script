Ext.define('Ext.tree.window.WindowWithSave', {
    extend: 'Ext.window.Window',
    draggable: true,
    height: 500,                          //高度
    width: 600,                           //宽度
    layout: "fit",                        //窗口布局类型
    modal: true, //是否模态窗口，默认为false
    resizable: false,
    constrainsTo: Ext.getDoc(),
    buttonAlign: 'center',

    initComponent: function () {
        this.items = this.panel;
        this.buttons = this.createButtons()
        this.callParent(arguments);
    },
    createButtons: function () {
        var me = this;
        buttons = [
            {
                text: '保存',
                handler: function () {
                    me.panel.savePanel();
                }
            },
            {
                text: '关闭',
                handler: function () {
                    me.close();
                }
            }
        ];
        return buttons;
    }
})