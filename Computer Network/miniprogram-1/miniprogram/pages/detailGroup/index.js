// pages/detailGroup/inde.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        groupId:"",
        todos: [], // 用户的所有待办事项
        pending: [], // 未完成待办事项
        finished: [] // 已完成待办事项
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        // 保存上一页传来的 groupId 字段，用于后续查询待办记录
    if (options.id !== undefined) {
        this.setData({
          groupId: options.id
        })
      }
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
        // 通过云函数调用获取用户 _openId
        getApp().getOpenId().then(async openid => {
          // 根据 _openId 数据，查询并展示待办列表
          const db = await getApp().database()
          db.collection(getApp().globalData.collection).get().then(res => {
            const {
              data
            } = res
            // 存储查询到的数据
            var tempRes = []
            var app = getApp()
            // &&(this.data.groupId == app.globalData.currentGroupId || data[i].pri == 1)
            for(var i = 0; i < data.length; i++) {
              if (data[i].groupId == this.data.groupId&&(this.data.groupId == app.globalData.currentGroupId || data[i].pri == 1)){
                tempRes.push(data[i])
              }
            }
            console.log(tempRes)
    
            this.setData({
              // data 为查询到的所有待办事项列表
              todos: tempRes,
              // 通过 filter 函数，将待办事项分为未完成和已完成两部分
              pending: data.filter(todo => todo.freq === 0),
              finished: data.filter(todo => todo.freq === 1)
            })
          })
        })
        // 配置首页左划显示的星标和删除按钮
        this.setData({
          slideButtons: [{
            extClass: 'starBtn',
            text: '星标',
            src: '../../images/list/star.png'
          }, {
            type: 'warn',
            text: '删除',
            src: '../../images/list/trash.png'
          }],
        })
      },

      // 响应左划按钮事件
  async slideButtonTap(e) {
    // 得到触发事件的待办序号
    const {
      index
    } = e.detail
    // 根据序号获得待办对象
    const todoIndex = e.currentTarget.dataset.index
    const todo = this.data.todos[todoIndex]
    const db = await getApp().database()
    // 处理星标按钮点击事件
    if (index === 0) {
      // 根据待办的 _id 找到并反转星标标识
      db.collection(getApp().globalData.collection).where({
        _id: todo._id
      }).update({
        data: {
          star: !todo.star
        }
      })
      // 更新本地数据，触发显示更新
      todo.star = !todo.star
      this.setData({
        todos: this.data.todo
      })
    }
    // 处理删除按钮点击事件
    if (index === 1) {
      // 根据待办的 _id 找到并删除待办记录
      db.collection(getApp().globalData.collection).where({
        _id: todo._id
      }).remove()
      // 更新本地数据，快速更新显示
      this.data.todos.splice(todoIndex, 1)
      this.setData({
        todos: this.data.todo
      })
      // 如果删除完所有事项，刷新数据，让页面显示无事项图片
      if (this.data.todos == 0) {
        this.setData({
          todos: [],
          pending: [],
          finished: []
        })
      }
    }
  },

  // 点击左侧单选框时，切换待办状态
  async finishTodo(e) {
    // 根据序号获得触发切换事件的待办
    const todoIndex = e.currentTarget.dataset.index
    const todo = this.data.pending[todoIndex]
    const db = await getApp().database()
    // 根据待办 _id，获得并更新待办事项状态
    db.collection(getApp().globalData.collection).where({
      _id: todo._id
    }).update({
      // freq == 1 表示待办已完成，不再提醒
      // freq == 0 表示待办未完成，每天提醒
      data: {
        freq: 1
      }
    })
    // 快速刷新数据
    todo.freq = 1
    this.setData({
      pending: this.data.todos.filter(todo => todo.freq === 0),
      finished: this.data.todos.filter(todo => todo.freq === 1)
    })
  },

  // 同上一函数，将待办状态设置为未完成
  async resetTodo(e) {
    const todoIndex = e.currentTarget.dataset.index
    const todo = this.data.finished[todoIndex]
    const db = await getApp().database()
    db.collection(getApp().globalData.collection).where({
      _id: todo._id
    }).update({
      data: {
        freq: 0
      }
    })
    todo.freq = 0
    this.setData({
      pending: this.data.todos.filter(todo => todo.freq === 0),
      finished: this.data.todos.filter(todo => todo.freq === 1)
    })
  },

  // 跳转响应函数
  toFileList(e) {
    const todoIndex = e.currentTarget.dataset.index
    const todo = this.data.pending[todoIndex]
    wx.navigateTo({
      url: '../file/index?id=' + todo._id,
    })
  },

  toDetailPage(e) {
    const todoIndex = e.currentTarget.dataset.index
    const todo = this.data.todos[todoIndex]
    wx.navigateTo({
      url: '../detail/index?id=' + todo._id,
    })
  },

  toAddPage() {
    wx.navigateTo({
      url: '../../pages/add/index',
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