# LX Music Player 构建指南

## 环境要求

- **JDK**: 17 或更高版本
- **Android SDK**: API 34
- **Gradle**: 8.0+
- **Android Studio**: Arctic Fox (2020.3.1) 或更高版本

## 构建方式

### 方式一：Android Studio (推荐)

1. **打开项目**
   ```
   Android Studio → File → Open → 选择 lx-music-player 目录
   ```

2. **同步 Gradle**
   ```
   点击 "Sync Now" 或 File → Sync Project with Gradle Files
   ```

3. **构建 APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

4. **查看 APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### 方式二：命令行 (需要配置环境)

1. **安装 JDK 17**
   - 下载：https://adoptium.net/
   - 设置环境变量：
     ```bash
     export JAVA_HOME=/path/to/jdk-17
     export PATH=$JAVA_HOME/bin:$PATH
     ```

2. **克隆项目**
   ```bash
   git clone https://github.com/wzq7340835/lx-music-player.git
   cd lx-music-player
   ```

3. **构建 Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```

4. **查看 APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### 方式三：GitHub Actions (自动构建)

1. **Push 到 GitHub**
   ```bash
   git push origin main
   ```

2. **等待构建完成**
   - 访问：https://github.com/wzq7340835/lx-music-player/actions
   - 构建成功后，在 Artifacts 中下载 APK

## 构建 Release APK

### 1. 生成签名密钥

```bash
keytool -genkey -v -keystore lx-music-release-key.keystore -alias lx-music -keyalg RSA -keysize 2048 -validity 10000
```

### 2. 配置签名

在 `app/build.gradle` 的 `android` 块中添加：

```gradle
signingConfigs {
    release {
        storeFile file('lx-music-release-key.keystore')
        storePassword 'YOUR_STORE_PASSWORD'
        keyAlias 'lx-music'
        keyPassword 'YOUR_KEY_PASSWORD'
    }
}

android {
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 3. 构建 Release APK

```bash
./gradlew assembleRelease
```

### 4. 查看 APK
```
app/build/outputs/apk/release/app-release.apk
```

## 常见问题

### 1. SDK 未安装

**错误**: `SDK location not found`

**解决**:
- 方式 1: 使用 Android Studio 打开项目，它会自动下载 SDK
- 方式 2: 手动设置 `local.properties`:
  ```properties
  sdk.dir=/path/to/Android/sdk
  ```

### 2. Gradle 版本不匹配

**错误**: `Gradle version incompatible`

**解决**:
- 更新 Android Studio 到最新版本
- 或修改 `gradle/wrapper/gradle-wrapper.properties`:
  ```properties
  distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
  ```

### 3. 空间不足

**错误**: `No space left on device`

**解决**:
```bash
./gradlew clean
rm -rf ~/.gradle/caches/
```

### 4. 网络问题

**错误**: `Could not resolve dependencies`

**解决**:
- 配置国内镜像（阿里云）
- 在 `build.gradle` 中添加:
  ```gradle
  repositories {
      maven { url 'https://maven.aliyun.com/repository/google' }
      maven { url 'https://maven.aliyun.com/repository/public' }
      google()
      mavenCentral()
  }
  ```

## APK 安装

### 方式一：ADB 安装

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 方式二：直接传输

1. 将 APK 文件传输到 Android 设备
2. 在设备上打开 APK 文件
3. 允许"未知来源"安装
4. 点击安装

## 版本说明

| 构建类型 | 用途 | 签名 | 优化 |
|---------|------|------|------|
| Debug | 开发测试 | 自动签名 | 无优化 |
| Release | 正式发布 | 需要签名 | ProGuard 混淆 |

## 性能优化建议

1. **启用 R8**
   ```gradle
   buildTypes {
       release {
           minifyEnabled true
           shrinkResources true
       }
   }
   ```

2. **APK 拆分**
   ```gradle
   splits {
       abi {
           enable true
           reset()
           include 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
       }
   }
   ```

3. **Multidex** (如果方法数超过 64K)
   ```gradle
   defaultConfig {
       multiDexEnabled true
   }
   ```

---

**构建时间**: 约 2-5 分钟 (首次构建较长)
**APK 大小**: Debug 约 20-30MB / Release 约 15-20MB
