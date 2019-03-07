const app=getApp()

Page({

 

  /**
   * 用户注册
   */
  doRegist:function(e){
    var form=e.detail.value;
    var username=form.username;
    var password=form.password;

    if(username.length==0||password.length==0){
      wx.showToast({
        title:'用户名或密码不能为空',
        icon:'none',
        duration:3000
      })
    }else{
      var serverUrl = app.serverUrl;
      wx.request({
        url: serverUrl +'/regist',
        method:"POST",
        data:{
          username:username,
          password:password
        },
        header:{
          'content-type':'application/json'
        },
        success:function(res){
          var status=res.data.status;
          if(status==200){
            wx.showToast({
              title: "用户注册成功~！！",
              icon:'none',
              duration:3000
            }),
              app.setGlobalUserInfo(res.data.data);
            wx.navigateTo({
              url: '../userLogin/login',
            })
          }else if(status==500){
            wx.showToast({
              title: res.data.msg,
              icon:'none',
              duration:3000
            })
          }
        }

      })
    }

  },
  goLoginPage:function(e){
    wx.navigateTo({
      url: '../userLogin/login',
    })
  }

})