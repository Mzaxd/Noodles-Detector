# Noodles-Detector

Detector For All Platform

Noodles项目的探测器 用于安装在需要管理的物理机上

## 使用方式

1. 启动NoodleDetectorApplicationTests中的生成UUID方法，并将控制台中打印出来的UUID复制到application.yml文件下的对应字段后
2. 在application.yml中配置RabbitMq地址
3. 通过Maven打成Jar包
4. 通过java -jar 部署到需要管理的物理机上

## 注意事项

> 默认端口为7070
