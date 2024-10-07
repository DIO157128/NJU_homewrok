// pages/joinPro/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        groupId: ''
    },

    onGroupInput(e) {
        this.setData({
          groupId: e.detail.value
        })
    },

    
    async joinGroup() {
        var app = getApp()
        var openId = app.globalData.userInfo.openId
        console.log(openId)
        const db = await getApp().database()
        db.collection('map').add({
            data:{
                userid: openId,
                groupId: this.data.groupId
            }
        }).then(() => {
            wx.navigateBack({
              delta: 0,
            })
          })
        app.globalData.groupList.push(this.data.groupId)
    },

    reset() {
        this.setData({
            groupId:''
        })
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