# Requirements Document

## Introduction

本项目是一个基于 Android 平台的原生音乐播放软件，支持导入自定义音乐源脚本（类似洛雪音乐格式），集成 ExoPlayer 播放器，提供美观流畅的 UI 界面。软件支持多平台音乐资源聚合搜索、在线播放、歌词显示、收藏管理和历史记录功能。

## Glossary

- **Music Source Script**: 自定义音乐源脚本，基于 JavaScript 格式，提供音乐搜索、播放链接获取等 API
- **ExoPlayer**: Google 开发的 Android 媒体播放器库，支持自适应流媒体和硬件解码
- **Aggregated Search**: 聚合搜索，同时搜索多个音乐平台（网易云、QQ 音乐、酷狗、酷我等）
- **Lyrics3**: LRC 格式歌词，支持逐字同步显示

## Requirements

### REQ-1: 音乐源脚本导入功能

**User Story:** AS 用户，I WANT 通过在线导入或本地文件方式导入音乐源脚本，SO THAT 可以获取不同平台的音乐资源

#### Acceptance Criteria

1. WHEN 用户提供有效的 HTTP/HTTPS URL 时，系统 SHALL 下载并验证 JS 脚本格式
2. WHEN 用户选择本地 JS 文件时，系统 SHALL 读取并验证脚本内容
3. IF 脚本格式无效或包含语法错误时，系统 SHALL 显示友好的错误提示
4. WHILE 导入过程中，系统 SHALL 显示加载进度指示器
5. 系统 SHALL 保存已导入的音乐源列表，支持启用/禁用切换
6. 系统 SHALL 支持脚本版本检测和更新提示

### REQ-2: 聚合音乐搜索功能

**User Story:** AS 用户，I WANT 同时搜索多个音乐平台，SO THAT 快速找到想听的歌曲

#### Acceptance Criteria

1. WHEN 用户输入搜索关键词时，系统 SHALL 在所有启用的音乐源中并行搜索
2. 系统 SHALL 展示搜索结果，包含：歌曲名、歌手、专辑、音源平台标识
3. 系统 SHALL 支持按平台筛选搜索结果
4. 系统 SHALL 支持排序（热度、时间、歌名字母序）
5. WHILE 搜索过程中，系统 SHALL 显示各平台的搜索状态（加载中/完成/失败）
6. 系统 SHALL 支持搜索历史保存和快速复用（最近 20 条）

### REQ-3: ExoPlayer 音频播放

**User Story:** AS 用户，I WANT 使用高性能播放器播放音乐，SO THAT 获得流畅的听歌体验

#### Acceptance Criteria

1. 系统 SHALL 使用 ExoPlayer 作为核心播放器
2. 系统 SHALL 支持硬件解码加速
3. 系统 SHALL 支持暂停、继续、停止、seek 等基本播放控制
4. 系统 SHALL 支持音量调节和系统音量同步
5. 系统 SHALL 支持播放进度实时显示和拖拽调节
6. WHILE 音频缓冲时，系统 SHALL 显示缓冲进度
7. IF 播放失败时，系统 SHALL 显示错误信息并提供自动换源重试选项
8. 系统 SHALL 支持后台播放和通知栏控制

### REQ-4: 歌词同步显示

**User Story:** AS 用户，I WANT 在播放时查看同步歌词，SO THAT 可以跟唱歌曲

#### Acceptance Criteria

1. WHEN 播放歌曲时，系统 SHALL 自动加载并显示 LRC 格式歌词
2. 系统 SHALL 支持逐行高亮显示当前播放位置
3. 系统 SHALL 支持歌词拖动调节进度
4. IF 本地无歌词时，系统 SHALL 尝试从音乐源获取网络歌词
5. IF 加载失败时，系统 SHALL 显示"暂无歌词"提示
6. 系统 SHALL 支持歌词字体大小调节

### REQ-5: 收藏夹管理

**User Story:** AS 用户，I WANT 收藏喜欢的歌曲，SO THAT 可以快速访问和收听

#### Acceptance Criteria

