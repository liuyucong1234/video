var videoUtil=require("../../util/util.js")

const app = getApp()

Page({
  data: {
    cover: "cover",
    videoId: "",
    src: "",
    videoInfo: {},

    userLikeVideo: false,


    commentsPage: 1,
    commentsTotalPage:1,
    commentsList:[],


    placeholder: "说点什么..."
  },

  videoCtx: {},

  onLoad:function(params){
   var me=this;
    me.videoCtx = wx.createVideoContext("myVideo", this);
    var videoInfo=JSON.parse(params.videoInfo);
    var height=videoInfo.videoHeight;
    var width=videoInfo.videoWidth;
    var cover="cover";
    if(width>=height){
      cover="";
    }
    me.setData({
      videoId: videoInfo.id,
      src: app.serverUrl+videoInfo.videoPath,
      videoInfo: videoInfo,
      cover:cover
    })
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo();
    var loginUserId = "";
    if (user != null && user != undefined && user != '') {
      loginUserId = user.id;
    }
    wx.request({
      url: serverUrl + '/user/creater?userId=' + loginUserId + "&videoId=" + videoInfo.id + "&createrId=" + videoInfo.userId,
      method: 'POST',
      success: function (res) {

        var creater = res.data.data.usersVO;
        var userLikeVideo = res.data.data.userLikeVideo;

        me.setData({
          serverUrl: serverUrl,
          creater: creater,
          userLikeVideo: userLikeVideo
        });
      }
    })

    me.getCommentsList(1);
  },

  onShow:function(){
    var me=this;
    me.videoCtx.play();
        
  },

  onHide:function(){
    var me=this;
    me.videoCtx.pause();
  },

  showSearch:function(){
    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })
  },

  upload:function(){
    var mine = app.getGlobalUserInfo();
    var me=this;
    var videoInfo=JSON.stringify(me.data.videoInfo);
    var redirectUrl="../videoInfo/videoInfo#videoInfo@"+videoInfo;

    if (mine == null || mine == undefined || mine == '') {
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl='+redirectUrl,
      })
    } else {
      videoUtil.uploadVideo();
    }
   videoUtil.uploadVideo();
  },

  showIndex:function(){
    wx.redirectTo({
      url: '../index/index',
    })
  },

  showMine:function(){
    var mine=app.getGlobalUserInfo();
    if(mine==null || mine==undefined || mine==''){
      wx.navigateTo({
        url: '../userLogin/login',
      })
    }else{
    wx.navigateTo({
      url: '../mine/mine',
    })
    }
  },
  likeVideoOrNot: function () {
    var me = this;
    var videoInfo = me.data.videoInfo;
    var user = app.getGlobalUserInfo();

    if (user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/login',
      })
    } else {

      var userLikeVideo = me.data.userLikeVideo;
      var url = '/video/userLikeVideo?userId=' + user.id + '&videoId=' + videoInfo.id + '&createrId=' + videoInfo.userId;
      if (userLikeVideo) {
        url = '/video/userNotLikeVideo?userId=' + user.id + '&videoId=' + videoInfo.id + '&createrId=' + videoInfo.userId;
      }

      var serverUrl = app.serverUrl;
      wx.showLoading({
        title: '...',
      })
      wx.request({
        url: serverUrl + url,
        method: 'POST',
        header: {
          'content-type': 'application/json', // 默认值
          'userId': user.id,
          'userToken': user.userToken
        },
        success: function (res) {
          wx.hideLoading();
          if (res.data.status == 200) {
            me.setData({
              userLikeVideo: !userLikeVideo
            });
            }
            else if (res.data.status == 502) {
            wx.showToast({
              title: res.data.msg,
              duration: 3000,
              icon: "none"
            })
          }
        }
      })


    }
  },

  showCreater: function () {
    var me = this;

    var user = app.getGlobalUserInfo();

    var videoInfo = me.data.videoInfo;
    var realUrl = '../mine/mine#createrId@' + videoInfo.userId;

    if (user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl=' + realUrl,
      })
    } else {
      wx.navigateTo({
        url: '../mine/mine?createrId=' + videoInfo.userId,
      })
    }

  },
  shareMe: function () {
    var me = this;
    var user = app.getGlobalUserInfo();

    wx.showActionSheet({
      itemList: ['下载到本地', '举报用户', '分享到朋友圈', '分享到QQ空间', '分享到微博'],
      success: function (res) {
        if (res.tapIndex == 0) {
          // 下载
          wx.showLoading({
            title: '下载中...',
          })
          wx.downloadFile({
            url: app.serverUrl + me.data.videoInfo.videoPath,
            success: function (res) {
              // 只要服务器有响应数据，就会把响应内容写入文件并进入 success 回调，业务需要自行判断是否下载到了想要的内容
              if (res.statusCode === 200) {

                wx.saveVideoToPhotosAlbum({
                  filePath: res.tempFilePath,
                  success: function (res) {
                    wx.hideLoading();
                  }
                })
              }
            }
          })
        } else if (res.tapIndex == 1) {
          // 举报
          var videoInfo = JSON.stringify(me.data.videoInfo);
          var realUrl = '../videoinfo/videoinfo#videoInfo@' + videoInfo;

          if (user == null || user == undefined || user == '') {
            wx.navigateTo({
              url: '../userLogin/login?redirectUrl=' + realUrl,
            })
          } else {
            var createrId = me.data.videoInfo.userId;
            var videoId = me.data.videoInfo.id;
            var currentUserId = user.id;
            wx.navigateTo({
              url: '../report/report?videoId=' + videoId + "&createrId=" + createrId
            })
          }
        } else {
          wx.showToast({
            title: '官方暂未开放...',
          })
        }
      }
    })
  },
  onShareAppMessage: function (res) {

    var me = this;
    var videoInfo = me.data.videoInfo;

    return {
      title: '短视频内容分析',
      path: "pages/videoinfo/videoinfo?videoInfo=" + JSON.stringify(videoInfo)
    }
  },

  leaveComment:function(){
    this.setData({
      commentFocus:true
    })
  },

  saveComment: function (e) {
    var me = this;
    var content = e.detail.value;

    var user = app.getGlobalUserInfo();
    var videoInfo = JSON.stringify(me.data.videoInfo);
    var realUrl = '../videoinfo/videoinfo#videoInfo@' + videoInfo;

    if (user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl=' + realUrl,
      })
    } else {
      wx.showLoading({
        title: '请稍后...',
      })
      wx.request({
        url: app.serverUrl + '/video/saveComment',
        method: 'POST',
        header: {
          'content-type': 'application/json', // 默认值
          'userId': user.id,
          'userToken': user.userToken
        },
        data: {
          fromUserId: user.id,
          videoId: me.data.videoInfo.id,
          comment: content
        },
        success: function (res) {
          wx.hideLoading();

          me.setData({
            contentValue: "",
            commentsList: []
          });

          me.getCommentsList(1);
        }
      })
    }
  },

  getCommentsList: function (page) {
    var me = this;

    var videoId = me.data.videoInfo.id;

    wx.request({
      url: app.serverUrl + '/video/findComment?videoId=' + videoId + "&page=" + page + "&pageSize=5",
      method: "POST",
      success: function (res) {
        var commentsList = res.data.data.rows;
        var newCommentsList = me.data.commentsList;

        me.setData({
          commentsList: newCommentsList.concat(commentsList),
          commentsPage: page,
          commentsTotalPage: res.data.data.total
        });
      }
    })
  },
  onReachBottom: function () {
    var me = this;
    var currentPage = me.data.commentsPage;
    var totalPage = me.data.commentsTotalPage;
    if (currentPage === totalPage) {
      return;
    }
    var page = currentPage + 1;
    me.getCommentsList(page);
  },

  replyFocus: function (e) {
    var fatherCommentId = e.currentTarget.dataset.fathercommentid;
    var toUserId = e.currentTarget.dataset.touserid;
    var toNickname = e.currentTarget.dataset.tonickname;

    this.setData({
      placeholder: "回复  " + toNickname,
      replyFatherCommentId: fatherCommentId,
      replyToUserId: toUserId,
      commentFocus: true
    });
  },



})