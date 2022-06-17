# FaithlPack 现代化仓库插件

### 概述
FaithlPack 是一款基于TabooLib + Kotlin的服务端插件，且时刻保持依赖为最新版本。

### 特性
- **自动拾取**
- **仓库分类**
  - 物品名称
  - 物品Lore
  - 物品NBT
  - 物品材料
  - Zaphkiel
- 禁止指定物品放入
- 仓库绑定
- 仓库权限
- 支持多仓库
- 支持多页
- 支持**查水表**
- 支持槽位解锁
- 支持通过Kether编写自己的**槽位解锁**逻辑
- 数据库支持
- 开源免费
- 采用物品填充的模式
- 可动态配置仓库

### 反馈
- [交流群](https://jq.qq.com/?_wv=1027&k=zYnpVX42) (107659088)
- [Github Issue](https://github.com/Leosouthey/FaithlPack/issues)

### 交流群
我们强烈建议您加入交流群，您可以在此快速高效的反馈插件BUG。  
[点击加入](https://jq.qq.com/?_wv=1027&k=zYnpVX42) (107659088)

### 使用方法
[文档](https://pack.faithl.com)
另请参阅下方注意事项

### 注意事项
**破坏性更新**:
本插件已经迭代到V2，重写全部代码，数据需要迁移。

**数据迁移**:
*! 迁移前请备份数据。* (该功能为实验性功能未经测试)
1. 如果你使用
  - SQLite: 将插件配置文件目录下的db文件复制到PackBridge的配置文件中。
  - MySQL: 将V1的数据表faithlpack_data改为其它名称。
2. 安装V2，卸载V1
3. 下载并安装名为 `Bridge` 的资源。
4. 填写配置文件。（填写你改后的配置）
5. 执行指令 /faithlbridge
6. 重启服务器。

**更新内容**:
- 优化数据结构
- 优化数据缓存
- 重新设计
- 修复原有BUG
- 在V2中你可以随意更改仓库行数，仓库默认槽位而不需要重新清空仓库数据。
- 新增手动保存数据指令
- 新增清空玩家仓库指令

### 插件截图
![image.png](https://attachment.mcbbs.net/public/resource/6760524e-b605-4e21-853c-def7969f0f2b.png)
![image.png](https://attachment.mcbbs.net/public/resource/6b6baf5a-d032-4a7f-b2f8-b26c50904524.png)
![image.png](https://attachment.mcbbs.net/public/resource/84cc510d-93f2-4e93-a2bb-4920f40def21.png)
![image.png](https://attachment.mcbbs.net/public/resource/310cd0ca-263d-4dfe-affa-5369e6bdff3f.png)
![image.png](https://attachment.mcbbs.net/public/resource/00cd0718-85d7-4410-be54-3f07d53889c9.png)
![image.png](https://attachment.mcbbs.net/public/resource/47fdae37-099b-4cff-a3ab-56fdceb12aa8.png)

### 关于作者
作者已经开学，近期很少活动。

### 统计
![Bstats](https://bstats.org/signatures/bukkit/FaithlPack.svg)

### 插件开源
开源协议: GPL-3.0  
开源地址: [Github](https://github.com/Leosouthey/FaithlPack/)  
如果您喜欢本插件，别忘了到Github点一颗小心心。  
本插件所用所有代码均为原创,不存在借用/抄袭等行为  