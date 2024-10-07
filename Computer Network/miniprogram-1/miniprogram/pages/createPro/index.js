// pages/createPro/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        title:"",
        desc:"",
        groupId:""
    },

    // 表单输入处理函数
  onTitleInput(e) {
    this.setData({
      title: e.detail.value
    })
  },

  onDescInput(e) {
    this.setData({
      desc: e.detail.value
    })
  },

  // 保存新建组的相关信息
  async createGroup() {
    // 对输入框内容进行校验
    if (this.data.title === '') {
      wx.showToast({
        title: '项目标题未填写',
        icon: 'error',
        duration: 2000
      })
      return
    }
    if (this.data.title.length > 15) {
      wx.showToast({
        title: '项目标题过长',
        icon: 'error',
        duration: 2000
      })
      return
    }
    if (this.data.desc.length > 100) {
      wx.showToast({
        title: '项目描述过长',
        icon: 'error',
        duration: 2000
      })
      return
    }

    //此处实现hash生成groupID
    var app = getApp()
    var openId = app.globalData.userInfo.openId
    var temp = this.hashFunc(openId,openId.length);

    // console.log(temp);
    const db = await getApp().database()
   
    db.collection('map').add({
      data:{
        userid:openId,
        groupId:temp
      }
    })
    app.globalData.groupList.push(temp)
    console.log(app.globalData.groupList)

    wx.showModal({
      title: '确认提示',
      content: '确认新建项目邀请码:' + temp,
      success: res=>{
        if (res.confirm) {
          db.collection('group').add({
            data: {
              title: this.data.title,       // 项目标题
              desc: this.data.desc,         // 项目描述
              groupId:temp
            }
          }).then(() => {
            wx.navigateBack({
              delta: 0,
            })
          })
        } else if (res.cancel) {
          console.log('取消')
        }
      }
    })
  },

  // 重置所有表单项
  reset() {
    this.setData({
      title: '',
      desc: ''
    })
  },

  hashFunc(str,size){
      var hash = 0;
      for(var i = 0; i<str.length;i++){
          hash = 37*hash + str.charCodeAt(i);
      }
      var index = hash%size;
      
      var res = "";
      for(var i = 0; i<str.length;i = i + index){
          res += str.charAt(i);
      }
      res += parseInt(Math.random () * 10000) 
      console.log(res)
      return res;
  },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {

    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})