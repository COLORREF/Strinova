# Strinova

Minecraft Java Edition 第三人称枪战 Mod。

- Minecraft：26.3
- 模组加载器：NeoForge 26.3.0.95
- Java：25（开发与游戏运行）、21（NeoForm 工具）
- 构建：Gradle 9.2.1、ModDevGradle 2.0.148
- 动画：GeckoLib 5.5.7、Player Animation Library 1.2.8+mc.26.3

## 目录结构

```text
Strinova/
├── .tools/                  # 本地 JDK，不提交
│   ├── jdk-25/              # Temurin JDK 25
│   └── jdk-21/              # Temurin JDK 21
├── .downloads/              # JDK / MDK 安装包，不提交
├── .gradle-user-home/       # Gradle、NeoForge、Minecraft 依赖与缓存，不提交
├── .gradle/                 # 项目级 Gradle 缓存，不提交
├── .tmp/                    # 临时文件，不提交
├── .logs/                   # 构建日志，不提交
├── build/                   # 构建产物与 Minecraft 源码，不提交
├── run/                     # 开发客户端运行目录，不提交
├── docs/                    # 调研报告
├── gradle/wrapper/          # Gradle Wrapper
├── src/                     # Mod 源码
├── build.gradle             # 构建配置
├── settings.gradle          # Gradle 项目设置
├── gradle.properties        # 版本、Mod 信息、本地 JDK 路径
├── gradlew / gradlew.bat    # Gradle Wrapper 启动脚本
├── .gitignore               # Git 忽略规则
├── .gitattributes           # Git 换行符等规则
└── README.md                # 本文件
```

### 各目录说明

| 路径 | 内容 | 是否可删除 | 说明 |
|---|---|---|---|
| `.tools/jdk-25/` | Temurin JDK 25 | 否 | Gradle 与游戏开发使用的 JDK |
| `.tools/jdk-21/` | Temurin JDK 21 | 否 | NeoForm Runtime 的 `downloadAssets` 工具链使用 |
| `.downloads/` | JDK、MDK 等安装包 | 是 | 删除后无法离线重装，重新下载即可 |
| `.gradle-user-home/` | Gradle 发行版、依赖、NeoForge/Minecraft 缓存、assets、daemon 日志 | 是 | 删除后首次构建会重新下载，耗时较长 |
| `.gradle/` | 项目级 Gradle 缓存 | 是 | 自动重建 |
| `.tmp/` | 临时文件 | 是 | 自动重建 |
| `.logs/` | 构建日志 | 是 | 仅用于排查问题 |
| `build/` | 编译产物、Mod jar、Minecraft 补丁 jar 与源码 jar | 是 | 重新执行 `build` 会生成；源码 jar 位于 `build/moddev/artifacts/minecraft-patched-26.3.0.95-sources.jar` |
| `run/` | 开发客户端运行目录（config、logs、mods、存档） | 是 | 删除会丢失本地客户端配置和存档 |
| `src/` | Mod Java 源码与资源模板 | 否 | 核心代码 |
| `docs/` | 版本与加载器调研报告 | 否 | 项目文档 |
| `gradle/wrapper/` | Gradle Wrapper jar 与配置 | 否 | 构建入口 |
| `gradlew`、`gradlew.bat` | 已本地化的 Wrapper 启动脚本 | 否 | 自动设置项目内 JDK 与缓存路径 |
| `build.gradle` | NeoForge、ModDevGradle、GeckoLib、PAL 配置 | 否 | 构建核心配置 |
| `settings.gradle` | Gradle 插件仓库与项目设置 | 否 | 构建入口 |
| `gradle.properties` | 游戏/NeoForge/Mod 版本、JDK 路径 | 否 | 版本与本地化配置 |
| `.gitignore` | Git 忽略规则 | 否 | 防止工具与缓存进入仓库 |
| `.gitattributes` | Git 文件属性 | 否 | 统一换行符等行为 |
| `README.md` | 项目说明 | 否 | 本文件 |

> `repo/` 是 Maven 发布输出目录，只有执行发布任务时才会生成，可删除且不需要提交。

