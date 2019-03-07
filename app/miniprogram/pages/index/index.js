const app = getApp()

Page({
  data: {
    screenWidth: 350,
    serverUrl:"",
    totalPage:1,
    page:1,
    videoList:[],
    searchValue:""

  },

  onLoad: function (params) {
    var me = this;
    var screenWidth = wx.getSystemInfoSync().screenWidth;
    me.setData({
      screenWidth: screenWidth,
    });
    
    var page=me.data.page;
    var serverUrl=app.serverUrl;
    var save=params.save;
    if(save==null || save==''||save==undefined){
      save=0;
    }
    var searchValue = params.searchValue;
    me.setData({
      searchValue: searchValue
    })

    me.getAllVideos(page,save);

  },
  
  getAllVideos:function(page,save){
    var me=this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请稍候',
    })
    var searchValue = me.data.searchValue;
    wx.request({
      url: serverUrl + '/video/all?page=' + page+"&save="+save,
      method: "POST",
      data:{
        videoDesc: searchValue
      },
      success: function (res) {
        console.log(res);
        wx.hideLoading();
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();

        if (page == 1) {
          me.setData({
            videoList: []
          })
        }

        var videoList = res.data.data.rows;
        var newVideoList = me.data.videoList;

        me.setData({
          videoList: newVideoList.concat(videoList),
          page: page,
          totalPage: res.data.data.total,
          serverUrl: serverUrl

        })
      }
    })

  },

  onReachBottom:function(page){
    var me=this;
    var nowPage=me.data.page;
    var totalPage=me.data.totalPage;

    if(nowPage==totalPage){
      wx.showToast({
        title: '已经没有视频了',
        icon:"none"
      })
      return;
    }

    var page=nowPage+1;
    me.getAllVideos(page,0);

  },

  onPullDownRefresh:function(){
    wx.showNavigationBarLoading();
    me.getAllVideos(1,0);
  },

  showVideoInfo:function(e){
    var me=this;
    var videoList=me.data.videoList;
    var num=e.target.dataset.arrindex;
    var videoInfo=JSON.stringify(videoList[num]);
    wx.redirectTo({
      url: '../videoinfo/videoinfo?videoInfo='+videoInfo,
    })
  }


})
