// pages/center/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        canIUse: wx.canIUse('button.open-type.getUserInfo'),
        isHide: true
    },

    onLoad: function() {
        const app = getApp()
        var that = this;
        // 查看是否授权
        wx.cloud.callFunction({
            name: 'getOpenId', //云函数的名字
            data:{}, //传参
            success: (res) => {
            console.log('返回的结果:',res)
            app.globalData.userInfo.openId = res.result.openid;
            console.log(app.globalData.userInfo.openId)
            }
        })
        getApp().getOpenId().then(async openid => {
            // 根据 _openId 数据，查询并展示待办列表
            const db = await getApp().database()
            db.collection("map").where({
              userid: openid
            }).get().then(res => {
              const {
                data
              } = res
              // 存储查询到的数据
              console.log(data)
              var templist = []               
              for (var i = 0; i<data.length;i++){
                  templist.push(data[i].groupId)
              }

              if (templist.length != 0){
                  app.globalData.currentGroupId = templist[0]
              }
              app.globalData.groupList = templist
              console.log(app.globalData.groupList)
            })
          })


        wx.getSetting({
        success: function(res) {
        if (res.authSetting['scope.userInfo']) {
            wx.getUserInfo({
            success: function(res) {
            // 用户已经授权过,不需要显示授权页面,所以不需要改变 isHide 的值
            wx.login({
            success: res => {
                // 获取到用户的 code 之后：res.code
                console.log("用户的code:" + res.code);
            }
            });
            }
            });
        } else {
            // 用户没有授权
            // 改变 isHide 的值，显示授权页面
            that.setData({
            isHide: true
            });
        }
        }
        });
        },
    
    bindGetUserInfo: function(e) {
        const app = getApp()
        if (e.detail.userInfo) {
        //用户按了允许授权按钮
        var that = this;
        // 获取到用户的信息了，打印到控制台上看下
        console.log("用户的信息如下：");
        console.log(e.detail.userInfo);
        app.globalData.userInfo.nickName = e.detail.userInfo.nickName
        //授权成功后,通过改变 isHide 的值，让实现页面显示出来，把授权页面隐藏起来
        console.log(app.globalData.userInfo.nickname)
        that.setData({
        isHide: false
        });
        } else {
        //用户按了拒绝按钮
        wx.showModal({
        title: '警告',
        content: '您点击了拒绝授权，将无法进入小程序，请授权之后再进入!!!',
        showCancel: false,
        confirmText: '返回授权',
        success: function(res) {
            // 用户没有授权成功，不需要改变 isHide 的值
            if (res.confirm) {
            console.log('用户点击了“返回授权”');
            }
        }
        });
        }
    }
})