## 本地化路径配置

所有开发文件都限制在项目目录内，不写入用户级 `.gradle`、`.m2` 或全局 JDK 目录。

| 路径或配置 | 作用 |
|---|---|
| `JAVA_HOME = .tools/jdk-25` | Gradle 与游戏使用 JDK 25 |
| `GRADLE_USER_HOME = .gradle-user-home` | Gradle 发行版、依赖、NeoForge/Minecraft 缓存、assets、daemon 日志 |
| `TMP` / `TEMP = .tmp` | Gradle、Java 与子进程的临时文件 |
| `org.gradle.java.installations.paths = .tools/jdk-25,.tools/jdk-21` | 告诉 Gradle 项目内两个 JDK 的位置 |
| `org.gradle.java.installations.auto-detect = false` | 不扫描项目外的 JDK |
| `org.gradle.java.installations.auto-download = false` | 不自动下载 JDK |
| `build/` | Gradle 构建产物 |
| `run/` | ModDevGradle 开发运行目录 |

配置入口：

- `gradlew.bat` 与 `gradlew` 会以脚本所在目录为基准，设置 `JAVA_HOME`、`GRADLE_USER_HOME`、`TMP`、`TEMP`。
- `gradle.properties` 使用相对路径 `.tools/jdk-25,.tools/jdk-21`。
- 因此整个项目目录移动到其他路径后，不需要修改 SDK 路径。

验证本地化是否生效：

```powershell
.\gradlew.bat --version
.\gradlew.bat javaToolchains
```

`--version` 中的 `Daemon JVM` 应指向项目内的 `.tools\jdk-25`；`javaToolchains` 应同时列出 JDK 21 和 JDK 25。

## 复制到另一台电脑

1. 复制整个项目文件夹，包括隐藏目录 `.tools`、`.gradle-user-home`、`.gradle`。
2. 如果希望完全离线，连 `.downloads` 一起复制；否则可以只复制 `.tools` 和 `.gradle-user-home`。
3. 项目路径变化后不需要修改 `gradlew.bat`、`gradlew` 或 `gradle.properties`，因为其中的路径是相对项目根目录的。
4. 复制后验证：

```powershell
.\tools\jdk-25\bin\java.exe -version
.\tools\jdk-21\bin\java.exe -version
.\gradlew.bat javaToolchains
.\gradlew.bat build
```

5. 只复制源码、没有复制 `.tools` 时，按下一节的步骤重新下载 JDK。
6. 只复制源码、没有复制 `.gradle-user-home` 时，首次构建会重新下载 Gradle、NeoForge、Minecraft 与依赖。
7. 想减小体积时，可删除 `.downloads`、`.logs`、`.tmp`、`build`、`run`；这些都能重新生成。
8. 如果使用 IntelliJ IDEA 或 CLion 导入项目，Gradle JVM 请选择 `.tools/jdk-25`。IDE 自身可能会在项目外生成缓存，这部分不受本仓库控制。

## 从 GitHub 克隆

### 需要预先安装或下载

- Windows 10/11 x64
- Git
- PowerShell 5+
- 网络连接
- Temurin JDK 25 与 JDK 21（放入项目内 `.tools`）
- Gradle、NeoForge、Minecraft、GeckoLib、PAL 不需要手动下载，由 Gradle 自动完成

### 1. 克隆仓库

```powershell
git clone https://github.com/COLORREF/Strinova.git
cd Strinova
```

### 2. 下载并放置两个 JDK

```powershell
New-Item -ItemType Directory -Force .downloads,.tmp,.tools | Out-Null

curl.exe -L --retry 5 -o .downloads\jdk25.zip "https://api.adoptium.net/v3/binary/latest/25/ga/windows/x64/jdk/hotspot/normal/eclipse?project=jdk"
curl.exe -L --retry 5 -o .downloads\jdk21.zip "https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse?project=jdk"

Expand-Archive .downloads\jdk25.zip .tmp\jdk25 -Force
$d = Get-ChildItem .tmp\jdk25 -Directory | Select-Object -First 1
Move-Item $d.FullName .tools\jdk-25
Remove-Item .tmp\jdk25 -Recurse -Force

Expand-Archive .downloads\jdk21.zip .tmp\jdk21 -Force
$d = Get-ChildItem .tmp\jdk21 -Directory | Select-Object -First 1
Move-Item $d.FullName .tools\jdk-21
Remove-Item .tmp\jdk21 -Recurse -Force
```

