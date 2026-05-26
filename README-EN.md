<p align="center">
    <a href="https://hutool.cn/"><img alt="hutool" src="https://plus.hutool.cn/images/hutool.svg" width="45%"></a>
</p>
<h3 align="center">🍬Make Java Sweet Again.</h3>
<h4 align="center">👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈</h4>

<p align="center">
    <a href="https://central.sonatype.com/artifact/cn.hutool.v7/hutool-all"><img alt="maven-central" src="https://img.shields.io/maven-central/v/cn.hutool/hutool-all.svg?label=Maven%20Central" /></a>
    <a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img alt="LICENSE" src="https://img.shields.io/:license-apache2.0-blue.svg?logo=apache" /></a>
    <a href="https://www.oracle.com/java/technologies/downloads/#java17"><img alt="jdk17" src="https://img.shields.io/badge/JDK-17+-green.svg" /></a>
    <a href="https://app.travis-ci.com/chinabugotech/hutool"><img alt="travis-ci" src="https://api.travis-ci.com/chinabugotech/hutool.svg?branch=v5-master" /></a>
    <a href="https://codecov.io/gh/chinabugotech/hutool"><img alt="" src="https://app.codacy.com/project/badge/Grade/8a6897d9de7440dd9de8804c28d2871d"/></a>
    <a href="https://codecov.io/gh/cn/hutool"><img alt="" src="https://codecov.io/gh/chinabugotech/hutool/branch/v7-master/graph/badge.svg" /></a>
    <a href='https://gitee.com/chinabugotech/hutool/stargazers'><img alt="star" src='https://gitee.com/chinabugotech/hutool/badge/star.svg'/></a>
    <a href='https://github.com/chinabugotech/hutool'><img alt="github star" src="https://img.shields.io/github/stars/chinabugotech/hutool.svg?style=social"/></a>
    <a href='https://gitcode.com/chinabugotech/hutool'><img src="https://gitcode.com/chinabugotech/hutool/star/badge.svg" alt="gitcode star"/></a>
    <a href="https://deepwiki.com/chinabugotech/hutool"><img src="https://deepwiki.com/badge.svg" alt="Ask DeepWiki"></a>
</p>

<br/>
<p align="center">
    <a href="https://qm.qq.com/q/YsLhyheHSA">
    <img alt="qq" src="https://img.shields.io/badge/QQ%E7%BE%A4%E2%91%A7-667030118-orange"/></a>
</p>

<br/>
<p align="center">
    <a href="https://qm.qq.com/q/YsLhyheHSA">
    <img alt="qq" src="https://img.shields.io/badge/QQ%E7%BE%A4%E2%91%A7-667030118-orange"/></a>
</p>

-------------------------------------------------------------------------------

[**🌎中文说明**](README.md)

-------------------------------------------------------------------------------

## 📚Introduction

**Hutool** is a small but comprehensive library of Java tools, achieved by encapsulation through static methods, reduce
the cost of learning related APIs, increase productivity, and make Java as elegant as a functional programming
language,let the Java be "sweet" too.

**Hutool** tools and methods from each user's crafted, it covers all aspects of the underlying code of Java development,
it is a powerful tool for large project development to solve small problems, but also the efficiency of small projects;

**Hutool** is a project "util" package friendly alternative, it saves developers on the project of common classes and
common tool methods of encapsulation time, so that development focus on business, at the same time can minimize the
encapsulation is not perfect to avoid the bugs.

### 🎁Origin of the 'Hutool' name

**Hutool = Hu + tool**，Is the original company project after the stripping of the underlying code of the open source
library , "Hu" is the short name of the company , 'tool' that tool .

Hutool,' Hútú '(Chinese Pinyin)，On the one hand, it is simple and easy to understand, on the other hand, it means "hard
to be confused".(note: confused means 'Hútú (糊涂)' in china )

### 🍺How Hutool is changing the way we code

The goal of  **Hutool**  is to use a simple function instead of a complex piece of code, thus avoiding the problem of "
copy and paste" code as much as possible and revolutionizing the way we write code.

To calculate MD5 for example:

- 👴【Before】Open a search engine -> search "Java MD5 encryption" -> open a blog -> copy and paste -> change it to work.
- 👦【Now 】import Hutool -> SecureUtil.md5()

Hutool exists to reduce code search costs and avoid bugs caused by imperfect code on the web.

### Thanks

