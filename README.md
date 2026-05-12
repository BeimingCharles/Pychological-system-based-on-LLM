# 基于大语言模型（LLM）的智能心理辅导与诊断系统
(Psychological Counseling and Diagnosis System based on LLM)

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Python](https://img.shields.io/badge/Backend-Flask_Python-3776AB.svg)

## 📖 项目简介
本项目（大学生创新创业训练计划项目）旨在探索大语言模型（LLM）在心理健康领域的深度应用。系统前后台分离，采用 **前端（Android 客户端） + 后端（Python Flask 服务）** 的架构，搭建了一个智能化的在线心理辅导平台。

系统不仅能根据用户的实时输入进行具备情感共鸣与心理学专业知识的**对话疏导**，还集成了一套标准化的**心理问卷测评系统**（结合图谱分析与打分算法），并将用户的心理状态与测试结果进行多维度的**数据可视化画像**，帮助用户更直观地认知自身心理健康状况。

> **版本说明**：当前核心迭代版本为 `12.26`。项目中保留的 `1.6` 和 `11.27` 为移动端的历史开发分支及备份记录。

---

## ✨ 核心特性与模块划分

### 🧠 智能辅导引擎 (LLM + Chat)
- **多轮情感拟真交互**：融合心理学咨询技巧设定 Prompt，使大模型扮演专业心理咨询师。
- **动态上下文追踪**：基于后端的会话状态管理，有效记住用户的背景信息和情绪前情。

### 📋 心理测评体系 (QA & Profile)
- **多维度心理问卷**：内置专业心理量表逻辑（涵盖抑郁、焦虑、压力等常见量表测评）。
- **自动化诊断分析计算**：收卷后，后端（`qa_result.py`）将进行加权计算、常模对比，生成诊断摘要。

### 📊 数据可视化与图谱化 (Echarts UI)
- **雷达图与趋势线**：客户端内嵌 `ECharts.js`，无缝桥接 Java/Kotlin 数据，动态渲染精美统计图表（`assets/chart.html`）。
- **用户档案追踪**：系统记录用户历次聊天情绪分析及测评成绩，形成连续的心理波动追踪曲线。

---

## 🛠️ 技术架构选型

### 🚀 前端 (Android 移动端)
- **架构/语言**：采用 Kotlin / Java 混编，基于 标准 Android 原生架构 开发。
- **构建系统**：采用现代化的 Gradle Kotlin DSL (`build.gradle.kts`) 和 `libs.versions.toml` 统一依赖版本管理。
- **UI & 渲染**：利用 Android 原生 `XML` 布局，并搭配 `WebView` 结合 `ECharts` 构建跨界面的数据看板。
- **网络通信**：标准的 HTTP Client（推测为 OkHttp/Retrofit）与后端接口进行 JSON 数据交换。

### ⚙️ 后端 (Python Flask 服务)
- **核心框架**：Python 3.x + `Flask` 轻量级 Web 框架
- **工程结构**：
  - `blueprints/auth.py`：负责注册、登录、Token 发放与鉴权验证。
  - `blueprints/chat.py`：负责对接 LLM API（如 ChatGPT/文心一言等），处理流式/非流式对话。
  - `blueprints/qa.py` & `qa_result.py`：问卷拉取、答题提交及结果算法评定。
  - `blueprints/profile.py`：用户个人档案、心理健康历史报告管理。
  - `models.py` & `exts.py`：数据库 ORM 模型设计与扩展管理（推测使用 SQLAlchemy 等）。
- **知识图谱/数据工具**：`tools/create_graph.py` 辅助构建心理学专业知识的数据结构或图谱关联。

---

## 📂 详细目录说明

```text
📦 Pychological-system-based-on-LLM
 ┣ 📂 app                   # ⭐️ [前端] Android 核心移动端代码目录
 ┃ ┣ 📂 src/main/java       # 前端 Java/Kotlin 源代码
 ┃ ┣ 📂 src/main/res        # 界面布局、动画、图标等静态资源
 ┃ ┗ 📂 src/main/assets     # 本地网页资源 (包含 ECharts 库与图表视图 chart.html)
 ┣ 📂 12.9python            # ⭐️ [后端] Python Flask 服务器端代码目录
 ┃ ┣ 📂 blueprints          # 路由蓝图 (Auth授权, Chat对话, QA测评, Profile履历)
 ┃ ┣ 📂 tools               # 后端脚本工具 (create_graph.py 图谱生成/数据清洗)
 ┃ ┣ 📜 app.py              # Flask 服务启动入口
 ┃ ┣ 📜 config.py           # 服务器配置文件 (DB配置, 密钥, LLM API keys等配置处)
 ┃ ┣ 📜 exts.py             # Flask 扩展初始化 (如 db 绑定)
 ┃ ┣ 📜 models.py           # 数据库映射表结构实体
 ┃ ┗ 📜 requirements.txt    # Python 库依赖单
 ┣ 📂 1.6 & 11.27           # 移动端旧版归档 / 里程碑备份
 ┣ 📜 build.gradle.kts      # Android 工程级构建脚本
 ┣ 📜 settings.gradle.kts   # Android 模块配置挂载
 ┗ 📜 README.md             # 本项目自述说明文件
```

---

## 🏃‍♂️ 部署与运行指南

### Step 1: 后端环境配置与运行
1. **安装环境**：确保本地已安装 Python 3.8 或更高版本。
2. **创建虚拟环境（推荐）**：
   ```bash
   cd 12.9python
   python -m venv venv
   # Windows 系统激活
   venv\Scripts\activate 
   # macOS/Linux 激活
   # source venv/bin/activate
   ```
3. **安装依赖**：
   ```bash
   pip install -r requirements.txt
   ```
4. **配置参数**：
   - 打开 `config.py`，配置你的数据库连接信息 (MySQL/SQLite等)。
   - 在配置中填入所使用的 **大模型 API Key**（如 OpenAI Key、百度千帆 API 密钥等）。
   - 初始化数据库表结构（如项目中存在对应的 init db 指令/脚本）。
5. **启动服务**：
   ```bash
   python app.py
   ```
   > 正常启动后，终端将显示运行由于 `http://127.0.0.1:5000`。请记录下本机的 **局域网 IPv4 地址** (如 `192.168.1.100`)。

### Step 2: Android 客户端编译与真机调试
1. **环境准备**：下载安装最新版 [Android Studio](https://developer.android.com/studio)。
2. **导入项目**：打开 Android Studio，选择 `Open`，选中本项目的根目录。
3. **环境变量链接**：
   - 全局搜索网络请求工具类或常量类（如 `RetrofitClient` 或 `Constants.java`）。
   - 将服务端的 `BASE_URL` 改为你刚记录的 **后端电脑的局域网 IP**：`http://192.168.x.x:5000/`。
   - *(确保电脑和手机连在同一个 WiFi 下)*
4. **编译与运行**：等待 Gradle Build 进度条完成后，点击绿色的 `▶ Run` 按钮，将 APP 安装至安卓模拟器或安卓物理真机。

---

## 📝 接口文档 (API Reference)
*简述蓝图(Blueprints)所开放的核心模块路径：*
- `/auth/`: `POST /login`, `POST /register`
- `/chat/`: `POST /message`, `GET /history`
- `/qa/`: `GET /questions`, `POST /submit`
- `/qa_result/`: `GET /report/<id>`
- `/profile/`: `GET /info`, `PUT /update`
> *具体参数与返回值定义，请参见后端各 Blueprint 路由函数注释。*

---

## 🤝 参与贡献
欢迎作为共同开发者参与：
1. Fork 本仓库。
2. 新建分支 (`git checkout -b feature/AwesomeFeature`)。
3. 提交更改 (`git commit -m 'Add some AwesomeFeature'`)。
4. 推送到分支 (`git push origin feature/AwesomeFeature`)。
5. 开启一个 Pull Request。

## 🛡️ License
Distributed under the MIT License. See `LICENSE` for more information.
