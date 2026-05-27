# LX Music Player

一个基于 Android 原生开发的音乐播放软件，支持导入自定义音乐源脚本（类似洛雪音乐格式），集成 ExoPlayer 播放器。

![Platform](https://img.shields.io/badge/platform-Android-brightgreen)
![Min SDK](https://img.shields.io/badge/minSdk-21-blue)
![Target SDK](https://img.shields.io/badge/targetSdk-34-brightgreen)
![License](https://img.shields.io/badge/license-Apache--2.0-green)

## 特性

- **多平台音源**: 支持导入自定义音乐源脚本，聚合网易云、QQ 音乐、酷狗、酷我等平台
- **高性能播放**: 集成 ExoPlayer，支持硬件解码和后台播放
- **歌词显示**: 支持 LRC 格式歌词同步显示
- **Material Design 3**: 美观流畅的现代化 UI
- **收藏管理**: 本地收藏和历史记录管理
- **播放队列**: 支持多种播放模式（单曲循环、列表循环、随机播放）

## 技术栈

- **语言**: Java 17
- **UI 框架**: Material Design 3 Components
- **架构模式**: MVVM + Repository
- **网络库**: OkHttp + Gson
- **数据库**: SQLite + Room
- **播放器**: ExoPlayer (androidx.media3)
- **JS 引擎**: QuickJS (音乐源脚本执行)

## 项目结构

```
lx-music-player/
├── app/
│   ├── src/main/
│   │   ├── java/cn/lxmusic/player/
│   │   │   ├── activity/          # Activity 类
│   │   │   ├── adapter/           # RecyclerView 适配器
│   │   │   ├── db/                # Room 数据库
│   │   │   ├── model/             # 数据模型
│   │   │   ├── player/            # 播放器封装
│   │   │   ├── service/           # 后台服务
│   │   │   ├── source/            # 音乐源管理
│   │   │   └── util/              # 工具类
│   │   ├── res/                   # Android 资源
│   │   │   ├── layout/            # 布局文件
│   │   │   ├── values/            # 字符串、颜色、主题
│   │   │   ├── drawable/          # 图片资源
│   │   │   └── menu/              # 菜单资源
│   │   └── AndroidManifest.xml    # 应用清单
│   └── build.gradle               # App 构建配置
├── .monkeycode/
│   ├── specs/lx-music-player/
│   │   ├── requirements.md        # 需求文档
│   │   └── design.md              # 技术设计
│   └── docs/                      # 开发文档
├── build.gradle                   # 根构建配置
└── README.md                      # 项目说明
```

## 快速开始

### 环境要求

- Android Studio Arctic Fox (2020.3.1) 或更高版本
- JDK 17
- Android SDK 34

### 构建步骤

1. 克隆仓库
```bash
git clone https://github.com/YOUR_USERNAME/lx-music-player.git
cd lx-music-player
```

2. 使用 Android Studio 打开项目

3. 同步 Gradle 依赖
```
点击 "Sync Now" 或执行：File → Sync Project with Gradle Files
```

4. 运行应用
```
点击 Run 按钮 或 Shift+F10
```

### 音乐源导入

应用启动后：

1. 进入 设置 → 音乐源管理
2. 点击 "在线导入" 或 "本地导入"
3. 导入后启用音乐源即可使用

推荐的开源音乐源：
- 六音音源：https://raw.githubusercontent.com/pdone/lx-music-source/main/sixyin/latest.js
- Huibq 音源：https://raw.githubusercontent.com/pdone/lx-music-source/main/huibq/latest.js
- Flower 音源：https://raw.githubusercontent.com/pdone/lx-music-source/main/flower/latest.js

## 音乐源脚本格式

```javascript
module.exports = {
    info: {
        name: "音源名称",
        version: "1.0.0",
        platforms: ["netease", "qq", "kugou"]
    },
    
    search: function(keyword) {
        return {
            code: 0,
            data: {
                songs: [
                    {
                        id: "song_id",
                        title: "歌曲名",
                        artist: "歌手",
                        album: "专辑",
                        duration: 240000,
                        cover: "封面 URL"
                    }
                ]
            }
        };
    },
    
    getPlayUrl: function(songId) {
        return {
            code: 0,
            data: {
                url: "播放 URL",
                quality: 1,
                format: "mp3"
            }
        };
    },
    
    getLyrics: function(songId) {
        return {
            code: 0,
            data: {
                lrc: "[00:00.00] 歌词内容"
            }
        };
    }
};
```

## 已知问题

1. 部分音乐源可能需要网络代理才能访问
2. 某些平台的 VIP 歌曲无法获取播放链接
3. 首次加载音乐源可能需要几分钟时间

## 开发计划

- [x] 基础播放器功能
- [x] 音乐源脚本导入
- [x] 聚合搜索
- [x] 歌词显示
- [ ] 收藏管理
- [ ] 历史记录
- [ ] 播放队列优化
- [ ] 通知栏控制
- [ ] 深色主题
- [ ] 桌面歌词
- [ ] 下载功能

## 免责声明

1. 本项目仅供学习交流使用，不存储任何音频数据
2. 所有音乐数据来源于第三方平台，本项目不对其合法性负责
3. 请在 24 小时内删除使用本项目产生的版权数据
4. 请尊重音乐版权，支持正版音乐

## 许可证

本项目采用 Apache License 2.0 许可证。详见 [LICENSE](LICENSE)

## 参考项目

- [洛雪音乐移动版](https://github.com/lyswhut/lx-music-mobile) - React Native 版本
- [ExoPlayer](https://exoplayer.dev/) - Google 官方播放器
- [Room Database](https://developer.android.com/training/data-storage/room) - Android 本地数据库

## 联系方式

如有问题或建议，请提 Issue 或联系开发者。

---

**注意**: 本项目完全免费，不接受任何商业合作及捐赠。
