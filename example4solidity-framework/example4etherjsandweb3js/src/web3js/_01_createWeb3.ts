/**
HttpProvider: 因为不能用于订阅，HTTP provider 已经**不推荐使用**。
WebsocketProvider: Websocket provider 是用于传统的浏览器中的标准方法.
IpcProvider: 当运行一个本地节点时，IPC provider 用于 node.js 下的D App 环境，提供最为安全的连接。.
 */

import Web3 from "web3";
import * as net from "net";

var web3 = new Web3("http://localhost:8545");
var web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));

// 在 node.js 中使用 IPC provider
var web3 = new Web3("\\\\.\\pipe\\geth.ipc", new net.Socket({})); // mac os 路径
var web3 = new Web3(
  new Web3.providers.IpcProvider("\\\\.\\pipe\\geth.ipc", net)
);

// 在浏览器中使用 Websocket provider
web3.setProvider("ws://localhost:8546");
web3.setProvider(new Web3.providers.WebsocketProvider("ws://localhost:8546"));
