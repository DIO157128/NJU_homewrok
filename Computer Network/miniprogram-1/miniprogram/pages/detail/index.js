/* 详情组件 */

Page({
  // 保存_id 和详细信息
  data: {
    _id: '',
    todo: {
      title: '',
      desc: ''
    },
    freqOptions: ['私密', '公开'],
    groupId: ''
  },

  onLoad(options) {
    // 保存上一页传来的 _id 字段
    if (options.id !== undefined) {
      this.setData({
        _id: options.id
      })
    }
    console.log(options.id)
  },

  // 根据 _id 值查询并显示
  async onShow() {
    if (this.data._id.length > 0) {
      const db = await getApp().database()
      // 根据 _id 值查询数据库
      db.collection(getApp().globalData.collection).where({
        _id: this.data._id
      }).get().then(res => {
        console.log(res)
        const {
          data: [todo]
        } = res
        // 将数据保存到本地、更新显示
        this.setData({
          todo,
          groupId: res.data.groupId
        })
      })
    }
  },

  // 跳转响应函数
  toFileList() {
    wx.navigateTo({
      url: '../file/index?id=' + this.data._id,
    })
  },
  
  toEditPage() {
    var app = getApp()
    var currentId = app.globalData.currentGroupId
    if(currentId != this.data.groupId) {
      wx.showToast({
        title: '您不是该项目成员，无编辑权限哦！',
        icon: 'none',
        duration: 2000//持续的时间
      })
      return
    }
    wx.navigateTo({
      url: '../edit/index?id=' + this.data._id,
    })
  }
})