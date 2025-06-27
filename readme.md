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
5. 离开IDE场景: 切换到中文输入法
### 支持输入法:
1. 微软拼音
2. 搜狗输入法
3. 百度输入法
#### 明确不支持的输入法:
1. 微信输入法
### 支持的应用:
其严格按照jetbrain api编写,默认支持所有IDE
### 使用平台: 
当前暂时只支持windows
### 关于更新:
目前正在准备秋招项目,等秋招结束后,再次重构并支持更丰富的功能