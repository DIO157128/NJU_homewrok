// pages/proExchange/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        groups: [],
        currentId: ''
    },

    radioChange(e) {
        console.log('radio发生change事件，携带value值为：', e.detail.value)
        const app = getApp()
        app.globalData.currentGroupId = e.detail.value
        console.log(app.globalData.currentGroupId)

    
        // this.setData({
        //   groups
        // })
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
        getApp().getOpenId().then(async openid => {
            // 根据 _openId 数据，查询并展示待办列表
            const db = await getApp().database()

            db.collection("group").get().then(res => {
              const {
                data
              } = res
              // 存储查询到的数据
              var app = getApp()
              var temp = app.globalData.groupList
              var t2 = []
              for (var i = 0; i < data.length; i++) {
                if((temp.indexOf(data[i].groupId)) != -1) {
                  t2.push(data[i])
                }
              }
              this.setData({
                groups: t2,
                currentId: app.globalData.currentGroupId
              })
            })
          })
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