如果 `Expand-Archive` 较慢，可以用 Windows 自带的 `tar -xf` 解压。

### 3. 验证 JDK

```powershell
.\tools\jdk-25\bin\java.exe -version
.\tools\jdk-21\bin\java.exe -version
.\gradlew.bat javaToolchains
```

预期：JDK 25 用于 Gradle/游戏，JDK 21 用于 NeoForm 工具。

### 4. 首次构建

```powershell
.\gradlew.bat build
```

首次构建会自动完成：

- 下载 Gradle 9.2.1 到 `.gradle-user-home/wrapper/dists`
- 下载 NeoForge 26.3.0.95、ModDevGradle、Minecraft 26.3、NeoForm 与运行库到 `.gradle-user-home/caches`
- 下载 GeckoLib 与 PAL 到 `.gradle-user-home/caches`
- 反编译/重编译 Minecraft，生成源码 jar 到 `build/moddev/artifacts`
- 下载 Minecraft assets 到 `.gradle-user-home/caches/neoformruntime/assets`

### 5. 启动开发客户端

```powershell
.\gradlew.bat runClient
```

首次启动会初始化 `run/` 目录；所有配置、日志、存档都留在项目内。

### 6. 可选：使用系统 JDK

如果不希望把 JDK 放在 `.tools`：

1. 安装 JDK 25 和 JDK 21。
2. 修改 `gradle.properties` 中的 `org.gradle.java.installations.paths` 为实际路径。
3. 将 `org.gradle.java.installations.auto-detect` 改为 `true`。
4. 将 `gradlew.bat` / `gradlew` 中的 `JAVA_HOME` 改为系统 JDK 25 路径，或删除该行并让系统 `JAVA_HOME` 生效。

项目内 JDK 方案更适合本仓库，能避免污染全局环境。

## 常见问题

| 问题 | 处理方式 |
|---|---|
| `Cannot find a Java installation ... languageVersion=21` | 缺少 `.tools/jdk-21`，按克隆步骤重新下载 |
| `JAVA_HOME is set to an invalid directory` | 缺少 `.tools/jdk-25`，先下载 JDK 25 |
| `Missing metadata in pack mod/geckolib` | 无害警告，GeckoLib 已加载 |
| GeckoLib Mixin `Class version 69` 警告 | 当前仅日志警告；正式做武器/玩家渲染时需要再验证 |
| GeckoLib 下载失败 | 项目使用 CurseMaven；检查 `https://cursemaven.com` 是否可访问 |
| 构建日志在哪里 | `.logs/` 与 `run/logs/` |
| 是否会在用户目录生成 `.gradle` | 正常情况下不会；Wrapper 会把 `GRADLE_USER_HOME` 指向项目内 |

## 提交规范

提交信息采用以下格式：

```text
<类型>[可选作用域]: <简短描述>

[可选详细描述]

[可选脚注]
```

### 类型

- `feat`：新功能（影响次版本号）
- `fix`：修复 bug（影响修订号）
- `docs`：仅文档修改（如 README）
- `style`：代码格式调整（不影响逻辑，如缩进、空格）
- `refactor`：代码重构（既非新功能也非修复）
- `test`：新增 / 修改测试代码
- `chore`：构建流程、工具配置等修改（不影响业务代码）

### 描述要求

- 简短描述控制在 50 字以内，简洁说明“做了什么”
- 不使用句号结尾
- 详细描述在简短描述不足以说明时补充，换行后开始，写明具体修改点
- 脚注可选，用于补充关联信息

### 示例

```text
docs: 补充项目结构与本地化环境说明

说明目录用途、可删除项，以及复制到其他电脑和从 GitHub 克隆后的环境配置流程
```
