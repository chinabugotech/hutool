<p align="center">
	<a href="https://hutool.cn/"><img src="https://plus.hutool.cn/images/hutool.svg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬Make Java Sweet Again.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

## 📚Hutool-socket 模块介绍

Socket（套接字）是计算机网络中进行通信的端点，它是TCP/IP协议栈中应用层与传输层之间的抽象接口。Socket允许应用程序通过网络进行通信，实现不同设备之间的数据传输。
在Java中，Socket编程通常涉及创建套接字（`java.net.Socket`）对象，并通过该对象进行数据的发送和接收。、

### 通信模式

1. **BIO（Blocking IO）**：同步阻塞IO，每个连接对应一个线程
2. **NIO（Non-blocking IO）**：同步非阻塞IO，单线程处理多个连接
3. **AIO（Asynchronous IO）**：异步非阻塞IO，基于事件驱动

`Hutool-socket`NIO和AIO做了简单的封装，用于简化Socket异步开发。

-------------------------------------------------------------------------------

## 🛠️包含内容

### 核心组件

| 组件 | 说明 | 包路径 |
|------|------|--------|
| SocketUtil | Socket工具类，提供常用Socket操作 | cn.hutool.v7.socket |
| SocketConfig | Socket配置类 | cn.hutool.v7.socket |
| SocketRuntimeException | Socket运行时异常 | cn.hutool.v7.socket |
| ChannelUtil | 通道工具类 | cn.hutool.v7.socket |

### AIO实现

| 组件 | 说明 | 包路径 |
|------|------|--------|
| AioClient | AIO客户端 | cn.hutool.v7.socket.aio |
| AioServer | AIO服务器 | cn.hutool.v7.socket.aio |
| AioSession | AIO会话 | cn.hutool.v7.socket.aio |
| IoAction | IO动作接口 | cn.hutool.v7.socket.aio |
| SimpleIoAction | 简单IO动作实现 | cn.hutool.v7.socket.aio |

### NIO实现

| 组件 | 说明 | 包路径 |
|------|------|--------|
| NioClient | NIO客户端 | cn.hutool.v7.socket.nio |
| NioServer | NIO服务器 | cn.hutool.v7.socket.nio |
| ChannelHandler | 通道处理器 | cn.hutool.v7.socket.nio |
| Operation | 操作类型枚举 | cn.hutool.v7.socket.nio |

### UDP实现

| 组件 | 说明 | 包路径 |
|------|------|--------|
| PacketBuilder | UDP数据包构建器 | cn.hutool.v7.socket.udp |
| UdpSession | UDP会话 | cn.hutool.v7.socket.udp |