> this README is PR by [chengxian-yi](https://gitee.com/yichengxian)
-------------------------------------------------------------------------------

## 🛠️Module

A Java-based tool class for files, streams, encryption and decryption, transcoding, regular, thread, XML and other JDK
methods for encapsulation，composing various Util tool classes, as well as providing the following modules：

| module         | description                                                                                                         |
|----------------|---------------------------------------------------------------------------------------------------------------------|
| hutool-core    | Core, including Bean operations, dates, various Utils, etc.                                                         |
| hutool-cron    | Task scheduling with Cron expressions                                                                               |
| hutool-crypto  | Provides symmetric, asymmetric and digest algorithm encapsulation                                                   |
| hutool-db      | Db operations based on ActiveRecord thinking.                                                                       |
| hutool-extra   | Extension modules, third-party wrappers (template engine, mail, servlet, QR code, Emoji, FTP, word splitting, etc.) |
| hutool-http    | Http client                                                                                                         |
| hutool-log     | Log (facade)                                                                                                        |
| hutool-script  | Script execution encapsulation, e.g. Javascript                                                                     |
| hutool-setting | Stronger Setting Profile tools and Properties tools                                                                 |
| hutool-json    | JSON                                                                                                                |
| hutool-poi     | Tools for working with Excel and Word in POI                                                                        |
| hutool-socket  | Java-based tool classes for NIO and AIO sockets                                                                     |
| hutool-swing   | Swing and AWT tools                                                                                                 |
| hutool-ai      | AI tools                                                                                                            |

Each module can be introduced individually, or all modules can be introduced by introducing `hutool-all` as required.

-------------------------------------------------------------------------------

## 📝Doc

[📘Chinese documentation](https://doc.hutool.cn/pages/index/)

[📘Chinese back-up documentation](https://plus.hutool.cn/docs/#/)

[📙API](https://plus.hutool.cn/apidocs/)

[🎬Video](https://www.bilibili.com/video/BV1bQ4y1M7d9?p=2)

-------------------------------------------------------------------------------

## 📦Install

### 🍊Maven

```xml

<dependency>
    <groupId>cn.hutool.v7</groupId>
    <artifactId>hutool-all</artifactId>
    <version>7.0.0-M6</version>
</dependency>
```

### 🍐Gradle

```
implementation 'cn.hutool.v7:hutool-all:7.0.0-M6'
```

## 📥Download

- [Maven Repo](https://repo1.maven.org/maven2/cn/hutool/v7/hutool-all/7.0.0-M6/)

> 🔔️note:
> Hutool 7.x supports JDK17 and is not tested on Android platforms, and cannot guarantee that all tool classes or tool
> methods are available.
> If your project uses JDK7, please use Hutool 4.x version.

### 🚽Compile and install

Download the entire project source code

gitee：[https://gitee.com/chinabugotech/hutool](https://gitee.com/chinabugotech/hutool)

github:[https://github.com/chinabugotech/hutool](https://github.com/chinabugotech/hutool)

```sh
cd ${hutool}
./hutool.sh install
```

-------------------------------------------------------------------------------

## 🏗️Other

### 🎋Branch Description

Hutool's source code is divided into two branches:

| branch    | description                                                                                                                                                         |
|-----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| v7-master | The master branch, the branch used by the release version, is the same as the jar committed to the central repository and does not receive any pr or modifications. |
| v7-dev    | Development branch, which defaults to the next SNAPSHOT version, accepts modifications or pr                                                                        |

### 🐞Provide feedback or suggestions on bugs

When submitting feedback, please indicate which JDK version, Hutool version, and related dependency library version you
are using.

- [Gitee issue](https://gitee.com/chinabugotech/hutool/issues)
- [Github issue](https://github.com/chinabugotech/hutool/issues)
- [Gitcode issue](https://gitcode.com/chinabugotech/hutool/issues)

### 🧬Principles of PR(pull request)

Hutool welcomes anyone to contribute code to Hutool, but the author suffers from OCD and needs to submit a pr (pull
request) that meets some specifications in order to care for the patient.：

1. Improve the comments, especially each new method should follow the Java documentation specification to indicate the
   method description, parameter description, return value description and other information, if necessary, please add
   unit tests, if you want, you can also add your name.
2. Code indentation according to Eclipse.
3. Newly added methods do not use third-party library methods，Unless the method tool is add to the '**extra module**'.
4. Please pull request to the `v7-dev` branch. Hutool uses a new branch after 7.x: `v7-master` is the master branch,
   which indicates the version of the central library that has been released, and this branch does not allow pr or
   modifications.

-------------------------------------------------------------------------------

## ⭐Star Hutool

[![Stargazers over time](https://starchart.cc/chinabugotech/hutool.svg)](https://starchart.cc/chinabugotech/hutool)

## 📌WeChat Official Account

<div align="center">
    <img src="https://dromara.org/img/qrcode/qrcode_1.png" height="150">
</div>
