// 1 导入js文件
var WxSearch = require('../../wxSearchView/wxSearchView.js');

const app = getApp()

Page({
  data: {
  },

  onLoad: function () {

    // 2 搜索栏初始化
    var that = this;

    // 查询热搜词
    var url=app.serverUrl;
    wx.request({
      url: url+'/video/hot',
      method:"POST",
      success:function(res){
        var hotList=res.data.data;
        WxSearch.init(
          that,  // 本页面一个引用
          hotList,
          hotList,
          that.mySearchFunction, // 提供一个搜索回调函数
          that.myGobackFunction //提供一个返回回调函数
        );
       }
       })
      },


  // 3 转发函数，固定部分，直接拷贝即可
  wxSearchInput: WxSearch.wxSearchInput,  // 输入变化时的操作
  wxSearchKeyTap: WxSearch.wxSearchKeyTap,  // 点击提示或者关键字、历史记录时的操作
  wxSearchDeleteAll: WxSearch.wxSearchDeleteAll, // 删除所有的历史记录
  wxSearchConfirm: WxSearch.wxSearchConfirm,  // 搜索函数
  wxSearchClear: WxSearch.wxSearchClear,  // 清空函数


  mySearchFunction:function(value){
    wx.redirectTo({
      url: '../index/index?save=1&searchValue='+value,
    })

  },


  myGobackFunction:function(){
    wx.redirectTo({
      url: '../index/index',
    })
  }
  


})