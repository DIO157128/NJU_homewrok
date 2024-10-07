// pages/square/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        groups: []
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
    async onShow() {
        // getApp().getOpenId().then(async openid => {
            // 根据 _openId 数据，查询并展示待办列表
            const db = await getApp().database()
            db.collection("group").get().then(res => {
              const {
                data
              } = res
              // 存储查询到的数据
              console.log(res)
              this.setData({
                groups: data,
              })
            })
          // })
    },

    toDetailPage(e){
      const groupIndex =  e.currentTarget.dataset.index

      console.log(e.currentTarget.dataset)
      const group = this.data.groups[groupIndex]

      console.log(group)

      wx.navigateTo({
        url: '../detailGroup/index?id=' + group.groupId,
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