1. 系统 SHALL 支持添加歌曲到收藏夹
2. 系统 SHALL 支持从收藏夹移除歌曲
3. 系统 SHALL 显示所有收藏的歌曲列表
4. 系统 SHALL 显示收藏状态标识（已收藏/未收藏）
5. IF 歌曲已在收藏中，系统 SHALL 提示"已收藏"
6. 收藏夹 SHALL 包含：歌曲名、歌手、专辑、音源 ID、添加时间
7. 系统 SHALL 支持导入/导出收藏列表（JSON 格式）

### REQ-6: 播放历史管理

**User Story:** AS 用户，I WANT 查看和管理播放历史，SO THAT 可以继续收听之前听过的歌曲

#### Acceptance Criteria

1. WHILE 用户播放歌曲时，系统 SHALL 自动记录播放历史
2. 系统 SHALL 显示最近播放历史列表（至少 100 条）
3. 系统 SHALL 支持清空历史记录功能
4. 系统 SHALL 支持单条删除历史记录
5. 历史记录 SHALL 包含：歌曲名、歌手、音源、播放时间戳、播放次数
6. 系统 SHALL 支持按播放时间排序

### REQ-7: 播放列表管理

**User Story:** AS 用户，I WANT 创建和管理播放列表，SO THAT 按顺序收听多首歌曲

#### Acceptance Criteria

1. 系统 SHALL 支持创建播放队列
2. 系统 SHALL 支持添加歌曲到播放队列
3. 系统 SHALL 支持从队列中移除歌曲
4. 系统 SHALL 支持多种播放模式（单曲循环、列表循环、随机播放）
5. 系统 SHALL 显示当前播放队列（当前歌曲标识）
6. 系统 SHALL 支持上下曲快捷切换

### REQ-8: Material Design 3 UI

**User Story:** AS 用户，I WANT 获得美观流畅的界面体验，SO THAT 方便浏览和操作

#### Acceptance Criteria

1. 系统 SHALL 使用 Material Design 3 设计规范
2. 系统 SHALL 支持深色/浅色主题切换
3. 系统 SHALL 适配不同屏幕尺寸（5-7 英寸）
4. 系统 SHALL 支持手势操作（滑动切歌、双击暂停）
5. 系统 SHALL 底部导航包含：首页、发现、我的
6. 系统 SHALL 动画流畅，帧率不低于 60fps

## Data Requirements

### DATA-1: 数据持久化

1. 系统 SHALL 使用 SQLite 数据库存储历史记录和收藏夹
2. 系统 SHALL 使用 SharedPreferences 存储用户设置和音乐源配置
3. 系统 SHALL 使用本地文件缓存歌词和专辑封面图片
4. 系统 SHALL 定期清理超过 90 天的缓存数据

### DATA-2: 网络请求

1. 系统 SHALL 使用 OkHttp 进行网络请求
2. 系统 SHALL 设置 30 秒超时时间
3. 系统 SHALL 支持 HTTPS 加密连接
4. 系统 SHALL 处理网络错误并显示友好提示
5. 系统 SHALL 支持请求重试机制（最多 3 次）

## Quality Requirements

### PERF-1: 性能要求

1. 应用启动时间 SHALL 小于 2 秒
2. 搜索响应时间 SHALL 小于 3 秒（网络良好时）
3. 音频加载时间 SHALL 小于 2 秒（网络良好时）
4. UI 响应时间 SHALL 小于 100 毫秒
5. 内存占用 SHALL 不超过 150MB

### SEC-1: 安全要求

1. 系统 SHALL 不存储任何用户隐私数据到外部存储
2. 系统 SHALL 不使用明文 HTTP 传输敏感数据
3. 系统 SHALL 不请求不必要的权限
4. 系统 SHALL 对音乐源脚本进行安全沙箱隔离

### USAB-1: 可用性要求

1. 界面 SHALL 简洁直观，无需教程即可使用
2. 系统 SHALL 提供中文界面
3. 错误提示 SHALL 清晰易懂，提供解决建议
