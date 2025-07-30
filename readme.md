## AUTO INPUT CH-EN
Automatically switch CH and EN based on the cursor position to avoid typing and coding errors caused by language switching. It also supports IdeaVim.

根据光标位置自动切换中文和英文输入法，避免因语言切换导致的打字和编码错误。它也支持 IdeaVim。
### 解决了什么问题
AICE 通过集成在IDE内.其可以通过输入位置分析当前处于什么场景,并完成自动切换.
### 使用场景
1. 默认场景: 英文输入法
2. 注释场景: 中文输入法
3. 工具窗口场景: 英文输入法
4. IdeaVIM场景: insert场景之外全为英文输入法,insert场景内,自动切换中英输入法
5. 离开IDE场景: 切换到中文输入法(更具体为默认输入中英文)
### 支持的版本

### 支持输入法:
1. 微软拼音
2. 搜狗输入法
3. 百度输入法
#### 明确不支持的输入法:
1. 微信输入法
### 支持的应用:
其严格按照jetbrain api编写,理论支持所有jetbrain应用,支持如下语言:java,python,javascript,kotlin,yaml,shell,csharp,typescript,php,ruby,swift,go,rust,scala,perl,lua,sql,css,html,xml,c,cpp.

### 使用平台: 
当前暂时只支持windows,未来计划添加linux.由于没有mac电脑,暂无适配的计划
### 更新计划:
#### 1.0.3
- [X] 解决问题 问题(https://github.com/lin-uice/AUTO-INPUT-CH-EN/issues/1)
#### 1.0.4
- [X] 重构插件,轻量化插件,优化插件大小以及响应速度
#### 1.0.4 
- [ ] 光标显示颜色,来展示当前是中文状态还是英文状态
- [ ] 解决bug (https://github.com/lin-uice/AUTO-INPUT-CH-EN/issues/2)
### 关于更新:
目前正在准备秋招项目,等秋招结束后,再次重构并支持更丰富的功能
### 许可证:
GPL
